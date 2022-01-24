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
import fr.insa.chatSystem.controller.MainController;
import fr.insa.chatSystem.controller.MainController.result;
import fr.insa.chatSystem.controller.RemoteDatabaseController;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ConnectWindow extends JFrame {

	/**
	 * Déclaration de variable
	 */
	private static final long serialVersionUID = 1L;
	public static ChatWindow MainWindow;
	private JFrame frame;
	private JButton ButtonExit;
	private JTextField textFieldUsername;
	private JLabel Label;
	private JLabel LabelLogin;
	public JTextField textFieldURL;
	public JTextField textFieldDatabase;
	public JTextField textFieldPasswordDB;
	public JTextField textFieldID;
	public JTextField textFieldPassword;
	public JRadioButton rdbtnSignIn = new JRadioButton("Sign In");
	public JRadioButton rdbtnSignUp = new JRadioButton("Sign Up");
	public JCheckBox chckbxUseCentralizedHistory = new JCheckBox("Use centralized history database");
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
		frame.setBounds(100, 100, 450, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		Label = new JLabel("Welcome");
		Label.setBounds(18, 11, 331, 107);
		Label.setHorizontalAlignment(SwingConstants.CENTER);
		Label.setForeground(new Color(153, 0, 255));
		Label.setFont(new Font("Times New Roman", Font.BOLD, 56));

		ButtonExit = new JButton("Exit");
		ButtonExit.setBounds(333, 428, 105, 30);
		ButtonExit.setForeground(new Color(255, 0, 0));
		ButtonExit.setFont(new Font("Times New Roman", Font.BOLD, 14));
		ButtonExit.setBackground(Color.CYAN);
		ButtonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // arrete le programme
			}
		});

		JButton ButtonConnexion = new JButton("Connexion");
		ButtonConnexion.setBounds(160, 427, 123, 32);
		ButtonConnexion.setFont(new Font("Times New Roman", Font.BOLD, 14));
		ButtonConnexion.setForeground(new Color(51, 0, 255));
		ButtonConnexion.setHorizontalAlignment(SwingConstants.LEFT);
		ButtonConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxUseCentralizedHistory.isSelected()) {
					String username = textFieldUsername.getText();
					String database = textFieldDatabase.getText();
					String url = textFieldURL.getText();
					String password_db = textFieldPasswordDB.getText();
					String password = textFieldPassword.getText();
					
					if(rdbtnSignIn.isSelected()) {
						String id = textFieldID.getText();
						result R = MainController.useDatabaseSignIn(url,database,password_db,username,id,password);
						switch (R) {
							case INVALID_CONTENT:
								break;

							case ALREADY_EXISTS:
								break;

							case INVALID_DB_AUTH:
								break;

							default:
								frame.dispose();
								ChattingSessionController.start_deamon(); 
								// Open le chat window
								new ChatWindow(username, null);
								break;
								

						}
					}else {
						
					}
				}else {
					String username = textFieldUsername.getText();
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
						ChattingSessionController.start_deamon(); 
						// Open le chat window
						new ChatWindow(username, null);
						break;
					}
					}
			}
		});

		LabelLogin = new JLabel("Username :");
		LabelLogin.setHorizontalAlignment(SwingConstants.CENTER);
		LabelLogin.setBounds(22, 149, 84, 26);
		LabelLogin.setFont(new Font("Times New Roman", Font.BOLD, 13));

		textFieldUsername = new JTextField(); // Zone texte avec le login
		textFieldUsername.setBounds(124, 149, 242, 26);
		textFieldUsername.setColumns(30);

		JLabel Logo = new JLabel("");
		Logo.setHorizontalAlignment(SwingConstants.CENTER);
		Logo.setIcon(new ImageIcon("./Images/MSN-icon.png"));
		Logo.setBounds(332, 11, 112, 126);

		// Tous les frames add
		frame.setMinimumSize(new Dimension(450, 300));
		frame.getContentPane().add(Label);
		frame.getContentPane().add(textFieldUsername);
		frame.getContentPane().add(ButtonConnexion);
		frame.getContentPane().add(ButtonExit);
		frame.getContentPane().add(Logo);
		frame.getContentPane().add(LabelLogin);
		
		//Activation du mode base de donnée si checked
		chckbxUseCentralizedHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnSignUp.setEnabled(!rdbtnSignUp.isEnabled());
				rdbtnSignIn.setEnabled(!rdbtnSignIn.isEnabled());
				textFieldPassword.setEnabled(!textFieldPassword.isEnabled());
				textFieldID.setEnabled(!textFieldID.isEnabled());
				textFieldPasswordDB.setEnabled(!textFieldPasswordDB.isEnabled());
				textFieldDatabase.setEnabled(!textFieldDatabase.isEnabled());
				textFieldURL.setEnabled(!textFieldURL.isEnabled());
			}
		});
		chckbxUseCentralizedHistory.setBounds(32, 183, 334, 23);
		frame.getContentPane().add(chckbxUseCentralizedHistory);
		
		JLabel lblUrl = new JLabel("URL :");
		lblUrl.setHorizontalAlignment(SwingConstants.CENTER);
		lblUrl.setFont(new Font("Dialog", Font.BOLD, 13));
		lblUrl.setBounds(24, 249, 84, 26);
		frame.getContentPane().add(lblUrl);
		
		textFieldURL = new JTextField();
		textFieldURL.setEnabled(false);
		textFieldURL.setColumns(30);
		textFieldURL.setBounds(126, 249, 242, 26);
		textFieldURL.setText(RemoteDatabaseController.DB_URL) ; 
		frame.getContentPane().add(textFieldURL);
		
		JLabel lblDatabase = new JLabel("Database :");
		lblDatabase.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatabase.setFont(new Font("Dialog", Font.BOLD, 13));
		lblDatabase.setBounds(24, 277, 84, 26);
		frame.getContentPane().add(lblDatabase);
		
		textFieldDatabase = new JTextField();
		textFieldDatabase.setEnabled(false);
		textFieldDatabase.setColumns(30);
		textFieldDatabase.setBounds(126, 277, 242, 26);
		textFieldDatabase.setText(RemoteDatabaseController.DB_USER) ; 
		
		frame.getContentPane().add(textFieldDatabase);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 13));
		lblPassword.setBounds(24, 307, 84, 26);
		frame.getContentPane().add(lblPassword);
		
		textFieldPasswordDB = new JTextField();
		textFieldPasswordDB.setEnabled(false);
		textFieldPasswordDB.setColumns(30);
		textFieldPasswordDB.setBounds(126, 307, 242, 26);
		textFieldPasswordDB.setText(RemoteDatabaseController.DB_PASSWORD) ; 
		
		frame.getContentPane().add(textFieldPasswordDB);
		
		textFieldID = new JTextField();
		textFieldID.setEnabled(false);
		textFieldID.setColumns(30);
		textFieldID.setBounds(124, 356, 242, 26);
		frame.getContentPane().add(textFieldID);
		
		JLabel LabelLogin_3_1 = new JLabel("ID :");
		LabelLogin_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		LabelLogin_3_1.setFont(new Font("Dialog", Font.BOLD, 13));
		LabelLogin_3_1.setBounds(22, 356, 84, 26);
		frame.getContentPane().add(LabelLogin_3_1);
		
		textFieldPassword = new JTextField();
		textFieldPassword.setEnabled(false);
		textFieldPassword.setColumns(30);
		textFieldPassword.setBounds(124, 389, 242, 26);
		frame.getContentPane().add(textFieldPassword);
		
		JLabel LabelLogin_3_1_1 = new JLabel("Password :");
		LabelLogin_3_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		LabelLogin_3_1_1.setFont(new Font("Dialog", Font.BOLD, 13));
		LabelLogin_3_1_1.setBounds(22, 389, 84, 26);
		frame.getContentPane().add(LabelLogin_3_1_1);
		rdbtnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnSignUp.setSelected(false);
			}
		});
		
		rdbtnSignIn.setEnabled(false);
		rdbtnSignIn.setBounds(70, 218, 149, 23);
		frame.getContentPane().add(rdbtnSignIn);
		rdbtnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnSignIn.setSelected(false);
			}
		});
		
		rdbtnSignUp.setEnabled(false);
		rdbtnSignUp.setSelected(true);
		rdbtnSignUp.setBounds(231, 218, 149, 23);
		frame.getContentPane().add(rdbtnSignUp);
		
		ZoneResponse = new JLabel("This software will work as intended.");
		ZoneResponse.setForeground(Color.RED);
		ZoneResponse.setHorizontalAlignment(SwingConstants.CENTER);
		ZoneResponse.setFont(new Font("Dialog", Font.BOLD, 13));
		ZoneResponse.setBounds(18, 117, 420, 26);
		frame.getContentPane().add(ZoneResponse);
		// Display the window
		frame.setVisible(true);
	}
}