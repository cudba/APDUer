package controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Forwarder implements Runnable {

	private ServerSocket serverSocket;
	private Socket sourceSocket;
	private Socket forwardingSocket;

	public Forwarder(Socket sourceSocket, Socket forwardingSocket) {
		this.sourceSocket = sourceSocket;
		this.forwardingSocket = forwardingSocket;
	}

	@Override
	public void run() {

		relayCommunication();

	}


	private void relayCommunication() {

		System.out.println("Relay from "
				+ sourceSocket.getInetAddress().getHostAddress() + " to "
				+ forwardingSocket.getInetAddress().getHostAddress());

		
		try {
			byte[] buf = new byte[4096];
			InputStream in = sourceSocket.getInputStream();

			int nRead;
			while ((nRead = in.read(buf, 0, buf.length)) != -1) {
				System.out.write(buf, 0, nRead);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
