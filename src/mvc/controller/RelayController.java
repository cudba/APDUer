package mvc.controller;

import java.io.IOException;
import java.net.Socket;

import relay.session.RelaySession;

public class RelayController {

	public void startRelaySession() {
		try {
			new RelaySession(new Socket("localhost", 4321));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
