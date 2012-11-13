package mvc.controller;

import java.io.IOException;
import java.net.Socket;

import mvc.model.ApduData;

import relay.session.RelaySession;

public class RelayController {
	

	private ApduData commandData;
	private ApduData responseData;

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
		// TODO Auto-generated method stub
		
	}

}
