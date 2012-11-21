package ch.compass.gonzoproxy.relay.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.ApduType;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.relay.io.ApduStreamHandler;
import ch.compass.gonzoproxy.relay.parser.ApduAnalyzer;

public class Forwarder implements Runnable {

	private boolean sessionIsAlive = true;
	private ApduAnalyzer parsingHandler = new ApduAnalyzer();
	private ApduStreamHandler streamHandler = new ApduStreamHandler();

	private Socket sourceSocket;
	private Socket forwardingSocket;
	private ApduType type;
	private CurrentSessionModel sessionModel;

	public Forwarder(Socket sourceSocket, Socket forwardingSocket,
			CurrentSessionModel sessionModel, ApduType type) {
		this.sourceSocket = sourceSocket;
		this.forwardingSocket = forwardingSocket;
		this.sessionModel = sessionModel;
		this.type = type;
	}

	@Override
	public void run() {

		relayCommunication();

	}

	private void relayCommunication() {
		System.out.println("Relay from "
				+ sourceSocket.getInetAddress().getHostAddress() + " to "
				+ forwardingSocket.getInetAddress().getHostAddress());

		try (InputStream inputStream = new BufferedInputStream(
				sourceSocket.getInputStream());
				OutputStream outStream = new BufferedOutputStream(
						forwardingSocket.getOutputStream())) {
			while (sessionIsAlive) {
				Queue<Apdu> receivedApdus = streamHandler.readApdu(inputStream);
				while (!receivedApdus.isEmpty()) {
					Apdu apdu = receivedApdus.poll();
					parsingHandler.processApdu(apdu);
					sessionModel.addApdu(apdu);
					// apdu needs new isModified field for type column in table
					// if isTrapped -> yield
					// if apdu is manually modified, apduData.getSendApdu is
					// overwritten by modified apdu and
					// modified apdu is added in apduData list
					// if modifier.isActive -> modifier.modify(apdu)
					// modified apdu is added to apduData list and
					// apduData.getSendApdu is overwritten by modified apdu
					apdu.setType(type);
					// new: streamHandler.sendApdu(outStream,
					// data.getSendApdu());

					streamHandler.sendApdu(outStream, apdu);
				}

			}
		} catch (IOException e) {
			try {
				sourceSocket.close();
				forwardingSocket.close();
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
			}
			
		}

	}

	public void stop() {
		sessionIsAlive = false;
	}
}
