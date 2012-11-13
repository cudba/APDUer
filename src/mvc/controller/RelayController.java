package mvc.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.prefs.Preferences;

import mvc.model.ApduData;

import relay.session.RelaySession;

public class RelayController {
	

	private ApduData commandData;
	private ApduData responseData;
	private Preferences sessionPrefs;
	
	public RelayController(){
		setSessionPrefs(Preferences.userRoot().node(this.getClass().getName()));
	}

	public void startRelaySession() {
		try {
			new RelaySession(new Socket("localhost", 4321), commandData, responseData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addModel(ApduData commandData, ApduData responseData) {
		this.commandData = commandData;
		this.responseData = responseData;
		
	}

	public void newSession(String portListen, String remoteHost,
			String remotePort) {
		sessionPrefs.put("listenPort", portListen);
		sessionPrefs.put("remoteHost", remoteHost);
		sessionPrefs.put("remotePort", remotePort);
		// TODO Auto-generated method stub
		
	}

	public Preferences getSessionPrefs() {
		return sessionPrefs;
	}

	public void setSessionPrefs(Preferences sessionPrefs) {
		this.sessionPrefs = sessionPrefs;
	}

}
