
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class StringDocument extends PlainDocument{
	int maxLength =1; 

	public StringDocument(int newMaxLength) {
		// TODO Auto-generated constructor stub
		super(); 
	    maxLength = newMaxLength; 
	}

	public StringDocument(){ 
	      this(1); 
	   } 
	
	public void insertString(int offset,String str,AttributeSet a)throws BadLocationException{ 
	    if(getLength()+str.length()>maxLength)
	    {
	         return; 
	    } 
	    else{  
	        super.insertString(offset,str,a); 

	    }     
	  } 

	

}
