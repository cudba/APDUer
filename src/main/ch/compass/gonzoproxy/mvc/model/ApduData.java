package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import ch.compass.gonzoproxy.mvc.listener.ApduListener;


public class ApduData {
	
	private ArrayList<ApduListener> listeners = new ArrayList<ApduListener>();
	private ArrayList<Apdu> apdu = new ArrayList<Apdu>();
	private Semaphore lock = new Semaphore(1);

	public void addApdu(Apdu data) {
		try {
			lock.acquire();
			apdu.add(data);
			lock.release();
			notifyApduReceived(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Apdu> getApduList() {
		return apdu;
	}

	public void addApduListener(ApduListener listener) {
		listeners.add(listener);
	}

	public void clear() {
		apdu.clear();
		notifyClear();
	}

	private void notifyClear() {
		for (ApduListener listener : listeners) {
			listener.apduCleared();
		}
	}

	private void notifyApduReceived(Apdu apdu) {
		for (ApduListener listener : listeners) {
			listener.apduReceived(apdu);
		}
	}
	
}
