package ch.compass.gonzoproxy.relay.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.UnexpectedException;
import java.util.Enumeration;
import java.util.Queue;
import java.util.ResourceBundle;

import ch.compass.gonzoproxy.GonzoProxy;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.model.ForwardingType;
import ch.compass.gonzoproxy.mvc.model.Packet;
import ch.compass.gonzoproxy.relay.io.ApduExtractor;
import ch.compass.gonzoproxy.relay.io.ApduStreamHandler;
import ch.compass.gonzoproxy.relay.io.ApduWrapper;
import ch.compass.gonzoproxy.relay.modifier.PacketModifier;
import ch.compass.gonzoproxy.relay.modifier.Rule;
import ch.compass.gonzoproxy.relay.modifier.RuleSet;
import ch.compass.gonzoproxy.relay.parser.ParsingHandler;

public class Forwarder implements Runnable {

	private boolean sessionIsAlive = true;

	private ParsingHandler parsingHandler;
	private ApduStreamHandler streamHandler;

	private Socket sourceSocket;
	private Socket forwardingSocket;
	private ForwardingType type;

	// TODO: NOT HERE !
	private PacketModifier packetModifier = new PacketModifier();

	private CurrentSessionModel sessionModel;

	public Forwarder(Socket sourceSocket, Socket forwardingSocket,
			CurrentSessionModel sessionModel, ForwardingType type) {
		this.sourceSocket = sourceSocket;
		this.forwardingSocket = forwardingSocket;
		this.sessionModel = sessionModel;
		this.type = type;
		initForwardingComponents();
		fakeRules();
	}

	// just for demonstration -- ruleSet for Select Response Packet, 1 rule defined for Status Byte 1
	private void fakeRules() {
		RuleSet selectResponseRule = new RuleSet("Select Response");
		Rule statusByteRule = new Rule("Status Byte 1", "90", "FF");
		selectResponseRule.add(statusByteRule);
		packetModifier.add(selectResponseRule);
		
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
				Queue<Packet> receivedPackets = streamHandler
						.readApdu(inputStream);
				while (!receivedPackets.isEmpty()) {

					Packet receivedPacket = receivedPackets.poll();
					Packet sendingPacket = processPacket(receivedPacket);

					handleTraps();

					streamHandler.sendPacket(outStream, sendingPacket);
					sessionModel.sendOneCommand(false);
					sessionModel.sendOneResponse(false);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: status update sessionModel -> connection lost
		} finally {
			closeSockets();
		}

	}

	private Packet processPacket(Packet packet) {
		packet.setType(type);
		parsingHandler.tryParse(packet);
		sessionModel.addPacket(packet);
	
		Packet processedPacket = packetModifier.tryModify(packet);
		if (processedPacket.isModified())
			sessionModel.addPacket(processedPacket);
	
		return processedPacket;
	}

	private void initForwardingComponents() {
		try {
			parsingHandler = new ParsingHandler(sessionModel.getSessionFormat());
			configureStreamHandler();
		} catch (UnexpectedException e) {
			e.printStackTrace();
			closeSockets();
		}
	}

	private void configureStreamHandler() throws UnexpectedException {
		ClassLoader cl = GonzoProxy.class.getClassLoader();
		ApduExtractor extractor = (ApduExtractor) selectMode(cl, "extractor");
		ApduWrapper wrapper = (ApduWrapper) selectMode(cl, "wrapper");
	
		System.out.println("Extractor: " + extractor.getName());
		System.out.println("Wrapper: " + wrapper.getName());
	
		streamHandler = new ApduStreamHandler(extractor, wrapper);
	}

	private Object selectMode(ClassLoader cl, String helper) {
	
		ResourceBundle bundle = ResourceBundle.getBundle("plugin");
	
		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String element = keys.nextElement();
			if (element.contains(helper)
					&& element.contains(sessionModel.getMode())) {
				try {
					return cl.loadClass(bundle.getString(element))
							.newInstance();
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private void handleTraps() {
		if (type == ForwardingType.COMMAND) {
			while (sessionModel.isCommandTrapped()
					&& !sessionModel.shouldSendOneCommand()) {
				Thread.yield();
			}
		} else {
			while (sessionModel.isResponseTrapped()
					&& !sessionModel.shouldSendOneResponse()) {
				Thread.yield();
			}
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
