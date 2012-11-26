package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.prefs.Preferences;

import ch.compass.gonzoproxy.mvc.listener.SessionListener;

public class CurrentSessionModel {

	private Preferences sessionPrefs;
	private ArrayList<SessionListener> sessionListeners = new ArrayList<SessionListener>();
	private ParserSettings sessionFormat = ParserSettings.LibNFC;
	private Boolean commandTrapped = false;
	private Boolean responseTrapped = false;
	private Boolean sendOneCommand;
	private Boolean sendOneResponse;
	private String mode;
	private ArrayList<Packet> sessionData = new ArrayList<Packet>();
	private Semaphore lock = new Semaphore(1);
	
	public CurrentSessionModel() {
		this.sessionPrefs = Preferences.userRoot().node(
				this.getClass().getName());
	}

	public void setSession(int listenPort, String remoteHost, int remotePort) {
		sessionPrefs.putInt("listenPort", listenPort);
		sessionPrefs.put("remoteHost", remoteHost);
		sessionPrefs.putInt("remotePort", remotePort);
		notifySessionChanged();
	}

	public int getListenPort() {
		return sessionPrefs.getInt("listenPort", 1234);
	}

	public String getRemoteHost() {
		return sessionPrefs.get("remoteHost", "127.0.0.1");
	}

	public int getRemotePort() {
		return sessionPrefs.getInt("remotePort", 4321);
	}

	public void addSessionListener(SessionListener listener) {
		sessionListeners.add(listener);
	}

	private void notifySessionChanged() {
		for (SessionListener listener : sessionListeners) {
			listener.sessionChanged();
		}
	}

	public Boolean isResponseTrapped() {
		return responseTrapped;
	}

	public Boolean isCommandTrapped() {
		return commandTrapped;
	}

	public void setCommandTrapped(Boolean cmdTrap) {
		this.commandTrapped = cmdTrap;
	}

	public void setResponseTrapped(Boolean resTrap) {
		this.responseTrapped = resTrap;
	}

	public void sendOneCommand(boolean send) {
		this.sendOneCommand = send;
	}

	public void sendOneResponse(boolean send) {
		this.sendOneResponse = send;
	}

	public Boolean shouldSendOneCommand() {
		return sendOneCommand;
	}

	public Boolean shouldSendOneResponse() {
		return sendOneResponse;
	}

//	public void addSessionData(Packet apdu) {
//		sessionData.addPacket(apdu);
//	}

	public ParserSettings getSessionFormat() {
		return sessionFormat;
	}

	public void setSessionFormat(ParserSettings sessionMode) {
		this.sessionFormat = sessionMode;
	}
	

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getMode() {
		return mode;
	}

	public void addPacket(Packet data) {
		try {
			lock.acquire();
			sessionData.add(data);
			lock.release();
			notifyPacketReceived(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Packet> getPacketList() {
		return sessionData;
	}

	public void clearData() {
		sessionData.clear();
		notifyClear();
	}

	private void notifyClear() {
		for (SessionListener listener : sessionListeners) {
			listener.packetCleared();
		}
	}

	private void notifyPacketReceived(Packet receivedPacket) {
		for (SessionListener listener : sessionListeners) {
			listener.packetReceived(receivedPacket);
		}
	}

	public void addList(ArrayList<Packet> readObject) {
		this.sessionData = readObject;
		notifyNewList();
	}

	private void notifyNewList() {
		for (SessionListener listener : sessionListeners) {
			listener.newList();
		}		
	}
	
	

}
