import javax.swing.*;
import java.awt.*;

class MainWindow {
    public static void main(String args[]) {

        //Creating the Frame
        JFrame frame = new JFrame("Chat Session");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("Change Name");
        JMenu m2 = new JMenu("Exit");
        mb.add(m1);
        mb.add(m2);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text :");
        JTextField tf = new JTextField(20); // accepts up to 20 characters
        JButton send = new JButton("Send");
        JButton sendfile = new JButton("Send file");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(sendfile);
  

        // Text Area at the Center
        JTextArea ta = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }
}