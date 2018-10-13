import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Management 
{
	public static int gameNumber;
	public static HashMap<String,User> users;
	public static ServerSocket server;
	public static HashMap<String,Boolean> alive;
	public static List<Game> games;
	public static HashMap<Integer,HashMap<String,Boolean>> vote;
	public static HashMap<String,List<JSONObject>> requests;
	public static HashMap<Integer,HashMap<String,Boolean>> reply;
	public static void initialise()
	{
		gameNumber=-1;
		games=new ArrayList<Game>();
		try 
		{
			server=new ServerSocket();
		}
		catch (IOException e) 
		{
			// error
		}
		users=new HashMap<String,User>();
		requests=new HashMap<String,List<JSONObject>>();
		vote=new HashMap<Integer,HashMap<String,Boolean>>();
		reply=new HashMap<Integer,HashMap<String,Boolean>>();
	}
	
	public synchronized static boolean checkUsernameDuplicated(String username)
	{
		if(users.containsKey(username))
			return true;
		return false;
	}
	
	public synchronized static String[] genearteUsernames()
	{
		String[] names=new String[users.size()];
		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
		int i=0;
		while (iterator.hasNext()) {
			Entry<String, User> entry = iterator.next();
			String user = entry.getKey();
			names[i++]=user;
		}
		return names;
	}
	
	public synchronized static List<User> getUsersByName(String[] names)
	{
		List<User> targetUsers=new ArrayList<User>();
		for(int i=0;i<names.length;i++)
		{
			try
			{
				if(users.containsKey(names[i]))
				{
					targetUsers.add(users.get(names[i]));
				}
			}
			catch(NullPointerException e)
			{
				continue;
			}
			
		}
		return targetUsers;
	}
	
	public static void listenToClients()
	{
		int port=4444;
		try 
		{
			ServerSocket server=new ServerSocket(port);
			while(true)
			{
				try 
				{
					Socket client = server.accept();
					Thread tt = new Thread(() -> coreControl(client));
					tt.start();
				}
				catch(SocketException e) 
				{
					System.out.println("Maybe the Internet access fail");
					return;
				}
			}
		} 
		catch (SocketException ex) 
    	{
			System.out.println("Maybe the Internet access fail");
		}
    	catch (IOException e) 
    	{
    		System.out.println("IO error occurs");
		} 
		finally 
		{
			if(server != null) 
			{
				try 
				{
					server.close();
					ServerForm.updateLog("Server closed\n");
				}
				catch (IOException e) 
				{
					System.out.println("error occuring when closing server");
				}
			}
		}

		
	}
	
	public static void coreControl(Socket client)
	{
		String username="";
		boolean duplicatedName=false;
		try(Socket clientSocket = client)
		{
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
		    DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
		    String jsonText=input.readUTF();
		    String[] texts=jsonText.split(";");
 			System.out.println("CLIENT: "+jsonText);
		    JSONParser parser = new JSONParser();
		    JSONObject command;
			try 
			{
				command = (JSONObject) parser.parse(texts[0]);
				username=(String) command.get("users");
			    if(checkUsernameDuplicated(username))
				{
					// duplicated username
			    	duplicatedName=true;
					JSONObject message=JsonParser.generateJsonLoginFail("Username has been used by other users, please change another one");
					System.out.println(message.toJSONString());
					output.writeUTF(message.toJSONString());
				    output.flush();
				    client.close();
				    return;
				}
				else
				{
					// add a user
					ServerForm.updateLog("New client "+username+" connected\n");
					User user=new User(clientSocket.getLocalAddress().getHostAddress(),username,false,5555);
					users.put(username, user);
					try
					{
						if(requests.containsKey(username))
						{
							requests.remove(username);
						}
					}
					catch(NullPointerException e)
					{
					}
					
					// unique username
					String[] usernames=genearteUsernames();
					JSONObject message=JsonParser.generateJsonLoginSuccess(usernames);
					System.out.println(message.toJSONString());
					output.writeUTF(message.toJSONString());
				    output.flush();
				    
				    // broadcasting all users about new user login
				    for(int i=0;i<usernames.length;i++)
				    {
				    	if(!usernames[i].equals(username))
				    	{
				    		JSONObject newTask=JsonParser.generateJsonUsersUpdated(usernames);
				    		try
				    		{
				    			if(requests.containsKey(usernames[i]))
								{
									requests.get(usernames[i]).add(newTask);
								}
								else
								{
									List<JSONObject> sendingTasks=new ArrayList<JSONObject>();
									sendingTasks.add(newTask);
									requests.put(usernames[i], sendingTasks);
								}
				    		}
							
							catch(NullPointerException e)
							{
								continue;
							}
				    	}
				    }
				    
				}
			    boolean stop=false;
			    while(true)
			    {
			    	stop=sendMessage(username,output,input, command, parser, client);
			    	if(stop)
			    	{
			    		if(client != null) 
						{
							try 
							{
								client.close();
								return;
							}
							catch (IOException e) 
							{
								System.out.println("error occuring when closing listen thread");
								return;
							}
						}
			    		return;
			    	}
			    }
			    
			}
			catch (ParseException e) 
			{
				System.out.println(username+" sent a wrong format message");
			}
		}
		catch (SocketException ex) 
    	{
			System.out.println(username+" might be disconnected with server(ex)");
		}
    	catch (IOException e) 
    	{
    		System.out.println(username+" might be disconnected with server(e)");
		} 
		finally 
		{
			if(client != null) 
			{
				try 
				{
					
					endUp(username,duplicatedName);
					client.close();
				}
				catch (IOException e) 
				{
					System.out.println("error occuring when closing listen thread");
				}
			}
		}

	}
	
	
	public synchronized static void endUp(String username, boolean duplicatedName)
	{
		if(duplicatedName)
			return;
		ServerForm.updateLog("Client "+username+" logout\n");
		System.out.println("connection to "+username+" closed");
		User user=users.get(username);
		if(user.inAGame())
		{
			int gameID=user.getCurrentGameID();
			List<String> players=games.get(gameID).getUsers();
			for(int i=0;i<players.size();i++)
			{
				// send terminate game message to other users
				if(!players.get(i).equals(username))
				{
					JSONObject task=JsonParser.generateJsonTerminateGame(gameID,players.get(i));
					try
					{
						users.get(players.get(i)).outOfGame();
						if(requests.containsKey(players.get(i)))
						{
							requests.get(players.get(i)).add(task);
						}
						else
						{
							List<JSONObject> sendingTasks=new ArrayList<JSONObject>();
							sendingTasks.add(task);
							requests.put(players.get(i), sendingTasks);
						}
					}
					catch(NullPointerException e)
					{
						continue;
					}
						
				}
			}
		}
			
		users.remove(username);
		String[] usernames=genearteUsernames();
		for(int i=0;i<usernames.length;i++)
		{
		    if(!usernames[i].equals(username))
		   	{
		   		JSONObject newTask=JsonParser.generateJsonUsersUpdated(usernames);
		   		try
		   		{
		   			if(requests.containsKey(usernames[i]))
					{
						requests.get(usernames[i]).add(newTask);
					}
					else
					{
						List<JSONObject> sendingTasks=new ArrayList<JSONObject>();
						sendingTasks.add(newTask);
						requests.put(usernames[i], sendingTasks);
					}
		   		}
				catch(NullPointerException e)
				{
					continue;
				}
	    	}
	    }

		
	}
	
	public synchronized static boolean sendMessage(String username, DataOutputStream output, DataInputStream input, JSONObject command, JSONParser parser, Socket client)
	{
		try
		{
			if(requests.containsKey(username))
	    	{
	    		List<JSONObject> sendingTasks=new ArrayList<JSONObject>(requests.get(username));
	    		requests.remove(username);
	    		for(int i=0;i<sendingTasks.size();i++)
	    		{
	    			if(!sendingTasks.get(i).get("commandType").equals("alive"))
	    			{
	    				System.out.println(username+" socket send: "+sendingTasks.get(i).toJSONString());
	    			}
//	    			System.out.println(sendingTasks.get(i).toJSONString()+";");
	    			output.writeUTF(sendingTasks.get(i).toJSONString()+";");
				    output.flush();
	    		}
	    	}
			if(input.available() > 0)
	    	{
	    		// receive message
	    		String messages=input.readUTF();
	    		String[] jsonMessages=messages.split(";",-1);
	    		boolean stop=false;
	    		for(int i=0;i<jsonMessages.length;i++)
	    		{
	    			if(!jsonMessages[i].equals(""))
	    			{
	    				// not empty
	    				
	    				try 
	    				{
							command = (JSONObject) parser.parse(jsonMessages[i]);
						} 
	    				catch (ParseException e) 
	    				{
							// TODO Auto-generated catch block
							return true;
						}
	    				if(!command.get("commandType").equals("aliveReply"))
	    				{
	    					System.out.println(username+" socket receive: "+jsonMessages[i]);
	    				}
	    				boolean connect=decideMessageType(jsonMessages[i]);
	    				if(connect==false)
	    				{
	    					return true;
	    				}
	    					
	    			}
	    		}
	    	}
				
			return false;
		}
		catch (SocketException ex) 
    	{
			System.out.println(username+" might be disconnected with server(ex)");
			return true;
		}
    	catch (IOException e) 
    	{
    		System.out.println(username+" might be disconnected with server(e)");
    		return true;
		} 
		
		
	}
	
	
	public synchronized static boolean decideMessageType(String jsonText)
	{
		JSONParser parser = new JSONParser();
		JSONObject command;
		try 
		{
			command = (JSONObject) parser.parse(jsonText);
			if(command.get("commandType").equals("createGame"))
			{
				// client wants to create a new game
				int gameID=++gameNumber;
				Game game=new Game(gameID);
				games.add(game);
				String[] username=JsonParser.recoverFromString((String) command.get("users"));
				List<String> temp=new ArrayList<String>();
				for(int i=0;i<username.length;i++)
					temp.add(username[i]);
				games.get(gameID).setUsers(temp);
				
				List<User> inviteUsers=getUsersByName(username);
				String host=(String) command.get("host");
				ServerForm.updateLog("Client "+host+" invited others to create a game(id:"+gameID+")\n");
				// invites other clients
				for(int i=0;i<inviteUsers.size();i++)
				{
					if(inviteUsers.get(i).inAGame())
					{
						// not invitable
						// reply host
						JSONObject newTask=JsonParser.generateJsonRefuseInvitation(gameID, inviteUsers.get(i).getUsername(),host,inviteUsers.get(i).getUsername()+" is now in a game and can not be invited");
						try
						{
							if(requests.containsKey(host))
							{
								requests.get(host).add(newTask);
							}
							else
							{
								List<JSONObject> sendingTasks=new ArrayList<JSONObject>();
								sendingTasks.add(newTask);
								requests.put(host, sendingTasks);
							}
							continue;
						}
						catch(NullPointerException e)
						{
							continue;
						}
						
					}
					JSONObject newTask=JsonParser.generateJsonInvitation(gameID, inviteUsers.get(i).getUsername(),host);
					try
					{
						if(requests.containsKey(inviteUsers.get(i).getUsername()))
						{
							requests.get(inviteUsers.get(i).getUsername()).add(newTask);
						}
						else
						{
							List<JSONObject> sendingTasks=new ArrayList<JSONObject>();
							sendingTasks.add(newTask);
							requests.put(inviteUsers.get(i).getUsername(), sendingTasks);
						}
					}
					catch(NullPointerException e)
					{
						continue;
					}
					
				}
				
			}
			else if(command.get("commandType").equals("invitationReply"))
			{
				// receive clients' reply for invitation
				boolean accept=false;
				if((boolean)command.get("reply").equals("yes"))
					accept=true;
				String username=(String)command.get("users");
				int gameID=Integer.parseInt(command.get("gameID").toString());
				String host=command.get("host").toString();
				if(!accept)
				{
					JSONObject task=JsonParser.generateJsonRefuseInvitation(gameID, username, host, username+" refuse your invitation");
					try
					{
						if(requests.containsKey(host))
						{
							requests.get(host).add(task);
						}
						else
						{
							List<JSONObject> tasks=new ArrayList<JSONObject>();
							tasks.add(task);
							requests.put(host, tasks);
						}
					}
					catch(NullPointerException e)
					{
					}
					
				}
				HashMap<String,Boolean> voteResult;
				if(reply.containsKey(gameID))
				{
					voteResult=new HashMap<String,Boolean>(reply.get(gameID));
					reply.remove(gameID);
				}
				else
				{
					voteResult=new HashMap<String,Boolean>();
				}
				if(accept) // client answer yes
				{
					voteResult.put(username, Boolean.TRUE);
				}
				else // client answer wrong
				{
					voteResult.put(username, Boolean.FALSE);
				}
				reply.put(gameID, voteResult);
				if(voteResult.size()==games.get(gameID).getUsers().size())
				{
					// all users responded
					reply.remove(gameID);
					Iterator<Entry<String, Boolean>> iterator = voteResult.entrySet().iterator();
					List<String> gamePlayer=new ArrayList<String>();
					while (iterator.hasNext()) 
					{
						Entry<String, Boolean> entry = iterator.next();
						if(entry.getValue().equals(Boolean.TRUE))
						{
							gamePlayer.add(entry.getKey());
						}
					}
					gamePlayer.add(host);
					String[] playersArray=new String[gamePlayer.size()];
					for(int i=0;i<playersArray.length;i++)
					{
						playersArray[i]=gamePlayer.get(i);
					}
					if(gamePlayer.size()>1)
					{
						// you have a lot of friends
						// initialise the new game
						ServerForm.updateLog("Client "+host+"'s game started\n");
						games.get(gameID).setUsers(gamePlayer);
						games.get(gameID).initialiseNewGame();
						// informing guests the new game begins
						for(int i=0;i<gamePlayer.size();i++)
						{
							try
							{
								users.get(gamePlayer.get(i)).inGame(gameID);
								JSONObject task=JsonParser.generateJsonCreateGameReply(gamePlayer.get(i), gameID, true, playersArray);
								if(requests.containsKey(gamePlayer.get(i)))
								{
									requests.get(gamePlayer.get(i)).add(task);
								}
								else
								{
									List<JSONObject> tasks=new ArrayList<JSONObject>();
									tasks.add(task);
									requests.put(gamePlayer.get(i), tasks);
								}
							}
							catch(NullPointerException e)
							{
								continue;
							}
							
						}
					}
					else
					{
						// no one wants to play with you
						// informing host creates game fail
						ServerForm.updateLog("Client "+host+"'s new game rejected: no users accepted invitation\n");
						JSONObject newTask=JsonParser.generateJsonCreateGameReply(host, gameID, false, playersArray);
						try
						{
							if(requests.containsKey(host))
							{
								requests.get(host).add(newTask);
							}
							else
							{
								List<JSONObject> sendingTasks=new ArrayList<JSONObject>();
								sendingTasks.add(newTask);
								requests.put(host, sendingTasks);
							}
						}
						catch(NullPointerException e)
						{
						}
						
					}
					
				}
			}
			else if(command.get("commandType").equals("message"))
			{
				String username=command.get("user").toString();
				int gameID=Integer.parseInt(command.get("gameID").toString());
				String message=command.get("message").toString();
				Game game=games.get(gameID);
				JSONObject task=JsonParser.generateJsonUserMessage(gameID, username, message);
				List<String> players=games.get(gameID).getUsers();
				for(int i=0;i<players.size();i++)
				{
					try
					{
						if(requests.containsKey(players.get(i)))
						{
							requests.get(players.get(i)).add(task);
						}
						else
						{
							List<JSONObject> tasks=new ArrayList<JSONObject>();
							tasks.add(task);
							requests.put(players.get(i), tasks);
						}
					}
					catch(NullPointerException e)
					{
						continue;
					}
					
				}
				
			}
			else if(command.get("commandType").equals("operate"))
			{
				String username=command.get("users").toString();
				int gameID=Integer.parseInt(command.get("gameID").toString());
				Game game=games.get(gameID);
				boolean pass=false;
				if((boolean)command.get("pass").equals("yes"))
				{
					pass=true;
					Operation operation=new Operation(username);
					game.nextState(operation);
					ServerForm.updateLog("Client "+username+"'s new operation: pass\n");
				}
				else
				{
					char letter=command.get("letter").toString().charAt(0);
					int x=Integer.parseInt(command.get("positionX").toString());
					int y=Integer.parseInt(command.get("positionY").toString());
					Operation operation=new Operation(letter, x, y, username);
					game.nextState(operation);
					ServerForm.updateLog("Client "+username+"'s new operation: position: "+x+","+y+"; letter: "+letter+"\n");
				}
				if(game.end)
				{
					List<String> players=game.getUsers();
					ServerForm.updateLog("Game "+gameID+"ended\n");
					for(int i=0;i<players.size();i++)
					{
						JSONObject newTask=JsonParser.generateJsonTerminateGame(gameID, players.get(i));
						try
						{
							users.get(players.get(i)).outOfGame();
							if(requests.containsKey(players.get(i)))
							{
								requests.get(players.get(i)).add(newTask);
							}
							else
							{
								List<JSONObject> tasks=new ArrayList<JSONObject>();
								tasks.add(newTask);
								requests.put(players.get(i), tasks);
							}
						}
						catch(NullPointerException e)
						{
							continue;
						}
						
					}
					return true;
					
				}
				if(pass)
				{
					// pass operation: no need to vote, directly goes to next state
					JSONObject update=JsonParser.generateJsonUpdateGame(gameID, username, game);
					List<String> players=games.get(gameID).getUsers();
					for(int i=0;i<players.size();i++)
					{
						try
						{
							if(requests.containsKey(players.get(i)))
							{
								requests.get(players.get(i)).add(update);
							}
							else
							{
								List<JSONObject> tasks=new ArrayList<JSONObject>();
								tasks.add(update);
								requests.put(players.get(i), tasks);
							}
						}
						catch(NullPointerException e)
						{
							continue;
						}
						
					}
				}
				else
				{
					// not pass: send vote message; client will check inside the message if need to vote
					boolean needToVote=false;
					if(command.get("vote").equals("yes"))
						needToVote=true;
					JSONObject update=JsonParser.generateJsonUpdateGame(gameID, username, game);
					JSONObject task=JsonParser.generateJsonVote(gameID, username,needToVote);
					if(!needToVote)
					{
						GameState state=games.get(gameID).getGameStates().get(games.get(gameID).getGameStates().size()-1);
						state.setScores(games.get(gameID).getGameStates().get(games.get(gameID).getGameStates().size()-2).getScores());
					}
					List<String> players=games.get(gameID).getUsers();
					for(int i=0;i<players.size();i++)
					{
//						if(players.get(i).equals(username))
//							continue;
						try
						{
							if(requests.containsKey(players.get(i)))
							{
								requests.get(players.get(i)).add(update);
								requests.get(players.get(i)).add(task);
							}
							else
							{
								List<JSONObject> tasks=new ArrayList<JSONObject>();
								tasks.add(update);
								tasks.add(task);
								requests.put(players.get(i), tasks);
							}
						}
						catch(NullPointerException e)
						{
							continue;
						}
						
					}
				}
			}
			else if(command.get("commandType").equals("terminateGame"))
			{
				int gameID=Integer.parseInt(command.get("gameID").toString());
				String user=command.get("users").toString();
				Game game=games.get(gameID);
				ServerForm.updateLog("Game "+gameID+"ended\n");
				List<String> players=game.getUsers();
				for(int i=0;i<players.size();i++)
				{
					if(!users.containsKey(players.get(i)))
						continue;
					users.get(players.get(i)).outOfGame();
					if(!players.get(i).equals(user))
					{
						JSONObject newTask=JsonParser.generateJsonTerminateGame(gameID, players.get(i));
						try
						{
							if(requests.containsKey(players.get(i)))
							{
								requests.get(players.get(i)).add(newTask);
							}
							else
							{
								List<JSONObject> tasks=new ArrayList<JSONObject>();
								tasks.add(newTask);
								requests.put(players.get(i), tasks);
							}
						}
						catch(NullPointerException e)
						{
							continue;
						}
						
					}
				}
				return true;
			}
			else if(command.get("commandType").equals("voteReply"))
			{
				boolean accept=false;
				if((boolean)command.get("reply").equals("yes"))
					accept=true;
				String username=(String)command.get("users");
				int gameID=Integer.parseInt(command.get("gameID").toString());
				String host=command.get("host").toString();
				HashMap<String,Boolean> voteResult;
				if(vote.containsKey(gameID))
				{
					voteResult=new HashMap<String,Boolean>(vote.get(gameID));
					vote.remove(gameID);
				}
				else
				{
					voteResult=new HashMap<String,Boolean>();
				}
				if(!accept) // client answer yes
				{
					voteResult.put(username, Boolean.FALSE);
				}
				else // client answer wrong
				{
					voteResult.put(username, Boolean.TRUE);
				}
				vote.put(gameID, voteResult);
				if(voteResult.size()==games.get(gameID).getUsers().size()-1)
				{
					// all users responded
					vote.remove(gameID);
					Iterator<Entry<String, Boolean>> iterator = voteResult.entrySet().iterator();
					List<String> gamePlayer=new ArrayList<String>();
					boolean result=true;
					while (iterator.hasNext()) 
					{
						Entry<String, Boolean> entry = iterator.next();
						gamePlayer.add(entry.getKey());
						if(entry.getValue().equals(Boolean.FALSE))
						{
							result=false;
						}
					}
					if(result)
						ServerForm.updateLog("Client "+host+"'s operation accepted\n");
					else
					{
						ServerForm.updateLog("Client "+host+"'s operation denied\n");
						GameState state=games.get(gameID).getGameStates().get(games.get(gameID).getGameStates().size()-1);
						state.setScores(games.get(gameID).getGameStates().get(games.get(gameID).getGameStates().size()-2).getScores());
					}
						
					gamePlayer.add(host);
					JSONObject task=JsonParser.generateJsonVoteReplyToClients(gameID, host, result);
					for(int i=0;i<gamePlayer.size();i++)
					{
						try
						{
							if(requests.containsKey(gamePlayer.get(i)))
							{
								requests.get(gamePlayer.get(i)).add(task);
							}
							else
							{
								List<JSONObject> tasks=new ArrayList<JSONObject>();
								tasks.add(task);
								requests.put(gamePlayer.get(i), tasks);
							}
						}
						catch(NullPointerException e)
						{
							continue;
						}
						
					}
				}
			}
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	
	public static void checkAlive()
	{
		while(true)
		{
			try 
			{
				Thread.sleep(1000);
//				alive=new HashMap<String,Boolean>();
				Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
				while (iterator.hasNext()) 
				{
					Entry<String, User> entry = iterator.next();
					String name = entry.getKey();
					JSONObject task=JsonParser.generateJsonAlive();
					try
					{
						if(requests.containsKey(name))
						{
							requests.get(name).add(task);
						}
						else
						{
							List<JSONObject> tasks=new ArrayList<JSONObject>();
							tasks.add(task);
							requests.put(name, tasks);
						}
//						System.out.println(name+" check alive");
					}
					catch(NullPointerException e)
					{
						continue;
					}
					
				}
					
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();	
			}
		} 
		
	}
	
	public static void startServer()
	{
		initialise();
		Thread t = new Thread(() -> listenToClients());
		t.start();
		Thread tt = new Thread(() -> checkAlive());
		tt.start();
	}
	
	public static void main(String[] args)
	{
		initialise();
		Thread t = new Thread(() -> listenToClients());
		t.start();
		Thread tt = new Thread(() -> checkAlive());
		tt.start();

	}
}
