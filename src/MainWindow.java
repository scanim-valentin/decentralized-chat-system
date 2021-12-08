import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.ImageIcon;

public class MainWindow {

	private JFrame frame;
	private JButton btnNewButton_1;
	private JLabel lblNewLabel;
	private JTextField textField;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;

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
		frame.getContentPane().setBackground(new Color(153, 255, 51));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[600px,grow][][]", "[][][243px][29px]"));
		
		lblNewLabel = new JLabel("Welcome");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(153, 0, 255));
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 56));
		frame.getContentPane().add(lblNewLabel, "cell 0 1,alignx center");
		
		btnNewButton_1 = new JButton("Exit");
		btnNewButton_1.setForeground(new Color(255, 0, 0));
		btnNewButton_1.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnNewButton_1.setBackground(Color.CYAN);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			System.exit(0); //arrete le programme
				//frame.dispose();
			}
		});
		
		JButton btnNewButton = new JButton("Connexion");
		btnNewButton.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btnNewButton.setForeground(new Color(51, 0, 255));
		btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Call ChatWindow();
				//frame.dispose(); // erme la fenetre
				
			}
		});
		
		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("/Users/lauricmarthrin-john/GitHub/decentralized-chat-system/Images/index.jpg"));
		frame.getContentPane().add(lblNewLabel_2, "cell 1 1,alignx center");
		
		lblNewLabel_1 = new JLabel("Login :");
		lblNewLabel_1.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		frame.getContentPane().add(lblNewLabel_1, "flowx,cell 0 2");
		
		textField = new JTextField();
		frame.getContentPane().add(textField, "cell 0 2,growx");
		textField.setColumns(10);
		frame.getContentPane().add(btnNewButton, "cell 1 2,alignx center,aligny center");
		frame.getContentPane().add(btnNewButton_1, "cell 0 3,alignx center,aligny center");
	}

}
