package ch.compass.gonzoproxy.mvc.model;

import javax.swing.table.AbstractTableModel;

import ch.compass.gonzoproxy.mvc.listener.PackageListener;

public class DetailTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437358812481945385L;
	private Package apdu;
	private PackageModel data;
	private String[] columnNames = { "Field", "Value", "Description" };
	  
	public DetailTableModel(Package apdu, PackageModel data) {
		this.apdu = apdu;
		this.data = data;
		this.data.addPackageListener(createApduListener());
	}

	private PackageListener createApduListener() {
		return new PackageListener() {
			
			@Override
			public void packageReceived(Package apdu) {
//				DetailTableModel.this.setApdu(apdu);
			}
			
			@Override
			public void apduCleared() {
				DetailTableModel.this.setApdu(new Package(new byte[0]));
			}
		};
	}

	public String getColumnName(int col) {
		return this.columnNames[col].toString();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		return apdu.getFields().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Field field = apdu.getFields().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return field.getName();
		case 1:
			return field.getValue();
		case 2:
			return field.getDescription();
		}
		return null;

	}

	public void setApdu(Package editApdu) {
		this.apdu = editApdu;
		fireTableDataChanged();
	}

}
