package mvc.model;

import java.util.ArrayList;

import mvc.listener.ApduListener;

public class ResponseData implements ApduData {

	private ArrayList<ApduListener> listeners = new ArrayList<ApduListener>();

	private ArrayList<Apdu> responseApdu = new ArrayList<Apdu>();

	public void addApdu(Apdu response) {
		responseApdu.add(response);
		notifyView(response);
	}

	public ArrayList<Apdu> getApduHistory() {
		return responseApdu;
	}

	public void addApduListener(ApduListener listener) {
		listeners.add(listener);
	}

	public void clearApduHistory() {
		responseApdu.clear();
	}

	private void notifyView(Apdu response) {
		for (ApduListener listener : listeners) {
			listener.responseReceived(response);
		}
	}

}
