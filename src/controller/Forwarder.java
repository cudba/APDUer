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
			
			while (in.read(buf, 0, buf.length) != -1) {
				
				// example -> fill in apdu
				Apdu apdu = new Apdu(buf);
				apdu.setDescription("Blabla - APDU");
				data.addApdu(apdu);
				
				
				out.write(apdu.getOriginalApdu());
				out.flush();
				System.out.write(apdu.getOriginalApdu());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
