package ch.compass.gonzoproxy.mvc.listener;

import ch.compass.gonzoproxy.mvc.model.Package;


public interface PackageListener {

	public void packageReceived(Package apdu);
	
	public void apduCleared();

}
