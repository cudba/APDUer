package controller;

import ui.view.MainFrame;

public class Apduer {

	
	public static void main(String[] args) {
		
		MainFrame view = new MainFrame();
		view.setVisible(false);

		RelayController controller = new RelayController();
		controller.startRelaySession();
		
		
	}
	
}
