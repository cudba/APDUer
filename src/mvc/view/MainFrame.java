package mvc.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mvc.listener.ApduListener;
import mvc.model.Apdu;
import mvc.model.ApduData;
import javax.swing.JTable;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	
	private ApduData data;
	private JTable table;
	private JPanel panel_options;
	private JButton btnTrap;
	private JButton btnSend;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					MainFrame frame = new MainFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		initUi();
	}

	private void initUi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 81, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel_table = new JPanel();
		GridBagConstraints gbc_panel_table = new GridBagConstraints();
		gbc_panel_table.insets = new Insets(0, 0, 5, 0);
		gbc_panel_table.fill = GridBagConstraints.BOTH;
		gbc_panel_table.gridx = 0;
		gbc_panel_table.gridy = 0;
		contentPane.add(panel_table, gbc_panel_table);
		
		table = new JTable();
		panel_table.add(table);
		
		panel_options = new JPanel();
		GridBagConstraints gbc_panel_options = new GridBagConstraints();
		gbc_panel_options.fill = GridBagConstraints.BOTH;
		gbc_panel_options.gridx = 0;
		gbc_panel_options.gridy = 1;
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
	
	
	// model with listener example
	public void addApd(ApduData data) {
		this.data = data;
		data.addApduListener(new ApduListener() {
			
			@Override
			public void responseReceived(Apdu responseApdu) {
				// updateResponseColumn(responseApdu)
			}
			
			@Override
			public void commandReceived(Apdu commandApdu) {
				// updateCommandColumn(commandApdu)
			}
		});
	}

}
