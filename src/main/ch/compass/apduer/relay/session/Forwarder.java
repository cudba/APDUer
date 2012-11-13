package ch.compass.apduer.relay.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;

import ch.compass.apduer.mvc.model.Apdu;
import ch.compass.apduer.mvc.model.ApduData;
import ch.compass.apduer.relay.io.ApduStreamHandler;


public class Forwarder implements Runnable {

	private Socket sourceSocket;
	private Socket forwardingSocket;
	private ApduData data;
	private ApduStreamHandler streamHandler = new ApduStreamHandler();

	public Forwarder(Socket sourceSocket, Socket forwardingSocket, ApduData data) {
		this.sourceSocket = sourceSocket;
		this.forwardingSocket = forwardingSocket;
		this.data = data;
	}

	@Override
	public void run() {

		relayCommunication();

	}

	private void relayCommunication() {
		System.out.println("Relay from "
				+ sourceSocket.getInetAddress().getHostAddress() + " to "
				+ forwardingSocket.getInetAddress().getHostAddress());

		try (InputStream inputStream = new BufferedInputStream(sourceSocket.getInputStream());
				OutputStream outStream = new BufferedOutputStream(forwardingSocket.getOutputStream())) {
			while (true) {
				Queue<Apdu> receivedApdus = streamHandler.readApdu(inputStream);
				System.out.println("Adding received Apdus");

				while(!receivedApdus.isEmpty()) {
					Apdu apdu = receivedApdus.poll();
					data.addApdu(apdu);
					streamHandler.sendApdu(outStream, apdu.getOriginalApdu());
					System.out.println(apdu.toString());
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
