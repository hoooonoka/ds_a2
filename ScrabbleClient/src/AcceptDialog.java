
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AcceptDialog extends JFrame{
	 private int n;

	public AcceptDialog() 
	{
		// TODO Auto-generated constructor stub
		n = JOptionPane.showConfirmDialog(null,"You are invited by "+ConnectServer.gameCreater+", accept it?","Acception Confirm", JOptionPane.YES_NO_OPTION);
        if(n==JOptionPane.YES_OPTION){
        	AddTasks.acceptInvitation();
        }
        else if(n==JOptionPane.NO_OPTION){
        	AddTasks.refuseInvitation();
        }
	}
    
     public int getN()
	 {
	      return n;
	 }
}
