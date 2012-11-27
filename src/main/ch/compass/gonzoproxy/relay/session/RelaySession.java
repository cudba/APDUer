package ch.compass.gonzoproxy.relay.session;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.model.ForwardingType;

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
	private Socket initiator;
	private Socket target;
	private CurrentSessionModel sessionModel;

	private Forwarder commandForwarder;
	private Forwarder responseForwarder;

	public RelaySession(CurrentSessionModel sessionModel) {
		this.sessionModel = sessionModel;
	}

	@Override
	public void run() {

		establishConnection();
		initForwarder();
		startForwardingThreads();

	}

	public void stopForwarder() {
//		if (commandForwarder != null) {
			commandForwarder.stop();
//		}
//		if (responseForwarder != null) {
			responseForwarder.stop();
			
			// serverSocket soett im finally nachem 'establish connection' gschlossa werda
//		}
//		if (serverSocket != null) {
//			try {
//				serverSocket.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	private void initForwarder() {
		commandForwarder = new Forwarder(initiator, target,
				sessionModel, ForwardingType.COMMAND);
		responseForwarder = new Forwarder(target, initiator,
				sessionModel, ForwardingType.RESPONSE);
	}

	private void establishConnection() {
		try {
			serverSocket = new ServerSocket(sessionModel.getListenPort());
			initiator = serverSocket.accept();
			target = new Socket(sessionModel.getRemoteHost(),
					sessionModel.getRemotePort());
		} catch (IOException openSocket) {
			try {
				initiator.close();
				target.close();
			} catch (IOException closeSocket) {
				closeSocket.printStackTrace();
			}

		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startForwardingThreads() {
		new Thread(commandForwarder).start();
		new Thread(responseForwarder).start();
	}

}
