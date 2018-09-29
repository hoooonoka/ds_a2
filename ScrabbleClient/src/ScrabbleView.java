
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
import javax.swing.text.Document;

import org.json.simple.JSONObject;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.swing.SwingConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ScrabbleView {

	public static JFrame frame;
	private String regx ="[A-Z]{1}";
	public static String[][] record;
	static JTextField[][] scrabbleTextField;
	static JButton yesBtn;
	static JButton noBtn ;
	static JButton changeBtn;
	static JButton passBtn;
	static HashMap<String, Integer> result;
	static JLabel userTurnDisplayLabel;
	static JTextArea chatTextArea;
	public static JList scorelist;

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
//		result.put("dogshng", 11);
//		result.put("wuzhouhui", 22);
		frame = new JFrame();
		frame.setBounds(100, 100, 950, 800);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Scrabble Game");
		
		
		record = new String[21][21];
		scrabbleTextField =new JTextField[21][21];
		yesBtn = new JButton("V");
		noBtn = new JButton("X");
		changeBtn = new JButton("Change");
		passBtn = new JButton("Pass");
		result =new HashMap<String, Integer>();
		userTurnDisplayLabel = new JLabel("Display user name ");
		scorelist = new JList();
		chatTextArea=new JTextArea();
		
		JMenuBar menubar;
	    JMenu gameMenu; 
	    JMenuItem quitItem;
	    menubar = new JMenuBar();
        gameMenu = new JMenu("Menu");
        quitItem = new JMenuItem("Quit current game");    
        quitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int n =JOptionPane.showConfirmDialog(null,"Do you want to quit current game?", null,JOptionPane.YES_NO_CANCEL_OPTION);
        		if(n ==JOptionPane.YES_OPTION)
        		{
        			frame.dispose();
        			MainWindow mv = new  MainWindow();
        			AddTasks.sendCloseCurrentGameMessage();
        		}
        		else if(n==JOptionPane.NO_OPTION){}
        		
        	}
        });
        gameMenu.add(quitItem);       
        menubar.add(gameMenu);
        frame.setJMenuBar(menubar);
        
        //user turn display
        JLabel turnLabel = new JLabel("User Turn:");
        turnLabel.setForeground(Color.red);
        turnLabel.setFont(new Font("Arial",Font.BOLD,18));
		turnLabel.setBounds(21, 12, 103, 31);
		frame.getContentPane().add(turnLabel);
		userTurnDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		userTurnDisplayLabel.setForeground(Color.black);
		userTurnDisplayLabel.setFont(new Font("Arial",Font.BOLD,18));
		userTurnDisplayLabel.setBounds(136, 19, 560, 16);
		frame.getContentPane().add(userTurnDisplayLabel);
		
		JScrollPane scoreScrollPane = new JScrollPane();
		 scoreScrollPane.setBounds(690, 39, 213, 64);
		frame.getContentPane().add(scoreScrollPane);
		scoreScrollPane.setViewportView(scorelist);
		//test

		
      //Player name and score table part
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 100, 650, 650);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(21,21));
		
		for(int x=0;x<21;x++)
		{
			for(int y=0;y<21;y++)
			{
				record[x][y]="";
			}
		}

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
							
				scrabbleTextField[i][j].setText(record[i][j]);
				scrabbleTextField[i][j].setEnabled(false);
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
		
		JLabel voteLabel = new JLabel("Your Vote");
		voteLabel.setForeground(Color.RED);
		voteLabel.setBounds(751, 207, 107, 16);
		voteLabel.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(voteLabel);
		

		yesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddTasks.YesVoteResult();
				yesBtn.setEnabled(false);
				noBtn.setEnabled(false);
			}
		});
		yesBtn.setBounds(690, 242, 63, 50);
		yesBtn.setEnabled(false);
		frame.getContentPane().add(yesBtn);
		

        noBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		AddTasks.NoVoteResult();
        		yesBtn.setEnabled(false);
        		noBtn.setEnabled(false);
        	}
        });
		noBtn.setBounds(817, 242, 61, 50);
		noBtn.setEnabled(false);
		frame.getContentPane().add(noBtn);
		
		JLabel decisionLabel = new JLabel("Your choice");
		decisionLabel.setForeground(Color.RED);
		decisionLabel.setBounds(741, 115, 137, 20);
		decisionLabel.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(decisionLabel);
		

		changeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("press change button");
				Boolean check=checkupdated(scrabbleTextField);
				if(check)
				{
				allButtonEnables(false);
				}
				
			}
		});
		changeBtn.setBounds(668, 147, 117, 42);
		changeBtn.setEnabled(false);
		frame.getContentPane().add(changeBtn);
		

		passBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allButtonEnables(false);
				updateScrabble();
				AddTasks.pass();
			}
		});
		passBtn.setBounds(802, 149, 117, 39);
		passBtn.setEnabled(false);
		frame.getContentPane().add(passBtn);
		
		chatTextArea = new JTextArea();
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
				String message=ConnectServer.username+":"+msg;
				JSONObject task=JsonParser.generateJsonUserMessage(ConnectServer.gameID,ConnectServer.username,message);
				ConnectServer.tasks.add(task);
				myMsgTextArea.setText(null);
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
		frame.setVisible(true);
		
		JLabel userScoreLabel = new JLabel("Score");
		userScoreLabel.setForeground(Color.RED);
		userScoreLabel.setFont(new Font("Arial",Font.BOLD,18));
		userScoreLabel.setBounds(765, 12, 61, 16);
		frame.getContentPane().add(userScoreLabel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	
	public boolean checkupdated(JTextField [][] textfield)
	{
		int time=0;
		int xText = 0;
		int yText = 0;
		String changedText = null;
		for(int i=1; i<21;i++)
		{
			for(int j=1;j<21;j++)
           {
              if(!record [i][j].equals(textfield[i][j].getText()))
              {
            	  xText=j-1;
            	  yText =i-1;
            	  changedText = textfield[i][j].getText();
            	  time+=1;
            	 // System.out.print(record[i][j]+"-----"+textfield[i][j].getText());
            	  //System.out.println(changedText.toCharArray()[0]);
            	  
              }
              
           }
		}
		if(time==1){
			System.out.println("insert letter");
			AddTasks.addLetter(changedText.toCharArray()[0], xText, yText);
			return true;
		}
		else if(time==0)
		{
			JOptionPane.showMessageDialog(null, "If you do not want to add a letter, please press Pass button",null,JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "You can only add one letter",null,JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	public static void updateScrabble()
	{
		for(int i=1;i<21;i++)
		{
			for(int j=1;j<21;j++)
			{
				scrabbleTextField[i][j].setText(record[i][j]);
				//System.out.println(scrabbleTextField[i][j].getText());
				if(!record[i][j].equals(""))
				{
					scrabbleTextField[i][j].setEnabled(false);
				}
				
			}
		}
	}
	public static void allButtonEnables(boolean enable)
	{
		changeBtn.setEnabled(enable);
		passBtn.setEnabled(enable);
		for(int i=0;i<21;i++)
		{
			for(int j=0;j<21;j++)
			{
				scrabbleTextField[i][j].setEnabled(enable);
			}
			
		}
	}
	
	
	
	public static void updateScore()
	{

		String[] temp = displayResult(result);
		scorelist.setListData(temp);
	}
	
	public static String[] displayResult( Map<String, Integer> result)
	{
        int contentSize = result.size();
		int index =0;
		String[] temp = new String[contentSize];
    	for (Map.Entry<String, Integer> entry : result.entrySet())
    	{ 
    		 temp[index]=entry.getKey()+" : "+entry.getValue();
   		     index++;
    		
    	}
		return temp;  	
		
   }

}
