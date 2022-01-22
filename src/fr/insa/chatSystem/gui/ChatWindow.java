package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.time.format.DateTimeFormatter;

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
	private JTextArea message_field;
	public static JLabel nameUser;
	public static JList<UserID> remoteUserList;
	public static UserID current_user = null ; 
	public static DefaultListModel<UserID> model_list = new DefaultListModel<UserID>();
	/**
	 * Create the frame.
	 */
	
	//Refreshes the list
	static public void refreshList() {
		model_list.clear() ; 
		for(UserID user: DistributedDataController.getUserList())
			 model_list.addElement(user) ; 
	}

		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ChatWindow(String username, String message_content) {

		JFrame window = new JFrame();
		window.setResizable(false); // ne pas changer la taille de la fenetre
		window.setTitle("Chat System V1.0");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 700, 500);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		window.setContentPane(contentPane);

		JButton btnSendFile = new JButton("Send File");
		btnSendFile.setForeground(new Color(0, 0, 255));
		btnSendFile.setBounds(580, 385, 110, 35);
		contentPane.add(btnSendFile);
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Envoie du fichier
				ChooseFile.SendFile();
			}
		});

		btnSend = new JButton("Send");
		btnSend.setForeground(new Color(0, 0, 128));
		btnSend.setBounds(580, 420, 110, 40);
		contentPane.add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = null;
				// Envoie "message_content" a l'utilisateur de nom "username"
				// ChattingSessionController.sendMessage(username, textArea.getText());
				// Envoie de message
				if(current_user != null)
					ChattingSessionController.sendMessage(message_field.getText(),current_user.getName());
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
		btnChangeName.setBounds(180, 12, 160, 29);
		contentPane.add(btnChangeName);
		btnChangeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Changement du nom
				new ChangeNameWindow(username, nameUser);
			}
		});

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setAutoscrolls(true);
		scrollPane_2.setBounds(259, 389, 313, 66);
		contentPane.add(scrollPane_2);

		message_field = new JTextArea();
		scrollPane_2.setViewportView(message_field);
		message_field.setLineWrap(true);
		message_field.setFocusTraversalPolicyProvider(true);
		message_field.setForeground(SystemColor.text);
		message_field.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 14));
		message_field.setBackground(SystemColor.windowText);
		message_field.setColumns(10);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setAutoscrolls(true);
		scrollPane_1.setBounds(17, 53, 117, 306);
		contentPane.add(scrollPane_1);

		// JList<UserID> remoteUserList = new JList<UserID>(remote_users_list);

		remoteUserList = new JList<UserID>(model_list);

		remoteUserList.setSelectionBackground(SystemColor.controlHighlight);
		scrollPane_1.setViewportView(remoteUserList);
		remoteUserList.setFocusCycleRoot(true);
		remoteUserList.setFocusTraversalPolicyProvider(true);
		remoteUserList.setCellRenderer(new CellRenderer());
		remoteUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		remoteUserList.setBackground(Color.LIGHT_GRAY);
		remoteUserList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				current_user = remoteUserList.getSelectedValue() ; 
				ChattingSessionController.newChat(current_user.getName()) ; 
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		scrollPane.setBounds(167, 53, 504, 306);
		contentPane.add(scrollPane);

		history_messages = new JTextArea();
		history_messages.setColumns(10);
		history_messages.setFocusTraversalPolicyProvider(true);
		history_messages.setFocusCycleRoot(true);
		history_messages.setLineWrap(true);
		scrollPane.setViewportView(history_messages);
		history_messages.setAutoscrolls(true);
		history_messages.setEditable(false);
		history_messages.setBackground(Color.LIGHT_GRAY);

		JLabel lblMessage = new JLabel("Message :");
		lblMessage.setForeground(new Color(153, 51, 0));
		lblMessage.setBounds(167, 394, 80, 24);
		contentPane.add(lblMessage);

		JLabel lblUserList = new JLabel("User list :");
		lblUserList.setForeground(new Color(0, 102, 255));
		lblUserList.setBounds(17, 12, 105, 24);
		contentPane.add(lblUserList);

		JButton btnHelp = new JButton("Help");
		btnHelp.setForeground(Color.MAGENTA);
		btnHelp.setBounds(17, 430, 117, 30);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Ouvrir une fentre avec une image help
				new HelpWindow();
			}
		});
		contentPane.add(btnHelp);

		// Ettquette avec le pseudo sur la fenetre de tchat
		nameUser = new JLabel("");
		nameUser.setFont(new Font("Times New Roman", Font.BOLD, 12));
		nameUser.setBounds(16, 388, 157, 35);
		contentPane.add(nameUser);
		nameUser.setText(username);

		JButton btnDataBase = new JButton("Connect DB");
		btnDataBase.setForeground(Color.BLUE);
		btnDataBase.setBounds(341, 12, 117, 29);
		btnDataBase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Ouvrir une fenetre pour se connecter à la Database
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
	public void sendMessage(String content) {
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
		String timeText = date.format(formatter);

		content = message_field.getText();
		if (!content.isBlank()) {
			// show sent message on text area
			this.history_messages.append("[" + nameUser.getText() + " at " + timeText + "] send : " + content + "\n");
			this.message_field.setText("");
			content = null;
		}
	}
}