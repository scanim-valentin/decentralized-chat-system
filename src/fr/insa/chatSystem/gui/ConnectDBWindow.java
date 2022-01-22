package fr.insa.chatSystem.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.Color;

public class ConnectDBWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField_Name;
	private JTextField textField_URL;
	private JPasswordField passwordField;

	/**
	 * Create the frame.
	 */
	public ConnectDBWindow() {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GREEN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);

		JButton btnNewButton = new JButton("Connect DB");
		btnNewButton.setForeground(Color.DARK_GRAY);
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnNewButton.setBounds(153, 191, 192, 39);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnNewButton);

		textField_Name = new JTextField();
		textField_Name.setBounds(167, 81, 164, 40);
		textField_Name.setText("tp_servlet_004");
		contentPane.add(textField_Name);
		textField_Name.setColumns(10);

		textField_URL = new JTextField();
		textField_URL.setBounds(167, 33, 164, 40);
		textField_URL.setText("jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/tp_servlet_004");
		contentPane.add(textField_URL);
		textField_URL.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(167, 126, 164, 40);
		passwordField.setText("ish6uo2U");
		contentPane.add(passwordField);

		JLabel lblNewLabel = new JLabel("Username :");
		lblNewLabel.setBounds(73, 87, 82, 29);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password :");
		lblNewLabel_1.setBounds(73, 132, 82, 28);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("URL DataBase :");
		lblNewLabel_2.setBounds(48, 40, 107, 26);
		contentPane.add(lblNewLabel_2);

		JLabel Logo = new JLabel("");
		Logo.setHorizontalAlignment(SwingConstants.CENTER);
		Logo.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		Logo.setBounds(332, 34, 112, 126);
		contentPane.add(Logo);

		frame.setVisible(true);
	}
}
