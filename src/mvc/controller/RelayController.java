package mvc.controller;

import java.io.IOException;
import java.net.Socket;

import mvc.model.ApduData;

import relay.session.RelaySession;

public class RelayController {
	

	private ApduData commandData;
	private ApduData responseData;
	private int serverPort;
	private String remoteHost;
	private int remotePort;

	public void startRelaySession() {
		try {
			Socket targetSocket = new Socket(remoteHost,remotePort);
			new RelaySession(serverPort, targetSocket, commandData, responseData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addModel(ApduData commandData, ApduData responseData) {
		this.commandData = commandData;
		this.responseData = responseData;
		
	}

	public void setConnectionParameters(int serverPort, String remoteHost, int remotePort) {
		this.serverPort = serverPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		
	}

}
