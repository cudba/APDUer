package ch.compass.gonzoproxy.mvc.controller;

import ch.compass.gonzoproxy.mvc.model.ApduData;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.model.SessionFormat;
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
		startRelaySession();
		
	}

	private void generateNewSessionDescription(String portListen,
			String remoteHost, String remotePort) {
		sessionModel.setSession(Integer.parseInt(portListen), remoteHost, Integer.parseInt(remotePort));
		apduData.clear();
		sessionModel.addSessionData(apduData);
		sessionModel.setSessionFormat(SessionFormat.LibNFC);
	}
	
	public void clearSession(){
//		apduData.clear();
		if(relaySession != null){
			relaySession.stopForwarder();
		}
	}

	public void stopRelaySession() {
		relaySession.stopForwarder();
	}

	public ApduData getApduData() {
		return apduData;
	}
	
	public CurrentSessionModel getSessionModel() {
		return sessionModel;
	}

	public void changeCommandTrap() {
		if(sessionModel.isCommandTrapped()){
			sessionModel.setCommandTrapped(false);
		}else{
			sessionModel.setCommandTrapped(true);
		}
	}

	public void changeResponseTrap() {
		if(sessionModel.isResponseTrapped()){
			sessionModel.setResponseTrapped(false);
		}else{
			sessionModel.setResponseTrapped(true);
		}
	}

	public void sendOneCmd() {
		sessionModel.sendOneCommand(true);
	}

	public void sendOneRes() {
		sessionModel.sendOneResponse(true);
	}
}
