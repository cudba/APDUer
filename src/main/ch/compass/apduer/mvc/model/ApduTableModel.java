package ch.compass.apduer.mvc.model;

import javax.swing.table.AbstractTableModel;

import ch.compass.apduer.mvc.listener.ApduListener;


public class ApduTableModel extends AbstractTableModel {

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

		if (columnIndex == 0) {
			return apdu.toString();
		} else {
			return apdu.getDescription();
		}
	}

}
