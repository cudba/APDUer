package ch.compass.gonzoproxy.mvc.model;

import javax.swing.table.AbstractTableModel;

import ch.compass.gonzoproxy.mvc.listener.ApduListener;

public class ApduTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437358812481945385L;
	private ApduData data;
	String[] columnNames;;

	public ApduTableModel(ApduData data, String[] columnNames) {
		this.data = data;
		this.columnNames = columnNames;
		this.data.addApduListener(createApduListener());
	}

	private ApduListener createApduListener() {
		return new ApduListener() {

			@Override
			public void apduReceived(Apdu apdu) {
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
		return data.getApduList().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Apdu apdu = data.getApduList().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return rowIndex;
		case 1:
			return apdu.getType().getId();
		case 2:
			return new String(apdu.getPlainApdu());
		case 3:
			return apdu.toAscii();
		case 4:
			return apdu.getDescription();
		}
		return null;

	}

}
