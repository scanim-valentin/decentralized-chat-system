package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import fr.insa.chatSystem.controller.*;
import fr.insa.chatSystem.model.*;

public class ChatWindow extends JFrame {

	/**
	 * JVM pour identifier les objets lorsqu'il les sérialise/désérialise
	 */
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	private JTextArea history_messages;
	private JButton btnSend;
	private JTextArea textArea;
	public JLabel nameUser;

	DefaultListModel<UserID> remote_users_list;
	JList<UserID> remote_users_jlist;

	/**
	 * Create the frame.
	 */
	public ChatWindow(String username, String message_content) {

		JFrame window = new JFrame();
		window.setResizable(false); // ne pas changer la taille de la fenetre
		window.setTitle("Chat System V1.0");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnSendFile = new JButton("Send File");
		btnSendFile.setForeground(new Color(0, 0, 255));
		btnSendFile.setBounds(574, 389, 120, 29);
		contentPane.add(btnSendFile);
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Envoie du fichier
				ChooseFile.SendFile(null);
			}
		});

		btnSend = new JButton("Send");
		btnSend.setForeground(new Color(0, 0, 128));
		btnSend.setBounds(574, 421, 120, 38);
		contentPane.add(btnSend);
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Envoie "message_content" a l'utilisateur de nom "username"
				//ChattingSessionController.sendMessage(username, textArea.getText());
				sendMessages(message_content);
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 371, 688, 12);
		contentPane.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(146, 12, 12, 367);
		contentPane.add(separator_1);

		JButton btnDisco = new JButton("Disconnect");
		btnDisco.setForeground(new Color(255, 51, 0));
		btnDisco.setBounds(564, 12, 117, 29);
		contentPane.add(btnDisco);
		btnDisco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ConnectWindow(username);
				// Demande de déconnexion
				ChattingSessionController.closeSession(username);
				// Notifie les autres utilisateur d'une connection
				DistributedDataController.notifyDisconnection();
				window.dispose();
			}
		});

		JButton btnChangeName = new JButton("Change Name");
		btnChangeName.setForeground(new Color(51, 204, 0));
		btnChangeName.setBounds(180, 12, 117, 29);
		contentPane.add(btnChangeName);
		btnChangeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangeNameWindow(username, nameUser); // Changement du nom
			}
		});

		textArea = new JTextArea();
		textArea.setForeground(new Color(255, 204, 0));
		textArea.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		textArea.setBackground(UIManager.getColor("EditorPane.inactiveForeground"));
		textArea.setBounds(259, 389, 313, 66);
		contentPane.add(textArea);
		textArea.setColumns(5);

		remote_users_list = new DefaultListModel<UserID>();
		JList<UserID> remoteUserList = new JList<UserID>(remote_users_list);
		remoteUserList.setCellRenderer(new CellRenderer());
		remoteUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		remoteUserList.setBackground(Color.LIGHT_GRAY);
		remoteUserList.setBounds(17, 53, 117, 306);
		contentPane.add(remoteUserList);

		history_messages = new JTextArea();
		history_messages.setEditable(false);
		history_messages.setBackground(Color.LIGHT_GRAY);
		history_messages.setBounds(167, 53, 504, 306);
		contentPane.add(history_messages);

		JLabel lblNewLabel = new JLabel("Message :");
		lblNewLabel.setForeground(new Color(153, 51, 0));
		lblNewLabel.setBounds(185, 394, 62, 24);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("User list :");
		lblNewLabel_1.setForeground(new Color(0, 102, 255));
		lblNewLabel_1.setBounds(17, 12, 105, 24);
		contentPane.add(lblNewLabel_1);

		JButton btnHelp = new JButton("Help");
		btnHelp.setForeground(UIManager.getColor("RadioButton.select"));
		btnHelp.setBounds(17, 430, 117, 29);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Ouvrir une fentre avec une image help
				new HelpWindow();
			}
		});
		contentPane.add(btnHelp);
		
		//Ettquette avec le pseudo sur la fenetre de tchat
		nameUser = new JLabel("");
		nameUser.setBounds(16, 388, 139, 29);
		contentPane.add(nameUser);
		nameUser.setText(username);

		JButton btnDataBase = new JButton("Connect DB");
		btnDataBase.setForeground(Color.BLUE);
		btnDataBase.setBounds(298, 12, 117, 29);
		btnDataBase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Ouvrir une fenetre pour se connecter à la Database
				new ConnectDBWindow();
			}
		});
		contentPane.add(btnDataBase);

		// Display the widow
		window.setVisible(true);

		// Retourne la liste des session de chat
		ChattingSessionController.getChatList();
	}
	// Method executed when the user click on send
    public void sendMessages(String content) {
        this.textArea.getText();
        // show sent message on text area
        this.history_messages.append("[" + UserID.class.getName() + 
        		" at " + LocalDateTime.now().withNano(0) + "]> " + content + "\n");
        this.textArea.setText("");
    }
}