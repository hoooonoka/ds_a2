import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
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
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel signinLabel = new JLabel("Scrabble Game Log in");
		signinLabel.setForeground(Color.black);
		signinLabel.setFont(new Font("Arial",Font.BOLD,18));
		signinLabel.setBounds(114, 22, 240, 16);
		frame.getContentPane().add(signinLabel);
		
		JTextField usernametextField = new JTextField();
		usernametextField.setBounds(80, 148, 130, 26);
		frame.getContentPane().add(usernametextField);
		usernametextField.setColumns(10);
		
		JButton loginButton = new JButton("Log in");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernametextField.getText();
				MainWindow mw = new MainWindow();
			}
		});
		loginButton.setBounds(287, 148, 117, 29);
		frame.getContentPane().add(loginButton);
		
		JLabel errorNewLabel = new JLabel();
		errorNewLabel.setBounds(90, 189, 290, 38);
		frame.getContentPane().add(errorNewLabel);
		
		JLabel ipLabel = new JLabel("IP address:");
		ipLabel.setBounds(37, 67, 91, 16);
		frame.getContentPane().add(ipLabel);
		
		JLabel portLabel = new JLabel("Port:");
		portLabel.setBounds(240, 67, 61, 16);
		frame.getContentPane().add(portLabel);
		
		JLabel usernameLabel = new JLabel("Your name:");
		usernameLabel.setBounds(37, 120, 84, 16);
		frame.getContentPane().add(usernameLabel);
		
		JTextField ipTesxtField = new JTextField();
		ipTesxtField.setBounds(80, 87, 130, 26);
		frame.getContentPane().add(ipTesxtField);
		ipTesxtField.setColumns(10);
		
		JTextField portField = new JTextField();
		portField.setBounds(278, 87, 130, 26);
		frame.getContentPane().add(portField);
		portField.setColumns(10);
		
		frame.setVisible(true);
		
	}
}
