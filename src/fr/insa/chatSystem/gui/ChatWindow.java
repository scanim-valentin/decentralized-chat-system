package fr.insa.chatSystem.gui;

import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import java.awt.TextArea;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class ChatWindow extends JFrame {

	/**
	 * JVM pour identifier les objets lorsqu'il les sérialise/désérialise
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow frame = new ChatWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	 */


	/**
	 * Create the frame.
	 * @param pseudo 
	 */
	public ChatWindow(String pseudo) {
		JFrame window = new JFrame(); 
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setForeground(new Color(0, 0, 255));
		btnNewButton.setBounds(564, 389, 117, 29);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Send File");
		btnNewButton_1.setBackground(Color.ORANGE);
		btnNewButton_1.setForeground(new Color(0, 0, 128));
		btnNewButton_1.setBounds(564, 418, 117, 29);
		contentPane.add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(146, 389, 406, 58);
		contentPane.add(textField);
		textField.setColumns(20);
		
		TextArea textArea = new TextArea();
		textArea.setBackground(new Color(153, 153, 0));
		textArea.setBounds(184, 100, 478, 251);
		contentPane.add(textArea);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 371, 688, 12);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(134, 12, 21, 367);
		contentPane.add(separator_1);
		
		JButton btnNewButton_2 = new JButton("Disconnect");
		btnNewButton_2.setBounds(564, 12, 117, 29);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Change Name");
		btnNewButton_3.setBounds(180, 12, 117, 29);
		contentPane.add(btnNewButton_3);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(146, 76, 535, -35);
		contentPane.add(separator_2);
		
		window.setContentPane(contentPane);
		window.setVisible(true);
	}
}
