package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class RelayController {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket relaySocket = new Socket("localhost", 4321);
		
		new RelaySession(relaySocket);
	}

}
