

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class GameState 
{
	private HashMap<String, Integer> scores;
	private List<String> userList;
	private String nextTurn;
	private char[][] grid;
	
	public GameState(String[] users)
	{
		scores= new HashMap<>();
		userList=new ArrayList();
		for(int i=0;i<users.length;i++)
		{
			scores.put(users[i], 0);
			userList.add(users[i]);
		}	
		nextTurn=new String(userList.get(0));
		grid=new char[20][20];
	}
	
	public GameState(GameState oneState)
	{
		scores= new HashMap<>();
		HashMap<String, Integer> oneScores=oneState.getScores();
		Iterator<Entry<String, Integer>> iterator = oneScores.entrySet().iterator();
		while (iterator.hasNext()) 
		{
			Entry<String, Integer> entry = iterator.next();
			String user = entry.getKey();
			Integer score = entry.getValue();
			scores.put(user, score);
		}
		grid=new char[20][20];
		char[][] oneGrid=oneState.getGrid();
		for(int i=0;i<20;i++)
		{
			for(int j=0;j<20;j++)
			{
				grid[i][j]=oneGrid[i][j];
			}
		}
		userList=new ArrayList();
		List<String> oneUserList=oneState.getUserList();
		for(int i=0;i<oneUserList.size();i++)
		{
			userList.add(new String(oneUserList.get(i)));
		}
		nextTurn=new String(oneState.getNextTurn());
	}
	
	public HashMap<String, Integer> getScores()
	{
		return this.scores;
	}
	
	public String getNextTurn()
	{
		return nextTurn;
	}
	
	public char[][] getGrid()
	{
		return grid;
	}
	
	public List<String> getUserList()
	{
		return userList;
	}
	
	public void changeNextTurn()
	{
		int num=0;
		for(int i=0;i<userList.size();i++)
		{
			if(userList.get(i).equals(nextTurn))
			{
				num=i;
				break;
			}
		}
		nextTurn=userList.get((num+1)%userList.size());
	}
	
	public void addUser(String user)
	{
		userList.add(user);
		scores.put(user, 0);
	}
	
	public void tryOperate(Operation operation)
	{
		this.grid[operation.getY()][operation.getX()]=operation.getLetter();
		int verticalScore=0, horizontalScore=0;
		// vertical
		for(int i=operation.getY()-1;i>=0;i--)
		{
			if(grid[i][operation.getX()]!='\0'&&grid[i+1][operation.getX()]!='\0')
				verticalScore++;
			else
				break;
		}
		for(int i=operation.getY()+1;i<20;i++)
		{
			if(grid[i][operation.getX()]!='\0'&&grid[i-1][operation.getX()]!='\0')
				verticalScore++;
			else
				break;
		}
		// horizontal
		for(int i=operation.getX()-1;i>=0;i--)
		{
			if(grid[operation.getY()][i]!='\0'&&grid[operation.getY()][i+1]!='\0')
				horizontalScore++;
			else
				break;
		}
		for(int i=operation.getX()+1;i<20;i++)
		{
			if(grid[operation.getY()][i]!='\0'&&grid[operation.getY()][i-1]!='\0')
				horizontalScore++;
			else
				break;
		}
		// sum up
		Iterator<Entry<String, Integer>> iterator = scores.entrySet().iterator();
		while (iterator.hasNext()) 
		{
			Entry<String, Integer> entry = iterator.next();
			String user = entry.getKey();
			Integer score = entry.getValue();
			System.out.println(user+score);
		}
		int score=scores.get(operation.getUser())+verticalScore+horizontalScore;
		scores.remove(operation.getUser());
		scores.put(operation.getUser(), score);
	}
	

}