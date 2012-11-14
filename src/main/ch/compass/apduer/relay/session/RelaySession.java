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
	private ApduData commandData;
	private ApduData responseData;
	private int serverPort;
	
	// TODO: initialize model somewhere else :>
	
	
	
	public RelaySession(int serverPort, Socket target, ApduData commandData, ApduData responseData) {
		this.serverPort = serverPort;
		this.commandData = commandData;
		this.responseData = responseData;
		establishConnection();
		initForwardingThread(target);
	}

	private void establishConnection() {
		try {
			serverSocket = new ServerSocket(serverPort);
			initiatorSocket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initForwardingThread(Socket target) {
		new Thread(new Forwarder(initiatorSocket,target, commandData)).start();
		new Thread(new Forwarder(target, initiatorSocket, responseData)).start();
	}

}
