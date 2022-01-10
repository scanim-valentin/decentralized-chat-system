package fr.insa.chatSystem.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HelpWindow extends JFrame {

	/**
	 * Déclarations
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	

	/**
	 * Create the frame.
	 */
	public HelpWindow() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame("Chat System V1.0 - Help");
		ImageIcon icon = new ImageIcon("./Images/helpchat.png");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(new JLabel(icon));

		// Display the windon	
		frame.pack();
		frame.setVisible(true);
	}
}
