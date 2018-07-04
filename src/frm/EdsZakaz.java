package frm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;
import javax.swing.AbstractButton;

import Com.DBManager;
import Com.JDialogResult;
import Com.JRDialog;
import Com.data.Klienti;
import Com.data.Smeta;
import Com.data.SmetaTableModel;
import Com.data.Sprav_mat;
import Com.data.VidiMat;
import Com.data.Zakaz;
import net.miginfocom.swing.MigLayout;

public class EdsZakaz extends JRDialog
{

	private DBManager manager;
	private BigDecimal old_key; // прежнее значение ключа
	// Два варианта заголовка окна
	private final static String title_add = "Добавление нового заказа";
	private final static String title_ed = "Редактирование заказа";
	private Zakaz zakaz = null;
	// Флаг режима «добавление новой строки»
	private boolean isNewRow = false;
	// Форматер для полей дат
	SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
	// Элементы (поля редактирования) для полей записи
	private JTextField edNomer;
	private JTextField edData_zakaza;
	private JTextField edData_nachala_rabot;
	private JTextField edOb_stoim;
	private JTextField edPlosh_pom;
	private JComboBox cmbKli;

	// кнопки
	private JButton btnOk;
	private JButton btnCancel;

	private JTable tblSmeta;
	private JToolBar tbSmeta;
	// кнопка редактирования
	private JButton btnEdit;
	// кнопка добавления новой строки
	private JButton btnNew;
	// кнопка удаления строки
	private JButton btnDelete;
	// Табличная модель
	private SmetaTableModel tblModel;
	private TableRowSorter<SmetaTableModel> tblSorter;
	private ArrayList<Smeta> smeta;

	// Конструктор класса
	public EdsZakaz(Window parent, Zakaz zakaz, DBManager manager)
	{
		this.manager = manager;
		// Установка флага для режима добавления новой строки
		isNewRow = zakaz == null ? true : false;
		// Определение заголовка для операций добавл./редакт.
		setTitle(isNewRow ? title_add : title_ed);
		// Определение объекта редактируемой строки
		if (!isNewRow)
		{
			this.zakaz = zakaz; // Существующий объект
			// Сохранение прежнего значения ключа
			old_key = zakaz.getId_zakaza();

		}
		else
			this.zakaz = new Zakaz(); // Новый объект
		// Создание графического интерфейса окна
		loadSmeta();
		createGui();
		// Назначение обработчиков для основных событий
		bindListeners();
		// Получение данных
		loadData();
		pack();
		// Задание режима неизменяемых размеров окна
		setResizable(true);
		setButton();
		setLocationRelativeTo(parent);
	}

	private void setButton()
	{
		if (btnOk.getText().equals("Сохранить"))
		{
			btnCancel.setText("Отмена");
			btnNew.setEnabled(false);
			btnEdit.setEnabled(false);
			btnDelete.setEnabled(false);
		}
		else
		{
			btnCancel.setText("Выход");
			btnNew.setEnabled(true);
			btnEdit.setEnabled(true);
			btnDelete.setEnabled(true);
		}
	}

	private void loadSmeta()
	{
		smeta = manager.loadSmeta(zakaz.getId_zakaza());
	}

	// Метод назначения обработчиков основных событий
	private void bindListeners()
	{
		// Обработчик нажатия клавиш
		setKeyListener(this, new KeyAdapter()
		{
			@Override
			// Обработка нажатия клавиши ESC – закрытие окна
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					setDialogResult(JDialogResult.Cancel);
					close();
					e.consume();
				}
				else
					super.keyPressed(e);
			}
		});

		// Обработка нажатия кнопки закрытия окна
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		});
		// Обработка кнопки «Отмена»
		btnCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (((AbstractButton) e.getSource()).getText().equals("Сохранить"))
				{
					// Проверка данных, возврат Ok и закрытие окна
					if (!constructZakaz())
						return;
					if (isNewRow)
						manager.addZakaz(zakaz);
					else
						manager.updateZakaz(zakaz, old_key);
					setDialogResult(JDialogResult.OK);
					((AbstractButton) e.getSource()).setText("Редактировать");
					setButton();
				}
				else
				{
					((AbstractButton) e.getSource()).setText("Сохранить");
					setButton();
				}
			}
		});
		// Обработка кнопки «Сохранить»
		btnOk.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (((AbstractButton) e.getSource()).getText().equals("Сохранить"))
				{
					// Проверка данных, возврат Ok и закрытие окна
					if (!constructZakaz())
						return;
					if (isNewRow)
						manager.addZakaz(zakaz);
					else
						manager.updateZakaz(zakaz, old_key);
					setDialogResult(JDialogResult.OK);
					((AbstractButton) e.getSource()).setText("Редактировать");
					setButton();
				}
				else
				{
					((AbstractButton) e.getSource()).setText("Сохранить");
					setButton();
				}
			}

		});
		// Событие выбора элемента списка
		cmbKli.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				// установка значения поля внешнего ключа
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					if (e.getItem() != null)
					{
						Klienti kl = (Klienti) e.getItem();
						zakaz.setId_klienta(kl.getId_klienta());
					}
				}
			}
		});

		btnNew.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addSmeta();
			}
		});
		// Для кнопки редактирования
		btnEdit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				editSmeta();
			}
		});
		// Для кнопки удаления
		btnDelete.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				deleteSmeta();
			}
		});
	}

	// Метод создания графического интерфейса
	private void createGui()
	{
		// Создание панели
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]", "[]5[]10[]"));
		// Создание полей для редактирования данных
		edNomer = new JTextField(50);
		edData_zakaza = new JTextField(20);
		edData_nachala_rabot = new JTextField(20);
		edOb_stoim = new JTextField(20);
		edPlosh_pom = new JTextField(20);
		cmbKli = new JComboBox();
		// Создание кнопок
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
		// Добавление элементов на панель
		pnl.add(new JLabel("Клиент"));
		pnl.add(cmbKli, "growx, wrap");
		pnl.add(new JLabel("Номер"));
		pnl.add(edNomer, "span");
		pnl.add(new JLabel("Дата заказа"));
		pnl.add(edData_zakaza, "span");
		pnl.add(new JLabel("Дата начала работ"));
		pnl.add(edData_nachala_rabot, "span");
		pnl.add(new JLabel("Общая стоимость"));
		pnl.add(edOb_stoim, "span");
		pnl.add(new JLabel("Площадь помещения"));
		pnl.add(edPlosh_pom, "span");
		pnl.add(btnOk, "growx 0, right, sg 1");
		pnl.add(btnCancel, "sg 1");
		// Добавление панели в окно фрейма
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnl, BorderLayout.NORTH);

		JPanel pnlSmeta = new JPanel(new MigLayout("insets 3,gapy 4", "[grow, fill]", "[]5[]10[][grow, fill][]"));
		// Создание объекта таблицы
		tblSmeta = new JTable();
		// Создание объекта табличной модели на базе
		// сформированного списка
		tblSmeta.setModel(tblModel = new SmetaTableModel(smeta));
		tblSmeta = new JTable(tblModel);
		RowSorter<SmetaTableModel> sorter = new TableRowSorter<SmetaTableModel>(tblModel);
		tblSmeta.setRowSorter(sorter);
		// Задаем параметры внешнего вида таблицы
		// Выделение полосой всей текущей строки
		tblSmeta.setRowSelectionAllowed(true);
		// Задаем промежутки между ячейками
		tblSmeta.setIntercellSpacing(new Dimension(0, 1));
		// Задаем цвет сетки
		tblSmeta.setGridColor(new Color(170, 170, 255).darker());
		// Автоматическое определение ширины последней колонки
		tblSmeta.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		// Возможность выделения только 1 строки
		tblSmeta.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Автоматическая настройка ширина колонок таблицы
		// Создание области прокрутки и вставка в нее таблицы
		JScrollPane scrlPane = new JScrollPane(tblSmeta);
		// Добавление на панель: области с таблицей и кнопки
		pnlSmeta.add(getToolBar(), "growx, wrap");
		pnlSmeta.add(scrlPane, "grow, span");
		getContentPane().add(pnlSmeta, BorderLayout.CENTER);
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
		btnNew.setToolTipText("Добавить смету");
		// Добавление кнопки «Удалить»
		url = FrmSpravMat.class.getResource("/images/delete.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить смету");
		// Добавление кнопки «Редактировать»
		url = FrmSpravMat.class.getResource("/images/edit.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные сметы");
		// Добавление кнопок на панель
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);
		// Возврат панели в качестве результата
		return res;
	}

	// Метод добавление слушателя клавиатуры
	// к компонентам окна
	private void setKeyListener(Component c, KeyListener kl)
	{
		c.addKeyListener(kl);
		if (c instanceof Container)
			for (Component comp : ((Container) c).getComponents())
				setKeyListener(comp, kl);
	}

	// Метод инициализации полей формы (при редактировании)
	private void loadData()
	{
		if (!isNewRow)
		{
			edNomer.setText(zakaz.getNomer().toString());
			edData_zakaza.setText(zakaz.getData_zakaza() == null ? "" : frmt.format(zakaz.getData_zakaza()));
			edData_nachala_rabot
					.setText(zakaz.getData_nachala_rabot() == null ? "" : frmt.format(zakaz.getData_nachala_rabot()));
			edOb_stoim.setText(zakaz.getOb_stoim() == null ? "" : zakaz.getOb_stoim().toString());
			edPlosh_pom.setText(zakaz.getPlosh_pom() == null ? "" : zakaz.getPlosh_pom().toString());
		}

		ArrayList<Klienti> lst = manager.loadKlients();

		if (lst != null)
		{
			// Создание модели данных на базе списка
			DefaultComboBoxModel model = new DefaultComboBoxModel(lst.toArray());
			// Установка модели для JComboBox
			cmbKli.setModel(model);
			// Определение поля внешнего ключа
			BigDecimal vdKod = (isNewRow ? null : zakaz.getKlient().getId_klienta());
			// Вызов метода установки элемента списка
			// соответствующего значению внешнего ключа
			setCmbItem(model, vdKod);
		}
	}

	private void setCmbItem(DefaultComboBoxModel model, BigDecimal klKod)
	{
		cmbKli.setSelectedItem(null);
		if (klKod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Klienti) model.getElementAt(i)).getId_klienta().equals(klKod))
				{
					cmbKli.setSelectedIndex(i);
					break;
				}
	}

	// Формирование объекта Продукция перед сохранением
	private boolean constructZakaz()
	{
		try
		{
			zakaz.setNomer(edNomer.getText().equals("") ? null : new BigDecimal(edNomer.getText()));
			zakaz.setData_zakaza(edData_zakaza.getText().substring(0, 1).trim().equals("") ? null
					: frmt.parse(edData_zakaza.getText()));
			zakaz.setData_nachala_rabot(edData_nachala_rabot.getText().substring(0, 1).trim().equals("") ? null
					: frmt.parse(edData_nachala_rabot.getText()));
			zakaz.setOb_stoim(edOb_stoim.getText().equals("") ? null : new BigDecimal(edOb_stoim.getText()));
			zakaz.setPlosh_pom(edPlosh_pom.getText().equals("") ? null : new BigDecimal(edPlosh_pom.getText()));
			Object obj = cmbKli.getSelectedItem();
			Klienti kl = (Klienti) obj;
			zakaz.setKlient(kl);
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

	// Возврат объекта Продукт
	public Zakaz getZakaz()
	{
		return zakaz;
	}

	private void addSmeta()
	{
		// Создание объекта окна редактирования
		EdsSmeta dlg = new EdsSmeta(this, null, manager, zakaz.getId_zakaza());
		// Вызов окна и проверка кода возврата
		if (dlg.showDialog() == JDialogResult.OK)
		{
			// Создание нового объекта по введенным данным
			Smeta sm = dlg.getSmeta();
			manager.refreshSmetaRow(sm);
			manager.refreshZakazRow(zakaz);
			manager.updateZakaz(zakaz, zakaz.getId_zakaza());
			// Вызов метода добавления строки в табличную модель
			tblModel.addRow(sm);
			System.out.println("Insert sm OK");
		}
		loadData();

	}

	// Редактирование текущей строки книги
	private void editSmeta()
	{
		int index = tblSmeta.getSelectedRow();
		if (index == -1)
			return;
		// Преобразование индекса таблицы в индекс модели
		int modelRow = tblSmeta.convertRowIndexToModel(index);
		// Получаем объект из модели по индексу
		Smeta sm = smeta.get(modelRow);
		BigDecimal key = sm.getId_smeti();
		// Создание объекта окна редактирования
		EdsSmeta dlg = new EdsSmeta(this, sm, manager, zakaz.getId_zakaza());
		// Вызов окна и проверка кода возврата
		if (dlg.showDialog() == JDialogResult.OK)
		{
			manager.refreshSmetaRow(sm);
			manager.refreshZakazRow(zakaz);
			manager.updateZakaz(zakaz, zakaz.getId_zakaza());
			// Вызов метода редактирования табличной модели
			tblModel.updateRow(modelRow);

			System.out.println("Update sm OK");
		}
		loadData();
	}

	// Удаление текущей позиции документа
	private void deleteSmeta()
	{
		// Определяем индекс текущей строки.
		int index = tblSmeta.getSelectedRow();
		// Если нет выделенной строки, то выход
		if (index == -1)
			return;
		// Вывод запроса на удаления. При отказе - выход
		if (JOptionPane.showConfirmDialog(this, "Удалить строку?", "Подтверждение", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
			return;
		// Преобразование индекса представления в индекс модели
		int modelRow = tblSmeta.convertRowIndexToModel(index);
		// Создание объекта для выделенной строки
		Smeta sm = smeta.get(modelRow);
		try
		{
			// Определение кода (первичного ключа) выделенной строки
			BigDecimal id = sm.getId_smeti();
			// Вызов метода менеджера для удаления строки
			if (manager.deleteSmeta(id))
			{
				// Вызов метода удаления строки из табличной модели
				manager.refreshZakazRow(zakaz);
				manager.updateZakaz(zakaz, zakaz.getId_zakaza());
				tblModel.deleteRow(modelRow);

				System.out.println("Delete sm OK");
			}
			else
				JOptionPane.showMessageDialog(this, "Ошибка удаления  строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
		loadData();
	}

}
