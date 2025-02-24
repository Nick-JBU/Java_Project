package com.mygame;

import javax.swing.*;

public class GameWindow {
    public static void main(String[] args) {
        JFrame frame = new JFrame("The Red Dot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set the window size (you can adjust)
        
        // Add the game panel to the window
        GamePanel panel = new GamePanel();
        frame.add(panel);
        
        frame.setVisible(true);
    }
}

