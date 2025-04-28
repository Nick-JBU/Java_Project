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
    private int frameDelay = 4;
    private int turnDelay = 1;
    private boolean mapLoaded = true;
    private boolean spriteLoaded = true;
    private boolean collisionMapLoaded = true;
    private boolean roofLoaded = true;
    private boolean moving = false;
    private double zoom = 3.0;
    private int playerRow = 0, playerCol = 0;
    private int pixelX, pixelY;
    private int targetX, targetY;
    private int walkSpeed = 4;
    private int runSpeed = 7;
    private boolean shiftHeld = false;
    private boolean fullMapView = false;
    private boolean fading = false;
    private int fadeAlpha = 0;
    private boolean fadeOut = true;
    private Runnable onFadeComplete;

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
        Direction facing;

        NPC(int row, int col, Direction facing) {
            this.row = row;
            this.col = col;
            this.facing = facing;
            this.pixelX = col * tileSize + (tileSize / 2 - spriteWidth / 2);
            this.pixelY = (row + 1) * tileSize - spriteHeight - 4;
        }

        void draw(Graphics2D g) {
            int frameIndex = 1;
            int spriteX = frameIndex * spriteWidth;
            int spriteY = switch (facing) {
                case DOWN -> 0;
                case UP -> spriteHeight;
                case LEFT -> spriteHeight * 2;
                case RIGHT -> spriteHeight * 3;
            };
            g.drawImage(
                npcSpriteSheet,
                pixelX, pixelY, pixelX + spriteWidth, pixelY + spriteHeight,
                spriteX, spriteY, spriteX + spriteWidth, spriteY + spriteHeight,
                null
            );
        }
    }

    private class Door {
        int row, col;
        Direction requiredFacing;
        String destinationMap, destinationCollision, destinationRoof;
        int destRow, destCol;

        Door(int row, int col, Direction requiredFacing, String destMap, String destCollision, String destRoof, int destRow, int destCol) {
            this.row = row;
            this.col = col;
            this.requiredFacing = requiredFacing;
            this.destinationMap = destMap;
            this.destinationCollision = destCollision;
            this.destinationRoof = destRoof;
            this.destRow = destRow;
            this.destCol = destCol;
        }
    }

    private List<NPC> npcs = new ArrayList<>();
    private List<Door> doors = new ArrayList<>();

    public GamePanel() {
        loadBuildings();

        try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/MaleSprites.png"));
            npcSpriteSheet = ImageIO.read(getClass().getResourceAsStream("/resources/sprites/NPCsprites.png"));
        } catch (IOException | NullPointerException e) {
            spriteLoaded = false;
            System.err.println("Sprite sheet failed to load.");
        }

        setPreferredSize(new Dimension(1600, 900));
        setupPlayerSpawn();
        setupNPCs();
        setupDoors();

        pixelX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
        pixelY = (playerRow + 1) * tileSize - spriteHeight - 4;
        targetX = pixelX;
        targetY = pixelY;

        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(16, e -> gameLoop());
        timer.start();
    }

    private void loadBuildings() {
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/default.png"));
            roofImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/defaultOverlay.png"));
            collisionMap = ImageIO.read(getClass().getResourceAsStream("/resources/maps/defaultCollision.png"));
            mapWidth = mapImage.getWidth();
            mapHeight = mapImage.getHeight();
        } catch (IOException e) {
            System.err.println("Failed to load default map.");
        }
    }

    private void loadMainBuilding() {
        try {
            mapImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/building1.png"));
            roofImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/building1Overlay.png"));
            collisionMap = ImageIO.read(getClass().getResourceAsStream("/resources/maps/building1Collision.png"));
            mapWidth = mapImage.getWidth();
            mapHeight = mapImage.getHeight();
        } catch (IOException e) {
            System.err.println("Failed to load building 1.");
        }
    }

    private void setupPlayerSpawn() {
        int tilesWide = mapWidth / tileSize;
        int tilesHigh = mapHeight / tileSize;

        if (tilesWide == 48 && tilesHigh == 27) {
            playerCol = 23;
            playerRow = 12;
        } else {
            playerCol = 4;
            playerRow = 4;
        }
    }

    private void setupNPCs() {
        npcs.clear();
        int tilesWide = mapWidth / tileSize;

        if (tilesWide == 48) {
            npcs.add(new NPC(25, 23, Direction.UP));
        } else {
            npcs.add(new NPC(3, 0, Direction.RIGHT));
        }
    }

    private void setupDoors() {
        doors.clear();
        doors.add(new Door(8, 23, Direction.UP, "/resources/maps/building1.png", "/resources/maps/building1Collision.png", "/resources/maps/building1Overlay.png", 4, 4));
        doors.add(new Door(4, 4, Direction.DOWN, "/resources/maps/default.png", "/resources/maps/defaultCollision.png", "/resources/maps/defaultOverlay.png", 9, 23));
    }

    private void teleportTo(String map, String collision, String roof, int row, int col) {
        fading = true;
        fadeOut = true;
        fadeAlpha = 0;
        onFadeComplete = () -> {
            try {
                mapImage = ImageIO.read(getClass().getResourceAsStream(map));
                collisionMap = ImageIO.read(getClass().getResourceAsStream(collision));
                roofImage = ImageIO.read(getClass().getResourceAsStream(roof));
                mapWidth = mapImage.getWidth();
                mapHeight = mapImage.getHeight();
                playerRow = row;
                playerCol = col;
                setupNPCs();
                setupDoors();
                pixelX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
                pixelY = (playerRow + 1) * tileSize - spriteHeight - 4;
                targetX = pixelX;
                targetY = pixelY;
            } catch (IOException e) {
                e.printStackTrace();
            }
            fadeOut = false;
        };
    }
    private void toggleFullscreen() {
        if (parentFrame == null) return;

        isFullscreen = !isFullscreen;

        parentFrame.dispose(); 
        parentFrame.setUndecorated(isFullscreen);
        parentFrame.setVisible(true);

        if (isFullscreen) {
            parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        } else {
            parentFrame.setSize(1600, 900);
            parentFrame.setLocationRelativeTo(null);
        }
    }

    private void gameLoop() {
        int speed = shiftHeld ? runSpeed : walkSpeed;

        if (pixelX < targetX) pixelX = Math.min(targetX, pixelX + speed);
        if (pixelX > targetX) pixelX = Math.max(targetX, pixelX - speed);
        if (pixelY < targetY) pixelY = Math.min(targetY, pixelY + speed);
        if (pixelY > targetY) pixelY = Math.max(targetY, pixelY - speed);

        moving = (pixelX != targetX || pixelY != targetY);

        if (!moving && !turning && !fading) {
            Direction input = getDirectionFromKeys();
            if (input != null) {
                if (input != facing) {
                    turnPreview = input;
                    turning = true;
                    turnStage = 0;
                } else {
                	if (playerRow == 4 && playerCol == 4 && input == Direction.DOWN && mapWidth == 288 && mapHeight == 160) {
                	    for (Door door : doors) {
                	        if (door.row == 4 && door.col == 4) {
                	            teleportTo(door.destinationMap, door.destinationCollision, door.destinationRoof, door.destRow, door.destCol);
                	            return;
                	        }
                	    }
                	}

                    int nextCol = playerCol + (input == Direction.RIGHT ? 1 : input == Direction.LEFT ? -1 : 0);
                    int nextRow = playerRow + (input == Direction.DOWN ? 1 : input == Direction.UP ? -1 : 0);

                    if (isWalkable(nextCol, nextRow)) {
                        playerCol = nextCol;
                        playerRow = nextRow;
                        targetX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
                        targetY = (playerRow + 1) * tileSize - spriteHeight - 4;
                        moving = true;

                        for (Door door : doors) {
                            if (playerRow == door.row && playerCol == door.col) {
                                if (!(door.row == 4 && door.col == 4)) {
                                    teleportTo(door.destinationMap, door.destinationCollision, door.destinationRoof, door.destRow, door.destCol);
                                }
                                break;
                            }
                        }
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
        if (e.getKeyCode() == KeyEvent.VK_F11) toggleFullscreen();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && isFullscreen) toggleFullscreen();

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

        int screenWidth = getWidth();
        int screenHeight = getHeight();

        BufferedImage worldImage = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D wg = worldImage.createGraphics();

        int centerX = pixelX + spriteWidth / 2;
        int centerY = pixelY + spriteHeight;
        int viewWidth = (int)(screenWidth / zoom);
        int viewHeight = (int)(screenHeight / zoom);
        int camX = centerX - viewWidth / 2;
        int camY = centerY - viewHeight / 2;

        camX = Math.max(0, Math.min(camX, mapWidth - viewWidth));
        camY = Math.max(0, Math.min(camY, mapHeight - viewHeight));

        wg.setColor(Color.BLACK);
        wg.fillRect(0, 0, mapWidth, mapHeight);

        if (mapLoaded) {
            wg.drawImage(mapImage, 0, 0, null);
        }

        for (NPC npc : npcs) {
            if (npc.pixelY < pixelY) npc.draw(wg);
        }

        if (spriteLoaded) {
            int spriteX = frame * spriteWidth;
            int spriteY = switch (facing) {
                case DOWN -> 0;
                case UP -> spriteHeight;
                case LEFT -> spriteHeight * 2;
                case RIGHT -> spriteHeight * 3;
            };
            wg.drawImage(spriteSheet, pixelX, pixelY, pixelX + spriteWidth, pixelY + spriteHeight, spriteX, spriteY, spriteX + spriteWidth, spriteY + spriteHeight, null);
        }

        for (NPC npc : npcs) {
            if (npc.pixelY >= pixelY) npc.draw(wg);
        }

        if (roofLoaded && roofImage != null) {
            wg.drawImage(roofImage, 0, 0, null);
        }

        wg.dispose();

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        if (fullMapView) {
            double scaleX = (double)screenWidth / mapWidth;
            double scaleY = (double)screenHeight / mapHeight;
            double scale = Math.min(scaleX, scaleY);

            int drawWidth = (int)(mapWidth * scale);
            int drawHeight = (int)(mapHeight * scale);
            int offsetX = (screenWidth - drawWidth) / 2;
            int offsetY = (screenHeight - drawHeight) / 2;

            g2.drawImage(worldImage, offsetX, offsetY, drawWidth, drawHeight, null);
        } else {
        	if (mapWidth == 288 && mapHeight == 160) {
        	    int drawWidth = (int)(mapWidth * zoom);
        	    int drawHeight = (int)(mapHeight * zoom);
        	    int offsetX = (screenWidth - drawWidth) / 2;
        	    int offsetY = (screenHeight - drawHeight) / 2;

        	    g2.translate(offsetX, offsetY);
        	    g2.scale(zoom, zoom);
        	    g2.drawImage(worldImage, 0, 0, null);
        	

            } else {
                g2.scale(zoom, zoom);

                 centerX = pixelX + spriteWidth / 2;
                 centerY = pixelY + spriteHeight;
                 viewWidth = (int)(screenWidth / zoom);
                 viewHeight = (int)(screenHeight / zoom);
                 camX = centerX - viewWidth / 2;
                 camY = centerY - viewHeight / 2;

                camX = Math.max(0, Math.min(camX, mapWidth - viewWidth));
                camY = Math.max(0, Math.min(camY, mapHeight - viewHeight));

                g2.drawImage(worldImage.getSubimage(
                    camX, camY,
                    Math.min(viewWidth, mapWidth - camX),
                    Math.min(viewHeight, mapHeight - camY)
                ), 0, 0, null);
            }
        }



        if (fading) {
            g2.setColor(new Color(0, 0, 0, fadeAlpha));
            g2.fillRect(0, 0, screenWidth, screenHeight);

            if (fadeOut) {
                fadeAlpha = Math.min(255, fadeAlpha + 15);
                if (fadeAlpha == 255 && onFadeComplete != null) {
                    onFadeComplete.run();
                }
            } else {
                fadeAlpha = Math.max(0, fadeAlpha - 15);
                if (fadeAlpha == 0) {
                    fading = false;
                }
            }
        }
    }
}