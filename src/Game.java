import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public abstract class Game {	
	private JFrame frame;
	private JPanel mainPanel, buttonArea;
	private JLabel label;
	private JButton saveButton, loadButton, controlButton;
	
	private ArrayList<TileView> tileViews;
	private ArrayList<SoundSystem> soundSystems;

	protected final String SAVE_FILE;
	
	protected Tile[][] tiles;
	
	private KeyListener defaultKeyListener = new KeyListener() {
		
	    @Override
	    public void keyTyped(KeyEvent e) {}
		
    	@Override
	    public void keyReleased(KeyEvent e) {
			int keyPress = e.getKeyCode();

    		if(keyPress == KeyEvent.VK_UP) {
    			onUp();
	    	} else if(keyPress == KeyEvent.VK_DOWN) {
		    	onDown();
			} else if(keyPress == KeyEvent.VK_LEFT) {
				onLeft();
			} else if(keyPress == KeyEvent.VK_RIGHT) {
				onRight();
			}
         }

        @Override
		public void keyPressed(KeyEvent e) {}
	};

	/** Creates the frame, sets up all Tiles and starts the game
	 * @param name The name of the frame
	 * @param size The amount of tiles in x and y direction
	 * @param tileSize The size of each tile in pixels
	 * @param isText Whether the content of the tiles is a String literal or a path to a png
	 */
	protected Game(String name, int size, int tileSize, boolean isText) {
		SAVE_FILE = "saves/" + name + ".ser";

		tiles = new Tile[size][size];
		tileViews = new ArrayList<>();
		soundSystems = new ArrayList<>();
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				tiles[j][i] = new Tile(j, i);
			}
		}
		
		frame = new JFrame(name + " controller");
		mainPanel = new JPanel(new BorderLayout());
		buttonArea = new JPanel();
		label = new JLabel(name + " controller");
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		controlButton = new JButton("Control");

		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		loadButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
		});
		
		addKeyListener(defaultKeyListener);

		buttonArea.add(controlButton);
		buttonArea.add(saveButton);
		buttonArea.add(loadButton);

		mainPanel.add(label, BorderLayout.CENTER);
		mainPanel.add(buttonArea, BorderLayout.SOUTH);
		
		frame.add(mainPanel);
		
		frame.setMinimumSize(new Dimension(300, 300));
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/** Attaches a TileView for visual display
	 * @param tileView The TileView to be added
	 */
	protected void attachTileView(TileView tileView) {
		tileViews.add(tileView);
	}
	
	/** Detaches the TileView from the list of TileViews
	 * @param tileView The TileView to be removed
	 */
	protected void detachTileView(TileView tileView) {
		tileViews.remove(tileView);
	}

	/** Attaches a SoundSystem for audio playback
	 * @param soundSystem The SoundSystem to be added
	 */
	protected void attachSoundSystem(SoundSystem soundSystem) {
		soundSystems.add(soundSystem);
	}

	/** Detaches the SoundSystem from the list of SoundSystems
	 * @param soundSystem
	 */
	protected void detachSoundSystem(SoundSystem soundSystem) {
		soundSystems.remove(soundSystem);
	}
	
	/** Sets the given Tile's content to the given content and subsequently informs all TileViews of the change
	 * @param tile The Tile to write to
	 * @param content The new content
	 */
	protected void setTileContent(Tile tile, String content) {
		tile.setContent(content);
		
		refreshTiles();
	}
	
	/**
	 * Tells all added TileViews to refresh
	 */
	protected void refreshTiles() {
		for(TileView tileView : tileViews) {
			tileView.refresh(tiles);
		}
	}

	/** Calls the soundEvent() function of all stored SoundSystems
	 * @param eventID The eventID to be passed on
	 */
	protected void callSoundEvents(int eventID) {
		for(SoundSystem s : soundSystems) {
			s.soundEvent(eventID);
		}
	}

	/** Shows a message dialog with the given text
	 * @param text The text to be presented
	 */
	protected void inform(String text) {
		JOptionPane.showMessageDialog(frame, text);
	}
	
	/** Plays the given wave file
	 * @param filename The path to the wave file
	 */
	public static void playAudio(String filename) {
	    try {
	        Clip clip = AudioSystem.getClip();
	        clip.open(AudioSystem.getAudioInputStream(new File(filename)));
	        clip.start();
	    } catch (Exception exc) {
	        exc.printStackTrace(System.out);
	    }
	}
	
	/**
	 * Destroys the frame (and thus exits the game)
	 */
	protected void exit() {
		for(TileView t : tileViews) {
			t.exit();
		}
		frame.dispose();
	}

	/**
	 * The code to run when the up button is pressed
	 */
	public abstract void onUp();
	
	/**
	 * The code to run when the down button is pressed
	 */
	public abstract void onDown();
	
	/**
	 * The code to run when the left button is pressed
	 */
	public abstract void onLeft();
	
	/**
	 * The code to run when the right button is pressed
	 */
	public abstract void onRight();
	
	/**
	 * The code to run when the Save button is pressed
	 */
	protected abstract void save();
	
	/**
	 * The code to run when the Load button is pressed
	 */
	protected abstract void load();
	
	/** Adds a MouseListener to the Game
	 * @param mL An instance of the MouseListener, preferably calling the onUp(), onDown(), onLeft(), onRight() functions
	 */
	public void addMouseListener(MouseListener mL) {
		controlButton.addMouseListener(mL);
	}
	
	/** Removes the given MouseListener from the Game
	 * @param mL The MouseListener to be removed
	 */
	public void removeMouseListener(MouseListener mL) {
		controlButton.removeMouseListener(mL);
	}
	
	/** Adds a KeyListener to the Game
	 * @param kL An instance of the KeyListener, preferably calling the onUp(), onDown(), onLeft(), onRight() functions
	 */
	public void addKeyListener(KeyListener kL) {
		controlButton.addKeyListener(kL);
	}
	
	/** Removes the given KeyListener from the Game
	 * @param kL The KeyListener to be removed
	 */
	public void removeKeyListener(KeyListener kL) {
		controlButton.removeKeyListener(kL);
	}
}