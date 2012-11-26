package ch.compass.gonzoproxy.mvc.view;

import java.awt.GridLayout;

import javax.swing.JDialog;

public class ModifierDialog extends JDialog {

	private static final long serialVersionUID = 9047578530331858262L;

	public ModifierDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
	}

}
