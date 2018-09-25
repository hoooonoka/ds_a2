
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginWindow {

	private JFrame frame;
	private JTextField textField;
	public static 		JLabel tips = new JLabel("");

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
		signinLabel.setBounds(24, 22, 180, 16);
		frame.getContentPane().add(signinLabel);
		
		JTextField usernametextField = new JTextField();
		usernametextField.setBounds(83, 110, 161, 26);
		frame.getContentPane().add(usernametextField);
		usernametextField.setColumns(10);
		

		tips.setBounds(143, 169, 211, 16);
		frame.getContentPane().add(tips);
		
		JButton loginButton = new JButton("Log in");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username=usernametextField.getText();
				if(username.isEmpty())
				{
					tips.setText("username can not be empty");
				}
				else{
					ConnectServer.username=username;
					Thread t = new Thread(() -> ConnectServer.creatUser(username));
					t.start();
				}
				
			}
		});
		loginButton.setBounds(287, 110, 117, 29);
		frame.getContentPane().add(loginButton);
		
		JLabel errorNewLabel = new JLabel();
		errorNewLabel.setBounds(37, 197, 369, 42);
		frame.getContentPane().add(errorNewLabel);
		

	}
	
	public static void up()
	{
		MainWindow mw = new MainWindow();
	}
}
