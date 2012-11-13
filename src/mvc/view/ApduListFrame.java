package mvc.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;

import mvc.controller.RelayController;
import mvc.listener.ApduListener;
import mvc.model.Apdu;
import mvc.model.ApduData;
import mvc.model.ApduTableModel;

public class ApduListFrame extends JFrame {

	private JPanel contentPane;
	private JPanel panel_options;
	private JButton btnTrap;
	private JButton btnSend;

	private ApduData rApdu;
	private ApduData cApdu;
	private RelayController controller;
	private JPanel panel_table;
	private JScrollPane scrollPane_0;
	private JScrollPane scrollPane_1;
	private JTable table_capdu;
	private JTable table_rapdu;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnTools;
	private JMenu mnHelp;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmExit;
	private JMenuItem mntmModifier;
	private JMenuItem mntmLoadTemplate;
	private JMenuItem mntmAbout;
	
	public ApduListFrame(ApduData responseApdu, ApduData commandApdu, RelayController controller) {
		this.controller = controller;
		rApdu = responseApdu;
		cApdu = commandApdu;
		rApdu.addApduListener(createApduListener());
		cApdu.addApduListener(createApduListener());
		initUi();
		
	}

	private ApduListener createApduListener() {
		return new ApduListener() {
			
			@Override
			public void apduReceived(Apdu apdu) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	private void initUi() {
		setTitle("APDUer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
	    setMinimumSize(new Dimension(750, 250));
	    
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);
		
		mntmNew = new JMenuItem("New");
		mntmNew.setMnemonic(KeyEvent.VK_N);
		mntmNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewSession ns = new NewSession(controller);
				ns.setVisible(true);
			}
		});
		mnFile.add(mntmNew);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		mntmModifier = new JMenuItem("Modifier");
		mnTools.add(mntmModifier);
		
		mntmLoadTemplate = new JMenuItem("Load template");
		mnTools.add(mntmLoadTemplate);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 26, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		panel_table = new JPanel();
		GridBagConstraints gbc_panel_table = new GridBagConstraints();
		gbc_panel_table.insets = new Insets(0, 0, 5, 0);
		gbc_panel_table.fill = GridBagConstraints.BOTH;
		gbc_panel_table.gridx = 0;
		gbc_panel_table.gridy = 1;
		contentPane.add(panel_table, gbc_panel_table);
		GridBagLayout gbl_panel_table = new GridBagLayout();
		gbl_panel_table.columnWidths = new int[]{0, 0, 0};
		gbl_panel_table.rowHeights = new int[]{0, 0};
		gbl_panel_table.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_table.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_table.setLayout(gbl_panel_table);
		
		scrollPane_0 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_0 = new GridBagConstraints();
		gbc_scrollPane_0.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_0.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_0.gridx = 0;
		gbc_scrollPane_0.gridy = 0;
		panel_table.add(scrollPane_0, gbc_scrollPane_0);
		
		table_capdu = new JTable();
		table_capdu.setModel(new ApduTableModel(cApdu, new String[]{"C-APDU","Description"}));
		configureTable(table_capdu);
		scrollPane_0.setViewportView(table_capdu);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 0;
		panel_table.add(scrollPane_1, gbc_scrollPane_1);
		
		table_rapdu = new JTable();
		table_rapdu.setModel(new ApduTableModel(rApdu, new String[]{"R-APDU","Description"}));
		configureTable(table_rapdu);
		scrollPane_1.setViewportView(table_rapdu);
		
		panel_options = new JPanel();
		GridBagConstraints gbc_panel_options = new GridBagConstraints();
		gbc_panel_options.fill = GridBagConstraints.BOTH;
		gbc_panel_options.gridx = 0;
		gbc_panel_options.gridy = 2;
		contentPane.add(panel_options, gbc_panel_options);
		GridBagLayout gbl_panel_options = new GridBagLayout();
		gbl_panel_options.columnWidths = new int[]{0, 0, 0};
		gbl_panel_options.rowHeights = new int[]{0, 0};
		gbl_panel_options.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_options.rowWeights = new double[]{0.0, Double.MIN_VALUE};
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
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 0;
		panel_options.add(btnSend, gbc_btnSend);
	}
	
	
	private void configureTable(JTable table) {
	    table.setSelectionMode(0);
//	    table.getSelectionModel().addListSelectionListener(this.selectListController);
	    table.getTableHeader().setReorderingAllowed(false);
	    Enumeration a = table.getColumnModel().getColumns();
	    for (int i = 0; a.hasMoreElements(); i++) {
	      TableColumn tb = (TableColumn)a.nextElement();
	      switch (i) {
	      case 0:
	        tb.setMinWidth(75);
	        tb.setPreferredWidth(90);
	        break;
	      case 1:
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
