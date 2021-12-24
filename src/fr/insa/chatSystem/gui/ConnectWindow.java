package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import fr.insa.chatSystem.controller.DistributedDataController;
import fr.insa.chatSystem.controller.MainController.result;

public class ConnectWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JButton btnNewButton_1;
	private JTextField textField;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;

	/**
	 * Create the application.
	 * @param pseudo
	 */
	public ConnectWindow(String username) {
		initialize(); // fenetre de connexion
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setResizable(false); // Ne pas changer la taille de la fenetre
		frame.setTitle("Chat System V1.0");
		frame.getContentPane().setEnabled(false);
		frame.getContentPane().setBackground(new Color(153, 255, 51));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		lblNewLabel = new JLabel("Welcome");
		lblNewLabel.setBounds(18, 11, 331, 107);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(153, 0, 255));
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
		frame.getContentPane().add(lblNewLabel);

		btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.setBounds(332, 225, 105, 30);
		btnNewButton_1.setForeground(new Color(255, 0, 0));
		btnNewButton_1.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		btnNewButton_1.setBackground(Color.CYAN);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // arrete le programme
			}
		});

		JButton btnNewButton = new JButton("Connexion");
		btnNewButton.setBounds(151, 189, 123, 32);
		btnNewButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		btnNewButton.setForeground(new Color(51, 0, 255));
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		// Action de la connexion
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				result R = DistributedDataController.changeUsername(username);
				switch(R) {
				case INVALID_CONTENT :
					lblNewLabel_3.setText("Invalid content !");
					break;
					
				case ALREADY_EXISTS : 
					lblNewLabel_3.setText("Username already exists !");
					break; 
					
				default : 
					frame.dispose();			
					new ChatWindow(username);
					break; 
				}
			}
		});

		lblNewLabel_1 = new JLabel("Login :");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(22, 149, 65, 26);
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		frame.getContentPane().add(lblNewLabel_1);

		textField = new JTextField(); // Zone texte avec le login
		textField.setBounds(93, 151, 242, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(30);
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(btnNewButton_1);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setIcon( new ImageIcon("./Images/MSN-icon.png"));
		lblNewLabel_2.setBounds(332, 11, 112, 126);
		frame.getContentPane().add(lblNewLabel_2);

		lblNewLabel_3 = new JLabel("Entrer login");
		lblNewLabel_3.setForeground(Color.GRAY);
		lblNewLabel_3.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(99, 118, 221, 32);
		frame.getContentPane().add(lblNewLabel_3);

		// Display the widow
		frame.setVisible(true);
	}
}