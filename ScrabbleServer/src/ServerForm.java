import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;

public class ServerForm {

	private JFrame frame;
	private JTextField gameIDTextField;
	public static JTextArea logTextArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Management.startServer();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerForm window = new ServerForm();
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
	public ServerForm() {
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
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 6, 438, 266);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Log", null, panel, null);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(40, 20, 339, 172);
		panel.add(scrollPane_1);
		
		logTextArea = new JTextArea();
		logTextArea.setText("Server Started;\n");
		scrollPane_1.setViewportView(logTextArea);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Game", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblGameId = new JLabel("Game ID");
		lblGameId.setBounds(29, 27, 61, 16);
		panel_1.add(lblGameId);
		
		gameIDTextField = new JTextField();
		gameIDTextField.setBounds(118, 22, 149, 26);
		panel_1.add(gameIDTextField);
		gameIDTextField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		
		btnSearch.setBounds(286, 22, 91, 29);
		panel_1.add(btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(118, 68, 259, 125);
		panel_1.add(scrollPane);
		
		JTextArea gameDataTextArea = new JTextArea();
		scrollPane.setViewportView(gameDataTextArea);
		
		JLabel lblGameData = new JLabel("Game Data");
		lblGameData.setBounds(29, 105, 77, 16);
		panel_1.add(lblGameData);
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int gameID=Integer.parseInt(gameIDTextField.getText());
				if(gameID<0 || gameID>Management.gameNumber)
				{
					gameDataTextArea.setText("No such game");
					return;
				}
				Game game=Management.games.get(gameID);
				List<Operation> operations=game.getOperations();
				List<GameState> states=game.getGameStates();
				if(operations.size()==0)
				{
					// empty game
					gameDataTextArea.setText("No such game");
				}
				else
				{
					String s="";
					GameState state=states.get(states.size()-1);
					HashMap<String,Integer> scores=state.getScores();
					char[][] grid=state.getGrid();
					s+="grid:\n";
					for(int i=0;i<20;i++)
					{
						for(int j=0;j<20;j++)
						{
							if(grid[i][j]!='\0')
								s+=grid[i][j];
							else
								s+=" ";
						}
						s+="\n";
					}
					s+="\n\n";
					s+="scores:\n";
					Iterator<Entry<String, Integer>> iterator = scores.entrySet().iterator();
					while (iterator.hasNext()) 
					{
						Entry<String, Integer> entry = iterator.next();
						String user = entry.getKey();
						Integer score = entry.getValue();
						s+=user+": "+score.toString()+"\n";
					}
					gameDataTextArea.setText(s);
				}
			}
		});
	}
	
	public static void updateLog(String logMessage)
	{
		String s=logTextArea.getText()+logMessage;
		logTextArea.setText(s);
	}
}
