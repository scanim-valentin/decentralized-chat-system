import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;

public class MainWindow {

	private JFrame frame;
	private JButton btnNewButton_1;
	private JLabel lblNewLabel;
	private JTextField textField;
	private JLabel lblNewLabel_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[600px,grow][][]", "[][][243px][29px]"));
		
		lblNewLabel = new JLabel("Welcome");
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 32));
		frame.getContentPane().add(lblNewLabel, "cell 0 1,alignx center");
		
		btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton btnNewButton = new JButton("Connexion");
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		lblNewLabel_1 = new JLabel("Login :");
		frame.getContentPane().add(lblNewLabel_1, "flowx,cell 0 2");
		
		textField = new JTextField();
		frame.getContentPane().add(textField, "cell 0 2,growx");
		textField.setColumns(10);
		frame.getContentPane().add(btnNewButton, "cell 1 2,alignx center,aligny center");
		frame.getContentPane().add(btnNewButton_1, "cell 0 3,alignx center,aligny center");
	}

}
