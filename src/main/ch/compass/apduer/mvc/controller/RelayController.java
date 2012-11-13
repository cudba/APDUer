package ch.compass.apduer.mvc.controller;

import java.io.IOException;
import java.net.Socket;
import java.util.prefs.Preferences;

import ch.compass.apduer.mvc.model.ApduData;
import ch.compass.apduer.mvc.model.CurrentSessionModel;
import ch.compass.apduer.relay.session.RelaySession;



public class RelayController {
	

	private ApduData commandData;
	private ApduData responseData;
	private CurrentSessionModel sessionModel;
	private Preferences sessionPrefs;
	
	public RelayController(){
		sessionPrefs = Preferences.userRoot().node(this.getClass().getName());
	}

	public void startRelaySession() {
		try {
			Socket targetSocket = new Socket(sessionModel.getRemoteHost(),sessionModel.getRemotePort());
			new RelaySession(sessionModel.getListenPort(), targetSocket, commandData, responseData);
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
		stopRelaySession();
		sessionPrefs.put("listenPort", portListen);
		sessionPrefs.put("remoteHost", remoteHost);
		sessionPrefs.put("remotePort", remotePort);
		sessionModel.setSession(Integer.parseInt(portListen), remoteHost, Integer.parseInt(remotePort));
		startRelaySession();
		
	}

	private void stopRelaySession() {
		// TODO Auto-generated method stub
		
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

	public Preferences getSessionPrefs() {
		return sessionPrefs;
	}

}