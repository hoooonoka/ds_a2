
public class Operation 
{
	private char letter;
	private int positionX;
	private int positionY;
	private String user;
	private boolean isPass;
	public Operation(char letter, int x, int y, String user)
	{
		this.user=user;
		this.positionX=x;
		this.positionY=y;
		this.letter=letter;
		this.isPass=false;
	}
	
	public Operation(String user)
	{
		this.isPass=true;
		this.user=user;
	}
	
	public char getLetter()
	{
		return this.letter;
	}
	
	public int getX()
	{
		return this.positionX;
	}
	
	public int getY()
	{
		return this.positionY;
	}
	
	public String getUser()
	{
		return this.user;
	}
	
	public boolean getPass()
	{
		return this.isPass;
	}
	
	public static String printOperation(Operation operation)
	{
		if(operation.isPass)
			return operation.user+": pass";
		return operation.user+": "+operation.letter+" ("+operation.positionX+","+operation.positionY+")";
	}
}
