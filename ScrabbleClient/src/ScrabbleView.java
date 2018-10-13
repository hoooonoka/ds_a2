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
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
	public static String regx1="[a-zA-Z0-9 ]*";
	public static JLabel messageTips = new JLabel("");
	public static JCheckBox voteCheckBox;
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
		turnLabel.setBounds(19, 45, 103, 31);
		frame.getContentPane().add(turnLabel);
		userTurnDisplayLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		userTurnDisplayLabel.setForeground(Color.black);
		userTurnDisplayLabel.setFont(new Font("Arial",Font.BOLD,18));
		userTurnDisplayLabel.setBounds(134, 52, 560, 16);
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
		 
		  
		  
		  for(int i=1; i<=20;i++)
		  {
			  scrabbleTextField[i][i].setBackground(Color.pink);
			  scrabbleTextField[i][21-i].setBackground(Color.pink);
			  
		  }
		  
		  for(int i=1;i<=20;i++){
			  for (int j=1;j<=20;j++)
			  {
				  scrabbleTextField[i][j].setForeground(Color.gray);
			  }
		  }
		  
		voteCheckBox= new JCheckBox("Tick the box if you want to vote");
		voteCheckBox.setForeground(Color.red);
		voteCheckBox.setBounds(678, 209, 240, 50);
		frame.getContentPane().add(voteCheckBox);
		
		JLabel decisionLabel = new JLabel("Your choice");
		decisionLabel.setForeground(Color.RED);
		decisionLabel.setBounds(741, 115, 137, 20);
		decisionLabel.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(decisionLabel);
		

		changeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isvote = false;
				isvote=voteCheckBox.isSelected();
				Boolean check=checkupdated(scrabbleTextField,isvote);
				
				if(check)
				{
				allButtonEnables(false);
				setBackGroundColor();
				}
			}
		});
		changeBtn.setBounds(668, 147, 117, 42);
		changeBtn.setEnabled(false);
		frame.getContentPane().add(changeBtn);
		

		passBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isvote = false;
				isvote=voteCheckBox.isSelected();
				allButtonEnables(false);
				ChangeScrabbleView.x=0;
				ChangeScrabbleView.y=0;
				updateScrabble();
				AddTasks.pass(isvote);
				setBackGroundColor();
			}
		});
		passBtn.setBounds(802, 149, 117, 39);
		passBtn.setEnabled(false);
		frame.getContentPane().add(passBtn);

		
		JScrollPane chatScrollPane = new JScrollPane();
		chatScrollPane.setBounds(668, 343, 251, 138);
		frame.getContentPane().add(chatScrollPane);
		
	    chatTextArea = new JTextArea();
		chatScrollPane.setViewportView(chatTextArea);
		
		JScrollPane myMsgScrollPane = new JScrollPane();
		myMsgScrollPane.setBounds(668, 521, 251, 42);
		frame.getContentPane().add(myMsgScrollPane);
		
		JTextArea myMsgTextArea = new JTextArea();
		myMsgScrollPane.setViewportView(myMsgTextArea);
		
		JButton sendBtn = new JButton("Send");
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = myMsgTextArea.getText();
				
				if(Pattern.matches(regx1, msg))
				{
					messageTips.setText(null);
					String message=ConnectServer.username+":"+msg;
					JSONObject task=JsonParser.generateJsonUserMessage(ConnectServer.gameID,ConnectServer.username,message);
					ConnectServer.tasks.add(task);
					myMsgTextArea.setText(null);

				}
				else{
					messageTips.setText("<html><p>Message only contain letters and numbers</p></html>");
				}
			
			}
		});
		sendBtn.setBounds(678, 584, 97, 31);
		frame.getContentPane().add(sendBtn);
		
		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myMsgTextArea.setText(null);			
			}
		});
		clearBtn.setBounds(802, 584, 97, 31);
		frame.getContentPane().add(clearBtn);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setBounds(668, 315, 117, 16);
		lblChat.setForeground(Color.RED);
		lblChat.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(lblChat);
		
		JLabel lblMyMessage = new JLabel("My Message");
		lblMyMessage.setBounds(668, 493, 117, 16);
		lblMyMessage.setForeground(Color.RED);
		lblMyMessage.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(lblMyMessage);
		frame.setVisible(true);
		
		JLabel userScoreLabel = new JLabel("Score");
		userScoreLabel.setForeground(Color.RED);
		userScoreLabel.setFont(new Font("Arial",Font.BOLD,18));
		userScoreLabel.setBounds(765, 12, 61, 16);
		frame.getContentPane().add(userScoreLabel);
		
		messageTips.setHorizontalAlignment(SwingConstants.CENTER);
		messageTips.setBounds(690, 628, 229, 31);
		frame.getContentPane().add(messageTips);
		
		JLabel lblYourName = new JLabel("Your Name:");
		lblYourName.setBounds(19, 17, 108, 16);
		lblYourName.setForeground(Color.red);
		lblYourName.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(lblYourName);
		
		JLabel lblUserName = new JLabel(ConnectServer.username);
		lblUserName.setBounds(364, 13, 61, 16);
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setForeground(Color.black);
		lblUserName.setFont(new Font("Arial",Font.BOLD,18));
		frame.getContentPane().add(lblUserName);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void updateBoard(JTextField [][] textfield)
	{	
		for(int i=1; i<21;i++)
		{
			for(int j=1;j<21;j++)
           {
              textfield[i][j].setText(record[i][j]);
           }
		}
		
	}
	
	public boolean checkNeighbour(JTextField [][] textfield)
	{
		int num=0;
		for(int i=1; i<21;i++)
		{
			for(int j=1;j<21;j++)
			{
				if(!textfield[i][j].getText().equals(""))
				{
					num++;
				}
			}
		}
		if(num==1)
			return false;
		for(int i=1; i<21;i++)
		{
			for(int j=1;j<21;j++)
           {
              
				if(!textfield[i][j].getText().equals(""))
				{
					boolean result=true;
					if(i>=2)
					{
						if(!textfield[i-1][j].getText().equals(""))
						{
							result=false;
							continue;
						}
					}
					if(i<=19)
					{
						if(!textfield[i+1][j].getText().equals(""))
						{
							result=false;
							continue;
						}
					}
					if(j>=2)
					{
						if(!textfield[i][j-1].getText().equals(""))
						{
							result=false;
							continue;
						}
					}
					if(j<=19)
					{
						if(!textfield[i][j+1].getText().equals(""))
						{
							result=false;
							continue;
						}
					}
					return true;
				}
              
           }
		}
		return false;
	}
	
	public boolean checkupdated(JTextField [][] textfield, boolean isvote)
	{
		int time=0;
		int xText = 0;
		int yText = 0;
		String changedText = null;
		if(checkNeighbour(textfield))
		{
			updateBoard(textfield);
			JOptionPane.showMessageDialog(null, "New letter must be adjacent to other letters",null,JOptionPane.ERROR_MESSAGE);
			return false;
		}
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
            	  
              }
              
           }
		}
		if(time==1){
			AddTasks.addLetter(changedText.toCharArray()[0], xText, yText,isvote);
			return true;
		}
		else if(time==0)
		{
			JOptionPane.showMessageDialog(null, "If you do not want to add a letter, please press Pass button",null,JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
		{
			updateBoard(textfield);
			JOptionPane.showMessageDialog(null, "You can only add one letter",null,JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	public static void updateScrabble()
	{
		if(ChangeScrabbleView.x==0&&ChangeScrabbleView.y==0)
		{
			
		}
		else
		{
			scrabbleTextField[ChangeScrabbleView.y+1][ChangeScrabbleView.x+1].setBackground(Color.blue);
		}
		
		for(int i=1;i<21;i++)
		{
			for(int j=1;j<21;j++)
			{
				
				scrabbleTextField[i][j].setText(record[i][j]);
				if(!record[i][j].equals(""))
				{
					scrabbleTextField[i][j].setEnabled(false);
				}
				
			}
		}
	}
	public static void setBackGroundColor()
	{
		
		for(int i=0; i<21;i++)
		{
			 for(int j=0;j<21;j++)
			 {
				 
				 scrabbleTextField[i][j].setBackground(Color.white);
			 }
		}
		 for(int j=0;j<21;j++)
		{
			
	    	  scrabbleTextField[0][j].setBackground(Color.orange);

	    	 
	    	  scrabbleTextField[j][0].setBackground(Color.orange);

		}
		  for(int i=1; i<=20;i++)
		  {
			 
			  scrabbleTextField[i][i].setBackground(Color.pink);
			  scrabbleTextField[i][21-i].setBackground(Color.pink);
			  
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
	
	public static void showMessageBox(String message)
	{
		JOptionPane.showMessageDialog(yesBtn, message);
	}
	public static void voteResult()
	{
		int n = JOptionPane.showConfirmDialog(null,"Please vote",null,JOptionPane.YES_NO_OPTION);
		if(n==JOptionPane.CLOSED_OPTION)
		{
			AddTasks.NoVoteResult();
		}
        if(n==JOptionPane.YES_OPTION){
			AddTasks.YesVoteResult();
        }
        else if(n==JOptionPane.NO_OPTION){
    		AddTasks.NoVoteResult();
        }
	}
	
	public static void tryOperate(Operation operation)
	{
		
		scrabbleTextField[operation.getY()+1][operation.getX()+1].setBackground(Color.red);
		// vertical
		for(int i=operation.getY()-1+1;i>=0+1;i--)
		{
			if(!record[i][operation.getX()+1].equals("")&&!record[i+1][operation.getX()+1].equals(""))
				scrabbleTextField[i][operation.getX()+1].setBackground(Color.red);
			else
				break;
		}
		for(int i=operation.getY()+1+1;i<20+1;i++)
		{
			if(!record[i][operation.getX()+1].equals("")&&!record[i-1][operation.getX()+1].equals(""))
				scrabbleTextField[i][operation.getX()+1].setBackground(Color.red);
			else
				break;
		}
		// horizontal
		for(int i=operation.getX()-1+1;i>=0+1;i--)
		{
			if(!record[operation.getY()+1][i].equals("")&&!record[operation.getY()+1][i+1].equals(""))
				scrabbleTextField[operation.getY()+1][i].setBackground(Color.red);
			else
				break;
		}
		for(int i=operation.getX()+1+1;i<20+1;i++)
		{
			if(!record[operation.getY()+1][i].equals("")&&!record[operation.getY()+1][i-1].equals(""))
				scrabbleTextField[operation.getY()+1][i].setBackground(Color.red);	
			else
				break;
		}
		
	}
}
