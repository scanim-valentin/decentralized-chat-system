package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.insa.chatSystem.controller.ChattingSessionController;
import fr.insa.chatSystem.controller.DistributedDataController;
import fr.insa.chatSystem.controller.MainController.result;

public class ConnectWindow extends JFrame {

	/**
	 * Déclaration de variable
	 */
	private static final long serialVersionUID = 1L;
	public static ChatWindow MainWindow;
	private JFrame frame;
	private JButton ButtonExit;
	private JTextField textFieldName;
	private JLabel Label;
	private JLabel LabelLogin;
	private JLabel ZoneResponse;

	/**
	 * Create the application
	 * 
	 * @param pseudo
	 */
	public ConnectWindow(String username) {
		// Lancement de la fenetre de connexion
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		Label = new JLabel("Welcome");
		Label.setBounds(18, 11, 331, 107);
		Label.setHorizontalAlignment(SwingConstants.CENTER);
		Label.setForeground(new Color(153, 0, 255));
		Label.setFont(new Font("Times New Roman", Font.BOLD, 56));

		ButtonExit = new JButton("Exit");
		ButtonExit.setBounds(332, 225, 105, 30);
		ButtonExit.setForeground(new Color(255, 0, 0));
		ButtonExit.setFont(new Font("Times New Roman", Font.BOLD, 14));
		ButtonExit.setBackground(Color.CYAN);
		ButtonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // arrete le programme
			}
		});

		JButton ButtonConnexion = new JButton("Connexion");
		ButtonConnexion.setBounds(151, 189, 123, 32);
		ButtonConnexion.setFont(new Font("Times New Roman", Font.BOLD, 14));
		ButtonConnexion.setForeground(new Color(51, 0, 255));
		ButtonConnexion.setHorizontalAlignment(SwingConstants.LEFT);
		ButtonConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textFieldName.getText();
				// Fonction de vérification du username
				result R = DistributedDataController.setUsername(username);
				switch (R) {
				case INVALID_CONTENT:
					ZoneResponse.setText("Empty field !");
					break;

				case ALREADY_EXISTS:
					ZoneResponse.setText("Username already exists !");
					break;

				default:
					ZoneResponse.setText("Username OK !");
					// Attendre 2sec pour voir le message OK pour le pseudo
					// MainController.wait(2000);

					// Close frame
					frame.dispose();

					// Open le chat window
					new ChatWindow(username, null);
					break;
				}
			}
		});

		LabelLogin = new JLabel("Login :");
		LabelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		LabelLogin.setBounds(22, 149, 65, 26);
		LabelLogin.setFont(new Font("Times New Roman", Font.BOLD, 13));

		textFieldName = new JTextField(); // Zone texte avec le login
		textFieldName.setBounds(93, 151, 242, 26);
		textFieldName.setColumns(30);

		JLabel Logo = new JLabel("");
		Logo.setHorizontalAlignment(SwingConstants.CENTER);
		Logo.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		Logo.setBounds(332, 11, 112, 126);

		ZoneResponse = new JLabel("Entrer login");
		ZoneResponse.setForeground(Color.GRAY);
		ZoneResponse.setFont(new Font("Times New Roman", Font.BOLD, 13));
		ZoneResponse.setHorizontalAlignment(SwingConstants.CENTER);
		ZoneResponse.setBounds(99, 118, 221, 32);

		// Tous les frames add
		frame.setMinimumSize(new Dimension(450, 300));
		frame.getContentPane().add(Label);
		frame.getContentPane().add(textFieldName);
		frame.getContentPane().add(ButtonConnexion);
		frame.getContentPane().add(ButtonExit);
		frame.getContentPane().add(Logo);
		frame.getContentPane().add(ZoneResponse);
		frame.getContentPane().add(LabelLogin);
		// Display the window
		frame.setVisible(true);
	}
}