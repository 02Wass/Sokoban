public interface TileView {
    /** The code to be run after a Tile's content has been updated
     * @param tiles The new list of tiles
     */
    public abstract void refresh(Tile[][] tiles);
    
    /**
     * The code to be run on exit, such as potential clean-up code
     */
    public abstract void exit();
}
