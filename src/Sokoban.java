import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

public class Sokoban extends Game {
	private static final String PATH = "sokoban_icons/";
	private static final String BLANK = PATH + "blank.png";
	private static final String BLANKMARKED = PATH + "blankmarked.png";
	private static final String CRATE = PATH + "crate.png";
	private static final String CRATEMARKED = PATH + "cratemarked.png";
	private static final String PLAYER = PATH + "player.png";
	private static final String WALL = PATH + "wall.png";
	
	private static final String BOX_MOVE_FILE = "audio/box_move.wav";
	private static final String MARKED_TILE_FILE = "audio/marked_tile.wav";

	private int playerPosX, playerPosY;
	private static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	private static final int BOX_MOVE = 0, MARKED_TILE = 1;

	private boolean markedTiles[][];
	private int totalMarkedTiles, placedMarkedTiles;
	
	/** Creates a Game instance with all the necessary boiler-plate code for Sokoban
	 * @param level The level to be loaded
	 */
	public Sokoban(int level) {
		super("Sokoban", getMapSize(level), 32, false);
		markedTiles = new boolean[getMapSize(level)][getMapSize(level)];
		
		totalMarkedTiles = 0;
		placedMarkedTiles = 0;

		attachSoundSystem(new SokobanSoundSystem());
		
		attachTileView(new GraphicView("Sokoban", getMapSize(level), 32, false));
		attachTileView(new SokobanTextView());

		loadLevel(level);
	}

	/** Gets the map size of the given level
	 * @param level The level to look up
	 * @return The map size
	 */
	private static int getMapSize(int level) {
		switch(level) {
			case 1:
			default:
				return 11;
			case 2:
			case 3:
				return 13;
		}
	}
	
	/** Updates the player position and resets the old player position to the appropriate sprite
	 * @param x The x position
	 * @param y The y position
	 */
	private void setPlayerPos(int x, int y) {
		if(markedTiles[playerPosX][playerPosY] == true) {
			super.setTileContent(tiles[playerPosX][playerPosY], BLANKMARKED);
		} else {
			super.setTileContent(tiles[playerPosX][playerPosY], BLANK);
		}
		
		playerPosX = x;
		playerPosY = y;
		super.setTileContent(tiles[x][y], PLAYER);
	}
	
	/** Checks whether the player can move in the given direction and moves them, moves any potential boxes and checks for the win
	 * @param direction The direction to move in
	 */
	private void move(int direction) {
		if(!canMove(direction)) {
			return;
		}
		
		Tile next = null;
		Tile nextNext = null;
		
		//Coordinates of next 		= playerPosX+deltaX, playerPosY+deltaY
		//Coordinates of nextNext 	= playerPosX+2*deltaX, playerPosY+2*deltaY
		int deltaX = 0;
		int deltaY = 0;
		
		switch(direction) {
			case UP:
				next = tiles[playerPosX][playerPosY].getUp(tiles);
				if(next != null) {
					nextNext = next.getUp(tiles);
				}
				deltaY = -1;
				break;
			case DOWN:
				next = tiles[playerPosX][playerPosY].getDown(tiles);
				if(next != null) {
					nextNext = next.getDown(tiles);
				}
				deltaY = +1;
				break;
			case LEFT:
				next = tiles[playerPosX][playerPosY].getLeft(tiles);
				if(next != null) {
					nextNext = next.getLeft(tiles);
				}
				deltaX = -1;
				break;
			case RIGHT:
				next = tiles[playerPosX][playerPosY].getRight(tiles);
				if(next != null) {
					nextNext = next.getRight(tiles);
				}
				deltaX = +1;
				break;
		}

		//If the nextNext tile is marked, update the placedMarkedTiles variable to avoid prematurely ending the game
		if(tiles[playerPosX+deltaX][playerPosY+deltaY].getContent().equals(CRATEMARKED)) {
			changePlacedTiles(-1);
		}

		
		if(next.getContent().equals(CRATE) || next.getContent().equals(CRATEMARKED)) {
			setPlayerPos(playerPosX+deltaX, playerPosY+deltaY);
			if(markedTiles[playerPosX+deltaX][playerPosY+deltaY] == true) {
				super.setTileContent(nextNext, CRATEMARKED);
				changePlacedTiles(+1);
				callSoundEvents(MARKED_TILE);
			} else {
				super.setTileContent(nextNext, CRATE);
				callSoundEvents(BOX_MOVE);
			}

			return;
		}

		setPlayerPos(playerPosX+deltaX, playerPosY+deltaY);
	}
	
	/**	Checks whether the player can move in the given direction, checking for boxes, walls and the map border
	 * @param direction The direction to check
	 * @return Whether the player can move
	 */
	private boolean canMove(int direction) {
		Tile next = null;
		Tile nextNext = null;
		
		switch(direction) {
			case UP:
				next = tiles[playerPosX][playerPosY].getUp(tiles);
				if(next != null) {
					nextNext = next.getUp(tiles);
				}
				break;
			case DOWN:
				next = tiles[playerPosX][playerPosY].getDown(tiles);
				if(next != null) {
					nextNext = next.getDown(tiles);
				}
				break;
			case LEFT:
				next = tiles[playerPosX][playerPosY].getLeft(tiles);
				if(next != null) {
					nextNext = next.getLeft(tiles);
				}
				break;
			case RIGHT:
				next = tiles[playerPosX][playerPosY].getRight(tiles);
				if(next != null) {
					nextNext = next.getRight(tiles);
				}
				break;
		}
		
		//On border
		if(next == null) {
			return false;
		}
		
		String nextContent = next.getContent();
		String nextNextContent = nextNext == null ? "" : nextNext.getContent();
		
		//If next space is empty
		if(nextContent.equals(BLANK) || nextContent.equals(BLANKMARKED)) {
			return true;
		}
		
		//If next is crate and nextNext is empty
		if((nextContent.equals(CRATE) || nextContent.equals(CRATEMARKED)) && (nextNextContent.equals(BLANK) || nextNextContent.equals(BLANKMARKED))) {
			return true;
		}
		
		return false;
	}
	
	/**	Initializes the level by reading the data stored on disk
	 * @param level The ID of the level
	 */
	private void loadLevel(int level) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("levels/" + level + ".txt")), Charset.forName("UTF-8")));

			String val = BLANK;
			int c;

			for(int i = 0; i < tiles.length; i++) {
				for(int j = 0; j < tiles.length; j++) {
					while((c = reader.read()) == '\n');

					switch(c) {
						case ' ':
							val = BLANK;
							break;
						case '#':
							val = WALL;
							break;
						case '@':
							val = PLAYER;
							break;
						case '*':
							val = CRATEMARKED;
							break;
						case '.':
							val = BLANKMARKED;
							break;
						case '$':
							val = CRATE;
							break;
					}

					if(val.equals(BLANKMARKED) || val.equals(CRATEMARKED)) {
						totalMarkedTiles++;
						markedTiles[j][i] = true;
						super.setTileContent(tiles[j][i], BLANKMARKED);
					} else if(val.equals(PLAYER)) {
						playerPosX = j;
						playerPosY = i;
						setPlayerPos(j, i);
					} else {
						super.setTileContent(tiles[j][i], val);
					}
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Keeps track of the placed tiles occupied by boxes, and subsequently checks for the win condition
	 * @param delta The amount to change with (+1 for a newly placed box, -1 for a box removed)
	 */
	private void changePlacedTiles(int delta) {
		placedMarkedTiles += delta;
		
		if(placedMarkedTiles == totalMarkedTiles) {
			inform("You win!");
			exit();
			//Restart the game
			Main.main(null);
		}
	}

	/**
	 * Serializes and writes all essential local variables to the Sokoban save file
	 */
	@Override
	protected void save() {
		try {
            FileOutputStream fileOut = new FileOutputStream(SAVE_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tiles);
			out.writeObject(markedTiles);
			out.writeObject(totalMarkedTiles);
			out.writeObject(placedMarkedTiles);
			out.writeObject(playerPosX);
			out.writeObject(playerPosY);
            out.close();
            fileOut.close();
        } catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads and de-serializes all essential local variables from the Sokoban save file
	 */
	@Override
	protected void load() {
		try {
            FileInputStream fileIn = new FileInputStream(SAVE_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tiles = (Tile[][]) in.readObject();
			markedTiles = (boolean[][]) in.readObject();
			totalMarkedTiles = (int) in.readObject();
			placedMarkedTiles = (int) in.readObject();
			playerPosX = (int) in.readObject();
			playerPosY = (int) in.readObject();
            in.close();
            fileIn.close();

			setPlayerPos(playerPosX, playerPosY);
			refreshTiles();
        } catch(IOException | ClassNotFoundException i) {
            i.printStackTrace();
            return;
        }
	}
	
	/**
	 * Move up
	 */
	@Override
	public void onUp() {
		move(UP);
	}

	/**
	 * Move down
	 */
	@Override
	public void onDown() {
		move(DOWN);
	}

	/**
	 * Move left
	 */
	@Override
	public void onLeft() {
		move(LEFT);
	}

	/**
	 * Move right
	 */
	@Override
	public void onRight() {
		move(RIGHT);
	}



	private class SokobanTextView implements TileView {
		/** Goes through all tiles in the list and prints them to the System.out PrintStream as characters
		 * @param tiles The list of tiles
		 */
		@Override
    	public void refresh(Tile[][] tiles) {
    	    System.out.println("\n-------------------\n");
    	    for(int i = 0; i < tiles.length; i++) {
    	        for(int j = 0; j < tiles.length; j++) {
					String c = "";
					String currentTileContent = tiles[j][i].getContent();

					if(currentTileContent == null) {
						System.out.println();
						return;
					}

					if(currentTileContent.equals(BLANK)) {
						c = " ";
					} else if(currentTileContent.equals(BLANKMARKED)) {
						c = ".";
					} else if(currentTileContent.equals(CRATE)) {
						c = "$";
					} else if(currentTileContent.equals(CRATEMARKED)) {
						c = "*";
					} else if(currentTileContent.equals(PLAYER)) {
						c = "@";
					} else if(currentTileContent.equals(WALL)) {
						c = "#";
					}

					System.out.print(c);
    	        }
    	        System.out.println();
    	    }
    	}

		@Override
		public void exit() {}
	}
	
	private class SokobanSoundSystem implements SoundSystem {
		/** Plays the selected audio file
		 * @param eventID The sound to call, should either be BOX_MOVE or MARKED_TILE
		 */
		@Override
		public void soundEvent(int eventID) {
			switch(eventID) {
				case BOX_MOVE:
					Game.playAudio(BOX_MOVE_FILE);
					break;
				case MARKED_TILE:
					Game.playAudio(MARKED_TILE_FILE);
					break;
				default:
					break;
			}
		}
	}
}
