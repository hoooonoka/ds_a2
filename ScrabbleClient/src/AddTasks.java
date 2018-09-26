
public class AddTasks {

	public static int score;
	
	//按下邀请接受按钮后
	public static void acceptInvitation(){
		ConnectServer.tasks.add(JsonParser.generateJsonInvitationReply(ConnectServer.username, ConnectServer.gameID, true,ConnectServer.gameCreater));
	}
	//按下邀请拒绝按钮后
	public static void refuseInvitation(){
		ConnectServer.tasks.add(JsonParser.generateJsonInvitationReply(ConnectServer.username, ConnectServer.gameID, false,ConnectServer.gameCreater));
	}
	
	//将添加单词操作添加到task列表中
	public static void addLetter(char changedText,int x,int y){
		Operation operation=new Operation(changedText,x,y,ConnectServer.username);
		ConnectServer.game.nextState(operation);
		ConnectServer.tasks.add(JsonParser.generateJsonOperation(operation, ConnectServer.gameID));
	}
	//将pass操作添加到task列表中
	public static void pass(){
		Operation operation=new Operation(ConnectServer.username);
		ConnectServer.tasks.add(JsonParser.generateJsonOperation(operation, ConnectServer.gameID));
	}
	
	//将投票结果添加到task列表
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
	
	//发送玩家退出游戏消息
	public static void sendCloseCurrentGameMessage(){
		ConnectServer.tasks.add(JsonParser.generateJsonTerminateGame(ConnectServer.gameID, ConnectServer.username));
	}
	
	
	

}
