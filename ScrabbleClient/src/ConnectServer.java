
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConnectServer {
	
	private static String ip = "localhost";
	private static int port = 4444;
	public static String login=null;
	public static String username;
	public static List<String> usernames;
	public static List<String> invitedUsers=new ArrayList<>();
	public static List<JSONObject> tasks=new ArrayList<>();
	public static int gameID;
	public static String gameCreater;
	public static String[] allPlayers;
	public static Game game;
	
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
						String[] usernames1=((String) message.get("users")).split(",");
						List listusername=Arrays.asList(usernames1);
						usernames=new ArrayList(listusername);
						//MainWindow mw = new MainWindow();
						Thread t = new Thread(() -> LoginWindow.up());
						t.start();
						MainWindow.avaliableUserList.setListData(usernames1);
						
						while(true){
							
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
								String reply1 = input.readUTF();
								System.out.println(reply1);
								String[] message2=reply1.split(";",-1);
								JSONObject operationMessage = (JSONObject)parser.parse(message2[0]);
								decidedReplyType(operationMessage);
								
							}

							

						}
						
					}
					if(message.get("commandType").equals("loginFail"))
					{
						String reason=(String) message.get("reason");
						login="loginFail";
						LoginWindow.tips.setText(reason);
					}
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    
		    
		} 
		catch (UnknownHostException e) {
			//return "ip address mistake";
		} 
		catch (IOException e) {
			//return "<html><p>Search word fail,server may not launched or Network connection is interrupted or port number is wrong</p></html>";
		}

	}
	
	public static void decidedReplyType(JSONObject operationMessage){
		
		if(operationMessage.get("commandType").equals("loginSuccess")){
			
			String[] usernames1=((String) operationMessage.get("users")).split(",");
			List listusername=Arrays.asList(usernames1);
			usernames=new ArrayList(listusername);
			MainWindow.avaliableUserList.setListData(usernames1);
		}
		
		if(operationMessage.get("commandType").equals("invitation"))
		{
			System.out.println("接受到邀请");
			gameID=Integer.parseInt(operationMessage.get("gameID").toString());
			gameCreater=operationMessage.get("host").toString();
			//接下来弹出对话框,问是否接受邀请
			MainWindow.openDialog();
		}
		if(operationMessage.get("commandType").equals("createGameReply"))
		{
			if(operationMessage.get("reply").equals("yes")){
				//进入游戏界面
				System.out.println("进入游戏界面");
				allPlayers=((String) operationMessage.get("players")).split(",");
				game=new Game(gameID,allPlayers);
				String nextPlayer=game.getNewstGameState().getNextTurn();
				if(username.equals(nextPlayer))
				{
					// 能执行
				}
				else
				{
					// 不能做
				}
				ScrabbleView sv = new ScrabbleView(); 
			}
			if(operationMessage.get("reply").equals("no")){
				//游戏开始失败（无人参加游戏，只有创建游戏者才会收到no）
			}
		}
		else if(operationMessage.get("commandType").equals("updateGameState"))
		{
			Operation operation;
			if(!operationMessage.get("pass").equals("yes"))
			{
				//进入更改游戏界面状态
				ChangeScrabbleView.letter=operationMessage.get("letter").toString().charAt(0);
				ChangeScrabbleView.x=Integer.parseInt(operationMessage.get("positionX").toString());
				ChangeScrabbleView.y=Integer.parseInt(operationMessage.get("positionY").toString());
				ChangeScrabbleView.score=Integer.parseInt(operationMessage.get("score").toString());
				ChangeScrabbleView.user= operationMessage.get("users").toString();
				operation=new Operation(ChangeScrabbleView.letter,ChangeScrabbleView.x,ChangeScrabbleView.y,ChangeScrabbleView.user);
				
				game.nextState(operation);
				// pass时不用投票，自动开始下一个人
				//想得到最新的操作就是game.getgamestate的最新一个。
				//更新GUI，显示添加的单词，但是不更改分数
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
				ScrabbleView.updateScrabble();
			}
			else
			{
				ChangeScrabbleView.user=(String) operationMessage.get("users");
				operation=new Operation(ChangeScrabbleView.user);
				game.nextState(operation);
			}
			
		}
		else if(operationMessage.get("commandType").equals("vote"))
		{
			// 投票开始,投票按钮变成可选状态
			
		}
		// 接收投票结果
		else if(operationMessage.get("commandType").equals("updateGameStateReply"))
		{
			//投票通过，更新GUI分数
			if((boolean) operationMessage.get("result")){
				HashMap<String, Integer> scores=game.getNewstGameState().getScores();
				
			}
			//投票未通过，回到上一个gamestate,更新GUI
			else{
				game.returnToLastGameState();
				char[][] grid=game.getNewstGameState().getGrid();
			}
		}
		
		
	}
	
	
	

}