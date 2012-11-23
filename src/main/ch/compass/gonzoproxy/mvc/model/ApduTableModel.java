package ch.compass.gonzoproxy.mvc.model;

import javax.swing.table.AbstractTableModel;

import ch.compass.gonzoproxy.mvc.listener.PackageListener;

public class ApduTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437358812481945385L;
	private PackageModel data;
	String[] columnNames;;

	public ApduTableModel(PackageModel data, String[] columnNames) {
		this.data = data;
		this.columnNames = columnNames;
		this.data.addPackageListener(createApduListener());
	}

	private PackageListener createApduListener() {
		return new PackageListener() {

			@Override
			public void packageReceived(Package apdu) {
				updateTable();
			}

			@Override
			public void apduCleared() {
				updateTable();
			}

		};
	}

	private void updateTable() {
		fireTableDataChanged();
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
		return data.getPackageList().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Package apdu = data.getPackageList().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return rowIndex;
		case 1:
			return apdu.getType().getId();
		case 2:
			return new String(apdu.getPlainPackage());
		case 3:
			return apdu.toAscii();
		case 4:
			return apdu.getDescription();
		}
		return null;

	}

}
