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

		ClassLoader cl = GonzoProxy.class.getClassLoader();
		
		ArrayList<String> inputModes = new ArrayList<>();
		ArrayList<ApduWrapper> wrapperList = new ArrayList<>();
		ArrayList<ApduExtractor> extractorList = new ArrayList<>();

		ResourceBundle bundle = ResourceBundle.getBundle("plugin");
		
		Enumeration<String> keys = bundle.getKeys();
		while(keys.hasMoreElements()){
			String element = keys.nextElement();
			if(element.contains("name")){
				inputModes.add(bundle.getString(element));
			}else if(element.contains("wrapper")){
				wrapperList.add((ApduWrapper) cl.loadClass(bundle.getString(element)).newInstance());
			}else if (element.contains("extractor")) {
				extractorList.add((ApduExtractor) cl.loadClass(bundle.getString(element)).newInstance());
			}
		}
		
		String[] modesArr = inputModes.toArray(new String[2]);

		PacketModel apduData = new PacketModel();
		CurrentSessionModel sessionModel = new CurrentSessionModel();
		sessionModel.addExtractor(extractorList);
		sessionModel.addWrapper(wrapperList);

		RelayController controller = new RelayController(modesArr);
		controller.addModel(apduData, sessionModel);
		// ApduListFrame view = new ApduListFrame(controller);
		// view.setVisible(true);
		GonzoProxyFrame frame = new GonzoProxyFrame(controller);
		frame.setVisible(true);

	}

}
