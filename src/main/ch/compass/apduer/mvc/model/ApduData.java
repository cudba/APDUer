package ch.compass.apduer.mvc.model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import ch.compass.apduer.mvc.listener.ApduListener;


public class ApduData {
	
	private ArrayList<ApduListener> listeners = new ArrayList<ApduListener>();
	private ArrayList<Apdu> apdu = new ArrayList<Apdu>();
	private Semaphore lock = new Semaphore(1);

	public void addApdu(Apdu data) throws InterruptedException {
		lock.acquire();
		apdu.add(data);
		lock.release();
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
	
}
