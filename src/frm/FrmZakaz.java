package frm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableRowSorter;

import Com.DBManager;
import Com.JDialogResult;
import Com.data.*;
import net.miginfocom.swing.MigLayout;

public class FrmZakaz extends JDialog
{
	// Поля класса
	private DBManager manager;
	private JTable tblProds;
	private JButton btnClose;
	private ZakazTableModel tblModel;
	// кнопка редактирования продукта
	private JButton btnEdit;
	// кнопка добавления нового продукта
	private JButton btnNew;
	// кнопка удаления продукта
	private JButton btnDelete;

	private TableRowSorter<ZakazTableModel> tblSorter;
	private ArrayList<Zakaz> zakaz;

	// Конструктор класса
	// параметр – менеджер соединения
	public FrmZakaz(DBManager manager)
	{
		super();
		this.manager = manager;
		// Установка модального режима вывода окна
		setModal(true);
		// при закрытии окна освобождаем используемые им ресурсы
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Получение данных
		loadData();
		// Построение графического интерфейса окна
		createGUI();
		// Добавление обработчиков для основных событий
		bindListeners();
		pack();
		// Заголовок окна
		setTitle("Список заказов");
		setLocationRelativeTo(this);
	}

	// Получение данных
	private void loadData()
	{
		// Получение данных через менеджер
		zakaz = manager.loadZakaz();
	}

	private JToolBar getToolBar()
	{
		// Создание панели
		JToolBar res = new JToolBar();
		// Неизменяемое положение панели
		res.setFloatable(false);
		// Добавление кнопки «Добавить»
		// Определение местоположения изображения для кнопки
		URL url = FrmSpravMat.class.getResource("/images/add.png");
		// Создание кнопки с изображением
		btnNew = new JButton(new ImageIcon(url));
		// На кнопку не устанавливается фокус
		btnNew.setFocusable(false);
		// Добавление всплывающей подсказки для кнопки
		btnNew.setToolTipText("Добавить новый заказ");
		// Добавление кнопки «Удалить»
		url = FrmSpravMat.class.getResource("/images/delete.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить заказ");
		// Добавление кнопки «Редактировать»
		url = FrmSpravMat.class.getResource("/images/edit.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные заказа");
		// Добавление кнопок на панель
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);
		// Возврат панели в качестве результата
		return res;
	}

	// Метод создания пользовательского интерфейса
	private void createGUI()
	{
		// Создание панели
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[]10[][grow, fill][]"));
		// Создание объекта таблицы
		tblProds = new JTable();
		// Создание объекта табличной модели на базе
		// сформированного списка
		tblProds.setModel(tblModel = new ZakazTableModel(zakaz, manager));
		// Создание объекта сортировки для табличной модели
		RowSorter<ZakazTableModel> sorter = new TableRowSorter<ZakazTableModel>(tblModel);
		// Назначение объекта сортировки таблице
		tblProds.setRowSorter(sorter);
		// Задаем параметры внешнего вида таблицы
		// Выделение полосой всей текущей строки
		tblProds.setRowSelectionAllowed(true);
		// Задаем промежутки между ячейками
		tblProds.setIntercellSpacing(new Dimension(0, 1));
		// Задаем цвет сетки
		tblProds.setGridColor(new Color(170, 170, 255).darker());
		// Автоматическое определение ширины последней колонки
		tblProds.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		// Возможность выделения только 1 строки
		tblProds.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Создание области прокрутки и вставка в нее таблицы
		JScrollPane scrlPane = new JScrollPane(tblProds);
		scrlPane.getViewport().setBackground(Color.WHITE);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3, 0, 3, 0), scrlPane.getBorder()));
		// Создание кнопки для закрытия формы
		btnClose = new JButton("Закрыть");
		
		pnl.add(getToolBar(), "growx,wrap");// Создание панели
		pnl.add(new JLabel("Справочник заказов:"), "growx,span");
		// Добавление на панель: метки, области с таблицей и кнопки
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		// Добавление панели в окно
		getContentPane().setLayout(new MigLayout("insets 0 2 0 2, gapy 0", "[grow, fill]", "[grow, fill]"));
		getContentPane().add(pnl, "grow");
	}

	// Метод назначения обработчиков
	private void bindListeners()
	{
		// Для кнопки Закрыть
		btnClose.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Закрываем окно
				dispose();
			}
		});

		// Для кнопки добавления
		btnNew.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addZakaz();
			}
		});
		// Для кнопки редактирования
		btnEdit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				editZakaz();
			}
		});
		// Для кнопки удаления
		btnDelete.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				deleteZakaz();
			}
		});

	}

	// Редактирование текущего продукта
	private void editZakaz()
	{
		int index = tblProds.getSelectedRow();
		if (index == -1)
			return;
		// Преобразование индекса таблицы в индекс модели
		int modelRow = tblProds.convertRowIndexToModel(index);
		// Получаем объект из модели по индексу
		Zakaz zk = zakaz.get(modelRow);
		BigDecimal key = zk.getId_zakaza();
		// Создание объекта окна редактирования
		EdsZakaz dlg = new EdsZakaz(this, zk, manager);
		// Вызов окна и проверка кода возврата
		if (dlg.showDialog() == JDialogResult.OK)
		{
			tblModel.updateRow(modelRow);
			manager.refreshZakazRow(zk);
			loadData();
			System.out.println("Update prod OK");
		}
	}

	// Создание нового продукта
	private void addZakaz()
	{
		// Создание объекта окна редактирования
		EdsZakaz dlg = new EdsZakaz(this, null, manager);
		// Вызов окна и проверка кода возврата
		if (dlg.showDialog() == JDialogResult.OK)
		{
			// Создание нового объекта по введенным данным
			Zakaz zk = dlg.getZakaz();
			if (zk!=null && zk.getId_zakaza()!=null) {
			// Вызов метода добавления строки в табличную модель
				manager.refreshZakazRow(zk);
				tblModel.addRow(zk);
				// System.out.println("Insert book OK");
			}
			// Добавление его в табличную модель
			tblModel.addRow(zk);
		}
	}

	// Удаление текущего продукта
	private void deleteZakaz()
	{
		// Определяем индекс текущей строки.
		int index = tblProds.getSelectedRow();
		// Если нет выделенной строки, то выход
		if (index == -1)
			return;
		// Вывод запроса на удаления. При отказе - выход
		if (JOptionPane.showConfirmDialog(this, "Удалить заказ?", "Подтверждение", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
			return;
		// Преобразование индекса представления в индекс модели
		int modelRow = tblProds.convertRowIndexToModel(index);
		// Создание объекта для выделенной строки
		Zakaz zk = zakaz.get(modelRow);
		try
		{
			// Определение кода (первичного ключа) выделенной строки
			BigDecimal kod = zk.getId_zakaza();
			// Вызов метода менеджера для удаления строки
			if (manager.deleteZakaz(kod))
			{
				// Вызов метода удаления строки из табличной модели
				tblModel.deleteRow(modelRow);
				System.out.println("Delete prod OK");
			}
			else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
}
