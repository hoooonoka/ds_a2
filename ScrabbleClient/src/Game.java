

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game 
{
	private int gameID;
	private List<GameState> states;
	private List<String> users;
	private List<Operation> operations;
	public boolean end=false;
	public Game(int id)
	{
		this.gameID=id;
		this.states=new ArrayList<GameState>();
		operations=new ArrayList<Operation>();
	}
	
	public Game(int id,String[] players)
	{
		this.gameID=id;
		this.users=new ArrayList<String>();
		for (int i = 0; i < players.length; i++) 
		{
			this.users.add(players[i]);
		}
		this.states=new ArrayList<>();
		GameState initialState=new GameState(players);
		states.add(initialState);
		operations=new ArrayList<Operation>();
	}
	
	public void initialiseNewGame()
	{
		String[] players=new String[this.users.size()];
		for (int i = 0; i < players.length; i++) 
		{
			players[i]=this.users.get(i);
		}
		GameState initialState=new GameState(players);
		states.add(initialState);
	}
	
	public void setUsers(List<String> gamePlayers)
	{
		this.users=new ArrayList<String>(gamePlayers);
	}
	
	public List<String> getUsers()
	{
		return this.users;
	}
	
	public List<GameState> getGameStates()
	{
		return this.states;
	}
	
	public List<Operation> getOperations()
	{
		return this.operations;
	}
	
	public void nextState(Operation operation)
	{
		GameState state=this.states.get(this.states.size()-1);
		GameState newState=new GameState(state);
		newState.tryOperate(operation);
		newState.changeNextTurn();
		operations.add(operation);
		states.add(newState);
		if(this.isFinish())
		{
			// end game
			this.end=true;
		}
	}
	
	public GameState getNewstGameState(){
		return this.states.get(this.states.size()-1);
	}
	
	public void returnToLastGameState(){
		this.states.remove(this.states.size()-1);
		this.operations.remove(this.operations.size()-1);
		this.states.get(this.states.size()-1).changeNextTurn();
	}
	
//	public void endGame()
//	{
//		Control.stopGame(this.gameID,this.states.get(this.states.size()-1));
//	}
	
	public boolean isFinish()
	{
		if(operations.size()<users.size())
			return false;
		for(int i=0;i<users.size();i++)
		{
			if(operations.get(operations.size()-i).getPass())
				return false;
		}
		return true;
	}
	
}