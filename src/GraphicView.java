import java.awt.*;
import javax.swing.*;

public class GraphicView implements TileView {
    private JFrame frame;
    private JPanel panel;
    private JLabel labels[][];
    
    private boolean isText;
    
	/** Creates the frame, sets up all Tiles and starts the game
	 * @param name The name of the frame
	 * @param size The amount of tiles in x and y direction
	 * @param tileSize The size of each tile in pixels
	 * @param isText Whether the content of the tiles is a String literal or a path to a png
	 */
	public GraphicView(String name, int size, int tileSize, boolean isText) {
		labels = new JLabel[size][size];
		
		this.isText = isText;
		
		frame = new JFrame(name);
        panel = new JPanel();
        panel.setLayout(new GridLayout(size, size));
        
        for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				labels[j][i] = new JLabel();
				labels[j][i].setMinimumSize(new Dimension(tileSize, tileSize));
				
				if(isText) {
					labels[j][i].setHorizontalAlignment(SwingConstants.CENTER);
					labels[j][i].setVerticalAlignment(SwingConstants.CENTER);
				}
				
				panel.add(labels[j][i]);
			}
		}
		
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setMinimumSize(new Dimension(size*tileSize, size*tileSize));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/** Sets the tile's font size (only relevant when running in text mode)
	 * @param size The new size of the font
	 */
	public void setFontSize(int size) {
       	for(int i = 0; i < labels.length; i++) {
            for(int j = 0; j < labels[0].length; j++) {
                labels[j][i].setFont(new Font("Arial", Font.PLAIN, size));
        	}
       	}
	}
	
	/** Goes through all JLabels in the JFrame and sets their content to the given Tiles' contents
	 * @param tiles The list of new tiles
	 */
	public void refresh(Tile[][] tiles) {
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[0].length; j++) {
                if(isText) {
                    labels[i][j].setText(tiles[i][j].getContent());
                } else {
                	//Only update the icons if it has changed to avoid slowdowns
					if(labels[i][j].getIcon() == null || !labels[i][j].getIcon().toString().equals(tiles[i][j].getContent())) {
						labels[i][j].setIcon(new ImageIcon(tiles[i][j].getContent()));
					}
                }
            }
        }
    }

	/**
	 * Closes the JFrame
	 */
	public void exit() {
		frame.dispose();
	}
}
