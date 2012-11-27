package ch.compass.gonzoproxy.mvc.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.compass.gonzoproxy.mvc.model.Field;
import ch.compass.gonzoproxy.mvc.model.Packet;
import javax.swing.border.EmptyBorder;

public class AddNewModifierDialog extends JDialog {
	private static final long serialVersionUID = -1789866876175936281L;
	private JPanel contentPane;
	private JTextField textFieldPacketname;
	private JTextField textFieldOldValue;
	private JTextField textFieldNewValue;
	private JTextField textFieldFieldname;

	public AddNewModifierDialog(Packet editApdu, int row) {
		initGui();
		setFields(editApdu, row);
	}

	private void setFields(Packet editApdu, int row) {
		Field field = editApdu.getFields().get(row);
		String description = "no description";
		if (field.getDescription() != null) {
			description = field.getDescription();
		}
		textFieldFieldname.setText(field.getName() + " - " + description);
		textFieldPacketname.setText(editApdu.getDescription());
		textFieldOldValue.setText(field.getValue());
	}

	private void initGui() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Add new modifier");
		setBounds(100, 100, 457, 227);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(gridBagLayout);

			JLabel lblPacketname = new JLabel("Packetname: ");
			GridBagConstraints gbc_lblPacketname = new GridBagConstraints();
			gbc_lblPacketname.anchor = GridBagConstraints.WEST;
			gbc_lblPacketname.insets = new Insets(0, 0, 5, 5);
			gbc_lblPacketname.gridx = 0;
			gbc_lblPacketname.gridy = 0;
			contentPane.add(lblPacketname, gbc_lblPacketname);
			textFieldPacketname = new JTextField();
			GridBagConstraints gbc_textFieldPacketname = new GridBagConstraints();
			gbc_textFieldPacketname.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldPacketname.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldPacketname.gridx = 1;
			gbc_textFieldPacketname.gridy = 0;
			contentPane.add(textFieldPacketname, gbc_textFieldPacketname);
			textFieldPacketname.setColumns(10);
			JLabel lblField = new JLabel("Field: ");
			GridBagConstraints gbc_lblField = new GridBagConstraints();
			gbc_lblField.anchor = GridBagConstraints.WEST;
			gbc_lblField.insets = new Insets(0, 0, 5, 5);
			gbc_lblField.gridx = 0;
			gbc_lblField.gridy = 1;
			contentPane.add(lblField, gbc_lblField);
			textFieldFieldname = new JTextField();
			GridBagConstraints gbc_textFieldFieldname = new GridBagConstraints();
			gbc_textFieldFieldname.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldFieldname.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldFieldname.gridx = 1;
			gbc_textFieldFieldname.gridy = 1;
			contentPane.add(textFieldFieldname, gbc_textFieldFieldname);
			textFieldFieldname.setColumns(10);
			JLabel lblOldValue = new JLabel("Old value: ");
			GridBagConstraints gbc_lblOldValue = new GridBagConstraints();
			gbc_lblOldValue.anchor = GridBagConstraints.WEST;
			gbc_lblOldValue.insets = new Insets(0, 0, 5, 5);
			gbc_lblOldValue.gridx = 0;
			gbc_lblOldValue.gridy = 2;
			contentPane.add(lblOldValue, gbc_lblOldValue);
			textFieldOldValue = new JTextField();
			GridBagConstraints gbc_textFieldOldValue = new GridBagConstraints();
			gbc_textFieldOldValue.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldOldValue.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldOldValue.gridx = 1;
			gbc_textFieldOldValue.gridy = 2;
			contentPane.add(textFieldOldValue, gbc_textFieldOldValue);
			textFieldOldValue.setColumns(10);
			JLabel lblNewValue = new JLabel("New value: ");
			GridBagConstraints gbc_lblNewValue = new GridBagConstraints();
			gbc_lblNewValue.anchor = GridBagConstraints.WEST;
			gbc_lblNewValue.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewValue.gridx = 0;
			gbc_lblNewValue.gridy = 3;
			contentPane.add(lblNewValue, gbc_lblNewValue);
			textFieldNewValue = new JTextField();
			GridBagConstraints gbc_textFieldNewValue = new GridBagConstraints();
			gbc_textFieldNewValue.insets = new Insets(0, 0, 5, 0);
			gbc_textFieldNewValue.fill = GridBagConstraints.HORIZONTAL;
			gbc_textFieldNewValue.gridx = 1;
			gbc_textFieldNewValue.gridy = 3;
			contentPane.add(textFieldNewValue, gbc_textFieldNewValue);
			textFieldNewValue.setColumns(10);
			JButton btnSave = new JButton("Save");
			GridBagConstraints gbc_btnSave = new GridBagConstraints();
			gbc_btnSave.insets = new Insets(0, 0, 0, 5);
			gbc_btnSave.gridx = 0;
			gbc_btnSave.gridy = 5;
			contentPane.add(btnSave, gbc_btnSave);
			JButton btnCancel = new JButton("Cancel");
			btnCancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					AddNewModifierDialog.this.dispose();
				}
			});
			GridBagConstraints gbc_btnCancel = new GridBagConstraints();
			gbc_btnCancel.anchor = GridBagConstraints.WEST;
			gbc_btnCancel.gridx = 1;
			gbc_btnCancel.gridy = 5;
			contentPane.add(btnCancel, gbc_btnCancel);
			
			textFieldFieldname.setEnabled(false);
			textFieldOldValue.setEnabled(false);
			textFieldPacketname.setEnabled(false);
	}
}