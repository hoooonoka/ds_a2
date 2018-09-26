

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Color;

public class MainWindow {
	static JList avaliableUserList = new JList();
	static JList invitedUserList = new JList();
	 static JFrame frame;
	 static	JTextArea inviteStatusTextArea = new JTextArea();
	 

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
		frame = new JFrame("Main window");
		frame.setBounds(100, 100, 450, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel ScrabbleLabel = new JLabel("Scrabble Game");
		ScrabbleLabel.setForeground(Color.RED);
		ScrabbleLabel.setBounds(22, 6, 101, 35);
		frame.getContentPane().add(ScrabbleLabel);
		
		JLabel availableUserLabel = new JLabel("Avaliable Users");
		availableUserLabel.setBounds(27, 41, 135, 16);
		frame.getContentPane().add(availableUserLabel);
		
		JScrollPane availableUserScrollPane = new JScrollPane(avaliableUserList);
		availableUserScrollPane.setBounds(27, 70, 150, 116);
		frame.getContentPane().add(availableUserScrollPane);
		
		JLabel addedUserLabel = new JLabel("Added Users");
		addedUserLabel.setBounds(269, 42, 113, 16);
		frame.getContentPane().add(addedUserLabel);
		
		
		JScrollPane addedUserScrollPane = new JScrollPane(invitedUserList);
		addedUserScrollPane.setBounds(269, 70, 150, 116);
		frame.getContentPane().add(addedUserScrollPane);
		
		JButton newGameButton = new JButton("New Game");
		newGameButton.setBounds(289, 219, 93, 29);
		frame.getContentPane().add(newGameButton);
		
		JButton addUserButton = new JButton("+");
		addUserButton.setBounds(203, 116, 37, 29);
		frame.getContentPane().add(addUserButton);
		
		JLabel statusLabel = new JLabel("Status");
		statusLabel.setBounds(22, 256, 61, 16);
		frame.getContentPane().add(statusLabel);
		
		inviteStatusTextArea.setBounds(23, 300, 390, 137);
		frame.getContentPane().add(inviteStatusTextArea);
		frame.setVisible(true);
		
		
		newGameButton.addActionListener(new creatNewWindow());
		
		addUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String chosenName=(String) avaliableUserList.getSelectedValue();
				if(chosenName!=null)
				{
					for(int i=0;i<ConnectServer.allUsersExceptSelf.size();i++)
					{
						if(ConnectServer.allUsersExceptSelf.get(i)==chosenName)
						{
							ConnectServer.allUsersExceptSelf.remove(i);
							i--;
						}
					}
					String[] changedUsernames = ConnectServer.allUsersExceptSelf.toArray(new String[ConnectServer.allUsersExceptSelf.size()]);
					avaliableUserList.setListData(changedUsernames);
					
					
					ConnectServer.invitedUsers.add(chosenName);
					String[] invitedUsers1 = ConnectServer.invitedUsers.toArray(new String[ConnectServer.invitedUsers.size()]);
					invitedUserList.setListData(invitedUsers1);
				}
				
				
			}
		});

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
// 注释的三行是对话框，如果需要重新设计窗口，需要注释这三行。
		
//		AcceptDialog acceptDialog = new AcceptDialog();
//		acceptDialog.setVisible(true);
//		acceptDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	//弹出对话框
	public static void openDialog(){
		AcceptDialog acceptDialog = new AcceptDialog();
		acceptDialog.setVisible(true);
		acceptDialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	class creatNewWindow implements ActionListener{
		public void actionPerformed(ActionEvent e){ 
			if(!ConnectServer.invitedUsers.isEmpty()){
				String[] invitedUsers1 = ConnectServer.invitedUsers.toArray(new String[ConnectServer.invitedUsers.size()]);
				ConnectServer.tasks.add(JsonParser.generateJsonCreateGame(invitedUsers1,ConnectServer.username));
			    frame.setVisible(false);
	           
			}
			else{
				
			}
        } 
	}
	
}