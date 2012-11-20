package ch.compass.gonzoproxy.mvc.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ch.compass.gonzoproxy.mvc.controller.RelayController;
import javax.swing.JSplitPane;
import java.awt.Insets;

public class GonzoProxyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2325146651722965630L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmNew;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;
	private JMenuItem mntmExit;
	private JMenu mnTools;
	private JMenuItem mntmModifier;
	private JMenu mnHelp;
	private JMenuItem mntmLoadTemplate;
	private JMenuItem mntmAbout;
	private JSplitPane splitPane;
	private JPanel panelList;
	private JPanel panelDetail;

	public GonzoProxyFrame(final RelayController controller) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("Gonzo Proxy");
		setMinimumSize(new Dimension(750, 450));
		
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
		gbl_contentPane.rowHeights = new int[]{134, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		contentPane.add(splitPane, gbc_splitPane);
		
		panelList = new ApduListPanel(controller);
		splitPane.setLeftComponent(panelList);
		
		panelDetail = new ApduDetailPanel();
		splitPane.setRightComponent(panelDetail);
	}

}
