package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import ch.compass.gonzoproxy.mvc.listener.SessionListener;


public class CurrentSessionModel {
	
	private Preferences sessionPrefs;
	
	private ArrayList<SessionListener> listeners = new ArrayList<SessionListener>();

	private ApduData apduData;
	
	public CurrentSessionModel(){
		this.sessionPrefs = Preferences.userRoot().node(this.getClass().getName());

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


	public void addSessionData(ApduData apduData) {
		this.apduData = apduData;
		
	}
	
	public ApduData getSessionData() {
		return apduData;
	}
}