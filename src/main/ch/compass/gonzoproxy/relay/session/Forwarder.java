package ch.compass.gonzoproxy.relay.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.UnexpectedException;
import java.util.Queue;

import ch.compass.gonzoproxy.mvc.model.Apdu;
import ch.compass.gonzoproxy.mvc.model.ForwardingType;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.relay.io.ApduStreamHandler;
import ch.compass.gonzoproxy.relay.io.LibNfcApduExtractor;
import ch.compass.gonzoproxy.relay.io.LibNfcApduWrapper;
import ch.compass.gonzoproxy.relay.parser.ApduAnalyzer;

public class Forwarder implements Runnable {

	private boolean sessionIsAlive = true;

	private ApduAnalyzer parsingHandler;
	private ApduStreamHandler streamHandler;

	private Socket sourceSocket;
	private Socket forwardingSocket;
	private ForwardingType type;

	private CurrentSessionModel sessionModel;

	public Forwarder(Socket sourceSocket, Socket forwardingSocket,
			CurrentSessionModel sessionModel, ForwardingType type) {
		this.sourceSocket = sourceSocket;
		this.forwardingSocket = forwardingSocket;
		this.sessionModel = sessionModel;
		this.type = type;
		initForwardingComponents();
	}

	private void initForwardingComponents() {
		try {
			parsingHandler = new ApduAnalyzer(sessionModel.getSessionMode(),
					type);
			configureStreamHandler();
		} catch (UnexpectedException e) {
			e.printStackTrace();
		} finally {
			closeSockets();
		}
	}

	private void configureStreamHandler() throws UnexpectedException {
		switch (sessionModel.getSessionMode()) {
		case LibNFC:
			LibNfcApduExtractor extractor = new LibNfcApduExtractor();
			LibNfcApduWrapper wrapper = new LibNfcApduWrapper();
			streamHandler = new ApduStreamHandler(extractor, wrapper);
			break;
		// TODO: no helpers found exception
		default:
			throw new UnexpectedException("No Matching StreamHelpers found");
		}
	}

	@Override
	public void run() {
		relayCommunication();
	}

	public void stop() {
		sessionIsAlive = false;
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
					apdu.setType(type);
					parsingHandler.processApdu(apdu);
					sessionModel.addApdu(apdu);
					// apdu needs new isModified field for type column in table
					// if isTrapped -> yield

					switch (type) {
					case COMMAND:
						while (sessionModel.isCommandTrapped()
								&& !sessionModel.getSendOneCmd()) {
							Thread.yield();
						}
						break;

					case RESPONSE:
						while (sessionModel.isResponseTrapped()
								&& !sessionModel.getSendOneRes()) {
							Thread.yield();
						}
					}

					// if apdu is manually modified, apduData.getSendApdu is
					// overwritten by modified apdu and
					// modified apdu is added in apduData list
					// if modifier.isActive -> modifier.modify(apdu)
					// modified apdu is added to apduData list and
					// apduData.getSendApdu is overwritten by modified apdu
					// new: streamHandler.sendApdu(outStream,
					// data.getSendApdu());

					streamHandler.sendApdu(outStream, apdu);
					sessionModel.sendOneCmd(false);
					sessionModel.sendOneRes(false);
				}

			}
		} catch (IOException e) {
			// TODO: status update sessionModel -> connection lost
		} finally {
			closeSockets();
		}

	}

	private void closeSockets() {
		try {
			sourceSocket.close();
			forwardingSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
