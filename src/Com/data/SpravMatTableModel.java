package Com.data;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import Com.helper.HelperConverter;

public class SpravMatTableModel extends AbstractTableModel
{
	private ArrayList<Sprav_mat> sprmat;

	public SpravMatTableModel(ArrayList<Sprav_mat> sprmat)
	{
		this.sprmat = sprmat;
	}

	@Override
	public int getRowCount()
	{
		return (sprmat == null ? 0 : sprmat.size());
	}

	@Override
	public int getColumnCount()
	{
		return 6;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		// Выделяем объект из списка по текущему индексу
		Sprav_mat sm = sprmat.get(rowIndex);
		// Каждой колонке сопоставляем поле объекта
		switch (columnIndex)
		{
			case 0:
				return rowIndex + 1;
			case 1:
				return sm.getName();
			case 2:
				return sm.getVidmat().getName();
			case 3:
				return sm.getZakup_cena();
			case 4:
				return sm.getOtpusk_cena();
			case 5:
				return sm.getRashod();
			default:
				return null;
		}
	}

	// Определение названия колонок
	@Override
	public String getColumnName(int column)
	{
		switch (column)
		{
			case 0:
				return "N";
			case 1:
				return "Название";
			case 2:
				return "Вид";
			case 3:
				return "Закупочная цена";
			case 4:
				return "Отпускная цена";
			case 5:
				return "Расход на 1 кв. м.";
			default:
				return null;
		}
	}

	public Class getColumnClass(int c)
	{
		if (c == 3)
			return java.lang.Number.class;
		else
			return getValueAt(0, c).getClass();
	}

	// Событие добавления строки
	public void addRow(Sprav_mat sprmats)
	{
		// Определяем положение добавляемой строки
		int len = sprmat.size();
		// Добавление в новой строки в список модели
		sprmat.add(sprmats);
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
		if (index != sprmat.size() - 1)
			fireTableRowsUpdated(index + 1, sprmat.size() - 1);
		// Удаление строки из списка модели
		sprmat.remove(index);
		// Обновление отображения после удаления
		fireTableRowsDeleted(index, index);
	}
}
