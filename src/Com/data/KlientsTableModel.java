package Com.data;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class KlientsTableModel extends AbstractTableModel
{
	private ArrayList<Klienti> klienti;

	public KlientsTableModel(ArrayList<Klienti> klienti)
	{
		this.klienti = klienti;
	}

	@Override
	public int getRowCount()
	{
		return (klienti == null ? 0 : klienti.size());

	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	// определяем содержимое ячеек таблиц
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Klienti kl = klienti.get(rowIndex);

		switch (columnIndex)
		{
		case 0:
			return rowIndex + 1;
		case 1:
			return kl.getKlient();
		case 2:
			return kl.getPhone();
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
			return "Клиент";
		case 2:
			return "Телефон";
		default:
			return null;
		}
	}

	// для определения отрисовщика ячеек колонок
	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}

	// Событие добавления строки
	public void addRow(Klienti kl)
	{
		// Определяем положение добавляемой строки
		int len = klienti.size();
		// Добавление в новой строки в список модели
		klienti.add(kl);
		// Обновление отображения строки с новыми данными
		fireTableRowsInserted(len, len);
	}

	// Событие редактирования
	public void updateRow(int index)
	{
		// Обновление отображения измененной строки
		fireTableRowsUpdated(index, index);
	}

	// Событие удаления
	public void deleteRow(int index)
	{
		// Если удаленная строка не конце таблицы
		if (index != klienti.size() - 1)
			fireTableRowsUpdated(index + 1, klienti.size() - 1);
		// Удаление строки из списка модели
		klienti.remove(index);
		// Обновление отображения после удаления
		fireTableRowsDeleted(index, index);
	}

}
