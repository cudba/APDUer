package ch.compass.apduer.mvc.view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.border.TitledBorder;

public class ApduDetailPanel extends JPanel {

	public ApduDetailPanel() {
		setBorder(new TitledBorder(null, "APDU Detail", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton btnThisIsThe = new JButton("this is the detail view");
		GridBagConstraints gbc_btnThisIsThe = new GridBagConstraints();
		gbc_btnThisIsThe.gridx = 0;
		gbc_btnThisIsThe.gridy = 0;
		add(btnThisIsThe, gbc_btnThisIsThe);

	}

}
