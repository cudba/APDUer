package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Curdin Barandun
 * 
 * A relay session consists of two forwarder threads, one for each side of the relay
 *
 */
public class RelaySession {
	
	private final int PORT = 1234;
	private ServerSocket serverSocket;
	private Socket initiatorSocket;
	
	public RelaySession(Socket target) {
		establishConnection();
		initForwarding(target);
	}

	private void establishConnection() {
		try {
			serverSocket = new ServerSocket(PORT);
			initiatorSocket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initForwarding(Socket target) {
		new Thread(new Forwarder(initiatorSocket,target)).start();
		new Thread(new Forwarder(target, initiatorSocket)).start();
	}

}
