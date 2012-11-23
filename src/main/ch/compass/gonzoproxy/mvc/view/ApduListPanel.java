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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import ch.compass.gonzoproxy.mvc.controller.RelayController;
import ch.compass.gonzoproxy.mvc.listener.SessionListener;
import ch.compass.gonzoproxy.mvc.model.PacketModel;
import ch.compass.gonzoproxy.mvc.model.ApduTableModel;
import ch.compass.gonzoproxy.mvc.model.CurrentSessionModel;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

public class ApduListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2478811834671649441L;
	private JPanel panel_options;
	private JToggleButton btnTrapCmd;
	private JToggleButton btnTrapRes;

	private PacketModel apduData;
	private CurrentSessionModel currentSession;
	private RelayController controller;
	private JPanel panel_table;
	private JScrollPane scrollPane_0;
	private JTable table_apduList;
	private JLabel lblListenport;
	private JLabel lblLPort;
	private JLabel lblRemotehost;
	private JLabel lblRHost;
	private JLabel lblRemoteport;
	private JLabel lblRPort;
	private ListSelectionListener lsl;
	private JButton btnSendRes;
	private JButton btnClear;
	private JButton btnSendCmd;

	public ApduListPanel(RelayController controller,
			ListSelectionListener listSelectionListener) {
		this.controller = controller;
		apduData = controller.getApduData();
		currentSession = controller.getSessionModel();
		currentSession.addSessionListener(createSessionListener());
		this.lsl = listSelectionListener;
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
		setMinimumSize(new Dimension(750, 150));

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 597, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0 };
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

		table_apduList = new JTable();
		table_apduList.setModel(new ApduTableModel(apduData, new String[] { "#",
				"Type", "APDU", "ASCII", "Description" }));

		table_apduList.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						int index = ApduListPanel.this.table_apduList.getSelectedRow();
						if (index != -1)
							lsl.valueChanged(new ListSelectionEvent(
									this, ApduListPanel.this.table_apduList
											.convertRowIndexToModel(index),
									ApduListPanel.this.table_apduList
											.convertRowIndexToModel(index),
									true));
						else
							lsl.valueChanged(new ListSelectionEvent(this,
											-1, -1, true));

					}
				});

		configureTable(table_apduList);
		scrollPane_0.setViewportView(table_apduList);

		panel_options = new JPanel();
		GridBagConstraints gbc_panel_options = new GridBagConstraints();
		gbc_panel_options.fill = GridBagConstraints.BOTH;
		gbc_panel_options.gridx = 0;
		gbc_panel_options.gridy = 1;
		add(panel_options, gbc_panel_options);
		GridBagLayout gbl_panel_options = new GridBagLayout();
		gbl_panel_options.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0 };
		gbl_panel_options.rowHeights = new int[] { 0, 0 };
		gbl_panel_options.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel_options.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_options.setLayout(gbl_panel_options);

		btnTrapCmd = new JToggleButton("");
		btnTrapCmd.setToolTipText("Trap command");
		btnTrapCmd.setIcon(new ImageIcon(ApduListPanel.class.getResource("/ch/compass/gonzoproxy/mvc/view/icons/right.png")));
		btnTrapCmd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.changeCommandTrap();
			}
		});
		GridBagConstraints gbc_btnTrapCmd = new GridBagConstraints();
		gbc_btnTrapCmd.insets = new Insets(0, 0, 0, 5);
		gbc_btnTrapCmd.gridx = 0;
		gbc_btnTrapCmd.gridy = 0;
		panel_options.add(btnTrapCmd, gbc_btnTrapCmd);

		btnTrapRes = new JToggleButton("");
		btnTrapRes.setToolTipText("Trap response");
		btnTrapRes.setIcon(new ImageIcon(ApduListPanel.class.getResource("/ch/compass/gonzoproxy/mvc/view/icons/left.png")));
		btnTrapRes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.changeResponseTrap();
			}
		});
		
		btnSendCmd = new JButton("");
		btnSendCmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.sendOneCmd();
			}
		});
		btnSendCmd.setIcon(new ImageIcon(ApduListPanel.class.getResource("/ch/compass/gonzoproxy/mvc/view/icons/refresh.png")));
		btnSendCmd.setToolTipText("Send trapped command");
		GridBagConstraints gbc_btnSendCmd = new GridBagConstraints();
		gbc_btnSendCmd.insets = new Insets(0, 0, 0, 5);
		gbc_btnSendCmd.gridx = 1;
		gbc_btnSendCmd.gridy = 0;
		panel_options.add(btnSendCmd, gbc_btnSendCmd);
		GridBagConstraints gbc_btnTrapRes = new GridBagConstraints();
		gbc_btnTrapRes.insets = new Insets(0, 0, 0, 5);
		gbc_btnTrapRes.gridx = 2;
		gbc_btnTrapRes.gridy = 0;
		panel_options.add(btnTrapRes, gbc_btnTrapRes);
		
		btnSendRes = new JButton("");
		btnSendRes.setToolTipText("Send trapped response");
		btnSendRes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.sendOneRes();
			}
		});
		btnSendRes.setIcon(new ImageIcon(ApduListPanel.class.getResource("/ch/compass/gonzoproxy/mvc/view/icons/refresh.png")));
		GridBagConstraints gbc_btnSendRes = new GridBagConstraints();
		gbc_btnSendRes.insets = new Insets(0, 0, 0, 5);
		gbc_btnSendRes.gridx = 3;
		gbc_btnSendRes.gridy = 0;
		panel_options.add(btnSendRes, gbc_btnSendRes);
		
		btnClear = new JButton("");
		btnClear.setToolTipText("Cancel session");
		btnClear.setIcon(new ImageIcon(ApduListPanel.class.getResource("/ch/compass/gonzoproxy/mvc/view/icons/cross.png")));
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.clearSession();
			}
		});
		
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 0, 5);
		gbc_btnClear.gridx = 4;
		gbc_btnClear.gridy = 0;
		panel_options.add(btnClear, gbc_btnClear);

		lblListenport = new JLabel("Listenport: ");
		lblListenport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblListenport = new GridBagConstraints();
		gbc_lblListenport.anchor = GridBagConstraints.EAST;
		gbc_lblListenport.insets = new Insets(0, 0, 0, 5);
		gbc_lblListenport.gridx = 6;
		gbc_lblListenport.gridy = 0;
		panel_options.add(lblListenport, gbc_lblListenport);

		lblLPort = new JLabel("port");
		GridBagConstraints gbc_lblLPort = new GridBagConstraints();
		gbc_lblLPort.anchor = GridBagConstraints.WEST;
		gbc_lblLPort.insets = new Insets(0, 0, 0, 5);
		gbc_lblLPort.gridx = 7;
		gbc_lblLPort.gridy = 0;
		panel_options.add(lblLPort, gbc_lblLPort);

		lblRemotehost = new JLabel("Remotehost: ");
		lblRemotehost.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblRemotehost = new GridBagConstraints();
		gbc_lblRemotehost.anchor = GridBagConstraints.EAST;
		gbc_lblRemotehost.insets = new Insets(0, 0, 0, 5);
		gbc_lblRemotehost.gridx = 8;
		gbc_lblRemotehost.gridy = 0;
		panel_options.add(lblRemotehost, gbc_lblRemotehost);

		lblRHost = new JLabel("host");
		GridBagConstraints gbc_lblRHost = new GridBagConstraints();
		gbc_lblRHost.anchor = GridBagConstraints.WEST;
		gbc_lblRHost.insets = new Insets(0, 0, 0, 5);
		gbc_lblRHost.gridx = 9;
		gbc_lblRHost.gridy = 0;
		panel_options.add(lblRHost, gbc_lblRHost);

		lblRemoteport = new JLabel("Remoteport: ");
		lblRemoteport.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblRemoteport = new GridBagConstraints();
		gbc_lblRemoteport.anchor = GridBagConstraints.EAST;
		gbc_lblRemoteport.insets = new Insets(0, 0, 0, 5);
		gbc_lblRemoteport.gridx = 10;
		gbc_lblRemoteport.gridy = 0;
		panel_options.add(lblRemoteport, gbc_lblRemoteport);

		lblRPort = new JLabel("remPort");
		GridBagConstraints gbc_lblRPort = new GridBagConstraints();
		gbc_lblRPort.anchor = GridBagConstraints.WEST;
		gbc_lblRPort.gridx = 11;
		gbc_lblRPort.gridy = 0;
		panel_options.add(lblRPort, gbc_lblRPort);
	}

	private void configureTable(JTable table) {
		table.setSelectionMode(0);
		// table.getSelectionModel().addListSelectionListener(this.selectListController);
		table.getTableHeader().setReorderingAllowed(false);
		Enumeration<TableColumn> a = table.getColumnModel().getColumns();
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

}
