package ch.compass.gonzoproxy;

import ch.compass.gonzoproxy.mvc.controller.RelayController;
import ch.compass.gonzoproxy.mvc.model.PackageModel;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.view.GonzoProxyFrame;

public class GonzoProxy {

	public static void main(String[] args) {
		
		//TODO inizialize models and view in controller and just start controller?
		
		PackageModel apduData = new PackageModel();
		CurrentSessionModel sessionModel = new CurrentSessionModel();
		
		RelayController controller = new RelayController();
		controller.addModel(apduData, sessionModel);		
//		ApduListFrame view = new ApduListFrame(controller);
//		view.setVisible(true);
		GonzoProxyFrame frame = new GonzoProxyFrame(controller);
		frame.setVisible(true);


	}
	
}
