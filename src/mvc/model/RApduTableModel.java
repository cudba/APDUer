package mvc.model;

import javax.swing.table.AbstractTableModel;

import mvc.listener.ApduListener;

public class RApduTableModel extends AbstractTableModel {

	private ApduData data;
	String[] columnNames = { "R-APDU", "Description" };

	public RApduTableModel(ApduData data) {
		this.data = data;
		data.addApduListener(createApduListener());
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
			return apdu.getOriginalApdu().toString();
		} else {
			return apdu.getDescription();
		}
	}

}
