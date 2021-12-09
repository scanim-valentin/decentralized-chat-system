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
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JLabel;

public class ChatWindow extends JFrame {

	/**
	 * JVM pour identifier les objets lorsqu'il les sérialise/désérialise
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

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
		window.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setForeground(new Color(0, 0, 255));
		btnNewButton.setBounds(574, 389, 117, 29);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Send File");
		btnNewButton_1.setBackground(Color.ORANGE);
		btnNewButton_1.setForeground(new Color(0, 0, 128));
		btnNewButton_1.setBounds(574, 430, 117, 29);
		contentPane.add(btnNewButton_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(6, 371, 688, 12);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(134, 12, 21, 367);
		contentPane.add(separator_1);
		
		JButton btnNewButton_2 = new JButton("Disconnect");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnNewButton_2.setBounds(564, 12, 117, 29);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Change Name");
		btnNewButton_3.setBounds(180, 12, 117, 29);
		contentPane.add(btnNewButton_3);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(146, 76, 535, -35);
		contentPane.add(separator_2);
		
		textField = new JTextField();
		textField.setBounds(259, 389, 313, 77);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JList list = new JList();
		list.setBounds(17, 53, 105, 312);
		contentPane.add(list);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(167, 53, 504, 306);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Message :");
		lblNewLabel.setBounds(185, 394, 62, 24);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("User list :");
		lblNewLabel_1.setBounds(17, 25, 105, 24);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton_4 = new JButton("New button");
		btnNewButton_4.setBounds(6, 117, 117, 29);
		contentPane.add(btnNewButton_4);
		
		window.setVisible(true);
	}
}
