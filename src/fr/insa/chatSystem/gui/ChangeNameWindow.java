package fr.insa.chatSystem.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.insa.chatSystem.controller.DistributedDataController;
import fr.insa.chatSystem.controller.MainController.result;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
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
	public ChangeNameWindow(String username, JLabel pseudoLabel) {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		frame.setTitle("Chat System V1.0");
		contentPane.setBackground(new Color(255, 215, 0));
		contentPane.setForeground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 13));
		lblNewLabel_1.setForeground(Color.RED);
		lblNewLabel_1.setBounds(116, 64, 209, 36);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(16, 6, 78, 71);
		lblNewLabel_2.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("");
		lblNewLabel_2_1.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		lblNewLabel_2_1.setBounds(16, 175, 85, 77);
		contentPane.add(lblNewLabel_2_1);

		JLabel lblNewLabel_2_1_1 = new JLabel("");
		lblNewLabel_2_1_1.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		lblNewLabel_2_1_1.setBounds(356, 175, 76, 77);
		contentPane.add(lblNewLabel_2_1_1);

		JLabel lblNewLabel_2_1_2 = new JLabel("");
		lblNewLabel_2_1_2.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		lblNewLabel_2_1_2.setBounds(356, 6, 76, 71);
		contentPane.add(lblNewLabel_2_1_2);

		JButton btnNewButton = new JButton("Change Name");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textField.getText();
				result R = DistributedDataController.setUsername(username);
				switch (R) {
				case INVALID_CONTENT:
					lblNewLabel_1.setText("Empty field !");
					break;

				case ALREADY_EXISTS:
					lblNewLabel_1.setText("Username already exists !");
					break;

				default:
					frame.dispose();
					// Changer le nom de l'username dans la fenetre
					pseudoLabel.setText(username);
					// Notifie les autres utilisateur d'un changement de pseudo
					DistributedDataController.notifyNewName(username);
					;
					break;
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.setBounds(133, 175, 171, 46);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel = new JLabel("Enter new username :");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(106, 22, 229, 53);
		contentPane.add(lblNewLabel);

		JButton btnNewButton_1 = new JButton("CANCEL");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Fermer la fenetre Change Name
				frame.dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnNewButton_1.setForeground(Color.RED);
		btnNewButton_1.setBounds(164, 233, 117, 29);
		contentPane.add(btnNewButton_1);

		textField = new JTextField();
		textField.setBounds(106, 103, 229, 46);
		contentPane.add(textField);
		textField.setColumns(20);

		// Display the frame
		frame.setVisible(true);
	}
}