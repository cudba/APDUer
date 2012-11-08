package mvc.model;

import java.util.ArrayList;

import mvc.listener.ApduListener;

public class CommandData implements ApduData {

	private ArrayList<ApduListener> listeners = new ArrayList<ApduListener>();

	private ArrayList<Apdu> commandApdu = new ArrayList<Apdu>();

	public void addApdu(Apdu command) {
		commandApdu.add(command);
		notifyView(command);
	}

	public ArrayList<Apdu> getApduHistory() {
		return commandApdu;
	}

	public void addApduListener(ApduListener listener) {
		listeners.add(listener);
	}

	public void clearApduHistory() {
		commandApdu.clear();
	}

	private void notifyView(Apdu command) {
		for (ApduListener listener : listeners) {
			listener.commandReceived(command);
		}
	}

}
