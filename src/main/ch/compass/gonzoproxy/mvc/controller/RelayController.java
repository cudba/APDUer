package ch.compass.gonzoproxy.mvc.controller;

import ch.compass.gonzoproxy.mvc.model.ApduData;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.relay.session.RelaySession;



public class RelayController {
	
	private RelaySession relaySession;
	private ApduData apduData;
	private CurrentSessionModel sessionModel;
	
	public RelayController(){
	}

	public void startRelaySession() {
		relaySession = new RelaySession(sessionModel); 
		new Thread(relaySession).start();
	}

	public void addModel(ApduData apduData, CurrentSessionModel sessionModel) {
		this.apduData = apduData;
		this.sessionModel = sessionModel;
	}

	public void newSession(String portListen, String remoteHost,
			String remotePort) {
		generateNewSessionDescription(portListen, remoteHost, remotePort);
//		stopRelaySession();
		startRelaySession();
		
	}

	private void generateNewSessionDescription(String portListen,
			String remoteHost, String remotePort) {
		sessionModel.setSession(Integer.parseInt(portListen), remoteHost, Integer.parseInt(remotePort));
		apduData.clear();
		sessionModel.addSessionData(apduData);
	}

	public void stopRelaySession() {
		relaySession.stop();
	}

	public ApduData getApduData() {
		return apduData;
	}
	
	public CurrentSessionModel getSessionModel() {
		return sessionModel;
	}
}
