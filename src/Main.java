import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Main{
	private static JFrame startMenu;
	private static JButton sokoban;
	private static JButton twentyFortyEight;
	private static JPanel buttonArea;
	private static JPanel textArea;
	private static JLabel welcomeText;
	private static JComboBox<String> levelChooser;
	
    public static void main(String[] args){
        startMenu();
    }
    
    private static void startMenu() {
    	startMenu = new JFrame("Start Menu");
    	sokoban = new JButton("Sokoban");
    	twentyFortyEight = new JButton("2048");
    	buttonArea = new JPanel();
    	textArea = new JPanel();
    	welcomeText = new JLabel("Please choose your game below");
    	levelChooser = new JComboBox<>(new String[] {"Level 1", "Level 2", "Level 3"});
    	
    	Font font = new Font("Times New Roman", Font.BOLD, 20);
    	welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
    	welcomeText.setFont(font);
    	
    	sokoban.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Sokoban(levelChooser.getSelectedIndex()+1);
				startMenu.dispose();
			}
		});
    	
    	twentyFortyEight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new TwentyFortyEight();
				startMenu.dispose();
			}
		});
    	
    	//welcome text area
    	textArea.setLayout(new GridLayout(1, 1));
    	textArea.add(welcomeText);
    	
    	// button area for choosing game
    	buttonArea.setLayout(new GridLayout(1, 3));
    	buttonArea.add(sokoban);
		buttonArea.add(levelChooser);
    	buttonArea.add(twentyFortyEight);
    	
    	startMenu.setMinimumSize(new Dimension(400, 400));
    	startMenu.add(buttonArea, BorderLayout.SOUTH);
    	startMenu.add(welcomeText, BorderLayout.NORTH);
        startMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	startMenu.setVisible(true);
    }
}