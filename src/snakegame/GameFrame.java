
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snakegame;

import javax.swing.JFrame;

/**
 *
 * @author 90546
 */
public class GameFrame extends JFrame {
    
    GameFrame() {
        
        // GamePanel gPanel = new GamePanel();
        // this.add(gPanel);
        // Shortcut for the above is shown below
        
        this.add(new GamePanel());
       
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null); // Centers the window on the screen
        
    }
    
}
