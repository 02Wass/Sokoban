import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TwentyFortyEight extends Game {

    private static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
    
    private class TwentyFortyEightView implements TileView {
		/** Goes through all tiles in the list and prints them to the System.out PrintStream as characters
		 * @param tiles The list of tiles
		 */
    	@Override
    	public void refresh(Tile[][] tiles) {
        	System.out.println("\n-------------------\n");
        	for(int i = 0; i < tiles.length; i++) {
            	for(int j = 0; j < tiles[0].length; j++) {
                	String c = tiles[j][i].getContent();
					if(c.equals("")) {
						c = " ";
					}
                    System.out.print(c + " ");
            	}
            	System.out.println();
        	}
    	}

        @Override
        public void exit() {}
    }
    
    /**
     * Creates a Game instance with all the necessary boiler-plate code for 2048
     */
    public TwentyFortyEight() {
        super("2048", 4, 100, true);
        
        GraphicView graphicView = new GraphicView("2048", 4, 100, true);
        graphicView.setFontSize(25);

        attachTileView(graphicView);
        attachTileView(new TwentyFortyEightView());

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                tiles[j][i].setContent("");
            }
        }

        addRandomTiles();
    }

    /**
     * Loops through all tiles in the board and fills random empty tiles, after which it checks for the lose condition
     */
    private void addRandomTiles() {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(getTileContent(tiles[j][i]) != 0) {
                    continue;
                }

                if(Math.random() < 0.1) {
                    double a = Math.random(); 
                    if(a < 0.2) {
                        setTileContent(tiles[j][i], 4);
                    } else if(a < 0.5) {
                        setTileContent(tiles[j][i], 2);
                    } else {
                        setTileContent(tiles[j][i], 1);
                    }
                }
            }
        }
        
        
        int content;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
            	content = getTileContent(tiles[j][i]);
            	
            	if(content == 0 || content == getTileContent(tiles[j][i].getRight(tiles)) || content == getTileContent(tiles[j][i].getDown(tiles))) {
            		return;
            	}
            }
        }
        
        inform("You lose!");
        exit();
		Main.main(null);
    }

    /** Performs a move on the board in the given direction, merging and checking for the win condition, and refilling the board afterwards
     * @param direction The direction to move in
     */
    private void move(int direction) {
        Tile current, previous;
        int currentContent, previousContent;

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                switch(direction) {
                    case UP:
                    default:
                        current = tiles[j][i];
                        break;

                    case DOWN:
                        current = tiles[j][3-i];
                        break;

                    case LEFT:
                        current = tiles[j][i];
                        break;

                    case RIGHT:
                        current = tiles[3-j][i];
                        break;
                }

                previous = getPreviousTile(current, direction);

                for(int k = 0; k < 4; k++) {
                    if(previous == null) {
                        break;
                    }

                    currentContent = getTileContent(current);
                    previousContent = getTileContent(previous);

                    if(previousContent != 0) {
                    	if(currentContent == previousContent) {
                            setTileContent(previous, 2*currentContent);
                            setTileContent(current, 0);
                            
                            if(2*currentContent == 2048) {
                            	inform("You win!");
                                exit();
                        		Main.main(null);
                            }
                        }
                        break;
                    }

                    setTileContent(previous, currentContent);
                    setTileContent(current, 0);

                    current = previous;
                    previous = getPreviousTile(current, direction);
                }
            }
        }

        addRandomTiles();
    }

    /** Gets the previous tile (the tile next to the given tile in the given direction)
     * @param tile The reference tile
     * @param direction The direction to check
     * @return The tile
     */
    private Tile getPreviousTile(Tile tile, int direction) {
        switch(direction) {
            case UP:
            default:
                return tile.getUp(tiles);
            
            case DOWN:
                return tile.getDown(tiles);

            case LEFT:
                return tile.getLeft(tiles);

            case RIGHT:
                return tile.getRight(tiles);
        }
    }

    /** Sets the tile's String content to the given integer, or to an empty String if it is 0
     * @param tile The tile to write to
     * @param content The content
     */
    private void setTileContent(Tile tile, int content) {
        if(content == 0) {
            super.setTileContent(tile, "");
        } else {
            super.setTileContent(tile, content + "");
        }
    }

    /** Gets the tile's content encoded in an integer, with an empty tile or a null tile returning 0
     * @param tile The tile to read from
     * @return The content
     */
    private int getTileContent(Tile tile) {
        if(tile == null || tile.getContent().equals("")) {
            return 0;
        }

        return Integer.parseInt(tile.getContent());
    }

    /**
     * Serializes and writes the gameboard to the 2048 save file
     */
    @Override
    protected void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream(SAVE_FILE);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(tiles);
            out.close();
            fileOut.close();
        } catch(IOException e) {
			e.printStackTrace();
		}
    }

	/**
	 * Reads and de-serializes all essential local variables from the 2048 save file
	 */
    @Override
    protected void load() {
        try {
            FileInputStream fileIn = new FileInputStream(SAVE_FILE);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            tiles = (Tile[][]) in.readObject();
            in.close();
            fileIn.close();

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
}
