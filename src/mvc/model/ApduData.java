package mvc.model;

import java.util.ArrayList;

import mvc.listener.ApduListener;

public class ApduData {
	
	private String listenPort;
	private String remoteHost;
	private String remotePort;	
	
	private ArrayList<ApduListener> listeners = new ArrayList<ApduListener>();

	private ArrayList<Apdu> apdu = new ArrayList<Apdu>();

	public void addApdu(Apdu data) {
		apdu.add(data);
		notifyApduReceived(data);
	}

	public ArrayList<Apdu> getApduList() {
		return apdu;
	}

	public void addApduListener(ApduListener listener) {
		listeners.add(listener);
	}

	public void clearApduHistory() {
		apdu.clear();
	}

	private void notifyApduReceived(Apdu apdu) {
		for (ApduListener listener : listeners) {
			listener.apduReceived(apdu);
		}
	}
	
	private void setSession(String listenPort, String remoteHost, String remotePort){
		this.listenPort = listenPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		notifySessionChanged();
	}

	private void notifySessionChanged() {
		for (ApduListener listener : listeners) {
			listener.sessionChanged();
		}
	}
	
}
