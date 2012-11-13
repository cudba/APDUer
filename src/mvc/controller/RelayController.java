package mvc.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.prefs.Preferences;

import mvc.model.ApduData;
import mvc.model.CurrentSessionModel;

import relay.session.RelaySession;

public class RelayController {
	

	private ApduData commandData;
	private ApduData responseData;
	private CurrentSessionModel sessionModel;
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

	public void addModel(ApduData commandData, ApduData responseData, CurrentSessionModel sessionModel) {
		this.commandData = commandData;
		this.responseData = responseData;
		this.sessionModel = sessionModel;
	}

	public void newSession(String portListen, String remoteHost,
			String remotePort) {
		sessionPrefs.put("listenPort", portListen);
		sessionPrefs.put("remoteHost", remoteHost);
		sessionPrefs.put("remotePort", remotePort);
		sessionModel.setSession(portListen, remoteHost, remotePort);
		// TODO Auto-generated method stub
		
	}

	public Preferences getSessionPrefs() {
		return sessionPrefs;
	}

	public void setSessionPrefs(Preferences sessionPrefs) {
		this.sessionPrefs = sessionPrefs;
	}
	
	public ApduData getCommandData() {
		return commandData;
	}
	
	public ApduData getResponseData() {
		return responseData;
	}
	
	public CurrentSessionModel getSessionModel() {
		return sessionModel;
	}

}
