package Com.data;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class SmetaTableModel extends AbstractTableModel
{
	private ArrayList<Smeta> smeta;

	public SmetaTableModel(ArrayList<Smeta> smeta)
	{
		this.smeta = smeta;
	}

	@Override
	public int getRowCount()
	{
		return (smeta == null ? 0 : smeta.size());
	}

	@Override
	public int getColumnCount()
	{
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Smeta sm = smeta.get(rowIndex);

		switch (columnIndex)
		{
			case 0:
				return rowIndex + 1;
			case 1:
				return sm.getSpr().getVidmat().getName();
			case 2:
				return sm.getSpr().getName();
			case 3:
				return sm.getKolvo();
			case 4:
				return sm.getCena();
			default:
				return null;
		}
	}

	@Override
	public String getColumnName(int column)
	{
		switch (column)
		{
			case 0:
				return "N";
			case 1:
				return "Вид материала";
			case 2:
				return "Название материала";
			case 3:
				return "Количество";
			case 4:
				return "Цена";
			default:
				return null;
		}
	}

	public Class getColumnClass(int c)
	{
		if (c == 3)
			return java.lang.Number.class;
		if (c == 4)
			return java.lang.Number.class;
		else
			return getValueAt(0, c).getClass();
	}

	public void addRow(Smeta sm)
	{
		int len = smeta.size();
		smeta.add(sm);
		fireTableRowsInserted(len, len);
	}

	public void updateRow(int index)
	{
		fireTableRowsUpdated(index, index);
	}

	public void deleteRow(int index)
	{
		if (index != smeta.size() - 1)
			fireTableRowsUpdated(index + 1, smeta.size() - 1);
		smeta.remove(index);
		fireTableRowsDeleted(index, index);
	}
}
