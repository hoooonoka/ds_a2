import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.swing.JScrollPane;

public class ScrabbleView {

	private JFrame frame;
	private String regx ="[A-Z]{1}";
	private String[][] record = new String[21][21];	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScrabbleView window = new ScrabbleView();
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
	public ScrabbleView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 950, 800);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Scrabble Game");
		
		//Menu part
		JMenuBar menubar;
	    JMenu gameMenu; 
	    JMenuItem quitItem;
	    menubar = new JMenuBar();
        gameMenu = new JMenu("Menu");
        quitItem = new JMenuItem("Quit");    
        quitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        gameMenu.add(quitItem);       
        menubar.add(gameMenu);
        frame.setJMenuBar(menubar);
        
        //user turn diaplay
        JLabel turnLabel = new JLabel("Turn:");
        turnLabel.setForeground(Color.red);
		 turnLabel.setBounds(21, 12, 103, 31);
		frame.getContentPane().add(turnLabel);
		
		JLabel userTurnDisplayLabel = new JLabel("Display user name here");
		userTurnDisplayLabel.setForeground(Color.red);
		userTurnDisplayLabel.setBounds(31, 55, 109, 16);
		frame.getContentPane().add(userTurnDisplayLabel);
		
        //Player name and score table part
        String[] title = {"USER NAME", "GAME SCORE"};
		Object[][] playerInfo = {
		            { "王鹏", new Integer(91)},
		            { "朱学莲", new Integer(82)},
		            { "梅婷", new Integer(47), },
		            { "赵龙", new Integer(61),  },
		            { "李兵", new Integer(90) }, };
		 
		JTable table = new JTable(playerInfo, title);
		
		JScrollPane resultscrollPane = new JScrollPane(table);
		resultscrollPane.setBounds(152, 18, 411, 66);
		frame.getContentPane().add(resultscrollPane);
		
		//Game board part
		JPanel panel = new JPanel();
		panel.setBounds(6, 100, 650, 650);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(21,21));
		
		JTextField[][] scrabbleTextField =new JTextField[21][21];
		for(int i=0;i<21;i++)
		{
			for(int j=0;j<21;j++)
			{
				scrabbleTextField[i][j]=new JTextField();
				scrabbleTextField[i][j].setDocument( new StringDocument(1));			
				scrabbleTextField[i][j].setHorizontalAlignment(JTextField.CENTER);
				scrabbleTextField[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				scrabbleTextField[i][j].setFont(new Font("Arial",Font.BOLD,18));		 
				panel.add(scrabbleTextField[i][j]);				
			}
			
		}		
		
		 for(int j=0;j<21;j++)
		{
	    	  scrabbleTextField[0][j].setBackground(Color.orange);
	    	  scrabbleTextField[0][j].setForeground(Color.white);
	    	  scrabbleTextField[0][j].setEditable(false);
	    	  scrabbleTextField[0][j].setDocument( new StringDocument(1));
	    	  
	    	  scrabbleTextField[j][0].setBackground(Color.orange);
	    	  scrabbleTextField[j][0].setForeground(Color.white); 	  
	    	  scrabbleTextField[j][0].setEditable(false);    
	    	  scrabbleTextField[j][0].setDocument( new StringDocument(2));
		}
		 
		  scrabbleTextField[0][1].setText("A");
	      scrabbleTextField[0][2].setText("B");
          scrabbleTextField[0][3].setText("C");
          scrabbleTextField[0][4].setText("D");
	      scrabbleTextField[0][5].setText("E");
	      scrabbleTextField[0][6].setText("F");
	      scrabbleTextField[0][7].setText("G");
	      scrabbleTextField[0][8].setText("H");
	      scrabbleTextField[0][9].setText("I");
	      scrabbleTextField[0][10].setText("J");
	      scrabbleTextField[0][11].setText("K");
	      scrabbleTextField[0][12].setText("L");
          scrabbleTextField[0][13].setText("M");
	      scrabbleTextField[0][14].setText("N");
	      scrabbleTextField[0][15].setText("O");
	      scrabbleTextField[0][16].setText("P");
	      scrabbleTextField[0][17].setText("Q");
	      scrabbleTextField[0][18].setText("R");
          scrabbleTextField[0][19].setText("S");
	      scrabbleTextField[0][20].setText("T");	      
	      scrabbleTextField[1][0].setText("1");
		  scrabbleTextField[2][0].setText("2");
		  scrabbleTextField[3][0].setText("3");
		  scrabbleTextField[4][0].setText("4");
		  scrabbleTextField[5][0].setText("5");
		  scrabbleTextField[6][0].setText("6");
		  scrabbleTextField[7][0].setText("7");
		  scrabbleTextField[8][0].setText("8");
		  scrabbleTextField[9][0].setText("9");
		  scrabbleTextField[10][0].setText("10");
		  scrabbleTextField[11][0].setText("11");
		  scrabbleTextField[12][0].setText("12");
		  scrabbleTextField[13][0].setText("13");
		  scrabbleTextField[14][0].setText("14");
		  scrabbleTextField[15][0].setText("15");
		  scrabbleTextField[16][0].setText("16");
		  scrabbleTextField[17][0].setText("17");
		  scrabbleTextField[18][0].setText("18");
		  scrabbleTextField[19][0].setText("19");
		  scrabbleTextField[20][0].setText("20");
		  
		//first char must be filled in this yellow box
		  scrabbleTextField[10][10].setBackground(Color.yellow);
		  
		  //score *3 red box
		  scrabbleTextField[1][1].setBackground(Color.red);
		  scrabbleTextField[1][10].setBackground(Color.red);
		  scrabbleTextField[10][1].setBackground(Color.red);
		  scrabbleTextField[1][20].setBackground(Color.red);
		  scrabbleTextField[20][1].setBackground(Color.red);
		  scrabbleTextField[20][10].setBackground(Color.red);
		  scrabbleTextField[10][20].setBackground(Color.red);
		  scrabbleTextField[20][20].setBackground(Color.red);
		  
		//score *2 blue box
		  scrabbleTextField[1][5].setBackground(Color.cyan);
		  scrabbleTextField[1][15].setBackground(Color.cyan);
		  scrabbleTextField[5][1].setBackground(Color.cyan);
		  scrabbleTextField[15][1].setBackground(Color.cyan);	
		  scrabbleTextField[5][20].setBackground(Color.cyan);
		  scrabbleTextField[15][20].setBackground(Color.cyan);	
		  scrabbleTextField[20][5].setBackground(Color.cyan);
		  scrabbleTextField[20][15].setBackground(Color.cyan);
		  scrabbleTextField[9][9].setBackground(Color.cyan);
		  scrabbleTextField[11][9].setBackground(Color.cyan);
		  scrabbleTextField[9][11].setBackground(Color.cyan);
		  scrabbleTextField[11][11].setBackground(Color.cyan);
		  
		  scrabbleTextField[8][2].setBackground(Color.blue);
		  scrabbleTextField[8][8].setBackground(Color.blue);
		  scrabbleTextField[8][12].setBackground(Color.blue);
		  scrabbleTextField[8][19].setBackground(Color.blue);
		  scrabbleTextField[12][2].setBackground(Color.blue);
		  scrabbleTextField[12][8].setBackground(Color.blue);
		  scrabbleTextField[12][12].setBackground(Color.blue);	
		  scrabbleTextField[12][19].setBackground(Color.blue);	
		  scrabbleTextField[2][8].setBackground(Color.blue);
		  scrabbleTextField[2][12].setBackground(Color.blue);	
		  scrabbleTextField[19][12].setBackground(Color.blue);
		  scrabbleTextField[19][8].setBackground(Color.blue);
		  
		  for(int i =2; i<=7;i++) {
			  scrabbleTextField[i][i].setBackground(Color.pink);
		  }
		  for(int i =13; i<=19;i++) {
			  scrabbleTextField[i][i].setBackground(Color.pink);
		  }
		  for(int i =1; i<=7;i++) {
			  scrabbleTextField[i][20-i].setBackground(Color.pink);
		  }
		  for(int i =13; i<=19;i++) {
			  scrabbleTextField[i][20-i].setBackground(Color.pink);
		  }
		  
		  for(int i=1;i<=20;i++){
			  for (int j=1;j<=20;j++)
			  {
				  scrabbleTextField[i][j].setForeground(Color.gray);
			  }
		  }
		
		//Vote part
		JLabel voteLabel = new JLabel("Your Vote");
		voteLabel.setForeground(Color.RED);
		voteLabel.setBounds(751, 207, 107, 16);
		voteLabel.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(voteLabel);
		
		JButton yesBtn = new JButton("V");
		yesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		yesBtn.setBounds(690, 242, 63, 50);
		frame.getContentPane().add(yesBtn);
		
        JButton noBtn = new JButton("X");
        noBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
		noBtn.setBounds(817, 242, 61, 50);
		frame.getContentPane().add(noBtn);
		
		//Choice part
		JLabel decisionLabel = new JLabel("Your choice");
		decisionLabel.setForeground(Color.RED);
		decisionLabel.setBounds(736, 100, 137, 20);
		decisionLabel.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(decisionLabel);
		
		JButton changeBtn = new JButton("Change");
		changeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		changeBtn.setBounds(668, 147, 117, 42);
		frame.getContentPane().add(changeBtn);
		
		JButton passBtn = new JButton("Pass");
		passBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		passBtn.setBounds(802, 149, 117, 39);
		frame.getContentPane().add(passBtn);
		
		
		//Chat part
		JTextArea chatTextArea = new JTextArea();
		chatTextArea.setBounds(668, 377, 251, 137);
		chatTextArea.setEditable(false);
		frame.getContentPane().add(chatTextArea);
				
		JTextArea myMsgTextArea = new JTextArea();
		myMsgTextArea.setBounds(668, 554, 251, 50);
		frame.getContentPane().add(myMsgTextArea);
		
		JButton sendBtn = new JButton("Send");
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = myMsgTextArea.getText();
			}
		});
		sendBtn.setBounds(678, 616, 107, 39);
		frame.getContentPane().add(sendBtn);
		
		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myMsgTextArea.setText(null);			
			}
		});
		clearBtn.setBounds(816, 616, 103, 39);
		frame.getContentPane().add(clearBtn);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setBounds(668, 349, 117, 16);
		lblChat.setForeground(Color.RED);
		lblChat.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(lblChat);
		
		JLabel lblMyMessage = new JLabel("My Message");
		lblMyMessage.setBounds(668, 526, 117, 16);
		lblMyMessage.setForeground(Color.RED);
		lblMyMessage.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(lblMyMessage);
		
		JLabel gameErrorLabel = new JLabel("<html><body>label for <br> game error information</body></html>");
		gameErrorLabel.setForeground(Color.RED);
		gameErrorLabel.setBounds(668, 304, 276, 42);
		frame.getContentPane().add(gameErrorLabel);
		
		JLabel chatErrorLable = new JLabel("label for chat error");
		chatErrorLable.setForeground(Color.RED);
		chatErrorLable.setBounds(668, 661, 276, 42);
		frame.getContentPane().add(chatErrorLable);		
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);	
	}
	
	public void updateBoard(JTextField [][] textfield)
	{		
		for(int i=0; i<21;i++)
		{
			for(int j=0;j<21;j++)
           {
              record [i][j]= textfield[i][j].getText();
           }
		}
		
	}
	
	public void checkupdated(JTextField [][] textfield)
	{
		//int time=0;
		for(int i=0; i<21;i++)
		{
			for(int j=0;j<21;j++)
           {
              if(record [i][j] != textfield[i][j].getText())
              {
            	  //time +=1;
            	  int xText=i;
            	  int yText =j;
            	  String changedText = textfield[i][j].getText();
            	  
              }
              
           }
		}
	}
	
	public static Object[][] displayResult(Map<String, Integer> result)
	{
		int index= 0;
		Object[][] obj =new Object[result.size()][2];
    	for (Map.Entry<String, Integer> entry : result.entrySet())
    	{ 
    		obj[index][0]= entry.getKey();
    		obj[index][1]= entry.getValue();
    		index++;
    	}   	
		return obj;      
   }
}
