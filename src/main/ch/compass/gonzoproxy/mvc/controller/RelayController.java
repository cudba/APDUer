package ch.compass.gonzoproxy.mvc.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.model.ParserSettings;
import ch.compass.gonzoproxy.relay.session.RelaySession;

public class RelayController {

	private RelaySession relaySession;
	private CurrentSessionModel sessionModel;
	private String[] modes;

	public RelayController() {
		this.sessionModel = new CurrentSessionModel();
		loadModes();
	}

	private void loadModes() {
		ArrayList<String> inputModes = new ArrayList<>();

		ResourceBundle bundle = ResourceBundle.getBundle("plugin");

		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String element = keys.nextElement();
			if (element.contains("name")) {
				inputModes.add(bundle.getString(element));
			}
		}

		this.modes = inputModes.toArray(new String[2]);
	}

	public void startRelaySession() {
		relaySession = new RelaySession(sessionModel);
		new Thread(relaySession).start();
	}

	public void newSession(String portListen, String remoteHost,
			String remotePort, String mode) {
		generateNewSessionDescription(portListen, remoteHost, remotePort, mode);
		startRelaySession();

	}

	private void generateNewSessionDescription(String portListen,
			String remoteHost, String remotePort, String mode) {
		sessionModel.setSession(Integer.parseInt(portListen), remoteHost,
				Integer.parseInt(remotePort));
		sessionModel.clearData();
		sessionModel.setMode(mode);
		sessionModel.setSessionFormat(ParserSettings.LibNFC);
	}

	public void clearSession() {
		if (relaySession != null) {
			relaySession.stopForwarder();
		}
	}

	public void stopRelaySession() {
		relaySession.stopForwarder();
	}

	public CurrentSessionModel getSessionModel() {
		return sessionModel;
	}

	public void changeCommandTrap() {
		if (sessionModel.isCommandTrapped()) {
			sessionModel.setCommandTrapped(false);
		} else {
			sessionModel.setCommandTrapped(true);
		}
	}

	public void changeResponseTrap() {
		if (sessionModel.isResponseTrapped()) {
			sessionModel.setResponseTrapped(false);
		} else {
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
