package frm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Com.DBManager;
import Com.JDialogResult;
import Com.JRDialog;
import Com.data.Sprav_mat;
import Com.data.VidiMat;
import net.miginfocom.swing.MigLayout;

public class EdsSpravMatDialog extends JRDialog
{
	private DBManager manager;
	private BigDecimal old_key; // прежнее значение ключа
	// Два варианта заголовка окна
	private final static String title_add = "Добавление нового материала";
	private final static String title_ed = "Редактирование материала";
	private Sprav_mat sprmat = null;
	// Флаг режима «добавление новой строки»
	private boolean isNewRow = false;
	// Форматер для полей дат
	SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
	// Элементы (поля редактирования) для полей записи
	private JTextField edName;
	private JTextField edZakup_cena;
	private JTextField edOtpusk_cena;
	private JTextField edRashod;
	private JComboBox cmbVid;

	// кнопки
	private JButton btnOk;
	private JButton btnCancel;

	// Конструктор класса
	public EdsSpravMatDialog(Window parent, Sprav_mat sprmat, DBManager manager)
	{
		this.manager = manager;
		// Установка флага для режима добавления новой строки
		isNewRow = sprmat == null ? true : false;
		// Определение заголовка для операций добавл./редакт.
		setTitle(isNewRow ? title_add : title_ed);
		// Определение объекта редактируемой строки
		if (!isNewRow)
		{
			this.sprmat = sprmat; // Существующий объект
			// Сохранение прежнего значения ключа
			old_key = sprmat.getId_sprav_mat();
		}
		else
			this.sprmat = new Sprav_mat(); // Новый объект
		// Создание графического интерфейса окна
		createGui();
		// Назначение обработчиков для основных событий
		bindListeners();
		// Получение данных
		loadData();
		pack();
		// Задание режима неизменяемых размеров окна
		setResizable(false);
		setLocationRelativeTo(parent);
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
				// Возврат Cancel и закрытие окна
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		});
		// Обработка кнопки «Сохранить»
		btnOk.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Проверка данных, выход
				// при неправильном заполнении полей
				if (!constructProduct())
					return;
				if (isNewRow)
				{
					// вызов метода менеджера Добавить новый продукт
					if (manager.addSpravMat(sprmat))
					{
						// при успехе возврат Ok и закрытие окна
						setDialogResult(JDialogResult.OK);
						close();
					}
				}
				else
				// вызов метода менеджера Изменить продукт
				if (manager.updateSpravMat(sprmat, old_key))
				{
					setDialogResult(JDialogResult.OK);
					close();
				}
			}
		});
		// Событие выбора элемента списка
		cmbVid.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				// установка значения поля внешнего ключа
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					if (e.getItem() != null)
					{
						VidiMat vm = (VidiMat) e.getItem();
						sprmat.setId_vida(vm.getId_vida());
					}
				}
			}
		});
	}

	// Метод создания графического интерфейса
	private void createGui()
	{
		// Создание панели
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]", "[]5[]10[]"));
		// Создание полей для редактирования данных
		edName = new JTextField(50);
		edZakup_cena = new JTextField(50);
		edOtpusk_cena = new JTextField(20);
		edRashod = new JTextField(20);
		cmbVid = new JComboBox();
		// Создание кнопок
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
		// Добавление элементов на панель
		pnl.add(new JLabel("Группа"));
		pnl.add(cmbVid, "growx, wrap");

		pnl.add(new JLabel("Наименоваание"));
		pnl.add(edName, "span");
		pnl.add(new JLabel("Закупочная цена"));
		pnl.add(edZakup_cena, "span");
		pnl.add(new JLabel("Отпускная цена"));
		pnl.add(edOtpusk_cena, "span");
		pnl.add(new JLabel("Расход на 1 кв. м."));
		pnl.add(edRashod, "span");
		pnl.add(btnOk, "growx 0, right, sg 1");
		pnl.add(btnCancel, "sg 1");
		// Добавление панели в окно фрейма
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnl, BorderLayout.CENTER);
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
			edName.setText(sprmat.getName());
			edZakup_cena.setText(sprmat.getZakup_cena() == null ? "" : sprmat.getZakup_cena().toString());
			edOtpusk_cena.setText(sprmat.getOtpusk_cena() == null ? "" : sprmat.getOtpusk_cena().toString());
			edRashod.setText(sprmat.getRashod() == null ? "" : sprmat.getRashod().toString());
		}

		ArrayList<VidiMat> lst = manager.loadVidForCmb();

		if (lst != null)
		{
			// Создание модели данных на базе списка
			DefaultComboBoxModel model = new DefaultComboBoxModel(lst.toArray());
			// Установка модели для JComboBox
			cmbVid.setModel(model);
			// Определение поля внешнего ключа
			BigDecimal vdKod = (isNewRow ? null : sprmat.getVidmat().getId_vida());
			// Вызов метода установки элемента списка
			// соответствующего значению внешнего ключа
			setCmbItem(model, vdKod);
		}
	}

	private void setCmbItem(DefaultComboBoxModel model, BigDecimal vdKod)
	{
		cmbVid.setSelectedItem(null);
		if (vdKod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((VidiMat) model.getElementAt(i)).getId_vida().equals(vdKod))
				{
					cmbVid.setSelectedIndex(i);
					break;
				}
	}

	// Формирование объекта Продукция перед сохранением
	private boolean constructProduct()
	{
		try
		{
			sprmat.setName(edName.getText());
			sprmat.setZakup_cena(new BigDecimal(edZakup_cena.getText()));
			sprmat.setOtpusk_cena(edOtpusk_cena.getText().equals("") ? null : new BigDecimal(edOtpusk_cena.getText()));
			sprmat.setRashod(edRashod.getText().equals("") ? null : new BigDecimal(edRashod.getText()));
			Object obj = cmbVid.getSelectedItem();
			VidiMat vm = (VidiMat) obj;
			sprmat.setVidmat(vm);
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

	// Возврат объекта Продукт
	public Sprav_mat getSprMat()
	{
		return sprmat;
	}
}
