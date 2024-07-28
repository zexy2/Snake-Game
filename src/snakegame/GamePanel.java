/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snakegame;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.JPanel;
// Game constants
public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 95;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int highScore;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    private Clip clip;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        playSound("bckground.wav");
        resetSnake(); // Reset the snake's starting position
        resetScore();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw(Graphics g) throws IOException {
        if (running) {
            ImageIcon imageIcon = new ImageIcon("apple.png"); // Load the image to an ImageIcon
            Image image = imageIcon.getImage(); // Transform it
            Image newImg = image.getScaledInstance(UNIT_SIZE, UNIT_SIZE, java.awt.Image.SCALE_SMOOTH); // Scale it smoothly
            imageIcon = new ImageIcon(newImg); // Transform it back

            imageIcon.paintIcon(this, g, appleX, appleY);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    ImageIcon imageIcon2 = new ImageIcon("snake.png");
                    Image image2 = imageIcon2.getImage(); // Transform it
                    Image newImg2 = image2.getScaledInstance(UNIT_SIZE, UNIT_SIZE, java.awt.Image.SCALE_SMOOTH); // Scale it smoothly
                    imageIcon2 = new ImageIcon(newImg2); // Transform it back
                    imageIcon2.paintIcon(this, g, x[i], y[i]);
                } else {
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.ITALIC, 20));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("High Score: " + highScore, SCREEN_WIDTH - 130, SCREEN_HEIGHT - (SCREEN_HEIGHT - 30));
        } else {
            running = false;
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        // Reset the snake's starting position
        if (!running) {
            resetSnake();
        }
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
            eatSound("eat.wav");
        }
    }

    public void checkCollisions() { // Collisions
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // Check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            clip.stop();
            timer.stop(); // Stop the timer
            gameOverSound("gameOver");
        }
    }

    public void playSound(String soundFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void gameOverSound(String soundFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
            Clip clip2 = AudioSystem.getClip();
            clip2.open(audioInputStream);
            clip2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eatSound(String soundFilePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFilePath));
            Clip clip3 = AudioSystem.getClip();
            clip3.open(audioInputStream);
            clip3.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void gameOver(Graphics g) {
        // Stop the background music
        clip.stop();

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        if (applesEaten > highScore) {
            g.drawString("!!!️ Congratulations you reached the highest score: " + applesEaten + " !!!", 330, 90);
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.drawString("The Old Highest Score: " + highScore, 470, SCREEN_HEIGHT / 4);
        } else {
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            g.drawString("Your Score: " + applesEaten, 520, g.getFont().getSize());
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            g.drawString("The  Highest Score: " + highScore, 500, SCREEN_HEIGHT / 4);
        }
        // Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
        tryAgain(g);
        if (applesEaten > highScore) {
            highScore = applesEaten;
        }
    }

    public void tryAgain(Graphics g) {
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD, 45));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Try Again (Space)", 470, 600);
    }

    public void resetSnake() {
        bodyParts = 6; // Reset the snake's body parts
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0; // Reset the snake's x coordinates
            y[i] = 0; // Reset the snake's y coordinates
        }
        direction = 'R'; // Set the snake's starting direction
    }

    public void resetScore() {
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

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!running && e.getKeyCode() == KeyEvent.VK_SPACE) {
                startGame(); // Restart the game
            } else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            }
        }
    }
}