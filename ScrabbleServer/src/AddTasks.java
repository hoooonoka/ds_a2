
public class AddTasks {

	public static int score;
	
	//After pressing the invite button,add task to tasks list
	public static void acceptInvitation(){
		ConnectServer.tasks.add(JsonParser.generateJsonInvitationReply(ConnectServer.username, ConnectServer.gameID, true,ConnectServer.gameCreater));
	}
	//After pressing refuse button
	public static void refuseInvitation(){
		ConnectServer.tasks.add(JsonParser.generateJsonInvitationReply(ConnectServer.username, ConnectServer.gameID, false,ConnectServer.gameCreater));
	}

	//Add the add word process into tasks list
	public static void addLetter(char changedText,int x,int y, boolean isvote){
		Operation operation=new Operation(changedText,x,y,ConnectServer.username);
		ConnectServer.game.nextState(operation);
		String nextPlayer=ConnectServer.game.getNewstGameState().getNextTurn();
		ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
		ConnectServer.tasks.add(JsonParser.generateJsonOperation(operation, ConnectServer.gameID,isvote));
	}
	//Add pass process into tasks list
	public static void pass(boolean isvote){
		Operation operation=new Operation(ConnectServer.username);
		ConnectServer.game.nextState(operation);
		String nextPlayer=ConnectServer.game.getNewstGameState().getNextTurn();
		ScrabbleView.userTurnDisplayLabel.setText(nextPlayer+"'s turn");
		ConnectServer.tasks.add(JsonParser.generateJsonOperation(operation, ConnectServer.gameID,isvote));
	}
	
	//Add vote result into tasks list
	public static void YesVoteResult(){
		ConnectServer.tasks.add(JsonParser.generateJsonVoteReply(ConnectServer.gameID,true,ConnectServer.username,ChangeScrabbleView.user));
	}
	public static void NoVoteResult(){
		ConnectServer.tasks.add(JsonParser.generateJsonVoteReply(ConnectServer.gameID,false,ConnectServer.username,ChangeScrabbleView.user));
	}
	//return alive
	public static void returnAlive(){
		ConnectServer.tasks.add(JsonParser.generateJsonAliveReply(ConnectServer.username));
	}
	//Add player quit message to tasks list
	public static void sendCloseCurrentGameMessage(){
		ConnectServer.tasks.add(JsonParser.generateJsonTerminateGame(ConnectServer.gameID, ConnectServer.username));
	}
	
	
	

}
