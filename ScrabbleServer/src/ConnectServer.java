import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConnectServer {
	
	public static String ip = "192.168.1.5";
	private static int port = 4444;
	public static String login=null;
	public static String username;
	public static List<String> allUsersExceptSelf=new ArrayList<>();
	public static List<String> usernames;
	public static List<String> invitedUsers=new ArrayList<>();
	public static List<JSONObject> tasks=new ArrayList<>();
	public static int gameID;
	public static String gameCreater;
	public static String[] allPlayers;
	public static Game game;
	public static HashMap<String, Integer> score;
	public static ScrabbleView sv=null;
	public static boolean isOffline=false;
	
	public static void main(String[] args) {
		


	}
	
	public static void creatUser(String username){
		try(Socket socket = new Socket();){
			socket.connect(new InetSocketAddress(ip, port),5000);
			// Output and Input Stream
			DataInputStream input = new DataInputStream(socket.
					getInputStream());
		    DataOutputStream output = new DataOutputStream(socket.
		    		getOutputStream());

	    	output.writeUTF(JsonParser.generateJsonLoginSuccessReply(username).toJSONString()+";");
	    	output.flush();
	    	
	    		String reply = input.readUTF();
	    		JSONParser parser=new JSONParser();
	    		try {
					String[] message1=reply.split(";");
					JSONObject message = (JSONObject)parser.parse(message1[0]);
					
					if(message.get("commandType").equals("loginSuccess")){
						login="loginSuccess";
						isOffline=false;
						LoginWindow.frame.setVisible(false);
						String[] temp=((String) message.get("users")).split(",");
						for(int i=0;i<temp.length;i++)
						{
							if(!temp[i].equals(username))
							{
								allUsersExceptSelf.add(temp[i]);
							}
						}
						String[] allUsersExceptSelf1 = allUsersExceptSelf.toArray(new String[allUsersExceptSelf.size()]); 
						List listusername=Arrays.asList(temp);
						usernames=new ArrayList(listusername);
						//up() function is used to create main window
						Thread t = new Thread(() -> LoginWindow.up());
						t.start();
						MainWindow.avaliableUserList.setListData(allUsersExceptSelf1);
						long startTime = System.currentTimeMillis(), endTime;
						while(true){
							if(isOffline)
							{
								break;
							}
							if(!tasks.isEmpty()){
								for(int i=0;i<tasks.size();i++)
								{
									output.writeUTF(tasks.get(i).toJSONString()+";");
									output.flush();
								}
								tasks.clear();
							}
							
							if(input.available()>0)
							{
								startTime = System.currentTimeMillis();
								String reply1 = input.readUTF();
								
								String[] message2=reply1.split(";",-1);
								JSONObject operationMessage = (JSONObject)parser.parse(message2[0]);
								
								decidedReplyType(operationMessage);
								
							}
							else
							{
								endTime = System.currentTimeMillis();
								long operationTime=endTime - startTime;
								if(operationTime>2000)
								{
									try{
									ScrabbleView.frame.dispose();
	
									}
									
									catch(NullPointerException e){
										
										
									}
									String[] a=new String[0];
									MainWindow.avaliableUserList.setListData(a);
									MainWindow.invitedUserList.setListData(a);
									allUsersExceptSelf.clear();
									invitedUsers.clear();
									MainWindow.frame.dispose();
									
									LoginWindow.frame.setVisible(true);
									LoginWindow.tips.setText("<html><p>Server may not launched or Network connection is interrupted or port number is wrong</p></html>");
									break;
								}
							}

							

						}
						
						
					}
					if(message.get("commandType").equals("loginFail"))
					{
						String reason=(String) message.get("reason");
						login="loginFail";
						LoginWindow.tips.setText("<html><p>"+reason+"</html></p>");
					}
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    
		    
		} 
		catch (UnknownHostException e) {
			//return "ip address mistake";
			LoginWindow.tips.setText("ip address mistake");
			
		} 
		catch (IOException e) {
			LoginWindow.tips.setText("<html><p>Server may not launched or Network connection is interrupted or port number is wrong</p></html>");
		}

	}
	
	public static void decidedReplyType(JSONObject operationMessage){
		
		if(operationMessage.get("commandType").equals("usersUpdated")){
			String users=(String) operationMessage.get("users");
			String[] usernames1;
			if(users.contains(","))
			{
				usernames1=((String) operationMessage.get("users")).split(",");
			}
			else
			{
				usernames1=new String[]{users};
			}
			allUsersExceptSelf.clear();
			for(int i=0;i<usernames1.length;i++)
			{
				if(!usernames1[i].equals(username))
				{
					allUsersExceptSelf.add(usernames1[i]);
				}
			}
			String[] allUsersExceptSelfTemp = allUsersExceptSelf.toArray(new String[allUsersExceptSelf.size()]);
			MainWindow.avaliableUserList.setListData(allUsersExceptSelfTemp);
		}
		
		if(operationMessage.get("commandType").equals("invitation"))
		{
			gameID=Integer.parseInt(operationMessage.get("gameID").toString());
			gameCreater=operationMessage.get("host").toString();
			//This is used to open new dialog which used to ask if user accept an invitation
			MainWindow.openDialog();
		}
		if(operationMessage.get("commandType").equals("createGameReply"))
		{
			if(operationMessage.get("reply").equals("yes")){
				//Enter the game interface
				gameID=Integer.parseInt(operationMessage.get("gameID").toString());
				allPlayers=((String) operationMessage.get("players")).split(",");
				game=new Game(gameID,allPlayers);
				HashMap<String, Integer> scores=game.getNewstGameState().getScores();
				score=scores;
				if(sv!=null)
					sv.frame.dispose();
				sv = new ScrabbleView();
				MainWindow.frame.setVisible(false);
				ScrabbleView.result=scores;
				ScrabbleView.updateScore();
				
				

				String nextPlayer=game.getNewstGameState().getNextTurn();
				ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
				
				if(username.equals(nextPlayer))
				{
					ScrabbleView.allButtonEnables(true);
					
				}
				else
				{
				}
				
			}
			if(operationMessage.get("reply").equals("no")){
				//Game starts fail(No on accept invitation, only the game-creater will receive no)
				MainWindow.frame.setVisible(true);

				
				String[] invitedUser =new String[0];
				MainWindow.invitedUserList.setListData(invitedUser);
				invitedUsers.clear();
				String[] names=new String[allUsersExceptSelf.size()];
				
				for(int i=0;i<allUsersExceptSelf.size();i++)
				{
					names[i]=allUsersExceptSelf.get(i);
				}
				MainWindow.avaliableUserList.setListData(names);
			}
		}
		else if(operationMessage.get("commandType").equals("updateGameState"))
		{
			ChangeScrabbleView.user= operationMessage.get("users").toString();
			if(operationMessage.get("users").equals(username))
			{
				return;
			}
			Operation operation;
			//if user press change button, it will enter this if control flow
			if(!operationMessage.get("pass").equals("yes"))
			{
				//check if the game table is full
				if(ScrabbleView.checkRecord())
				{
					ConnectServer.tasks.add(JsonParser.generateJsonTerminateGame(ConnectServer.gameID, ConnectServer.username));
				}
				ChangeScrabbleView.letter=operationMessage.get("letter").toString().charAt(0);
				ChangeScrabbleView.x=Integer.parseInt(operationMessage.get("positionX").toString());
				ChangeScrabbleView.y=Integer.parseInt(operationMessage.get("positionY").toString());
				ChangeScrabbleView.score=Integer.parseInt(operationMessage.get("score").toString());
				ChangeScrabbleView.user= operationMessage.get("users").toString();
				operation=new Operation(ChangeScrabbleView.letter,ChangeScrabbleView.x,ChangeScrabbleView.y,ChangeScrabbleView.user);
				
				game.nextState(operation);
				//when user press pass button, no need to vote and will change to next player's turn
				//update the Scrabble GUI but do not change score
				char[][] grid=game.getNewstGameState().getGrid();
				for(int x=1;x<21;x++)
				{
					for(int y=1;y<21;y++)
					{
						if(grid[x-1][y-1]=='\0')
							ScrabbleView.record[x][y]="";
						else
							ScrabbleView.record[x][y]= String.valueOf(grid[x-1][y-1]);
					}
				}
				if(!ChangeScrabbleView.user.equals(username))
				{
					ScrabbleView.setBackGroundColor();
					ScrabbleView.updateScrabble();
				}
				
			}
			else
			{
				//check if the game table is full
				if(ScrabbleView.checkRecord())
				{
					ConnectServer.tasks.add(JsonParser.generateJsonTerminateGame(ConnectServer.gameID, ConnectServer.username));
				}
				ChangeScrabbleView.x=0;
				ChangeScrabbleView.y=0;
				
				ChangeScrabbleView.user=operationMessage.get("users").toString();
				operation=new Operation(ChangeScrabbleView.user);
				game.nextState(operation);
				if(!game.isFinish())
				{
					String nextPlayer=game.getNewstGameState().getNextTurn();
					ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
					if(username.equals(nextPlayer))
					{
						ScrabbleView.allButtonEnables(true);
					}

					//get the newest grid
					char[][] grid=game.getNewstGameState().getGrid();
					for(int x=1;x<21;x++)
					{
						for(int y=1;y<21;y++)
						{
							if(grid[x-1][y-1]=='\0')
								ScrabbleView.record[x][y]="";
							else
								ScrabbleView.record[x][y]= String.valueOf(grid[x-1][y-1]);
						}
					}
					if(!ChangeScrabbleView.user.equals(username))
					{
						ScrabbleView.setBackGroundColor();
						ScrabbleView.updateScrabble();
					}
					
				}
			}
			
		}
		else if(operationMessage.get("commandType").equals("vote"))
		{
			//vote is beginning and all button enables to edit
			if(operationMessage.get("needToVote").equals("yes"))
			{
				if(username.equals(operationMessage.get("users")))
				{
					String nextPlayer=game.getNewstGameState().getNextTurn();
					ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
					return;
				}
				Operation operation=new Operation(ChangeScrabbleView.letter,ChangeScrabbleView.x,ChangeScrabbleView.y,ChangeScrabbleView.user);
				ScrabbleView.tryOperate(operation);
				ScrabbleView.userTurnDisplayLabel.setText("Please Vote");
				ScrabbleView.voteResult();
			}
			else{
				GameState state=game.getNewstGameState();
				state.setScores(game.getGameStates().get(game.getGameStates().size()-2).getScores());
				String nextPlayer=game.getNewstGameState().getNextTurn();
				ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
				if(username.equals(nextPlayer))
				{
					ScrabbleView.allButtonEnables(true);
				}
				
				char[][] grid=game.getNewstGameState().getGrid();
				for(int x=1;x<21;x++)
				{
					for(int y=1;y<21;y++)
					{
						if(grid[x-1][y-1]=='\0')
							ScrabbleView.record[x][y]="";
						else
							ScrabbleView.record[x][y]= String.valueOf(grid[x-1][y-1]);
					}
				}
				if(!ChangeScrabbleView.user.equals(username))
				{
					ScrabbleView.setBackGroundColor();
					ScrabbleView.updateScrabble();
				}
				
			
			}
		}
		// Receive the vote result
		else if(operationMessage.get("commandType").equals("updateGameStateReply"))
		{
			//vote is passed, update GUI score and turn to next plater
			if(ScrabbleView.checkRecord())
			{
				ConnectServer.tasks.add(JsonParser.generateJsonTerminateGame(ConnectServer.gameID, ConnectServer.username));
			}
			if((boolean) operationMessage.get("result")){
				HashMap<String, Integer> scores=game.getNewstGameState().getScores();
				score=scores;
				ScrabbleView.result=scores;
				String nextPlayer=game.getNewstGameState().getNextTurn();
				ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
				if(username.equals(nextPlayer))
				{
					ScrabbleView.allButtonEnables(true);
				}
				//update score
				ScrabbleView.updateScore();
				char[][] grid=game.getNewstGameState().getGrid();
				for(int x=1;x<21;x++)
				{
					for(int y=1;y<21;y++)
					{
						if(grid[x-1][y-1]=='\0')
							ScrabbleView.record[x][y]="";
						else
							ScrabbleView.record[x][y]= String.valueOf(grid[x-1][y-1]);
					}
				}
				//update scrabble and set back ground color
				if(!ChangeScrabbleView.user.equals(username))
				{
					ScrabbleView.setBackGroundColor();
					ScrabbleView.updateScrabble();
				}
				
			}
			//vote is refused. return to last game state, update GUI and turn to next player
			else{
				if(ScrabbleView.checkRecord())
				{
					ConnectServer.tasks.add(JsonParser.generateJsonTerminateGame(ConnectServer.gameID, ConnectServer.username));
				}
				game.returnToLastGameState();
				char[][] grid=game.getNewstGameState().getGrid();
				for(int x=1;x<21;x++)
				{
					for(int y=1;y<21;y++)
					{
						if(grid[x-1][y-1]=='\0')
							ScrabbleView.record[x][y]="";
						else
							ScrabbleView.record[x][y]= String.valueOf(grid[x-1][y-1]);
					}
				}
				
				String nextPlayer=game.getNewstGameState().getNextTurn();
				ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
				if(username.equals(nextPlayer))
				{
					
					ScrabbleView.allButtonEnables(true);
				}
				if(!ChangeScrabbleView.user.equals(username))
				{
					ScrabbleView.setBackGroundColor();
					ScrabbleView.updateScrabble();
				}
				
			}
		}
		//Game is over. all button are not available and display the game result
		else if(operationMessage.get("commandType").equals("terminateGame"))
		{
			ScrabbleView.allButtonEnables(false);
			
			Iterator<Entry<String, Integer>> iterator = score.entrySet().iterator();
			String winner=null;
			Integer highestScore=-1;
			while (iterator.hasNext()) 
			{
				Entry<String, Integer> entry = iterator.next();
				String user = entry.getKey();
				Integer score = entry.getValue();
				System.out.print("user: "+user+"  score: "+score);
				if(score>highestScore)
				{
					highestScore=score;
					winner=user+",";
				}
				
			}
			iterator = score.entrySet().iterator();
			while (iterator.hasNext()) 
			{
				Entry<String, Integer> entry = iterator.next();
				String user = entry.getKey();
				Integer score = entry.getValue();
				if(score==highestScore)
				{
					highestScore=score;
					if(!winner.contains(user))
					{
						winner=winner+user+",";
					}
				}
			}
			winner = winner.substring(0,winner.length() - 1);
			
			String result="Winner is "+winner+" and highest score is "+highestScore;
			ScrabbleView.userTurnDisplayLabel.setText("Winner is "+winner+" and highest score is "+highestScore);
			ScrabbleView.showMessageBox(result);
			
			
		}
		else if(operationMessage.get("commandType").equals("alive"))
		{
			AddTasks.returnAlive();
		}
		
		else if(operationMessage.get("commandType").equals("refuse"))
		{
			String reason=MainWindow.inviteStatusTextArea.getText();
			reason=reason+operationMessage.get("reason").toString();
			MainWindow.inviteStatusTextArea.setText(reason);
			MainWindow.frame.setVisible(true);
			String[] invitedUser =new String[0];
			MainWindow.invitedUserList.setListData(invitedUser);
			invitedUsers.clear();
			
		}
		else if(operationMessage.get("commandType").equals("message"))
		{
			String message=operationMessage.get("message").toString();
			String previousMessage=ScrabbleView.chatTextArea.getText();
			String fullMessage=previousMessage+message+"\n";
			ScrabbleView.chatTextArea.setText(fullMessage);

			
		}
		
		
	}
	
	
	

}