package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import fr.insa.chatSystem.model.UserID;

public class ChatWindow extends JFrame {

	/**
	 * JVM pour identifier les objets lorsqu'il les sérialise/désérialise
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	DefaultListModel<UserID> remote_users_list;
	JList<UserID> remote_users_jlist;

	/**
	 * Create the frame.
	 */
	public ChatWindow(String username) {

		JFrame window = new JFrame();
		window.setResizable(false); // ne pas changer la taille de la fenetre
		window.setTitle("Chat System V1.0");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		window.setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage(textField.getText()); // recupéré le texte
			}
		});
		btnNewButton.setForeground(new Color(0, 0, 255));
		btnNewButton.setBounds(574, 389, 120, 40);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Send File");
		btnNewButton_1.setForeground(new Color(0, 0, 128));
		btnNewButton_1.setBounds(574, 430, 120, 29);
		contentPane.add(btnNewButton_1);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendFile(); // Envoie du fichier
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(6, 371, 688, 12);
		contentPane.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(143, 16, 12, 367);
		contentPane.add(separator_1);

		JButton btnNewButton_2 = new JButton("Disconnect");
		btnNewButton_2.setForeground(new Color(255, 51, 0));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ConnectWindow(username);
				window.dispose();
				// Demande de déconnexion
			}
		});
		btnNewButton_2.setBounds(564, 12, 117, 29);
		contentPane.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Change Name");
		btnNewButton_3.setForeground(new Color(51, 204, 0));
		btnNewButton_3.setBounds(180, 12, 117, 29);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangeNameWindow(username);
				//window.dispose();
			}
		});
		contentPane.add(btnNewButton_3);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(146, 76, 535, -35);
		contentPane.add(separator_2);

		textField = new JTextField();
		textField.setForeground(new Color(255, 204, 0));
		textField.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		textField.setBackground(UIManager.getColor("EditorPane.inactiveForeground"));
		textField.setBounds(259, 389, 313, 77);
		contentPane.add(textField);
		textField.setColumns(10);

		JList<UserID> list = new JList<UserID>();
		list.setBackground(Color.LIGHT_GRAY);
		list.setBounds(17, 53, 117, 306);
		contentPane.add(list);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(167, 53, 504, 306);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Message :");
		lblNewLabel.setForeground(new Color(153, 51, 0));
		lblNewLabel.setBounds(185, 394, 62, 24);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("User list :");
		lblNewLabel_1.setForeground(new Color(0, 102, 255));
		lblNewLabel_1.setBounds(17, 12, 105, 24);
		contentPane.add(lblNewLabel_1);

		JButton btnNewButton_4 = new JButton("Help");
		btnNewButton_4.setForeground(UIManager.getColor("RadioButton.select"));
		btnNewButton_4.setBounds(17, 430, 117, 29);
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new HelpWindow();
			}
		});
		contentPane.add(btnNewButton_4);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(16, 388, 139, 29);
		contentPane.add(lblNewLabel_2);
		lblNewLabel_2.setText(username);

		JButton DataBase = new JButton("Connect DB");
		DataBase.setForeground(Color.BLUE);
		DataBase.setBounds(298, 12, 117, 29);
		DataBase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ConnectDBWindow();
			}
		});
		contentPane.add(DataBase);

		// Display the widow
		window.setVisible(true);
	}

	protected void sendFile() {

	}

	protected void sendMessage(String text) {

	}
}
