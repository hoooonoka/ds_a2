
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
		
		JMenuBar menubar;
	    JMenu gameMenu; 
	    JMenuItem quitItem;
	    menubar = new JMenuBar();
        gameMenu = new JMenu("Menu");
        quitItem = new JMenuItem("Quit current game");    
        quitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int n =JOptionPane.showConfirmDialog(null,"Do you want to log out?", null,JOptionPane.YES_NO_CANCEL_OPTION);
        		if(n ==JOptionPane.YES_OPTION)
        		{
        			frame.dispose();
        			LoginWindow.frame.setVisible(true);
        			ConnectServer.isOffline=true;
        		}
        		else if(n==JOptionPane.NO_OPTION){}
        		
        	}
        	
        });
        gameMenu.add(quitItem);       
        menubar.add(gameMenu);
        frame.setJMenuBar(menubar);
        
		JLabel ScrabbleLabel = new JLabel("Scrabble Game");
		ScrabbleLabel.setForeground(Color.RED);
		ScrabbleLabel.setBounds(165, 36, 101, 35);
		frame.getContentPane().add(ScrabbleLabel);
		
		JLabel availableUserLabel = new JLabel("Avaliable Users");
		availableUserLabel.setBounds(30, 71, 135, 16);
		frame.getContentPane().add(availableUserLabel);
		
		JScrollPane availableUserScrollPane = new JScrollPane(avaliableUserList);
		availableUserScrollPane.setBounds(27, 99, 150, 116);
		frame.getContentPane().add(availableUserScrollPane);
		
		JLabel addedUserLabel = new JLabel("Added Users");
		addedUserLabel.setBounds(269, 71, 113, 16);
		frame.getContentPane().add(addedUserLabel);
		
		
		JScrollPane addedUserScrollPane = new JScrollPane(invitedUserList);
		addedUserScrollPane.setBounds(269, 97, 150, 110);
		frame.getContentPane().add(addedUserScrollPane);
		
		JButton newGameButton = new JButton("New Game");
		newGameButton.setBounds(289, 219, 93, 29);
		frame.getContentPane().add(newGameButton);
		
		JButton addUserButton = new JButton("+");
		addUserButton.setBounds(203, 140, 37, 29);
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
					for(int i=0;i<ConnectServer.invitedUsers.size();i++)
					{
						if(chosenName.equals(ConnectServer.invitedUsers.get(i)))
						{
							return;
						}
					}
					
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
		
		
	}
	// Open dialog
	public static void openDialog(){
		AcceptDialog acceptDialog = new AcceptDialog();
		acceptDialog.setVisible(true);
		acceptDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		acceptDialog.dispose();
	}
	
	
	class creatNewWindow implements ActionListener{
		public void actionPerformed(ActionEvent e){ 
			if(!ConnectServer.invitedUsers.isEmpty()){
				String[] invitedUsers1 = ConnectServer.invitedUsers.toArray(new String[ConnectServer.invitedUsers.size()]);
				ConnectServer.tasks.add(JsonParser.generateJsonCreateGame(invitedUsers1,ConnectServer.username));
				ConnectServer.invitedUsers.clear();
				MainWindow.invitedUserList.setListData(new String[0]);
			    frame.setVisible(false);
				
	           
			}
			else{
				
			}
        } 
	}
	
}