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
import java.util.HashMap;
import java.util.Map;



class GamePanel extends JPanel implements KeyListener {
    private boolean isFullscreen = false;
    public JFrame parentFrame;
    private BufferedImage mapImage, collisionMap, spriteSheet, overlayImage, npcSpriteSheet;
    private int tileSize = 32;
    private int spriteWidth = 32, spriteHeight = 40;
    private int frame = 0;
    private int frameTick = 0;
    private int frameDelay = 2;
    private int turnDelay = 1;
    private boolean mapLoaded = true;
    private boolean spriteLoaded = true;
    private boolean collisionMapLoaded = true;
    private boolean overlayLoaded = true;
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
    private static final long serialVersionUID = 1L;
    private DebugPanel debugPanel = new DebugPanel();
    private String loopStatus = "Idle";
    private String playerAction = "None";


    private enum Direction { DOWN, UP, LEFT, RIGHT }
    private Direction facing = Direction.DOWN;
    private Direction turnPreview = null;
    private boolean turning = false;
    private int turnStage = 0;
    private int mapWidth = 1600, mapHeight = 900;
    private final Set<Integer> pressedKeys = new HashSet<>();
    
    private class NPC {
        @SuppressWarnings("unused")
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
        String reqCurMap;
        String destinationMap, destinationCollision, destinationOverlay;
        int destRow, destCol;

        Door(int row, int col, Direction requiredFacing, String reqCurMap,
             String destinationMap, String destinationCollision, String destinationOverlay,
             int destRow, int destCol) {
            this.row = row;
            this.col = col;
            this.requiredFacing = requiredFacing;
            this.reqCurMap = reqCurMap;
            this.destinationMap = destinationMap;
            this.destinationCollision = destinationCollision;
            this.destinationOverlay = destinationOverlay;
            this.destRow = destRow;
            this.destCol = destCol;
        }
    }

    private List<NPC> npcs = new ArrayList<>();
    private List<Door> doors = new ArrayList<>();

    private String currentMapPath = "/resources/maps/default/default.png";

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

		Timer timer = new Timer(16, _ -> gameLoop());
        timer.start();
    }
    
    private final Map<String, Set<Direction>> directionalTiles = new HashMap<>();

    private final Map<String, Set<Direction>> defaultDirectional = Map.of();

    private final Map<String, Set<Direction>> building1Directional = Map.of(
    		"2,1", Set.of(Direction.UP, Direction.RIGHT),
    		"3,1", Set.of(Direction.UP, Direction.LEFT),
    		"4,1", Set.of(Direction.UP, Direction.LEFT, Direction.RIGHT),
    		"5,1", Set.of(Direction.UP, Direction.RIGHT),
    		"6,1", Set.of(Direction.UP, Direction.LEFT),
            "7,1", Set.of(Direction.UP, Direction.DOWN, Direction.RIGHT),
            "8,1", Set.of(Direction.UP, Direction.DOWN),
            "9,1", Set.of(Direction.RIGHT, Direction.DOWN)
            );
    private final Map<String, Set<Direction>> building1_2Directional = Map.of();

    private void setupDirectionalCollisions() {
        directionalTiles.clear();
        if (currentMapPath.equals("/resources/maps/default/default.png")) {
            directionalTiles.putAll(defaultDirectional);
        } else if (currentMapPath.equals("/resources/maps/building_1/building_1.png")) {
            directionalTiles.putAll(building1Directional);
        } else if (currentMapPath.equals("/resources/maps/building_1_2/building_1_2.png")) {
            directionalTiles.putAll(building1_2Directional);
        }
    }

    private void loadBuildings() {
        try {
            currentMapPath = "/resources/maps/default/default.png";
            mapImage = ImageIO.read(getClass().getResourceAsStream(currentMapPath));
            overlayImage = ImageIO.read(getClass().getResourceAsStream("/resources/maps/default/defaultOverlay.png"));
            collisionMap = ImageIO.read(getClass().getResourceAsStream("/resources/maps/default/defaultCollision.png"));
            mapWidth = mapImage.getWidth();
            mapHeight = mapImage.getHeight();
        } catch (IOException e) {
            System.err.println("Failed to load default map.");
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

    private final List<NPC> defaultMapNPCs = List.of(
	    new NPC(25, 23, Direction.UP),
	    new NPC(6, 29, Direction.UP)
	);

	private final List<NPC> building1NPCs = List.of(
	    new NPC(3, 0, Direction.RIGHT)
	);

	private final List<NPC> building1_2NPCs = List.of(
	    new NPC(4, 3, Direction.UP)
	);

	private void setupNPCs() {
	    npcs.clear();
	    if (currentMapPath.equals("/resources/maps/default/default.png")) {
	        npcs.addAll(defaultMapNPCs);
	    } else if (currentMapPath.equals("/resources/maps/building_1/building_1.png")) {
	        npcs.addAll(building1NPCs);
	    } else if (currentMapPath.equals("/resources/maps/building_1_2/building_1_2.png")) {
	        npcs.addAll(building1_2NPCs);
	    }
	}


	private void setupDoors() {
	    doors.clear();

	 // Building 1 doors
	    doors.add(new Door(9, 23, Direction.UP, "/resources/maps/default/default.png",
	        "/resources/maps/building_1/building_1.png", "/resources/maps/building_1/building_1Collision.png",
	        "/resources/maps/building_1/building_1Overlay.png", 4, 4));

	    doors.add(new Door(6, 18, Direction.RIGHT, "/resources/maps/default/default.png",
	        "/resources/maps/building_1/building_1.png", "/resources/maps/building_1/building_1Collision.png",
	        "/resources/maps/building_1/building_1Overlay.png", 1, 0));

	    doors.add(new Door(4, 4, Direction.DOWN, "/resources/maps/building_1/building_1.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 9, 23));

	    doors.add(new Door(1, 0, Direction.LEFT, "/resources/maps/building_1/building_1.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",	
	        "/resources/maps/default/defaultOverlay.png", 6, 18));

	    doors.add(new Door(0, 8, Direction.UP, "/resources/maps/building_1/building_1.png",
	        "/resources/maps/building_1_2/building_1_2.png", "/resources/maps/building_1_2/building_1_2Collision.png",
	        "/resources/maps/building_1_2/building_1_2Overlay.png", 1, 6));

	    doors.add(new Door(1, 6, Direction.RIGHT, "/resources/maps/building_1_2/building_1_2.png",
	        "/resources/maps/building_1/building_1.png", "/resources/maps/building_1/building_1Collision.png",
	        "/resources/maps/building_1/building_1Overlay.png", 0, 8));


	    // Building 2
	    doors.add(new Door(13, 7, Direction.UP, "/resources/maps/default/default.png",
	        "/resources/maps/building_2/building_2.png", "/resources/maps/building_2/building_2Collision.png",
	        "/resources/maps/building_2/building_2Overlay.png", 1, 1));
	    doors.add(new Door(1, 1, Direction.DOWN, "/resources/maps/building_2/building_2.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png",13, 7));

	    // Building 3
	    doors.add(new Door(10, 33, Direction.UP, "/resources/maps/default/default.png",
	        "/resources/maps/building_3/building_3.png", "/resources/maps/building_3/building_3Collision.png",
	        "/resources/maps/building_3/building_3Overlay.png", 3, 2));
	    doors.add(new Door(10, 34, Direction.UP, "/resources/maps/default/default.png",
	        "/resources/maps/building_3/building_3.png", "/resources/maps/building_3/building_3Collision.png",
	        "/resources/maps/building_3/building_3Overlay.png", 3, 3));
	    doors.add(new Door(3, 2, Direction.DOWN, "/resources/maps/building_3/building_3.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 10, 33));
	    doors.add(new Door(3, 3, Direction.DOWN, "/resources/maps/building_3/building_3.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 10, 34));

	    // Building 4
	    doors.add(new Door(16, 24, Direction.RIGHT, "/resources/maps/default/default.png",
	        "/resources/maps/building_4/building_4.png", "/resources/maps/building_4/building_4Collision.png",
	        "/resources/maps/building_4/building_4Overlay.png", 0, 0));
	    doors.add(new Door(17, 24, Direction.RIGHT, "/resources/maps/default/default.png",
	        "/resources/maps/building_4/building_4.png", "/resources/maps/building_4/building_4Collision.png",
	        "/resources/maps/building_4/building_4Overlay.png", 1, 0));
	    doors.add(new Door(0, 0, Direction.LEFT, "/resources/maps/building_4/building_4.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 16, 24));
	    doors.add(new Door(1, 0, Direction.LEFT, "/resources/maps/building_4/building_4.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 17, 24));

	    // Building 5
	    doors.add(new Door(20, 22, Direction.LEFT, "/resources/maps/default/default.png",
	        "/resources/maps/building_5/building_5.png", "/resources/maps/building_5/building_5Collision.png",
	        "/resources/maps/building_5/building_5Overlay.png", 0, 2));
	    doors.add(new Door(21, 22, Direction.LEFT, "/resources/maps/default/default.png",
	        "/resources/maps/building_5/building_5.png", "/resources/maps/building_5/building_5Collision.png",
	        "/resources/maps/building_5/building_5Overlay.png", 1, 2));
	    doors.add(new Door(0, 2, Direction.RIGHT, "/resources/maps/building_5/building_5.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 20, 22));
	    doors.add(new Door(1, 2, Direction.RIGHT, "/resources/maps/building_5/building_5.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 21, 22));

	    // Building 6
	    doors.add(new Door(24, 24, Direction.RIGHT, "/resources/maps/default/default.png",
	        "/resources/maps/building_6/building_6.png", "/resources/maps/building_6/building_6Collision.png",
	        "/resources/maps/building_6/building_6Overlay.png", 0, 0));
	    doors.add(new Door(25, 24, Direction.RIGHT, "/resources/maps/default/default.png",
	        "/resources/maps/building_6/building_6.png", "/resources/maps/building_6/building_6Collision.png",
	        "/resources/maps/building_6/building_6Overlay.png", 1, 0));
	    doors.add(new Door(0, 0, Direction.LEFT, "/resources/maps/building_6/building_6.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 24, 24));
	    doors.add(new Door(1, 0, Direction.LEFT, "/resources/maps/building_6/building_6.png",
	        "/resources/maps/default/default.png", "/resources/maps/default/defaultCollision.png",
	        "/resources/maps/default/defaultOverlay.png", 25, 24));
	}


    private void teleportTo(String map, String collision, String roof, int row, int col) {
        fading = true;
        fadeOut = true;
        fadeAlpha = 0;
        onFadeComplete = () -> {
            try {
                currentMapPath = map;
                mapImage = ImageIO.read(getClass().getResourceAsStream(map));
                collisionMap = ImageIO.read(getClass().getResourceAsStream(collision));
                overlayImage = ImageIO.read(getClass().getResourceAsStream(roof));
                mapWidth = mapImage.getWidth();
                mapHeight = mapImage.getHeight();
                playerRow = row;
                playerCol = col;
                setupNPCs();
                setupDoors();
                setupDirectionalCollisions();
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

    private void gameLoop() {
    	loopStatus = "Running";

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
                    for (Door door : doors) {
                        if (playerRow == door.row && playerCol == door.col &&
                            input == door.requiredFacing && currentMapPath.equals(door.reqCurMap)) {
                        	if (debugPanel.isVisible()) {
                        	    debugPanel.logDoorEntry(playerRow, playerCol);
                        	}
                            teleportTo(door.destinationMap, door.destinationCollision, door.destinationOverlay,
                                       door.destRow, door.destCol);
                            return;
                        }
                    }

                    int nextCol = playerCol + (input == Direction.RIGHT ? 1 : input == Direction.LEFT ? -1 : 0);
                    int nextRow = playerRow + (input == Direction.DOWN ? 1 : input == Direction.UP ? -1 : 0);

                    if (isWalkable(playerCol, playerRow, nextCol, nextRow, input)) {
                        playerCol = nextCol;
                        playerRow = nextRow;
                        targetX = playerCol * tileSize + (tileSize / 2 - spriteWidth / 2);
                        targetY = (playerRow + 1) * tileSize - spriteHeight - 4;
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
        if (debugPanel.isVisible()) {
            debugPanel.updateDebugInfo(loopStatus, playerAction, playerRow, playerCol, currentMapPath);
        }
        loopStatus = "Idle";

    }

    private Direction getDirectionFromKeys() {
        if (pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_UP)) {
            playerAction = "Moving Up";
            return Direction.UP;
        }
        if (pressedKeys.contains(KeyEvent.VK_S) || pressedKeys.contains(KeyEvent.VK_DOWN)) {
            playerAction = "Moving Down";
            return Direction.DOWN;
        }
        if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_LEFT)) {
            playerAction = "Moving Left";
            return Direction.LEFT;
        }
        if (pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            playerAction = "Moving Right";
            return Direction.RIGHT;
        }
        playerAction = "Idle";
        return null;
    }


    private boolean isWalkable(int fromCol, int fromRow, int toCol, int toRow, Direction dir) {
        if (!collisionMapLoaded || collisionMap == null) return true;
        int x = toCol * tileSize + tileSize / 2;
        int y = toRow * tileSize + tileSize / 2;
        if (x < 0 || y < 0 || x >= collisionMap.getWidth() || y >= collisionMap.getHeight()) return false;

        int rgb = collisionMap.getRGB(x, y);
        Color color = new Color(rgb);
        if (!(color.getRed() > 200 && color.getGreen() > 200 && color.getBlue() > 200)) return false;

        String key = toCol + "," + toRow;
        return !directionalTiles.containsKey(key) || directionalTiles.get(key).contains(dir);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) shiftHeld = true;
        if (e.getKeyCode() == KeyEvent.VK_F11) toggleFullscreen();
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && isFullscreen) toggleFullscreen();
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (pressedKeys.contains(KeyEvent.VK_SHIFT)) {
            	if (debugPanel.isVisible()) {
                    debugPanel.setVisible(false);
                } else {
                    debugPanel.setVisible(true);
                    Point location = parentFrame.getLocationOnScreen();
                    int parentX = location.x;
                    int parentY = location.y;

                    int parentWidth = parentFrame.getWidth();
                    int debugWidth = debugPanel.getWidth();

                    debugPanel.setLocation(parentX + parentWidth - debugWidth, parentY);
                }
            } else {
                fullMapView = !fullMapView;
             }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) shiftHeld = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

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

        if (overlayLoaded && overlayImage != null) {
            wg.drawImage(overlayImage, 0, 0, null);
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
            int drawWidth = (int)(mapWidth * zoom);
            int drawHeight = (int)(mapHeight * zoom);

            if (drawWidth <= screenWidth && drawHeight <= screenHeight) {
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