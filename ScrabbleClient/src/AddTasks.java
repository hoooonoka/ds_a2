
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
		ConnectServer.tasks.add(JsonParser.generateJsonVoteReply(ConnectServer.gameID,true,ConnectServer.username,ConnectServer.gameCreater));
	}
	public static void NoVoteResult(){
		ConnectServer.tasks.add(JsonParser.generateJsonVoteReply(ConnectServer.gameID,false,ConnectServer.username,ConnectServer.gameCreater));
	}
	
	
	
	
	

}
