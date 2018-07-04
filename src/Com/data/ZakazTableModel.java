package Com.data;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

import Com.DBManager;
import Com.helper.HelperConverter;

public class ZakazTableModel extends AbstractTableModel
{
	private ArrayList<Zakaz> zakaz;
	private DBManager manager;
	
	// Конструктор объекта класса
	public ZakazTableModel(ArrayList<Zakaz> zakaz, DBManager manager)
	{
		this.zakaz = zakaz;
		this.manager = manager;
	}

	// Количество колонок в таблице
	@Override
	public int getColumnCount()
	{
		return 7;
	}

	// Количество строк в таблице = размеру списка
	@Override
	public int getRowCount()
	{
		return (zakaz == null ? 0 : zakaz.size());
	}

	// Определение содержимого ячеек
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		// Выделяем объект из списка по текущему индексу
		Zakaz zk = zakaz.get(rowIndex);
		// Каждой колонке сопоставляем поле объекта
		switch (columnIndex)
		{
		case 0:
			return rowIndex + 1;
		case 1:
			return zk.getNomer();
		case 2:
			return HelperConverter.getDateString(zk.getData_zakaza());
		case 3:
			return HelperConverter.getDateString(zk.getData_nachala_rabot());
		case 4:
			return zk.getOb_stoim();
		case 5:
			return zk.getPlosh_pom();
		case 6:
			return zk.getKlient().getKlient();
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
			return "Номер";
		case 2:
			return "Дата заказа";
		case 3:
			return "Дата начала работ";
		case 4:
			return "Общая стоимость";
		case 5:
			return "Площадь помещения";
		case 6:
			return "Клиент";
		default:
			return null;
		}
	}

	// Этот метод используется для определения отрисовщика
	// ячеек колонок в зависимости от типа данных
	public Class getColumnClass(int c)
	{
		if (c == 4) // защита от null в колонке 4
			return java.lang.Number.class;
		//else if (c == 2) // защита от null в колонке 2
			//return Date.class; //не работает на Win10
		//else if (c == 3)// защита от null в колонке 2
			//return Date.class; //не работает на Win10
		if (c == 6) // защита от null в колонке 6
			return java.lang.String.class;
		else
			return getValueAt(0, c).getClass();
			
	}
	// Событие добавления строки
		public void addRow(Zakaz zk)
		{
			// Определяем положение добавляемой строки
			int len = zakaz.size();
			manager.refreshZakazRow(zk);
			// Добавление в новой строки в список модели
			zakaz.add(zk);
			// Обновление отображения строки с новыми данными
			fireTableRowsInserted(len, len);
		}

		// Событие редактирования
		public void updateRow(int index)
		{
			Zakaz zk = zakaz.get(index);
			manager.refreshZakazRow(zk);
			fireTableRowsUpdated(index, index);
		}

		// Событие удаления
		public void deleteRow(int index)
		{
			// Если удаленная строка не конце таблицы
			if (index != zakaz.size() - 1)
				fireTableRowsUpdated(index + 1, zakaz.size() - 1);
			// Удаление строки из списка модели
			zakaz.remove(index);
			// Обновление отображения после удаления
			fireTableRowsDeleted(index, index);
		}
}
