package app;

import mvc.controller.RelayController;
import mvc.model.ApduData;
import mvc.view.MainFrame;

public class Apduer {

	//TODO: move into another package
	
	public static void main(String[] args) {
		
		MainFrame view = new MainFrame(new ApduData(), new ApduData());
		view.setVisible(true);

//		ApduData commandData = new ApduData();
//		ApduData responseData = new ApduData();
//		RelayController controller = new RelayController();
//		controller.addModel(commandData, responseData);
//		controller.startRelaySession();
		
		
	}
	
}
