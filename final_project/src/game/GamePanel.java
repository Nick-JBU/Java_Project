package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashSet;
import java.util.Set;

class GamePanel extends JPanel implements KeyListener {
    private BufferedImage mapImage, collisionMap, spriteSheet;
    private int tileSize = 50;
    private int spriteWidth = 32, spriteHeight = 40;
    private int frame = 0;
    private boolean mapLoaded = true;
    private boolean spriteLoaded = true;
    private boolean collisionMapLoaded = true;
    private boolean moving = false;

    private int playerRow = 0, playerCol = 0;
    private int pixelX, pixelY;
    private int targetX, targetY;
    private int baseSpeed = 5;
    private int walkSpeed = baseSpeed;
    private boolean shiftHeld = false;

    private int mapWidth = 1600, mapHeight = 900;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public GamePanel() {
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream("/Map idea.png"));
            collisionMap = ImageIO.read(getClass().getResourceAsStream("/mapcollision.png"));
            if (mapImage != null) {
                mapWidth = mapImage.getWidth();
                mapHeight = mapImage.getHeight();
            }
        } catch (IOException | NullPointerException e) {
            mapLoaded = false;
            collisionMapLoaded = false;
            System.err.println("Map or collision image failed to load.");
        }

        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/MaleSprites.png"));
        } catch (IOException | NullPointerException e) {
            spriteLoaded = false;
            System.err.println("Sprite sheet failed to load.");
        }

        setPreferredSize(new Dimension(1600, 900));

        if (collisionMapLoaded) {
            int rows = collisionMap.getHeight() / tileSize;
            int cols = collisionMap.getWidth() / tileSize;
            int centerRow = 8;
            int startCol = 19; 

            if (isWalkable(startCol, centerRow)) {
                playerRow = centerRow;
                playerCol = startCol;
            } else {
                int radius = 1;
                boolean found = false;
                while (!found && radius < Math.max(rows, cols)) {
                    for (int r = centerRow - radius; r <= centerRow + radius; r++) {
                        for (int c = startCol - radius; c <= startCol + radius; c++) {
                            if (r >= 0 && r < rows && c >= 0 && c < cols && isWalkable(c, r)) {
                                playerRow = r;
                                playerCol = c;
                                found = true;
                                break;
                            }
                        }
                        if (found) break;
                    }
                    radius++;
                }
            }
        }

        pixelX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
        pixelY = playerRow * tileSize + (tileSize / 2 - spriteHeight / 2);
        targetX = pixelX;
        targetY = pixelY;

        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(16, e -> {
            int currentSpeed = shiftHeld ? walkSpeed * 2 : walkSpeed;

            if (pixelX < targetX) pixelX = Math.min(targetX, pixelX + currentSpeed);
            if (pixelX > targetX) pixelX = Math.max(targetX, pixelX - currentSpeed);
            if (pixelY < targetY) pixelY = Math.min(targetY, pixelY + currentSpeed);
            if (pixelY > targetY) pixelY = Math.max(targetY, pixelY - currentSpeed);

            moving = (pixelX != targetX || pixelY != targetY);
            if (!moving && !pressedKeys.isEmpty()) {
                int dy = 0;
                if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_W)) dy = -1;
                if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) dy = 1;

                int dx = 0;
                if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) dx = -1;
                if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) dx = 1;

                if (dy != 0 && isWalkable(playerCol, playerRow + dy)) {
                    playerRow += dy;
                    targetY = playerRow * tileSize + (tileSize / 2 - spriteHeight / 2);
                    targetX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
                } else if (dx != 0 && isWalkable(playerCol + dx, playerRow)) {
                    playerCol += dx;
                    targetX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
                    targetY = playerRow * tileSize + (tileSize / 2 - spriteHeight / 2);
                }
            }

            if (moving) {
                frame = (frame + 1) % 4;
            } else {
                frame = 1;
            }
            repaint();
        });
        timer.start();
    }

    private boolean isWalkable(int col, int row) {
        if (!collisionMapLoaded) return true;
        int x = col * tileSize + tileSize / 2;
        int y = row * tileSize + tileSize / 2;
        if (x < 0 || y < 0 || x >= collisionMap.getWidth() || y >= collisionMap.getHeight()) return false;

        int rgb = collisionMap.getRGB(x, y);
        Color color = new Color(rgb);
        return color.getRed() > 200 && color.getGreen() > 200 && color.getBlue() > 200;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) shiftHeld = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) shiftHeld = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int centerX = (getWidth() - mapWidth) / 2;
        int centerY = (getHeight() - mapHeight) / 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (mapLoaded) {
            g.drawImage(mapImage, centerX, centerY, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(centerX, centerY, mapWidth, mapHeight);
        }

        if (spriteLoaded) {
            int drawX = centerX + pixelX;
            int drawY = centerY + pixelY;
            int spriteX = (moving ? (frame % 4) * 32 : 32);
            g.drawImage(spriteSheet, drawX, drawY, drawX + spriteWidth, drawY + spriteHeight,
                    spriteX, 0, spriteX + spriteWidth, spriteHeight, null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(centerX + pixelX, centerY + pixelY, spriteWidth, spriteHeight);
        }
    }
}
