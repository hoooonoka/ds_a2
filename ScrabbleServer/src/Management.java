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
	
	public static boolean checkUsernameDuplicated(String username)
	{
		if(users.containsKey(username))
			return true;
		return false;
	}
	
	public static String[] genearteUsernames()
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
	
	public static List<User> getUsersByName(String[] names)
	{
		List<User> targetUsers=new ArrayList<User>();
		for(int i=0;i<names.length;i++)
		{
			targetUsers.add(users.get(names[i]));
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
					System.out.println("closing listen thread");
				}
			}
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void coreControl(Socket client)
	{
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
				String username=(String) command.get("users");
			    if(checkUsernameDuplicated(username))
				{
					// duplicated username
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
					User user=new User(clientSocket.getLocalAddress().getHostAddress(),username,false,5555);
					users.put(username, user);
					
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
				    }
				    
				}
			    while(true)
			    {
			    	if(requests.containsKey(username))
			    	{
			    		List<JSONObject> sendingTasks=new ArrayList<JSONObject>(requests.get(username));
			    		requests.remove(username);
			    		for(int i=0;i<sendingTasks.size();i++)
			    		{
			    			System.out.println(username+" socket send: "+sendingTasks.get(i).toJSONString());
			    			output.writeUTF(sendingTasks.get(i).toJSONString()+";");
						    output.flush();
			    		}
			    	}
			    	if(input.available() > 0)
			    	{
			    		// receive message
			    		String messages=input.readUTF();
			    		
			    		String[] jsonMessages=messages.split(";",-1);
			    		for(int i=0;i<jsonMessages.length;i++)
			    		{
			    			if(!jsonMessages[i].equals(""))
			    			{
			    				// not empty
			    				System.out.println(username+" socket receive: "+jsonMessages[i]);
			    				decideMessageType(jsonMessages[i]);
			    			}
			    		}
			    	}
			    }
//			    client.close();
			}
			catch (ParseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void decideMessageType(String jsonText)
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
				// invites other clients
				for(int i=0;i<inviteUsers.size();i++)
				{
					JSONObject newTask=JsonParser.generateJsonInvitation(gameID, inviteUsers.get(i).getUsername(),host);
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
				
			}
			else if(command.get("commandType").equals("logout"))
			{
				// receive clients' logout information
				String username=command.get("users").toString();
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
					}
				}
				
				users.remove(username);
				String[] usernames=genearteUsernames();
				for(int i=0;i<usernames.length;i++)
			    {
			    	if(!usernames[i].equals(username))
			    	{
			    		JSONObject newTask=JsonParser.generateJsonUsersUpdated(usernames);
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
						games.get(gameID).setUsers(gamePlayer);
						games.get(gameID).initialiseNewGame();
//						JSONObject newTask=JsonParser.generateJsonCreateGameReply(host, gameID, true, playersArray);
//						// informing host the new game begins
//						if(requests.containsKey(host))
//						{
//							requests.get(host).add(newTask);
//						}
//						else
//						{
//							List<JSONObject> sendingTasks=new ArrayList<JSONObject>();
//							sendingTasks.add(newTask);
//							requests.put(host, sendingTasks);
//						}
						// informing guests the new game begins
						for(int i=0;i<gamePlayer.size();i++)
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
					}
					else
					{
						// no one wants to play with you
						// informing host creates game fail
						JSONObject newTask=JsonParser.generateJsonCreateGameReply(host, gameID, false, playersArray);
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
				}
				else
				{
					char letter=command.get("letter").toString().charAt(0);
					int x=Integer.parseInt(command.get("positionX").toString());
					int y=Integer.parseInt(command.get("positionY").toString());
					Operation operation=new Operation(letter, x, y, username);
					game.nextState(operation);
				}
				if(game.end)
				{
					List<String> players=game.getUsers();
					for(int i=0;i<players.size();i++)
					{
						JSONObject newTask=JsonParser.generateJsonTerminateGame(gameID, players.get(i));
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
					return;
					
				}
				if(pass)
				{
					// pass operation: no need to vote, directly goes to next state
					JSONObject update=JsonParser.generateJsonUpdateGame(gameID, username, game);
					List<String> players=games.get(gameID).getUsers();
					for(int i=0;i<players.size();i++)
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
				}
				else
				{
					// not pass: need to vote
					JSONObject update=JsonParser.generateJsonUpdateGame(gameID, username, game);
					JSONObject task=JsonParser.generateJsonVote(gameID, username);
					List<String> players=games.get(gameID).getUsers();
					for(int i=0;i<players.size();i++)
					{
						if(players.get(i).equals(username))
							continue;
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
				}
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
					voteResult.put(username, Boolean.TRUE);
				}
				else // client answer wrong
				{
					voteResult.put(username, Boolean.FALSE);
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
					gamePlayer.add(host);
					JSONObject task=JsonParser.generateJsonVoteReplyToClients(gameID, host, result);
					for(int i=0;i<gamePlayer.size();i++)
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
				}
			}
			
		} 
		catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)
	{
		initialise();
		listenToClients();
	}
}
