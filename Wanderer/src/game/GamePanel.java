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
    private boolean isFullscreen = false;
    public JFrame parentFrame;
    private BufferedImage mapImage, collisionMap, spriteSheet;
    private int tileSize = 32;
    private int spriteWidth = 32, spriteHeight = 40;
    private int frame = 0;
    private int frameTick = 0;
    private int frameDelay = 3; // Speed up animation
    private int turnDelay = 1; // Lower = faster turning transition
    private boolean mapLoaded = true;
    private boolean spriteLoaded = true;
    private boolean collisionMapLoaded = true;
    private boolean moving = false;
    private double zoom = 3.0;
    private int playerRow = 0, playerCol = 0;
    private int pixelX, pixelY;
    private int targetX, targetY;
    private int walkSpeed = 4;
    private int runSpeed = 7;
    private boolean shiftHeld = false;
    private enum Direction { DOWN, UP, LEFT, RIGHT }
    private Direction facing = Direction.DOWN;
    private Direction turnPreview = null;
    private boolean turning = false;
    private int turnStage = 0;
    private int mapWidth = 1600, mapHeight = 900;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public void toggleFullscreen() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (isFullscreen) {
            parentFrame.dispose();
            parentFrame.setUndecorated(false);
            parentFrame.setExtendedState(JFrame.NORMAL);
            parentFrame.setSize(1600, 900);
            parentFrame.setLocationRelativeTo(null);
            parentFrame.setVisible (true);
        } else {
            parentFrame.dispose();
            parentFrame.setUndecorated(true);
            parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            parentFrame.setVisible(true);
        }
        isFullscreen = !isFullscreen;
    }

    public GamePanel() {
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream("/resources/Map idea3.png"));
            collisionMap = ImageIO.read(getClass().getResourceAsStream("/resources/mapcollision3.png"));
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
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/resources/MaleSprites.png"));
        } catch (IOException | NullPointerException e) {
            spriteLoaded = false;
            System.err.println("Sprite sheet failed to load.");
        }

        setPreferredSize(new Dimension(1600, 900));

        int spawnRow = 8, spawnCol = 19;
        if (collisionMap != null && isWalkable(spawnCol, spawnRow)) {
            playerRow = spawnRow;
            playerCol = spawnCol;
        }

        pixelX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
        pixelY = (playerRow + 1) * tileSize - spriteHeight - 1;
        targetX = pixelX;
        targetY = pixelY;

        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(16, e -> {
            int speed = shiftHeld ? runSpeed : walkSpeed;

            if (pixelX < targetX) pixelX = Math.min(targetX, pixelX + speed);
            if (pixelX > targetX) pixelX = Math.max(targetX, pixelX - speed);
            if (pixelY < targetY) pixelY = Math.min(targetY, pixelY + speed);
            if (pixelY > targetY) pixelY = Math.max(targetY, pixelY - speed);

            moving = (pixelX != targetX || pixelY != targetY);

            if (!moving && !turning) {
                Direction input = getDirectionFromKeys();
                if (input != null) {
                    if (input != facing) {
                        turnPreview = input;
                        turning = true;
                        turnStage = 0;
                    } else {
                        int nextCol = playerCol + (input == Direction.RIGHT ? 1 : input == Direction.LEFT ? -1 : 0);
                        int nextRow = playerRow + (input == Direction.DOWN ? 1 : input == Direction.UP ? -1 : 0);
                        if (isWalkable(nextCol, nextRow)) {
                            playerCol = nextCol;
                            playerRow = nextRow;
                            targetX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
                            targetY = (playerRow + 1) * tileSize - spriteHeight - 1;
                            moving = true;
                        }
                    }
                }
            }

            if (moving) {
                frameTick++;
                if (frameTick >= frameDelay) {
                    frame = (frame + 1) % 4;
                    frameTick = 0;
                }
            } else if (turning) {
                frameTick++;
                if (frameTick >= turnDelay) {
                    frameTick = 0;
                    if (turnStage == 0) {
                        frame = 0; // left foot
                        turnStage++;
                    } else if (turnStage == 1) {
                        facing = turnPreview;
                        frame = 2; // right foot
                        turnStage++;
                    } else {
                        frame = 3; // idle
                        turning = false;
                    }
                }
            } else {
                frame = 3;
            }

            repaint();
        });
        timer.start();
    }

    private Direction getDirectionFromKeys() {
        if (pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_UP)) return Direction.UP;
        if (pressedKeys.contains(KeyEvent.VK_S) || pressedKeys.contains(KeyEvent.VK_DOWN)) return Direction.DOWN;
        if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_LEFT)) return Direction.LEFT;
        if (pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_RIGHT)) return Direction.RIGHT;
        return null;
    }

    private boolean isWalkable(int col, int row) {
        if (!collisionMapLoaded || collisionMap == null) return true;
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
        if (e.getKeyCode() == KeyEvent.VK_F11) toggleFullscreen();
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
        int viewWidth = (int)(getWidth() / zoom);
        int viewHeight = (int)(getHeight() / zoom);
        BufferedImage worldImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D wg = worldImage.createGraphics();

        int centerX = pixelX + spriteWidth / 2;
        int centerY = pixelY + spriteHeight;
        int camX = centerX - viewWidth / 2;
        int camY = centerY - viewHeight / 2;
        camX = Math.max(0, Math.min(camX, mapWidth - viewWidth));
        camY = Math.max(0, Math.min(camY, mapHeight - viewHeight));

        wg.setColor(Color.BLACK);
        wg.fillRect(0, 0, mapWidth, mapHeight);
        if (mapLoaded) wg.drawImage(mapImage, 0, 0, null);
        else {
            wg.setColor(Color.BLUE);
            wg.fillRect(0, 0, mapWidth, mapHeight);
        }

        if (spriteLoaded) {
            int spriteX = frame * spriteWidth;
            int spriteY = switch (facing) {
                case DOWN -> 0;
                case UP -> spriteHeight;
                case LEFT -> spriteHeight * 2;
                case RIGHT -> spriteHeight * 3;
            };
            wg.drawImage(spriteSheet, pixelX, pixelY, pixelX + spriteWidth, pixelY + spriteHeight,
                    spriteX, spriteY, spriteX + spriteWidth, spriteY + spriteHeight, null);
        } else {
            wg.setColor(Color.ORANGE);
            wg.fillRect(pixelX, pixelY, spriteWidth, spriteHeight);
        }

        wg.dispose();
        ((Graphics2D) g).scale(zoom, zoom);
        g.drawImage(worldImage.getSubimage(camX, camY, viewWidth, viewHeight), 0, 0, null);
    }
}