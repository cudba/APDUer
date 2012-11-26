package ch.compass.gonzoproxy;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import ch.compass.gonzoproxy.mvc.controller.RelayController;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import ch.compass.gonzoproxy.mvc.model.PacketModel;
import ch.compass.gonzoproxy.mvc.view.GonzoProxyFrame;
import ch.compass.gonzoproxy.relay.io.ApduExtractor;
import ch.compass.gonzoproxy.relay.io.ApduWrapper;

public class GonzoProxy {

	public static void main(String[] args) throws MalformedURLException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		// TODO inizialize models and view in controller and just start
		// controller?
		

		PacketModel apduData = new PacketModel();
		CurrentSessionModel sessionModel = new CurrentSessionModel();

		RelayController controller = new RelayController();
		controller.addModel(apduData, sessionModel);
		// ApduListFrame view = new ApduListFrame(controller);
		// view.setVisible(true);
		GonzoProxyFrame frame = new GonzoProxyFrame(controller);
		frame.setVisible(true);

	}

}
