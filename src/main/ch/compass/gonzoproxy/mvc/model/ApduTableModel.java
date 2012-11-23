package ch.compass.gonzoproxy.mvc.model;

import javax.swing.table.AbstractTableModel;

import ch.compass.gonzoproxy.mvc.listener.PacketListener;

public class ApduTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437358812481945385L;
	private PacketModel data;
	String[] columnNames;;

	public ApduTableModel(PacketModel data, String[] columnNames) {
		this.data = data;
		this.columnNames = columnNames;
		this.data.addPacketListener(createApduListener());
	}

	private PacketListener createApduListener() {
		return new PacketListener() {

			@Override
			public void packetReceived(Packet apdu) {
				updateTable();
			}

			@Override
			public void packetCleared() {
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
		return data.getPacketList().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Packet apdu = data.getPacketList().get(rowIndex);

		switch (columnIndex) {
		case 0:
			return rowIndex;
		case 1:
			return apdu.getType().getId();
		case 2:
			return new String(apdu.getPlainPacket());
		case 3:
			return apdu.toAscii();
		case 4:
			return apdu.getDescription();
		}
		return null;

	}

}
