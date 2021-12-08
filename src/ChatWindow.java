import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JLabel;

public class ChatWindow {

	private JFrame frame;
	protected Object textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public ChatWindow() {
		initialize();
	}
	public ChatWindow(String pseudo) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField_1 = new JTextField();
		frame.getContentPane().add(textField_1, BorderLayout.SOUTH);
		textField_1.setColumns(10);
		
		JLabel label = new JLabel(pseudo);
		frame.getContentPane().add(label, BorderLayout.NORTH);
		
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textField_1 = new JTextField();
		frame.getContentPane().add(textField_1, BorderLayout.SOUTH);
		textField_1.setColumns(10);
		
		JLabel label = new JLabel("New label");
		frame.getContentPane().add(label, BorderLayout.NORTH);
		
		frame.setVisible(true);
	
	}

}
