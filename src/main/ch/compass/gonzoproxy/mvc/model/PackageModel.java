package ch.compass.gonzoproxy.mvc.model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import ch.compass.gonzoproxy.mvc.listener.PackageListener;


public class PackageModel {
	
	private ArrayList<PackageListener> listeners = new ArrayList<PackageListener>();
	private ArrayList<Package> packages = new ArrayList<Package>();
	private Semaphore lock = new Semaphore(1);

	public void addPackage(Package data) {
		try {
			lock.acquire();
			packages.add(data);
			lock.release();
			notifyPackageReceived(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Package> getPackageList() {
		return packages;
	}

	public void addPackageListener(PackageListener listener) {
		listeners.add(listener);
	}

	public void clear() {
		packages.clear();
		notifyClear();
	}

	private void notifyClear() {
		for (PackageListener listener : listeners) {
			listener.apduCleared();
		}
	}

	private void notifyPackageReceived(Package receivedPackage) {
		for (PackageListener listener : listeners) {
			listener.packageReceived(receivedPackage);
		}
	}
	
}
