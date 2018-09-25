//import java.util.HashMap;

public class User 
{
	private String ip;
	private int port;
	private String username;
//	private int score;
	private boolean vip=false;
	private boolean isInGame=false;
	private int currentGameID;
	public User(String ip,String username,boolean vip,int port)
	{
		this.ip=ip;
		this.username=username;
//		this.score=0;
		this.vip=vip;
		this.port=port;
	}
	
	public void inGame(int gameID)
	{
		this.currentGameID=gameID;
		this.isInGame=true;
	}
	
	public void outOfGame()
	{
		this.isInGame=false;
	}
	
	public boolean inAGame()
	{
		return this.isInGame;
	}
	
	public int getCurrentGameID()
	{
		return this.currentGameID;
	}
	
	public String getIP()
	{
		return this.ip;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
//	public int getScore()
//	{
//		return this.score;
//	}
	
	public boolean isVip()
	{
		return this.vip;
	}
	
	public int getPort()
	{
		return this.port;
	}
	
//	public void addScore()
//	{
//		
//	}
	
	public static String toString(String userName)
	{
		User user=null;
//		for(int i=0;i<Control.users.size();i++)
//		{
//			if(Control.users.get(i).getUsername().equals(userName))
//			{
//				user=Control.users.get(i);
//				break;
//			}
//		}
		if(user==null)
			return "No such user";
		return user.getUsername()+"("+user.getIP()+" "+user.isVip()+")";
	}
}
