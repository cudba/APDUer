package ch.compass.gonzoproxy.mvc.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

import ch.compass.gonzoproxy.mvc.controller.RelayController;
import ch.compass.gonzoproxy.mvc.listener.SessionListener;
import ch.compass.gonzoproxy.mvc.model.ApduData;
import ch.compass.gonzoproxy.mvc.model.ApduTableModel;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;

public class ApduListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2478811834671649441L;
	private JPanel panel_options;
	private JButton btnTrap;
	private JButton btnSend;

	private ApduData apduData;
	private CurrentSessionModel currentSession;
	private RelayController controller;
	private JPanel panel_table;
	private JScrollPane scrollPane_0;
	private JTable table_capdu;
	private JLabel lblListenport;
	private JLabel lblLPort;
	private JLabel lblRemotehost;
	private JLabel lblRHost;
	private JLabel lblRemoteport;
	private JLabel lblRPort;

	public ApduListPanel(RelayController controller) {
		this.controller = controller;
		apduData = controller.getApduData();
		currentSession = controller.getSessionModel();
		currentSession.addSessionListener(createSessionListener());
		initUi();
		updateSessionPrefs();
	}

	private SessionListener createSessionListener() {
		return new SessionListener() {

			@Override
			public void sessionChanged() {
				updateSessionPrefs();
			}
		};
	}

	protected void updateSessionPrefs() {
		lblLPort.setText(Integer.toString(currentSession.getListenPort()));
		lblRPort.setText(Integer.toString(currentSession.getRemotePort()));
		lblRHost.setText(currentSession.getRemoteHost());

	}

	private void initUi() {
		setMinimumSize(new Dimension(750, 100));
//		setMaximumSize(new Dimension(750, 250));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		setLayout(gridBagLayout);

		panel_table = new JPanel();
		GridBagConstraints gbc_panel_table = new GridBagConstraints();
		gbc_panel_table.insets = new Insets(0, 0, 5, 0);
		gbc_panel_table.fill = GridBagConstraints.BOTH;
		gbc_panel_table.gridx = 0;
		gbc_panel_table.gridy = 0;
		add(panel_table, gbc_panel_table);
		
		GridBagLayout gbl_panel_table = new GridBagLayout();
		gbl_panel_table.columnWidths = new int[] { 0, 0 };
		gbl_panel_table.rowHeights = new int[] { 0, 0 };
		gbl_panel_table.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_table.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_table.setLayout(gbl_panel_table);

		scrollPane_0 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_0 = new GridBagConstraints();
		gbc_scrollPane_0.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_0.gridx = 0;
		gbc_scrollPane_0.gridy = 0;
		panel_table.add(scrollPane_0, gbc_scrollPane_0);

		table_capdu = new JTable();
		table_capdu.setModel(new ApduTableModel(apduData, new String[] { "#",
				"Type", "APDU", "ASCII", "Description" }));
		configureTable(table_capdu);
		scrollPane_0.setViewportView(table_capdu);

		panel_options = new JPanel();
		GridBagConstraints gbc_panel_options = new GridBagConstraints();
		gbc_panel_options.fill = GridBagConstraints.BOTH;
		gbc_panel_options.gridx = 0;
		gbc_panel_options.gridy = 1;
		add(panel_options, gbc_panel_options);
		GridBagLayout gbl_panel_options = new GridBagLayout();
		gbl_panel_options.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0 };
		gbl_panel_options.rowHeights = new int[] { 0, 0 };
		gbl_panel_options.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_options.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_options.setLayout(gbl_panel_options);

		btnTrap = new JButton("Trap");
		GridBagConstraints gbc_btnTrap = new GridBagConstraints();
		gbc_btnTrap.insets = new Insets(0, 0, 0, 5);
		gbc_btnTrap.gridx = 0;
		gbc_btnTrap.gridy = 0;
		panel_options.add(btnTrap, gbc_btnTrap);

		btnSend = new JButton("Send trapped APDU");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 0;
		panel_options.add(btnSend, gbc_btnSend);

		lblListenport = new JLabel("Listenport: ");
		lblListenport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblListenport = new GridBagConstraints();
		gbc_lblListenport.anchor = GridBagConstraints.EAST;
		gbc_lblListenport.insets = new Insets(0, 0, 0, 5);
		gbc_lblListenport.gridx = 3;
		gbc_lblListenport.gridy = 0;
		panel_options.add(lblListenport, gbc_lblListenport);

		lblLPort = new JLabel("port");
		GridBagConstraints gbc_lblLPort = new GridBagConstraints();
		gbc_lblLPort.anchor = GridBagConstraints.WEST;
		gbc_lblLPort.insets = new Insets(0, 0, 0, 5);
		gbc_lblLPort.gridx = 4;
		gbc_lblLPort.gridy = 0;
		panel_options.add(lblLPort, gbc_lblLPort);

		lblRemotehost = new JLabel("Remotehost: ");
		lblRemotehost.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblRemotehost = new GridBagConstraints();
		gbc_lblRemotehost.anchor = GridBagConstraints.EAST;
		gbc_lblRemotehost.insets = new Insets(0, 0, 0, 5);
		gbc_lblRemotehost.gridx = 5;
		gbc_lblRemotehost.gridy = 0;
		panel_options.add(lblRemotehost, gbc_lblRemotehost);

		lblRHost = new JLabel("host");
		GridBagConstraints gbc_lblRHost = new GridBagConstraints();
		gbc_lblRHost.anchor = GridBagConstraints.WEST;
		gbc_lblRHost.insets = new Insets(0, 0, 0, 5);
		gbc_lblRHost.gridx = 6;
		gbc_lblRHost.gridy = 0;
		panel_options.add(lblRHost, gbc_lblRHost);

		lblRemoteport = new JLabel("Remoteport: ");
		lblRemoteport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblRemoteport = new GridBagConstraints();
		gbc_lblRemoteport.anchor = GridBagConstraints.EAST;
		gbc_lblRemoteport.insets = new Insets(0, 0, 0, 5);
		gbc_lblRemoteport.gridx = 7;
		gbc_lblRemoteport.gridy = 0;
		panel_options.add(lblRemoteport, gbc_lblRemoteport);

		lblRPort = new JLabel("remPort");
		GridBagConstraints gbc_lblRPort = new GridBagConstraints();
		gbc_lblRPort.anchor = GridBagConstraints.WEST;
		gbc_lblRPort.gridx = 8;
		gbc_lblRPort.gridy = 0;
		panel_options.add(lblRPort, gbc_lblRPort);
	}

	private void configureTable(JTable table) {
		table.setSelectionMode(0);
		// table.getSelectionModel().addListSelectionListener(this.selectListController);
		table.getTableHeader().setReorderingAllowed(false);
		Enumeration a = table.getColumnModel().getColumns();
		for (int i = 0; a.hasMoreElements(); i++) {
			TableColumn tb = (TableColumn) a.nextElement();
			switch (i) {
			case 0:
				tb.setMinWidth(20);
				tb.setMaxWidth(20);
				break;
			case 1:
				tb.setMinWidth(40);
				tb.setMaxWidth(40);
				break;
			case 2:
				tb.setMinWidth(35);
				tb.setPreferredWidth(250);
				break;
			case 3:
				tb.setMinWidth(35);
				tb.setPreferredWidth(250);
				break;
			case 4:
				tb.setMinWidth(35);
				tb.setPreferredWidth(250);
				break;
			}

		}
	}

	// model with listener example
	public void addApd(ApduData data) {
	}

}
