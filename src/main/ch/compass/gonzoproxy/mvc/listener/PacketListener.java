package ch.compass.gonzoproxy.mvc.listener;

import ch.compass.gonzoproxy.mvc.model.Packet;


public interface PacketListener {

	public void packetReceived(Packet apdu);
	
	public void packetCleared();

}
