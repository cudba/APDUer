package mvc.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import mvc.model.CommandData;
import mvc.model.ResponseData;

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
	
	// TODO: initialize model somewhere else :>
	private CommandData apduCommands = new CommandData();
	private ResponseData apduResponses = new ResponseData();
	
	
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
		new Thread(new Forwarder(initiatorSocket,target, apduCommands)).start();
		new Thread(new Forwarder(target, initiatorSocket, apduResponses)).start();
	}

}
