package controller;

import java.io.IOException;
import java.net.Socket;

public class RelayController {

	public void startRelaySession() {
		try {
			new RelaySession(new Socket("localhost", 4321));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
