package frm;

import java.awt.Window;
import java.awt.event.*;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

import Com.DBManager;
import Com.JDialogResult;
import Com.JRDialog;
import Com.rpt.RptParam;
import net.miginfocom.swing.MigLayout;

public class RptParamDialog extends JRDialog
{
	// Поля
	private JDateChooser startDate;
	private JDateChooser endDate;
	private JButton btnOk, btnCancel;

	// менеджер организации взаимодействия с БД
	private DBManager manager;
	private RptParam params = null;

	// Конструктор
	public RptParamDialog(Window parent, DBManager manager)
	{
		setTitle("Параметры отчета");
		this.manager = manager;
		createGUI();
		loadData();
		bindListeners();
		// пакуем окно
		pack();
		// запрет изменения размеров
		setResizable(false);
	}

	// Метод создания графического интерфейса
	private void createGUI()
	{
		MigLayout layout = new MigLayout("insets 7, gapy 5, wrap 2", "[]10[]", "[]7[]5[]10[]");
		JPanel pnl = new JPanel(layout);
		startDate = new JDateChooser();
		endDate = new JDateChooser();

		pnl.add(new JLabel("Дата начала:"));
		pnl.add(startDate, "w 120::,grow,  sg 1");
		pnl.add(new JLabel("Дата окончания:"));
		pnl.add(endDate, "grow, sg 1");

		btnOk = new JButton("Принять");
		btnCancel = new JButton("Отмена");
		pnl.add(btnOk, "span, split 2, right, sg");
		pnl.add(btnCancel, "sg");
		getContentPane().add(pnl);
	}

	// Загрузка данных
	private void loadData()
	{
		startDate.setDate(new Date());
		endDate.setDate(new Date());
	}

	private void bindListeners()
	{
		btnOk.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (buildParams())
				{
					setDialogResult(JDialogResult.OK);
					close();
				}
			}
		});

		btnCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		});
	}

	public RptParam getParams()
	{
		return params;
	}

	/**
	 * Формирование параметров отчета
	 * 
	 * @return True, если параметры удачно сформированы
	 */
	protected boolean buildParams()
	{
		if (!validateData())
			return false;

		RptParam params = new RptParam();
		params.StartDate = startDate.getDate();
		params.EndDate = endDate.getDate();
		this.params = params;
		return true;
	}

	/**
	 * Проверка правильности ввода данных
	 * 
	 * @return True, если данные введены правильно
	 */
	private boolean validateData()
	{
		try
		{
			Date start = startDate.getDate();
			Date end = endDate.getDate();
			if (start == null)
				throw new Exception("Неверно указана дата начала.");
			if (end == null)
				throw new Exception("Неверно указана дата окончания.");
			if (start.after(end))
				throw new Exception("Временной диапазон задан неверно.");
			return true;
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка ввода данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}
