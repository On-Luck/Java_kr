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
import Com.JRDialog;
import Com.DBManager;
import Com.JDialogResult;
import Com.JRDialog;
import Com.data.*;
import net.miginfocom.swing.MigLayout;

public class EdsSmeta extends JRDialog
{
	private DBManager manager;
	private BigDecimal old_key; // прежнее значение ключа
	// Два варианта заголовка окна
	private final static String title_add = "Добавление записи";
	private final static String title_ed = "Редактирование записи";
	private Smeta smeta = null;
	// Флаг режима «добавление новой строки»
	private boolean isNewRow = false;

	private JTextField edKolvo;
	private JTextField edCena;
	private JComboBox cmbMat;

	// кнопки
	private JButton btnOk;
	private JButton btnCancel;

	public EdsSmeta(Window parent, Smeta smeta, DBManager manager, BigDecimal id_zak)
	{
		this.manager = manager;
		// Установка флага для режима добавления новой строки
		isNewRow = smeta == null ? true : false;
		// Определение заголовка для операций добавл./редакт.
		setTitle(isNewRow ? title_add : title_ed);
		// Определение объекта редактируемой строки
		if (!isNewRow)
		{
			this.smeta = smeta; // Существующий объект
			// Сохранение прежнего значения ключа
			old_key = smeta.getId_smeti();
		}
		else
			this.smeta = new Smeta(); // Новый объект
		// Создание графического интерфейса окна
		this.smeta.setId_zakaza(id_zak);
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
					if (manager.addSmeta(smeta))
					{
						// при успехе возврат Ok и закрытие окна
						setDialogResult(JDialogResult.OK);
						close();
					}
				}
				else
				// вызов метода менеджера Изменить продукт
				if (manager.updateSmeta(smeta, old_key))
				{
					setDialogResult(JDialogResult.OK);
					close();
				}
			}
		});
		// Событие выбора элемента списка
		cmbMat.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				// установка значения поля внешнего ключа
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					if (e.getItem() != null)
					{
						Sprav_mat sprm = (Sprav_mat) e.getItem();
						smeta.setId_mat(sprm.getId_sprav_mat());
						edCena.setText(sprm.getOtpusk_cena().toString());
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
		edKolvo = new JTextField(50);
		edCena = new JTextField(50);
		cmbMat = new JComboBox();
		// Создание кнопок
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
		// Добавление элементов на панель
		pnl.add(new JLabel("Материал"));
		pnl.add(cmbMat, "growx, wrap");

		pnl.add(new JLabel("Количество"));
		pnl.add(edKolvo, "span");
		pnl.add(new JLabel("Цена"));
		pnl.add(edCena, "span");
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
			edKolvo.setText(smeta.getKolvo().toString());
			edCena.setText(smeta.getSpr().getOtpusk_cena() == null ? "" : smeta.getSpr().getOtpusk_cena().toString());
		}

		ArrayList<Sprav_mat> lst = manager.loadSpravMat();

		if (lst != null)
		{
			// Создание модели данных на базе списка
			DefaultComboBoxModel model = new DefaultComboBoxModel(lst.toArray());
			// Установка модели для JComboBox
			cmbMat.setModel(model);
			// Определение поля внешнего ключа
			BigDecimal sprKod = (isNewRow ? null : smeta.getSpr().getId_sprav_mat());
			// Вызов метода установки элемента списка
			// соответствующего значению внешнего ключа
			setCmbItem(model, sprKod);
		}
	}

	private void setCmbItem(DefaultComboBoxModel model, BigDecimal sprKod)
	{
		cmbMat.setSelectedItem(null);
		if (sprKod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Sprav_mat) model.getElementAt(i)).getId_sprav_mat().equals(sprKod))
				{
					cmbMat.setSelectedIndex(i);
					break;
				}
	}

	// Формирование объекта Продукция перед сохранением
	private boolean constructProduct()
	{
		try
		{
			smeta.setKolvo(new BigDecimal(edKolvo.getText()));
			smeta.setCena(new BigDecimal(edCena.getText()));
			Object obj = cmbMat.getSelectedItem();
			Sprav_mat spr = (Sprav_mat) obj;
			smeta.setSpr(spr);
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}

	}

	// Возврат объекта Продукт
	public Smeta getSmeta()
	{
		return smeta;
	}
}