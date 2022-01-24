package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.insa.chatSystem.controller.*;
import fr.insa.chatSystem.model.*;

public class ChatWindow extends JFrame {

	/**
	 * JVM pour identifier les objets lorsqu'il les sérialise/désérialise
	 */
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane;
	private static JTextArea history_messages;
	private static JButton btnSend;
	private static JTextArea message_field;
	public static JLabel nameUser;
	public static JList<UserID> remoteUserList;
	public static UserID currentUser = null;
	public static DefaultListModel<UserID> model_list = new DefaultListModel<UserID>();
	JLabel currentUserLbl = new JLabel("*");

	/**
	 * Create the frame.
	 */

	// Refreshes the list
	static public void refreshList() {
		model_list.clear();
		for (UserID user : DistributedDataController.getUserList())
			model_list.addElement(user);
	}

	static public void refreshMessages() {
		history_messages.setText(ChattingSessionController.getConversation(currentUser.getName()));
	}

	public ChatWindow(String username, String message_content) {

		JFrame window = new JFrame();
		window.setResizable(false); // ne pas changer la taille de la fenetre
		window.setTitle("Chat System V1.0");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 716, 509);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnSendFile = new JButton("Send File");
		btnSendFile.setBounds(580, 385, 114, 35);
		btnSendFile.setForeground(new Color(0, 0, 255));
		contentPane.add(btnSendFile);
		btnSendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Envoie du fichier
				// ChooseFile.SendFile();
			}
		});

		btnSend = new JButton("Send");
		btnSend.setBounds(580, 425, 114, 30);
		btnSend.setForeground(new Color(0, 0, 128));
		contentPane.add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Envoie "message_content" a l'utilisateur de nom "username"
				// ChattingSessionController.sendMessage(username, textArea.getText());
				// Envoie de message
				if (currentUser != null) {
					switch (ChattingSessionController.sendMessage(currentUser.getName(), message_field.getText())) {
					case INVALID_CONTENT:
						message_field.setBackground(Color.BLUE);
						break;
					case SESSION_DOES_NOT_EXIST:
						message_field.setBackground(Color.RED);
						break;
					case INVALID_USERNAME:
						message_field.setBackground(Color.GREEN);
						break;
					default:
						message_field.setText("");
						break;
					}

				}
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 371, 688, 12);
		contentPane.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(166, 12, 12, 367);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(separator_1);

		JButton btnDisco = new JButton("Disconnect");
		btnDisco.setFont(new Font("Lucida Grande", Font.BOLD, 11));
		btnDisco.setBounds(584, 12, 110, 29);
		btnDisco.setForeground(new Color(255, 51, 0));
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
		btnChangeName.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		btnChangeName.setBounds(180, 12, 131, 29);
		btnChangeName.setForeground(new Color(51, 204, 0));
		contentPane.add(btnChangeName);
		btnChangeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Changement du nom
				new ChangeNameWindow(username, nameUser);
			}
		});

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(259, 389, 313, 66);
		scrollPane_2.setAutoscrolls(true);
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
		scrollPane_1.setBounds(17, 53, 138, 306);
		scrollPane_1.setAutoscrolls(true);
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
				currentUser = remoteUserList.getSelectedValue();
				currentUserLbl.setText(currentUser.getName());
				ChattingSessionController.newChat(currentUser.getName());
				refreshMessages();
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(190, 53, 504, 306);
		scrollPane.setAutoscrolls(true);
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
		lblMessage.setBounds(167, 394, 80, 24);
		lblMessage.setForeground(new Color(153, 51, 0));
		contentPane.add(lblMessage);

		JLabel lblUserList = new JLabel("User list :");
		lblUserList.setBounds(17, 12, 105, 24);
		lblUserList.setForeground(new Color(0, 102, 255));
		contentPane.add(lblUserList);

		JButton btnHelp = new JButton("Help");
		btnHelp.setBounds(6, 425, 117, 30);
		btnHelp.setForeground(Color.MAGENTA);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Ouvrir une fentre avec une image help
				new HelpWindow();
			}
		});
		contentPane.add(btnHelp);

		// Ettquette avec le pseudo sur la fenetre de tchat
		nameUser = new JLabel("");
		nameUser.setBounds(16, 388, 157, 35);
		nameUser.setFont(new Font("Times New Roman", Font.BOLD, 12));
		contentPane.add(nameUser);
		nameUser.setText(username);
		currentUserLbl.setBounds(323, 11, 205, 29);
		contentPane.add(currentUserLbl);

		// Display the widow
		window.setVisible(true);

		// Retourne la liste des session de chat
		ChattingSessionController.getChatList();
	}

}