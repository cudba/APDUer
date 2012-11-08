package mvc.model;

import java.util.ArrayList;

import mvc.listener.ApduListener;

public interface ApduData {
	
	public void addApdu(Apdu apdu);
	
	public ArrayList<Apdu> getApduHistory();
	
	public void addApduListener(ApduListener listener);
	
	public void clearApduHistory();
	
}
