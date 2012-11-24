package ch.compass.gonzoproxy.mvc.controller;

import java.util.ArrayList;

import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.model.PacketModel;
import ch.compass.gonzoproxy.mvc.model.ParserSettings;
import ch.compass.gonzoproxy.relay.session.RelaySession;



public class RelayController {
	
	private RelaySession relaySession;
	private PacketModel apduData;
	private CurrentSessionModel sessionModel;
	private String[] modes;
	
	public RelayController(String[] modesArr){
		this.modes = modesArr;
	}

	public void startRelaySession() {
		relaySession = new RelaySession(sessionModel); 
		new Thread(relaySession).start();
	}

	public void addModel(PacketModel apduData, CurrentSessionModel sessionModel) {
		this.apduData = apduData;
		this.sessionModel = sessionModel;
	}

	public void newSession(String portListen, String remoteHost,
			String remotePort, String mode) {
		generateNewSessionDescription(portListen, remoteHost, remotePort, mode);
		startRelaySession();
		
	}

	private void generateNewSessionDescription(String portListen,
			String remoteHost, String remotePort, String mode) {
		sessionModel.setSession(Integer.parseInt(portListen), remoteHost, Integer.parseInt(remotePort));
		apduData.clear();
		sessionModel.addSessionData(apduData);
		sessionModel.setMode(mode);
		sessionModel.setSessionFormat(ParserSettings.LibNFC);
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

	public PacketModel getApduData() {
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
	
	public String[] getModes() {
		return modes;
	}
}
