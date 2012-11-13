package ch.compass.apduer;

import ch.compass.apduer.mvc.controller.RelayController;
import ch.compass.apduer.mvc.model.ApduData;
import ch.compass.apduer.mvc.model.CurrentSessionModel;
import ch.compass.apduer.mvc.view.ApduListFrame;

public class Apduer {

	public static void main(String[] args) {
		
		//TODO inizialize models and view in controller and just start controller?
		
		ApduData commandData = new ApduData();
		ApduData responseData = new ApduData();	
		CurrentSessionModel sessionModel = new CurrentSessionModel();
		
		RelayController controller = new RelayController();
		controller.addModel(commandData, responseData, sessionModel);
		
		ApduListFrame view = new ApduListFrame(controller);
		view.setVisible(true);


	}
	
}
