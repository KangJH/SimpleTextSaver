package home.text.simpletextsaver;

public class AccountChunk {
	String strContent;
	String strExpense;
	String strDate;
	String strTimeByMilliSec;
	boolean blChecked;
	
	public AccountChunk() {
		strContent = null;
		strExpense = null;
		strDate = null;
		strTimeByMilliSec = null;
		blChecked = false;
	}
	
	public void copy(AccountChunk A) {
		strContent = new String(A.strContent);
		strExpense = new String(A.strExpense);
		strDate = new String(A.strDate);
		strTimeByMilliSec = new String(strTimeByMilliSec);
		blChecked = A.blChecked;
	}
}
