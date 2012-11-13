package app;

import mvc.controller.RelayController;
import mvc.model.ApduData;
import mvc.model.CurrentSessionModel;
import mvc.view.ApduListFrame;

public class Apduer {

	//TODO: move into another package
	
	public static void main(String[] args) {
		
		ApduData commandData = new ApduData();
		ApduData responseData = new ApduData();	
		CurrentSessionModel sessionModel = new CurrentSessionModel();
		
		//start entrypoint
		//get settings
		//start relaysession
		//start apdulistframe
		RelayController controller = new RelayController();
		controller.addModel(commandData, responseData, sessionModel);
		
		ApduListFrame view = new ApduListFrame(controller);
		view.setVisible(true);


//		RelayController controller = new RelayController(int listenPort, String remoteHost, int remotePort);
//		controller.addModel(commandData, responseData);
//		controller.startRelaySession();
		
		
	}
	
}
