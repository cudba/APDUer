package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class RelayController {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		
		// Example: relaySocket = initiator ip & port from socat (nfc-relay-picc)
		
		
		Socket relaySocket = new Socket("localhost", 4321);
		
		new RelaySession(relaySocket);
	}

}
