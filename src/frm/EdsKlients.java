package frm;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Com.DBManager;
import Com.JDialogResult;
import Com.JRDialog;
import Com.data.Klienti;
import Com.data.Sprav_mat;
import net.miginfocom.swing.MigLayout;

public class EdsKlients extends JRDialog
{
	private DBManager manager;
	private BigDecimal old_key; // прежнее значение ключа
	// Два варианта заголовка окна
	private final static String title_add = "Добавление нового клиента";
	private final static String title_ed = "Редактирование данных клиента";
	private Klienti kl = null;
	// Флаг режима «добавление новой строки»
	private boolean isNewRow = false;
	// Форматер для полей дат
	SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
	// Элементы (поля редактирования) для полей записи
	private JTextField edKlient;
	private JTextField edPhone;
	// кнопки
	private JButton btnOk;
	private JButton btnCancel;

	// Конструктор класса
	public EdsKlients(Window parent, Klienti kl, DBManager manager)
	{
		this.manager = manager;
		// Установка флага для режима добавления новой строки
		isNewRow = kl == null ? true : false;
		// Определение заголовка для операций добавл./редакт.
		setTitle(isNewRow ? title_add : title_ed);
		// Определение объекта редактируемой строки
		if (!isNewRow)
		{
			this.kl = kl; // Существующий объект
			// Сохранение прежнего значения ключа
			old_key = kl.getId_klienta();
		}
		else
			this.kl = new Klienti(); // Новый объект
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
					if (manager.addKlient(kl))
					{
						// при успехе возврат Ok и закрытие окна
						setDialogResult(JDialogResult.OK);
						close();
					}
				}
				else
				// вызов метода менеджера Изменить продукт
				if (manager.updateKlient(kl, old_key))
				{
					setDialogResult(JDialogResult.OK);
					close();
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
		edKlient = new JTextField(50);
		edPhone = new JTextField(50);

		// Создание кнопок
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
		// Добавление элементов на панель
		pnl.add(new JLabel("Клиент"));
		pnl.add(edKlient, "span");
		pnl.add(new JLabel("Телефон"));
		pnl.add(edPhone, "span");
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
			edKlient.setText(kl.getKlient() == null ? "" : kl.getKlient().toString());
			edPhone.setText(kl.getPhone() == null ? "" : kl.getPhone().toString());
		}
	}

	// Формирование объекта Продукция перед сохранением
	private boolean constructProduct()
	{
		try
		{
			kl.setKlient(edKlient.getText().equals("") ? null : edKlient.getText().toString());
			kl.setPhone(edPhone.getText().equals("") ? null : edPhone.getText().toString());
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	// Возврат объекта Продукт
	public Klienti getKlient()
	{
		return kl;
	}
}
