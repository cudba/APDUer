package mvc.model;

import java.util.ArrayList;

import mvc.listener.SessionListener;

public class CurrentSessionModel {
	
	private String listenPort;
	private String remoteHost;
	private String remotePort;
	
	private ArrayList<SessionListener> listeners = new ArrayList<SessionListener>();

	
	public void setSession(String listenPort, String remoteHost, String remotePort) {
		this.listenPort = listenPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		notifySessionChanged();
	}
	
	public String getListenPort() {
		return listenPort;
	}
	
	public String getRemoteHost() {
		return remoteHost;
	}
	
	public String getRemotePort() {
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
