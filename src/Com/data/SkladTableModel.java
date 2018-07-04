package Com.data;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class SkladTableModel extends AbstractTableModel
{
	private ArrayList<Sklad> skl;

	public SkladTableModel(ArrayList<Sklad> skl)
	{
		this.skl = skl;
	}

	@Override
	public int getRowCount()
	{
		return (skl == null ? 0 : skl.size());
	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Sklad sk = skl.get(rowIndex);

		switch (columnIndex)
		{
			case 0:
				return rowIndex + 1;
			case 1:
				return sk.getSpr().getName();
			case 2:
				return sk.getKolvo();
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
				return "Материал";
			case 2:
				return "Количество";
			default:
				return null;
		}
	}
	public Class getColumnClass(int c)
	{
		if (c == 2)
			return java.lang.Number.class;
		else
			return getValueAt(0, c).getClass();
	}
	
	public void addRow(Sklad sk)
	{
		int len = skl.size();
		skl.add(sk);
		fireTableRowsInserted(len, len);
	}

	public void updateRow(int index)
	{
		fireTableRowsUpdated(index, index);
	}

	public void deleteRow(int index)
	{
		if (index != skl.size() - 1)
			fireTableRowsUpdated(index + 1, skl.size() - 1);
		skl.remove(index);
		fireTableRowsDeleted(index, index);
	}
}
