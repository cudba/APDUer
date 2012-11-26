package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import ch.compass.gonzoproxy.mvc.listener.SessionListener;

public class CurrentSessionModel {

	private Preferences sessionPrefs;
	private ArrayList<SessionListener> listeners = new ArrayList<SessionListener>();
	private PacketModel sessionData;
	private ParserSettings sessionFormat = ParserSettings.LibNFC;
	private Boolean commandTrapped = false;
	private Boolean responseTrapped = false;
	private Boolean sendOneCommand;
	private Boolean sendOneResponse;
	private String mode;

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
		listeners.add(listener);
	}

	private void notifySessionChanged() {
		for (SessionListener listener : listeners) {
			listener.sessionChanged();
		}
	}

	public void addSessionData(PacketModel apduData) {
		this.sessionData = apduData;

	}

	public PacketModel getSessionData() {
		return sessionData;
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

	public void addSessionData(Packet apdu) {
		sessionData.addPacket(apdu);
	}

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

}
