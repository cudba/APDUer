package model;

import java.util.ArrayList;

import listener.ApduListener;

public interface ApduData {
	
	public void addApdu(Apdu apdu);
	
	public ArrayList<Apdu> getApduHistory();
	
	public void setApduListener(ApduListener listener);
	
	public void clearApduHistory();
	
}
