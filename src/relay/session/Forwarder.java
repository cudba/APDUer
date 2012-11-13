package relay.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import mvc.model.Apdu;
import mvc.model.ApduData;
import relay.io.ApduStreamHandler;

public class Forwarder implements Runnable {

	private Socket sourceSocket;
	private Socket forwardingSocket;
	private ApduData data;
	private ApduStreamHandler streamHandler = new ApduStreamHandler('#');

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

		try (InputStream inputStream = sourceSocket.getInputStream();
				OutputStream outStream = forwardingSocket.getOutputStream()) {
			while (true) {
				byte[] receivedApdu = streamHandler.readApdu(inputStream);
				Apdu apdu = new Apdu(receivedApdu);
				data.addApdu(apdu);
				streamHandler.sendApdu(outStream, receivedApdu);
				System.out.print(new String(apdu.getOriginalApdu()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
