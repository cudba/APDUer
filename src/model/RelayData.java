package model;

import java.util.ArrayList;

import listener.ApduListener;

public class RelayData {
	
	private ArrayList<ApduListener> listeners = new ArrayList<ApduListener>();

	private ArrayList<Apdu> commandApdu = new ArrayList<Apdu>();
	
	private ArrayList<Apdu> responseApdu = new ArrayList<Apdu>();

	
	public void addCommandApdu(Apdu command) {
		commandApdu.add(command);
	}
	
	public void addResponseApdu(Apdu response) {
		responseApdu.add(response);
	}
	
	
	public ArrayList<Apdu> getCommandApduHistory() {
		return commandApdu;
	}
	
	public ArrayList<Apdu> getResponseApduHistory() {
		return responseApdu;
	}
	
	public void setApduListener(ApduListener listener) {
		listeners.add(listener);
	}
	
	public void clearCommandApduHistory() {
		commandApdu.clear();
	}
	
	public void clearResponseApduHistory() {
		responseApdu.clear();
	}
}

