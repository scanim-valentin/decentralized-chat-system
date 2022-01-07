package fr.insa.chatSystem.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import fr.insa.chatSystem.controller.ChattingSessionController;
import fr.insa.chatSystem.model.*;
import fr.insa.chatSystem.TCPFile.*;

import javax.swing.JTextArea;

public class ChatWindow extends JFrame {

	/**
	 * JVM pour identifier les objets lorsqu'il les sérialise/désérialise
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	private JFileChooser fc;

	DefaultListModel<RemoteUser> remote_users_list;
	JList<RemoteUser> remote_users_jlist;
	
	public JLabel nameUser;
	private LinkedList<File> FileToSend;
    private Model model;


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
				ChattingSessionController.closeSession(username); // Demande de déconnexion
				window.dispose();
			}
		});
		btnNewButton_2.setBounds(564, 12, 117, 29);
		contentPane.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Change Name");
		btnNewButton_3.setForeground(new Color(51, 204, 0));
		btnNewButton_3.setBounds(180, 12, 117, 29);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangeNameWindow(username, nameUser); //Changement du nom
			}
		});
		contentPane.add(btnNewButton_3);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(146, 76, 535, -35);
		contentPane.add(separator_2);

		textArea = new JTextArea();
		textArea.setForeground(new Color(255, 204, 0));
		textArea.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		textArea.setBackground(UIManager.getColor("EditorPane.inactiveForeground"));
		textArea.setBounds(259, 389, 313, 77);
		contentPane.add(textArea);
		textArea.setColumns(10);

		remote_users_list = new DefaultListModel<RemoteUser>();
		JList<RemoteUser> remoteList = new JList<RemoteUser>(this.remote_users_list);
		remoteList.setCellRenderer(new CellRenderer());
		remoteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		remoteList.setBackground(Color.LIGHT_GRAY);
		remoteList.setBounds(17, 53, 117, 306);
		contentPane.add(remoteList);

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

		nameUser = new JLabel(""); 
		nameUser.setBounds(16, 388, 139, 29);
		contentPane.add(nameUser);
		nameUser.setText(username);

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
		int returnVal = fc.showOpenDialog(contentPane);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
		if(!ChatWindow.this.remote_users_jlist.isSelectionEmpty()) {
			ChatWindow.this.processEnvoieFichier(file, ChatWindow.this.remote_users_jlist.getSelectedValue());
		}
		}
		
	}
	
    public Model getModel(){
        return model;
    }
    
	public File fileWait (String f) {
		File aux = null;
		for(File t : FileToSend) {
			if(t.getName().equals(f))
				aux = t;
		}
		return aux;
	}
	
	public void processEnvoieFichier(File file, RemoteUser remote_user) {
		FileToSend = new LinkedList<File>();
		this.FileToSend.add(file);	
	}
	   public void sendFileRequest(String name, String hostname) {
	        Message m;
	        m = new FileRequest(name);
	        System.out.println("Send file request");
	        _client.sendTo(m,hostname,_port); 
	    }
	    
	    public void sendFileResponse(boolean response, String name, String hostname) {
	        Message m;
	        m = new FileResponse(response, name);
	        _client.sendTo(m,hostname,_port); 
	    }
	    
	    public void sendFile(File file, String hostname) {
	        
	        System.out.println("Thread TCP Send created");
	        TCPSend send=new TCPSend(file,_portTCP,hostname);
	        send.start();
	        System.out.println("file sended : "+file.getName());
	    }

	    void receiveFile(String file_name) throws IOException {
	        System.out.println("Thread TCP Receive created");
	            
	            TCPReceive receive=new TCPReceive(_portTCP,file_name);
	            
	            receive.start();
	    }
}
