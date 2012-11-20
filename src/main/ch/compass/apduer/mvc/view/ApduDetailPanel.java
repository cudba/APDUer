package ch.compass.apduer.mvc.view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class ApduDetailPanel extends JPanel {
	private JTable table_detail;

	public ApduDetailPanel() {
		setBorder(new TitledBorder(null, "APDU Detail", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);
		
		table_detail = new JTable();
		scrollPane.setViewportView(table_detail);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblHex = new JLabel("Hex: ");
		GridBagConstraints gbc_lblHex = new GridBagConstraints();
		gbc_lblHex.anchor = GridBagConstraints.NORTH;
		gbc_lblHex.insets = new Insets(0, 0, 5, 5);
		gbc_lblHex.gridx = 0;
		gbc_lblHex.gridy = 0;
		panel.add(lblHex, gbc_lblHex);
		
		JScrollPane scrollPane_hex = new JScrollPane();
		GridBagConstraints gbc_scrollPane_hex = new GridBagConstraints();
		gbc_scrollPane_hex.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_hex.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_hex.gridx = 1;
		gbc_scrollPane_hex.gridy = 0;
		panel.add(scrollPane_hex, gbc_scrollPane_hex);
		
		JTextPane textPane_hex = new JTextPane();
		scrollPane_hex.setViewportView(textPane_hex);
		
		JLabel lblAscii = new JLabel("Ascii: ");
		GridBagConstraints gbc_lblAscii = new GridBagConstraints();
		gbc_lblAscii.anchor = GridBagConstraints.NORTH;
		gbc_lblAscii.insets = new Insets(0, 0, 0, 5);
		gbc_lblAscii.gridx = 0;
		gbc_lblAscii.gridy = 1;
		panel.add(lblAscii, gbc_lblAscii);
		
		JScrollPane scrollPane_ascii = new JScrollPane();
		GridBagConstraints gbc_scrollPane_ascii = new GridBagConstraints();
		gbc_scrollPane_ascii.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_ascii.gridx = 1;
		gbc_scrollPane_ascii.gridy = 1;
		panel.add(scrollPane_ascii, gbc_scrollPane_ascii);
		
		JTextPane textPane_ascii = new JTextPane();
		scrollPane_ascii.setViewportView(textPane_ascii);
		


	}

}
