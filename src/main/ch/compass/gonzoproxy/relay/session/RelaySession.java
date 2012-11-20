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
 *         A relay session consists of two forwarder threads, one for each side
 *         of the relay
 * 
 */
public class RelaySession implements Runnable {

	private ServerSocket serverSocket;
	private Socket initiatorSocket;
	private Socket target;
	private CurrentSessionModel sessionModel;

	private Forwarder commandForwarder;
	private Forwarder responseForwarder;

	public RelaySession(CurrentSessionModel sessionModel) {
		this.sessionModel = sessionModel;
	}

	@Override
	public void run() {

		try {
			establishConnection();
			initForwarder();
			startForwardingThreads();
		} catch (IOException e) {
			try {
				serverSocket.close();
				initiatorSocket.close();
				target.close();
				sessionModel.setSession(sessionModel.getListenPort(),
						"Connection lost...", 0);
				stop();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public void stop() {
		commandForwarder.stop();
		responseForwarder.stop();
	}

	private void initForwarder() {
		commandForwarder = new Forwarder(initiatorSocket, target,
				sessionModel.getSessionData(), ApduType.COMMAND);
		responseForwarder = new Forwarder(target, initiatorSocket,
				sessionModel.getSessionData(), ApduType.RESPONSE);
	}

	private void establishConnection() throws IOException {
		serverSocket = new ServerSocket(sessionModel.getListenPort());
		initiatorSocket = serverSocket.accept();
		target = new Socket(sessionModel.getRemoteHost(),
				sessionModel.getRemotePort());
	}

	private void startForwardingThreads() {
		new Thread(commandForwarder).start();
		new Thread(responseForwarder).start();
	}

}
