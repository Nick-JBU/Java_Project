package com.mygame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;          // For Timer class
import java.awt.event.ActionListener;  // For ActionListener interface
import java.awt.event.ActionEvent;     // For ActionEvent class

public class GamePanel extends JPanel {
    private int charX = 100;
    private int charY = 100;
    private final int tileSize = 20;
    
    public GamePanel() {
        setPreferredSize(new Dimension(800, 600)); // Set panel size
        
        // Add key listener for movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) charX -= tileSize;
                if (key == KeyEvent.VK_RIGHT) charX += tileSize;
                if (key == KeyEvent.VK_UP) charY -= tileSize;
                if (key == KeyEvent.VK_DOWN) charY += tileSize;
                if (key == KeyEvent.VK_A) charX -= tileSize;
                if (key == KeyEvent.VK_D) charX += tileSize;
                if (key == KeyEvent.VK_W) charY -= tileSize;
                if (key == KeyEvent.VK_S) charY += tileSize;
                repaint();
            }
        });
        setFocusable(true);
        
        // Set up a simple game loop with a timer
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint(); // Update screen every ~16ms (~60 FPS)
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);
        drawCharacter(g);
    }

    private void drawTiles(Graphics g) {
        for (int y = 0; y < getHeight(); y += tileSize) {
            for (int x = 0; x < getWidth(); x += tileSize) {
                g.setColor(Color.BLUE);
                g.fillRect(x, y, tileSize, tileSize);
            }
        }
    }

    private void drawCharacter(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(charX, charY, tileSize, tileSize); // Draw character
    }
}
