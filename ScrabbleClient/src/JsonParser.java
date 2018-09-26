
import org.json.simple.JSONObject;

public class JsonParser 
{
	// login message: send from client to server
	public static JSONObject generateJsonLogin(String usernames, int port)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "login");
		newCommand.put("users", usernames);
		newCommand.put("port", port);
		return newCommand;
	}
	
	// logout message: send from client to server
	public static JSONObject generateJsonLogout(String usernames)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "logout");
		newCommand.put("users", usernames);
		return newCommand;
	}
	
	// login success message: send from server to client
	public static JSONObject generateJsonLoginSuccess(String[] usernames)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "loginSuccess");
		String temp=generateStringFromArray(usernames);
		newCommand.put("users", temp);
		return newCommand;
	}
	
	// users updated message: send from server to client
	public static JSONObject generateJsonUsersUpdated(String[] usernames)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "usersUpdated");
		String temp=generateStringFromArray(usernames);
		newCommand.put("users", temp);
		return newCommand;
	}
	
	// login success reply message: send from client to server
	public static JSONObject generateJsonLoginSuccessReply(String usernames)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "loginSuccessReply");
		newCommand.put("users", usernames);
		return newCommand;
	}
	
	// login fail message: send from server to client
	public static JSONObject generateJsonLoginFail(String reason)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "loginFail");
		newCommand.put("reason", reason);
		return newCommand;
	}
	
	// create game message: send from client to server
	public static JSONObject generateJsonCreateGame(String[] usernames, String host)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "createGame");
		String temp=generateStringFromArray(usernames);
		newCommand.put("users", temp);
		newCommand.put("host", host);
		return newCommand;
	}
	
	// invitation message: send from server to client
	public static JSONObject generateJsonInvitation(int gameID, String users, String host)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "invitation");
		newCommand.put("users", users);
		newCommand.put("gameID", gameID);
		newCommand.put("host", host);
		return newCommand;
	}
	
	// invitation message: send from server to client
	public static JSONObject generateJsonRefuseInvitation(int gameID, String users, String host, String reason)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "refuse");
		newCommand.put("users", users);
		newCommand.put("gameID", gameID);
		newCommand.put("host", host);
		newCommand.put("reason", reason);
		return newCommand;
		}
	
	// invitation reply message: send from client to server
	public static JSONObject generateJsonInvitationReply(String user, int gameID, boolean reply, String host)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "invitationReply");
		newCommand.put("users", user);
		newCommand.put("gameID", gameID);
		newCommand.put("host", host);
		if(reply)
			newCommand.put("reply", "yes");
		else
			newCommand.put("reply", "no");	
		return newCommand;
	}
	
	// create game reply message: send from server to client
	public static JSONObject generateJsonCreateGameReply(String user, int gameID, boolean reply, String[] players)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "createGameReply");
		newCommand.put("users", user);
		newCommand.put("gameID", gameID);
		newCommand.put("players", generateStringFromArray(players));
		if(reply)
			newCommand.put("reply", "yes");
		else
			newCommand.put("reply", "no");	
		return newCommand;
	}
	
	// add operation message: send from client to server
	public static JSONObject generateJsonOperation(Operation operation, int gameID)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "operate");
		newCommand.put("users", operation.getUser());
		newCommand.put("gameID", gameID);
		if(operation.getPass())
		{
			newCommand.put("pass", "yes");
		}
		else
		{
			newCommand.put("pass", "no");
			newCommand.put("positionX", operation.getX());
			newCommand.put("positionY", operation.getY());
			newCommand.put("letter", String.valueOf(operation.getLetter()));
		}
		return newCommand;
	}
	
	// vote message: send from server to client
	public static JSONObject generateJsonVote( int gameID, String user)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "vote");
		newCommand.put("users", user);
		newCommand.put("gameID", gameID);
		return newCommand;
	}
	
	// vote reply message: send from client to server
	public static JSONObject generateJsonVoteReply( int gameID, boolean reply, String user, String host)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "voteReply");
		newCommand.put("users", user);
		newCommand.put("host", host);
		newCommand.put("gameID", gameID);
		if(reply)
			newCommand.put("reply", "yes");
		else
			newCommand.put("reply", "no");
		return newCommand;
	}
	
	// update game message: send from server to client
	public static JSONObject generateJsonUpdateGame( int gameID, String user, Game game)
	{
		Operation operation=game.getOperations().get(game.getOperations().size()-1);
		int score=game.getGameStates().get(game.getGameStates().size()-1).getScores().get(user);
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "updateGameState");
		newCommand.put("users", user);
		newCommand.put("gameID", gameID);
		newCommand.put("letter", String.valueOf(operation.getLetter()));
		newCommand.put("positionX", operation.getX());
		newCommand.put("positionY", operation.getY());
		newCommand.put("score", score);
		return newCommand;
	}
	
	// update game reply message: send from client to server
	public static JSONObject generateJsonUpdateGameReply( int gameID, String user)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "updateGameStateReply");
		newCommand.put("users", user);
		newCommand.put("gameID", gameID);
		return newCommand;
	}
	
	// vote reply message: send from server to client
	public static JSONObject generateJsonVoteReplyToClients( int gameID, String user, boolean result)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "updateGameStateReply");
		newCommand.put("users", user);
		newCommand.put("gameID", gameID);
		newCommand.put("result", result);
		return newCommand;
	}
	
	// terminate game message: send from server to client
	public static JSONObject generateJsonTerminateGame( int gameID, String user)
	{
		JSONObject newCommand = new JSONObject();
		newCommand.put("commandType", "terminateGame");
		newCommand.put("users", user);
		newCommand.put("gameID", gameID);
		return newCommand;
	}
	
	public static String generateStringFromArray(String[] words)
	{
		if(words.length==0)
		{
			return "";
		}
		String s="";
		for(int i=0;i<words.length-1;i++)
		{
			s+=words[i]+",";
		}
		s+=words[words.length-1];
		return s;
	}
	
	public static String[] recoverFromString(String longString)
	{
		if(longString.equals(""))
			return null;
		String[] words=longString.split(",");
		return words;
	}
	// check alive: server to client
	 public static JSONObject generateJsonAlive()
	 {
	  JSONObject newCommand = new JSONObject();
	  newCommand.put("commandType", "alive");
	  return newCommand;
	 }
	 
	 // check alive reply: client to server
	 public static JSONObject generateJsonAliveReply(String username)
	 {
	  JSONObject newCommand = new JSONObject();
	  newCommand.put("commandType", "aliveReply");
	  newCommand.put("user", username);
	  return newCommand;
	 }
}