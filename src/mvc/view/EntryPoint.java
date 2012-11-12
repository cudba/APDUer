package mvc.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

public class EntryPoint extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldPortListen;
	private JTextField textFieldForwardIP;
	private JTextField textFieldForwardPort;

	public EntryPoint() {
		setResizable(false);
		initGui();
	}

	private void initGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 294, 176);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{110, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblSelectInputMethod = new JLabel("Select input method: ");
		lblSelectInputMethod.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblSelectInputMethod = new GridBagConstraints();
		gbc_lblSelectInputMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectInputMethod.anchor = GridBagConstraints.WEST;
		gbc_lblSelectInputMethod.gridx = 0;
		gbc_lblSelectInputMethod.gridy = 0;
		contentPane.add(lblSelectInputMethod, gbc_lblSelectInputMethod);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"nfc-relay-picc", "pureAPDU"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		contentPane.add(comboBox, gbc_comboBox);
		
		JLabel lblListen = new JLabel("Listen on port:");
		lblListen.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblListen = new GridBagConstraints();
		gbc_lblListen.anchor = GridBagConstraints.WEST;
		gbc_lblListen.insets = new Insets(0, 0, 5, 5);
		gbc_lblListen.gridx = 0;
		gbc_lblListen.gridy = 1;
		contentPane.add(lblListen, gbc_lblListen);
		
		textFieldPortListen = new JTextField();
		textFieldPortListen.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldPortListen.setText("1234");
		GridBagConstraints gbc_textFieldPortListen = new GridBagConstraints();
		gbc_textFieldPortListen.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldPortListen.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPortListen.gridx = 1;
		gbc_textFieldPortListen.gridy = 1;
		contentPane.add(textFieldPortListen, gbc_textFieldPortListen);
		textFieldPortListen.setColumns(10);
		
		JLabel lblForwardHost = new JLabel("Forward to IP: ");
		GridBagConstraints gbc_lblForwardHost = new GridBagConstraints();
		gbc_lblForwardHost.anchor = GridBagConstraints.WEST;
		gbc_lblForwardHost.insets = new Insets(0, 0, 5, 5);
		gbc_lblForwardHost.gridx = 0;
		gbc_lblForwardHost.gridy = 2;
		contentPane.add(lblForwardHost, gbc_lblForwardHost);
		
		textFieldForwardIP = new JTextField();
		textFieldForwardIP.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldForwardIP.setText("127.0.0.1");
		GridBagConstraints gbc_textFieldForwardIP = new GridBagConstraints();
		gbc_textFieldForwardIP.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldForwardIP.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldForwardIP.gridx = 1;
		gbc_textFieldForwardIP.gridy = 2;
		contentPane.add(textFieldForwardIP, gbc_textFieldForwardIP);
		textFieldForwardIP.setColumns(10);
		
		JLabel lblForwardToPort = new JLabel("Forward to port: ");
		GridBagConstraints gbc_lblForwardToPort = new GridBagConstraints();
		gbc_lblForwardToPort.anchor = GridBagConstraints.WEST;
		gbc_lblForwardToPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblForwardToPort.gridx = 0;
		gbc_lblForwardToPort.gridy = 3;
		contentPane.add(lblForwardToPort, gbc_lblForwardToPort);
		
		textFieldForwardPort = new JTextField();
		textFieldForwardPort.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldForwardPort.setText("1234");
		GridBagConstraints gbc_textFieldForwardPort = new GridBagConstraints();
		gbc_textFieldForwardPort.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldForwardPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldForwardPort.gridx = 1;
		gbc_textFieldForwardPort.gridy = 3;
		contentPane.add(textFieldForwardPort, gbc_textFieldForwardPort);
		textFieldForwardPort.setColumns(10);
		
		JButton btnStart = new JButton("Start");
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.gridx = 1;
		gbc_btnStart.gridy = 4;
		contentPane.add(btnStart, gbc_btnStart);
	}

}
