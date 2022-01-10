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
		Label.setFont(new Font("Comic Sans MS", Font.BOLD, 60));
		frame.getContentPane().add(Label);

		ButtonExit = new JButton("Exit");
		ButtonExit.setBounds(332, 225, 105, 30);
		ButtonExit.setForeground(new Color(255, 0, 0));
		ButtonExit.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		ButtonExit.setBackground(Color.CYAN);
		ButtonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // arrete le programme
			}
		});

		JButton ButtonConnexion = new JButton("Connexion");
		ButtonConnexion.setBounds(151, 189, 123, 32);
		ButtonConnexion.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		ButtonConnexion.setForeground(new Color(51, 0, 255));
		ButtonConnexion.setHorizontalAlignment(SwingConstants.LEFT);
		ButtonConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = textFieldName.getText();
				result R = DistributedDataController.setUsername(username);
				switch (R) {
				case INVALID_CONTENT:
					ZoneResponse.setText("Empty field !");
					break;

				case ALREADY_EXISTS:
					ZoneResponse.setText("Username already exists !");
					break;

				default:
					//Close frame
					frame.dispose();
					//Vérifier le username
					
					//Open le chat window
					new ChatWindow(username, null);
					
					// Lancement du client
					ChattingSessionController.start_deamon();
					
					// Notifie les autres utilisateur d'une connection
					DistributedDataController.notifyConnection();
					break;
				}
			}
		});

		LabelLogin = new JLabel("Login :");
		LabelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		LabelLogin.setBounds(22, 149, 65, 26);
		LabelLogin.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		frame.getContentPane().add(LabelLogin);

		textFieldName = new JTextField(); // Zone texte avec le login
		textFieldName.setBounds(93, 151, 242, 26);
		textFieldName.setColumns(30);
		frame.getContentPane().add(textFieldName);
		frame.getContentPane().add(ButtonConnexion);
		frame.getContentPane().add(ButtonExit);

		JLabel Logo = new JLabel("");
		Logo.setHorizontalAlignment(SwingConstants.CENTER);
		Logo.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		Logo.setBounds(332, 11, 112, 126);
		frame.getContentPane().add(Logo);

		ZoneResponse = new JLabel("Entrer login");
		ZoneResponse.setForeground(Color.GRAY);
		ZoneResponse.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		ZoneResponse.setHorizontalAlignment(SwingConstants.CENTER);
		ZoneResponse.setBounds(99, 118, 221, 32);
		frame.getContentPane().add(ZoneResponse);

		// Display the window
		frame.setVisible(true);
	}
}