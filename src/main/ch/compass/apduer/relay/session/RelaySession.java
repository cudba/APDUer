package ch.compass.apduer.relay.session;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.compass.apduer.mvc.model.ApduData;


/**
 * 
 * @author Curdin Barandun
 * 
 * A relay session consists of two forwarder threads, one for each side of the relay
 *
 */
public class RelaySession {
	
	private ServerSocket serverSocket;
	private Socket initiatorSocket;
	private ApduData apduData;
	private int serverPort;
	
	// TODO: initialize model somewhere else :>
	
	
	
	public RelaySession(int serverPort, Socket target, ApduData apduData) {
		this.serverPort = serverPort;
		this.apduData = apduData;
		establishConnection();
		initForwardingThreads(target);
	}

	private void establishConnection() {
		try {
			serverSocket = new ServerSocket(serverPort);
			initiatorSocket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initForwardingThreads(Socket target) {
		new Thread(new Forwarder(initiatorSocket,target, apduData, "command")).start();
		new Thread(new Forwarder(target, initiatorSocket, apduData, "response")).start();
	}

}
