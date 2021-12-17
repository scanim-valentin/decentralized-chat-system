package fr.insa.chatSystem.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangeNameWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame frame;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public ChangeNameWindow(String username) {
		
		frame = new JFrame(); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		frame.setResizable(false); // Ne pas changer la taille de la fenetre
		frame.setTitle("Chat System V1.0");
		contentPane.setBackground(new Color(255, 215, 0));
		contentPane.setForeground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Change Name");
		btnNewButton.addActionListener(new ActionListener() {
			private AbstractButton lblNewLabel_1;

			public void actionPerformed(ActionEvent e) {
				String username;
				if (!("").equals(username = textField.getText())) {
					frame.dispose();			
					new ChatWindow(username);
				} else {lblNewLabel_1 = null;
				lblNewLabel_1.setText("ERROR!");}}
		});
		btnNewButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.setBounds(154, 175, 136, 46);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Enter new username :");
		lblNewLabel.setForeground(SystemColor.controlHighlight);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(106, 38, 229, 53);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("CANCEL");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnNewButton_1.setForeground(Color.RED);
		btnNewButton_1.setBounds(164, 233, 117, 29);
		contentPane.add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(106, 103, 229, 46);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBounds(347, 106, 85, 36);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(16, 6, 78, 71);
		lblNewLabel_2.setIcon( new ImageIcon("/Users/lauricmarthrin-john/git/decentralized-chat-system/Images/MSN-icon.png"));
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("");
		lblNewLabel_2_1.setIcon(new ImageIcon("/Users/lauricmarthrin-john/git/decentralized-chat-system/Images/MSN-icon.png"));
		lblNewLabel_2_1.setBounds(16, 175, 85, 77);
		contentPane.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("");
		lblNewLabel_2_1_1.setIcon(new ImageIcon("/Users/lauricmarthrin-john/git/decentralized-chat-system/Images/MSN-icon.png"));
		lblNewLabel_2_1_1.setBounds(356, 175, 76, 77);
		contentPane.add(lblNewLabel_2_1_1);
		
		JLabel lblNewLabel_2_1_2 = new JLabel("");
		lblNewLabel_2_1_2.setIcon(new ImageIcon("/Users/lauricmarthrin-john/git/decentralized-chat-system/Images/MSN-icon.png"));
		lblNewLabel_2_1_2.setBounds(356, 6, 76, 71);
		contentPane.add(lblNewLabel_2_1_2);
		
		// Display the widow
		//frame.add(contentPane);
		frame.setVisible(true);
	}
}
