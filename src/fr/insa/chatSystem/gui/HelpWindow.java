package fr.insa.chatSystem.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;

public class HelpWindow extends JFrame {

	/**
	 * 
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
		
		frame = new JFrame();
		frame.setTitle("Chat System V1.0");
		frame.getContentPane().setEnabled(false);
		frame.getContentPane().setBackground(new Color(153, 255, 51));
		frame.setBounds(800, 50, 620, 840);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel HelpImage = new JLabel("");
		HelpImage.setHorizontalAlignment(SwingConstants.CENTER);
		HelpImage.setIcon(new ImageIcon("./Images/helpchat.png"));
		HelpImage.setBounds(6, 6, 607, 805);
		frame.getContentPane().add(HelpImage);
		
		// Display the windon
		frame.setVisible(true);
	}
}
