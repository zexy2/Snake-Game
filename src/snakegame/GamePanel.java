package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Main game panel that handles rendering, game logic, and user input.
 * Contains the snake movement, collision detection, and scoring system.
 */
public class GamePanel extends JPanel implements ActionListener {

    // Game dimensions
    private static final int SCREEN_WIDTH = 1300;
    private static final int SCREEN_HEIGHT = 750;
    private static final int UNIT_SIZE = 50;
    private static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / (UNIT_SIZE * UNIT_SIZE);
    private static final int GAME_DELAY_MS = 95;

    // Asset paths
    private static final String APPLE_IMAGE_PATH = "assets/images/apple.png";
    private static final String SNAKE_HEAD_IMAGE_PATH = "assets/images/snake.png";
    private static final String BACKGROUND_MUSIC_PATH = "assets/sounds/background.wav";
    private static final String EAT_SOUND_PATH = "assets/sounds/eat.wav";
    private static final String GAME_OVER_SOUND_PATH = "assets/sounds/game_over.wav";

    // Snake position arrays
    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];

    // Game state
    private int bodyParts = 6;
    private int applesEaten = 0;
    private int highScore = 0;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;

    // Game components
    private Timer gameTimer;
    private final Random random;
    private final AudioManager audioManager;

    // Cached images for performance
    private Image appleImage;
    private Image snakeHeadImage;

    public GamePanel() {
        this.random = new Random();
        this.audioManager = new AudioManager();
        
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());
        
        loadImages();
        startGame();
    }

    /**
     * Pre-loads and scales images for better performance.
     */
    private void loadImages() {
        ImageIcon appleIcon = new ImageIcon(APPLE_IMAGE_PATH);
        appleImage = appleIcon.getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);

        ImageIcon snakeIcon = new ImageIcon(SNAKE_HEAD_IMAGE_PATH);
        snakeHeadImage = snakeIcon.getImage().getScaledInstance(UNIT_SIZE, UNIT_SIZE, Image.SCALE_SMOOTH);
    }

    /**
     * Initializes and starts a new game.
     */
    public void startGame() {
        resetSnake();
        resetScore();
        spawnApple();
        running = true;
        gameTimer = new Timer(GAME_DELAY_MS, this);
        gameTimer.start();
        audioManager.playBackgroundMusic(BACKGROUND_MUSIC_PATH);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Renders the game state to the screen.
     */
    private void draw(Graphics g) {
        if (running) {
            drawGame(g);
        } else {
            drawGameOver(g);
        }
    }

    /**
     * Draws the active game state (snake, apple, score).
     */
    private void drawGame(Graphics g) {
        // Draw apple
        g.drawImage(appleImage, appleX, appleY, this);

        // Draw snake
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                // Draw head with image
                g.drawImage(snakeHeadImage, snakeX[i], snakeY[i], this);
            } else {
                // Draw body with random colors for visual effect
                g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        // Draw current score
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String scoreText = "Score: " + applesEaten;
        g.drawString(scoreText, (SCREEN_WIDTH - metrics.stringWidth(scoreText)) / 2, 45);

        // Draw high score
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.ITALIC, 20));
        g.drawString("High Score: " + highScore, SCREEN_WIDTH - 150, 30);
    }

    /**
     * Draws the game over screen with final score.
     */
    private void drawGameOver(Graphics g) {
        // Update high score if needed
        if (applesEaten > highScore) {
            highScore = applesEaten;
        }

        // Draw score information
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());

        if (applesEaten >= highScore && applesEaten > 0) {
            String newHighText = "New High Score: " + applesEaten + "!";
            g.drawString(newHighText, (SCREEN_WIDTH - metrics.stringWidth(newHighText)) / 2, 90);
        } else {
            String scoreText = "Your Score: " + applesEaten;
            g.drawString(scoreText, (SCREEN_WIDTH - metrics.stringWidth(scoreText)) / 2, 45);
            
            g.setColor(Color.BLUE);
            String highScoreText = "High Score: " + highScore;
            g.drawString(highScoreText, (SCREEN_WIDTH - metrics.stringWidth(highScoreText)) / 2, SCREEN_HEIGHT / 4);
        }

        // Draw "Game Over" text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics gameOverMetrics = getFontMetrics(g.getFont());
        String gameOverText = "Game Over";
        g.drawString(gameOverText, (SCREEN_WIDTH - gameOverMetrics.stringWidth(gameOverText)) / 2, SCREEN_HEIGHT / 2);

        // Draw restart prompt
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 45));
        FontMetrics restartMetrics = getFontMetrics(g.getFont());
        String restartText = "Press SPACE to Restart";
        g.drawString(restartText, (SCREEN_WIDTH - restartMetrics.stringWidth(restartText)) / 2, 600);
    }

    /**
     * Spawns a new apple at a random location.
     */
    private void spawnApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    /**
     * Moves the snake in the current direction.
     */
    private void move() {
        // Shift body segments
        for (int i = bodyParts; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        // Move head based on direction
        switch (direction) {
            case 'U' -> snakeY[0] -= UNIT_SIZE;
            case 'D' -> snakeY[0] += UNIT_SIZE;
            case 'L' -> snakeX[0] -= UNIT_SIZE;
            case 'R' -> snakeX[0] += UNIT_SIZE;
        }
    }

    /**
     * Checks if the snake has eaten the apple.
     */
    private void checkApple() {
        if (snakeX[0] == appleX && snakeY[0] == appleY) {
            bodyParts++;
            applesEaten++;
            spawnApple();
            audioManager.playSound(EAT_SOUND_PATH);
        }
    }

    /**
     * Checks for collisions with walls and snake body.
     */
    private void checkCollisions() {
        // Check self-collision
        for (int i = bodyParts; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
                break;
            }
        }

        // Check wall collisions
        if (snakeX[0] < 0 || snakeX[0] >= SCREEN_WIDTH ||
            snakeY[0] < 0 || snakeY[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        // Handle game over
        if (!running) {
            gameTimer.stop();
            audioManager.stopBackgroundMusic();
            audioManager.playSound(GAME_OVER_SOUND_PATH);
        }
    }

    /**
     * Resets snake to starting position.
     */
    private void resetSnake() {
        bodyParts = 6;
        for (int i = 0; i < bodyParts; i++) {
            snakeX[i] = 0;
            snakeY[i] = 0;
        }
        direction = 'R';
    }

    /**
     * Resets the score counter.
     */
    private void resetScore() {
        applesEaten = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    /**
     * Handles keyboard input for snake control and game restart.
     */
    private class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!running && e.getKeyCode() == KeyEvent.VK_SPACE) {
                startGame();
                return;
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                    if (direction != 'R') direction = 'L';
                }
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                    if (direction != 'L') direction = 'R';
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    if (direction != 'D') direction = 'U';
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    if (direction != 'U') direction = 'D';
                }
            }
        }
    }
}