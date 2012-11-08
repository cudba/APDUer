package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import model.Apdu;
import model.ApduData;

public class Forwarder implements Runnable {

	private Socket sourceSocket;
	private Socket forwardingSocket;
	private ApduData data;

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

		
		//TODO: move into ApduReader Class
		
		try {
			byte[] buf = new byte[4096];
			InputStream in = sourceSocket.getInputStream();
			OutputStream out = forwardingSocket.getOutputStream();
			
			int nRead;
			while ((nRead = in.read(buf, 0, buf.length)) != -1) {
				
				// Example
				Apdu apdu = new Apdu(buf);
				apdu.setDescription("Blabla - APDU");
				data.addApdu(apdu);
				
				
				
				out.write(buf, 0, nRead);
				out.flush();
				System.out.write(buf, 0, nRead);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
