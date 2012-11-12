package mvc.model;

import javax.swing.table.AbstractTableModel;

import mvc.listener.ApduListener;

public class CAdpuTableModel extends AbstractTableModel {

	private ApduData data;
	String[] columnNames = { "C-APDU", "Description" };

	public CAdpuTableModel(ApduData data) {
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

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.getApduHistory().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Apdu apdu = data.getApduHistory().get(rowIndex);

		if (columnIndex == 0) {
			return apdu.getOriginalApdu().toString();
		} else {
			return apdu.getDescription();
		}
	}

}
