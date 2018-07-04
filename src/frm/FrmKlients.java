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

public class FrmKlients extends JDialog
{
	// Поля класса
	private DBManager manager;
	private JTable tblKlients;
	private JButton btnClose;
	private KlientsTableModel tblModel;
	private TableRowSorter<KlientsTableModel> tblSorter;
	private ArrayList<Klienti> klienti;

	private JButton btnEdit;
	// кнопка добавления нового продукта
	private JButton btnNew;
	// кнопка удаления продукта
	private JButton btnDelete;

	public FrmKlients(DBManager manager)
	{
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Справочник клиентов");
		setLocationRelativeTo(this);
	}

	private void loadData()
	{
		// Получение данных через менеджер
		klienti = manager.loadKlients();
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
		btnNew.setToolTipText("Добавить нового клиента");
		// Добавление кнопки «Удалить»
		url = FrmSpravMat.class.getResource("/images/delete.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить клиента");
		// Добавление кнопки «Редактировать»
		url = FrmSpravMat.class.getResource("/images/edit.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные клиента");
		// Добавление кнопок на панель
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);
		// Возврат панели в качестве результата
		return res;
	}

	private void createGUI()
	{
		// Создание панели
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[]10[][grow, fill][]"));
		// Создание объекта таблицы
		tblKlients = new JTable();
		// Создание объекта табличной модели на базе
		// сформированного списка
		tblKlients.setModel(tblModel = new KlientsTableModel(klienti));
		// Создание объекта сортировки для табличной модели
		RowSorter<KlientsTableModel> sorter = new TableRowSorter<KlientsTableModel>(tblModel);
		// Назначение объекта сортировки таблице
		tblKlients.setRowSorter(sorter);
		// Задаем параметры внешнего вида таблицы
		// Выделение полосой всей текущей строки
		tblKlients.setRowSelectionAllowed(true);
		// Задаем промежутки между ячейками
		tblKlients.setIntercellSpacing(new Dimension(0, 1));
		// Задаем цвет сетки
		tblKlients.setGridColor(new Color(170, 170, 255).darker());
		// Минимальные и максимальные размеры колонок 0 и 1
		tblKlients.getColumnModel().getColumn(0).setMinWidth(30);
		tblKlients.getColumnModel().getColumn(0).setMaxWidth(50);
		tblKlients.getColumnModel().getColumn(1).setMinWidth(150);
		// Автоматическое определение ширины последней колонки
		tblKlients.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		// Возможность выделения только 1 строки
		tblKlients.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Создание области прокрутки и вставка в нее таблицы
		JScrollPane scrlPane = new JScrollPane(tblKlients);
		scrlPane.getViewport().setBackground(Color.WHITE);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3, 0, 3, 0), scrlPane.getBorder()));
		// Создание кнопки для закрытия формы
		btnClose = new JButton("Закрыть");
		pnl.add(getToolBar(), "growx,wrap");// Создание панели
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
		btnNew.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				addKlient();
				loadData();
			}
		});
		// Для кнопки редактирования
		btnEdit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				editKlient();
				loadData();
			}
		});
		// Для кнопки удаления
		btnDelete.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				deleteKlient();
				loadData();
			}
		});
	}

	private void editKlient()
	{
		int index = tblKlients.getSelectedRow();
		if (index == -1)
			return;
		// Преобразование индекса таблицы в индекс модели
		int modelRow = tblKlients.convertRowIndexToModel(index);
		// Получаем объект из модели по индексу
		Klienti kl = klienti.get(modelRow);
		BigDecimal key = kl.getId_klienta();
		// Создание объекта окна редактирования
		EdsKlients dlg = new EdsKlients(this, kl, manager);
		// Вызов окна и проверка кода возврата
		if (dlg.showDialog() == JDialogResult.OK)
		{
			tblModel.updateRow(modelRow);
			System.out.println("Update prod OK");
		}
	}

	// Создание нового продукта
	private void addKlient()
	{
		// Создание объекта окна редактирования
		EdsKlients dlg = new EdsKlients(this, null, manager);
		// Вызов окна и проверка кода возврата
		if (dlg.showDialog() == JDialogResult.OK)
		{
			// Создание нового объекта по введенным данным
			Klienti kl = dlg.getKlient();
			// Добавление его в табличную модель
			tblModel.addRow(kl);
		}
	}

	// Удаление текущего продукта
	private void deleteKlient()
	{
		// Определяем индекс текущей строки.
		int index = tblKlients.getSelectedRow();
		// Если нет выделенной строки, то выход
		if (index == -1)
			return;
		// Вывод запроса на удаления. При отказе - выход
		if (JOptionPane.showConfirmDialog(this, "Удалить продукцию?", "Подтверждение", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
			return;
		// Преобразование индекса представления в индекс модели
		int modelRow = tblKlients.convertRowIndexToModel(index);
		// Создание объекта для выделенной строки
		Klienti kl = klienti.get(modelRow);
		try
		{
			// Определение кода (первичного ключа) выделенной строки
			BigDecimal kod = kl.getId_klienta();
			// Вызов метода менеджера для удаления строки
			if (manager.deleteKlient(kod))
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
