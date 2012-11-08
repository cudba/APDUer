package app;

import mvc.controller.RelayController;
import mvc.view.MainFrame;

public class Apduer {

	//TODO: move into another package
	
	public static void main(String[] args) {
		
		MainFrame view = new MainFrame();
		
		//cause output on console
		view.setVisible(false);

		RelayController controller = new RelayController();
		controller.startRelaySession();
		
		
	}
	
}
