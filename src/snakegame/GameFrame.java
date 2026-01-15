package snakegame;

import javax.swing.JFrame;

/**
 * Main game window frame for the Snake game.
 * Sets up the window properties and contains the game panel.
 */
public class GameFrame extends JFrame {

    public GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
