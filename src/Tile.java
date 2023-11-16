import java.io.Serializable;

public class Tile implements Serializable {
	private static final long serialVersionUID = -4747467725986366242L;
	private final int x, y;
	private String content;
	
	/** Create a new Tile instance
	 * @param x The x coordinate of this tile in the array
	 * @param y The y coordinate of this tile in the array
	 */
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/** Sets the content of the tile to the given content, following the isText variable
	 * @param content The new content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/** Returns the tile to the right of this tile, or null if there is none
	 * @param tiles A reference to the tile array
	 * @return The tile, or null
	 */
	public Tile getRight(Tile[][] tiles) {
		if(x == tiles[0].length-1) {
			return null;
		}
		
		return tiles[x+1][y];
	}
	
	/** Returns the tile to the left of this tile, or null if there is none
	 * @param tiles A reference to the tile array
	 * @return The tile, or null
	 */
	public Tile getLeft(Tile[][] tiles) {
		if(x == 0) {
			return null;
		}
		
		return tiles[x-1][y];
	}
	
	/** Returns the tile above this tile, or null if there is none
	 * @param tiles A reference to the tile array
	 * @return The tile, or null
	 */
	public Tile getUp(Tile[][] tiles) {
		if(y == 0) {
			return null;
		}
		
		return tiles[x][y-1];
	}
	
	/** Returns the tile below this tile, or null if there is none
	 * @param tiles A reference to the tile array
	 * @return The tile, or null
	 */
	public Tile getDown(Tile[][] tiles) {
		if(y == tiles.length-1) {
			return null;
		}
		
		return tiles[x][y+1];
	}
	
	/** Get the currently stored content of the Tile
	 * @return The content
	 */
	public String getContent() {
		return content;
	}
}
