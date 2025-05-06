package game;

public class changesFile {
	// this file tells you how to make the changes i made

	// 1. add these imports
	import java.util.LinkedList;
	import java.util.Queue;

	// 2. replace the entire npc class in your file with the one below

	private class NPC {
	    int row, col;
	    int pixelX, pixelY;
	    int targetX, targetY;
	    boolean moving = false;
	    int frame = 0;
	    int frameTick = 0;
	    Direction facing;
	    BufferedImage sprite;
	    boolean turning = false;
	    Direction turnTarget = null;
	    int turnFrameTick = 0;
	    int turnStage = 0;
	    int turnDelay = 2; 

	    Queue<Direction> movementQueue = new LinkedList<>();
	    Queue<Boolean> movementIsTurnOnly = new LinkedList<>();
	    int moveDelayFrames = 10;
	    int moveDelayCounter = 0;

	    NPC(int row, int col, Direction facing, String imagePath) {
	        this.row = row;
	        this.col = col;
	        this.facing = facing;
	        this.pixelX = col * tileSize + (tileSize / 2 - spriteWidth / 2);
	        this.pixelY = (row + 1) * tileSize - spriteHeight - 4;
	        this.targetX = pixelX;
	        this.targetY = pixelY;

	        try {
	            this.sprite = ImageIO.read(getClass().getResourceAsStream(imagePath));
	        } catch (IOException e) {
	            System.err.println("Failed to load NPC image: " + imagePath);
	        }
	    }

	    void draw(Graphics2D g) {
	        int frameIndex = moving ? frame : 1;
	        int spriteX = frameIndex * spriteWidth;
	        int spriteY = switch (facing) {
	            case DOWN -> 0;
	            case UP -> spriteHeight;
	            case LEFT -> spriteHeight * 2;
	            case RIGHT -> spriteHeight * 3;
	        };
	        g.drawImage(sprite, pixelX, pixelY, pixelX + spriteWidth, pixelY + spriteHeight,
	                    spriteX, spriteY, spriteX + spriteWidth, spriteY + spriteHeight, null);
	    }

	    void queueMovementWithTurnFlags(Direction[] steps, boolean[] isTurnOnly) {
	        for (int i = 0; i < steps.length; i++) {
	            movementQueue.add(steps[i]);
	            movementIsTurnOnly.add(isTurnOnly[i]);
	        }
	    }

	    void updateMovement() {
	        int speed = 4;
	        if (turning) {
	            turnFrameTick++;
	            if (turnFrameTick >= turnDelay) {
	                turnFrameTick = 0;
	                if (turnStage == 0) {
	                    frame = 0;
	                    turnStage++;
	                } else if (turnStage == 1) {
	                    this.facing = turnTarget;
	                    frame = 2;
	                    turnStage++;
	                } else {
	                    frame = 1; 
	                    turning = false;
	                }
	            }
	            return;
	        }

	        if (pixelX < targetX) pixelX = Math.min(targetX, pixelX + speed);
	        if (pixelX > targetX) pixelX = Math.max(targetX, pixelX - speed);
	        if (pixelY < targetY) pixelY = Math.min(targetY, pixelY + speed);
	        if (pixelY > targetY) pixelY = Math.max(targetY, pixelY - speed);

	        moving = (pixelX != targetX || pixelY != targetY);

	        if (moving) {
	            frameTick++;
	            if (frameTick >= frameDelay) {
	                frame = (frame + 1) % 4;
	                frameTick = 0;
	            }
	        } else {
	            frame = 1;
	            processNextMove();
	        }
	    }

	    void processNextMove() {
	        if (!movementQueue.isEmpty() && moveDelayCounter == 0) {
	            Direction dir = movementQueue.poll();
	            boolean turnOnly = movementIsTurnOnly.poll();
	            if (turnOnly) {
	                if (dir != this.facing) {
	                    this.turnTarget = dir;
	                    this.turning = true;
	                    this.turnStage = 0;
	                    this.turnFrameTick = 0;
	                }
	            }  else {
	                move(dir);
	            }
	            moveDelayCounter = moveDelayFrames;
	        } else if (moveDelayCounter > 0) {
	            moveDelayCounter--;
	        }
	    }

	    void move(Direction dir) {
	        if (moving) return;

	        int nextCol = col + (dir == Direction.RIGHT ? 1 : dir == Direction.LEFT ? -1 : 0);
	        int nextRow = row + (dir == Direction.DOWN ? 1 : dir == Direction.UP ? -1 : 0);
	        if (!isWalkable(col, row, nextCol, nextRow, dir)) return;

	        this.facing = dir;
	        this.col = nextCol;
	        this.row = nextRow;
	        this.targetX = col * tileSize + (tileSize / 2 - spriteWidth / 2);
	        this.targetY = (row + 1) * tileSize - spriteHeight - 4;
	        this.moving = true;
	    }

	    void queueMovement(Direction... steps) {
	        for (Direction step : steps) {
	            movementQueue.add(step);
	            movementIsTurnOnly.add(false); 
	        }
	    }

	    void clearQueue() {
	        movementQueue.clear();
	    }
	}

	// 3. add this line inside the gameLoop method, at the end
	
	//			it should come after these lines:
	//		} else {
	//		    frame = 3;
	//		}
		
	for (NPC npc : npcs) {
	    npc.updateMovement();
	}

	// this part is optional, but 4. add this at the keycode area to test npc movement on the main map
	if (e.getKeyCode() == KeyEvent.VK_T) {
	    for (NPC npc : npcs) {
	        if (npc.row == 25 && npc.col == 18) {
	            npc.queueMovement(Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN);
	        }
	    }
	}

// this should work, if it doesn't, call me.
