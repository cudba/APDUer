package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import ch.compass.gonzoproxy.mvc.listener.PacketListener;



public class PacketModel {
	
	private ArrayList<PacketListener> listeners = new ArrayList<PacketListener>();
	private ArrayList<Packet> packets = new ArrayList<Packet>();
	private Semaphore lock = new Semaphore(1);

	public void addPacket(Packet data) {
		try {
			lock.acquire();
			packets.add(data);
			lock.release();
			notifyPacketReceived(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Packet> getPacketList() {
		return packets;
	}

	public void addPacketListener(PacketListener listener) {
		listeners.add(listener);
	}

	public void clear() {
		packets.clear();
		notifyClear();
	}

	private void notifyClear() {
		for (PacketListener listener : listeners) {
			listener.packetCleared();
		}
	}

	private void notifyPacketReceived(Packet receivedPacket) {
		for (PacketListener listener : listeners) {
			listener.packetReceived(receivedPacket);
		}
	}
	
}
