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
import java.util.ArrayList;
import java.util.List;

class GamePanel extends JPanel implements KeyListener {
    private boolean isFullscreen = false;
    public JFrame parentFrame;
    private BufferedImage mapImage, collisionMap, spriteSheet, roofImage, npcSpriteSheet;
    private int tileSize = 32;
    private int spriteWidth = 32, spriteHeight = 40;
    private int frame = 0;
    private int frameTick = 0;
    private int frameDelay = 2; // Faster animation
    private int turnDelay = 1;
    private boolean mapLoaded = true;
    private boolean spriteLoaded = true;
    private boolean collisionMapLoaded = true;
    private boolean roofLoaded = true;
    private boolean moving = false;
    private double zoom = 4.0;
    private int playerRow = 0, playerCol = 0;
    private int pixelX, pixelY;
    private int targetX, targetY;
    private int walkSpeed = 4;
    private int runSpeed = 7;
    private boolean shiftHeld = false;
    private boolean fullMapView = false;
    private enum Direction { DOWN, UP, LEFT, RIGHT }
    private Direction facing = Direction.DOWN;
    private Direction turnPreview = null;
    private boolean turning = false;
    private int turnStage = 0;
    private int mapWidth = 1600, mapHeight = 900;
    private final Set<Integer> pressedKeys = new HashSet<>();

    private class NPC {
        int row, col;
        int pixelX, pixelY;

        NPC(int row, int col) {
            this.row = row;
            this.col = col;
            this.pixelX = col * tileSize + (tileSize / 2 - spriteWidth / 2);
            this.pixelY = (row + 1) * tileSize - spriteHeight - 12;
        }

        void draw(Graphics2D g) {
            int frameIndex = 1; // Frame 2 (index 1)
            int spriteX = frameIndex * spriteWidth;
            int spriteY = spriteHeight; // UP row (second row)
            g.drawImage(
                npcSpriteSheet,
                pixelX, pixelY, pixelX + spriteWidth, pixelY + spriteHeight,
                spriteX, spriteY, spriteX + spriteWidth, spriteY + spriteHeight,
                null
            );
        }
    }

    private List<NPC> npcs = new ArrayList<>();

    public GamePanel() {
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/buildings.png"));
            roofImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/Roofs.png"));
            collisionMap = ImageIO.read(getClass().getResourceAsStream("/resources/maps/mapCollision.png"));
            npcSpriteSheet = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/NPCsprites.png"));
            if (mapImage != null) {
                mapWidth = mapImage.getWidth();
                mapHeight = mapImage.getHeight();
            }
        } catch (IOException | NullPointerException e) {
            mapLoaded = false;
            collisionMapLoaded = false;
            roofLoaded = false;
            System.err.println("Map, roof, or collision image failed to load.");
        }

        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/FemaleSprites.png"));
        } catch (IOException | NullPointerException e) {
            spriteLoaded = false;
            System.err.println("Sprite sheet failed to load.");
        }

        setPreferredSize(new Dimension(1600, 900));

        int spawnRow = 12, spawnCol = 23;
        if (collisionMap != null && isWalkable(spawnCol, spawnRow)) {
            playerRow = spawnRow;
            playerCol = spawnCol;
        }

        pixelX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
        pixelY = (playerRow + 1) * tileSize - spriteHeight - 12;
        targetX = pixelX;
        targetY = pixelY;

        npcs.add(new NPC(25, 23)); // Spawn the NPC at row 25, col 23

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
                            targetY = (playerRow + 1) * tileSize - spriteHeight - 12;
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
                        frame = 0;
                        turnStage++;
                    } else if (turnStage == 1) {
                        facing = turnPreview;
                        frame = 2;
                        turnStage++;
                    } else {
                        frame = 3;
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) fullMapView = !fullMapView;
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

        int viewWidth = fullMapView ? getWidth() : (int)(getWidth() / zoom);
        int viewHeight = fullMapView ? getHeight() : (int)(getHeight() / zoom);

        BufferedImage worldImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D wg = worldImage.createGraphics();

        int centerX = pixelX + spriteWidth / 2;
        int centerY = pixelY + spriteHeight;
        int camX = fullMapView ? 0 : centerX - viewWidth / 2;
        int camY = fullMapView ? 0 : centerY - viewHeight / 2;

        if (!fullMapView) {
            camX = Math.max(0, Math.min(camX, mapWidth - viewWidth));
            camY = Math.max(0, Math.min(camY, mapHeight - viewHeight));
        }

        wg.setColor(Color.BLACK);
        wg.fillRect(0, 0, mapWidth, mapHeight);

        if (mapLoaded) wg.drawImage(mapImage, 0, 0, null);
        else {
            wg.setColor(Color.BLUE);
            wg.fillRect(0, 0, mapWidth, mapHeight);
        }

        for (NPC npc : npcs) {
            if (npc.pixelY < pixelY) npc.draw(wg); // Draw NPC behind player
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

        for (NPC npc : npcs) {
            if (npc.pixelY >= pixelY) npc.draw(wg); // Draw NPC in front of player
        }

        if (roofLoaded && roofImage != null) {
            wg.drawImage(roofImage, 0, 0, null);
        }

        wg.dispose();

        if (!fullMapView) {
            ((Graphics2D) g).scale(zoom, zoom);
            g.drawImage(worldImage.getSubimage(camX, camY, viewWidth, viewHeight), 0, 0, null);
        } else {
            g.drawImage(worldImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
