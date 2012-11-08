package relay.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import mvc.model.Apdu;
import mvc.model.ApduData;

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

		// TODO: move into ApduReader Class

		OutputStream out;
		InputStream in;
		
		try {
			
			in = sourceSocket.getInputStream();
			out = forwardingSocket.getOutputStream();
		
		while (true) {
			try {
				byte[] buf = new byte[4096];

				while (in.read(buf, 0, buf.length) != -1) {
					out.write(buf,0,buf.length);
					out.flush();
					System.out.write(buf, 0, buf.length);
					System.out.flush();
					buf = new byte[4096];

				}
				

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
