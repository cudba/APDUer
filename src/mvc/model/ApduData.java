package mvc.model;

import java.util.ArrayList;

import mvc.listener.ApduListener;

public class ApduData {
	
	private ArrayList<ApduListener> listeners = new ArrayList<ApduListener>();

	private ArrayList<Apdu> apdu = new ArrayList<Apdu>();

	public void addApdu(Apdu data) {
		apdu.add(data);
		notifyView(data);
	}

	public ArrayList<Apdu> getApduHistory() {
		return apdu;
	}

	public void addApduListener(ApduListener listener) {
		listeners.add(listener);
	}

	public void clearApduHistory() {
		apdu.clear();
	}

	private void notifyView(Apdu apdu) {
		for (ApduListener listener : listeners) {
			listener.apduReceived(apdu);
		}
	}
	
}
