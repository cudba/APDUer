package ch.compass.gonzoproxy.relay.session;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.compass.gonzoproxy.mvc.model.ApduType;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;


/**
 * 
 * @author Curdin Barandun
 * 
 * A relay session consists of two forwarder threads, one for each side of the relay
 *
 */
public class RelaySession implements Runnable {
	
	private ServerSocket serverSocket;
	private Socket initiatorSocket;
	private Socket target;
	private CurrentSessionModel sessionModel;
	
	
	
	public RelaySession(CurrentSessionModel sessionModel) {
		this.sessionModel = sessionModel;
	}

	@Override
	public void run() {
		establishConnection();
		initForwardingThreads();
	}

	private void establishConnection() {
		try {
			serverSocket = new ServerSocket(sessionModel.getListenPort());
			initiatorSocket = serverSocket.accept();
			target = new Socket(sessionModel.getRemoteHost(), sessionModel.getRemotePort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initForwardingThreads() {
		new Thread(new Forwarder(initiatorSocket,target, sessionModel.getSessionData(), ApduType.COMMAND)).start();
		new Thread(new Forwarder(target, initiatorSocket, sessionModel.getSessionData(), ApduType.RESPONSE)).start();
	}


}
