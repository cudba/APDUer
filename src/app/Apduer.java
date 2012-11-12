package app;

import mvc.controller.RelayController;
import mvc.model.ApduData;
import mvc.view.ApduListFrame;
import mvc.view.EntryPoint;

public class Apduer {

	//TODO: move into another package
	
	public static void main(String[] args) {
		
		ApduData commandData = new ApduData();
		ApduData responseData = new ApduData();	
		
		//start entrypoint
		//get settings
		//start relaysession
		//start apdulistframe
		
		EntryPoint entryPoint = new EntryPoint();
		entryPoint.setVisible(true);
		
		ApduListFrame view = new ApduListFrame(responseData, commandData);
		view.setVisible(true);


//		RelayController controller = new RelayController(int listenPort, String remoteHost, int remotePort);
//		controller.addModel(commandData, responseData);
//		controller.startRelaySession();
		
		
	}
	
}
