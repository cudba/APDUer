package ch.compass.apduer.mvc.model;

import java.util.ArrayList;

import ch.compass.apduer.mvc.listener.SessionListener;


public class CurrentSessionModel {
	
	private int listenPort;
	private String remoteHost;
	private int remotePort;
	
	private ArrayList<SessionListener> listeners = new ArrayList<SessionListener>();

	
	public void setSession(int listenPort, String remoteHost, int remotePort) {
		this.listenPort = listenPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		notifySessionChanged();
	}
	
	public int getListenPort() {
		return listenPort;
	}
	
	public String getRemoteHost() {
		return remoteHost;
	}
	
	public int getRemotePort() {
		return remotePort;
	}
	
	public void addSessionListener(SessionListener listener) {
		listeners.add(listener);
	}

	private void notifySessionChanged() {
		for (SessionListener listener : listeners) {
			listener.sessionChanged();
		}
	}
}
