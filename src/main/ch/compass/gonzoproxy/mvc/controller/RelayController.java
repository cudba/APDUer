package ch.compass.gonzoproxy.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.mvc.model.ParserSettings;
import ch.compass.gonzoproxy.relay.modifier.PacketModifier;
import ch.compass.gonzoproxy.relay.modifier.Rule;
import ch.compass.gonzoproxy.relay.session.RelaySession;

public class RelayController {

	private PacketModifier packetModifier;
	private RelaySession relaySession;
	private CurrentSessionModel sessionModel;
	private String[] modes;

	public RelayController() {
		this.sessionModel = new CurrentSessionModel();
		loadModes();
		loadModifier();

	}

	private void loadModifier() {
		// TODO: load persited modifiers from file
		packetModifier = new PacketModifier();
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
		sessionModel.setPacketModifier(packetModifier);
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

	public void addModifierRule(String packetName, String fieldName,
			String originalValue, String replacedValue, Boolean updateLength) {
		Rule fieldRule = new Rule(fieldName, originalValue, replacedValue);
		packetModifier.addRule(packetName, fieldRule);
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

	@SuppressWarnings("unchecked")
	public void openFile(File file) {
		FileInputStream fin;
		try {
			fin = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fin);
			sessionModel.addList((ArrayList<Packet>) ois.readObject());
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveFile(File file) {
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(sessionModel.getPacketList());
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
