package Com;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;

import Com.rpt.RptMatData;
import Com.rpt.RptParam;
import frm.*;
import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class MainWindow extends JFrame
{
	JMenuBar mainMenu; // Контейнер меню
	JMenuItem miExit; // Пункт меню
	JMenu mSpr; // Подменю Справочники
	JMenuItem miSpravMat; // справочник материалов
	JMenuItem miKli; // Пункт меню Группы
	JMenuItem miSklad;
	JMenu mOper; // Подменю Операции
	JMenuItem miZak; // Пункт меню Продукция
	JMenuItem miRpt;

	// Поля класса
	private DBManager manager;

	// Конструктор класса
	public MainWindow(DBManager manager)
	{
		this.manager = manager;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Тест соединения
		testConn();
		// Создание графического интерфейса окна
		createGUI();
		// Назначение окну слушателей
		bindListeners();
		// Пакуем окно
		pack();
		// Заголовок окна
		setTitle("Основное окно");
		// Вывод окна по центру экрана
		setLocationRelativeTo(null);

		manager.setPath();
	}

	// Назначение элементам окна слушателей
	private void bindListeners()
	{
		// Выход из приложения
		miExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		miZak.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ShowZakaz();
			}
		});
		miKli.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ShowKlients();
			}
		});
		miSpravMat.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ShowSprMat();
			}
		});
		miSklad.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ShowSklad();
			}
		});
		miRpt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ShowRpt(); // вызов метода формирования отчета
			}
		});

	}

	protected void ShowSklad()
	{
		FrmSklad frm = new FrmSklad(manager);
		frm.setVisible(true);
	}

	protected void ShowSprMat()
	{
		FrmSpravMat frm = new FrmSpravMat(manager);
		frm.setVisible(true);
	}

	protected void ShowKlients()
	{
		FrmKlients frm = new FrmKlients(manager);
		frm.setVisible(true);
	}

	protected void ShowZakaz()
	{
		FrmZakaz frm = new FrmZakaz(manager);
		frm.setVisible(true);
	}

	protected void ShowRpt()
	{
		final RptParamDialog dlg = new RptParamDialog(this, manager);
		if (dlg.showDialog() != JDialogResult.OK)
			return;
		RptParam params = dlg.getParams();
		System.out.println("Параметры получены");
		// Объявление объекта Отчета
		JasperPrint jp = null;
		// Получение объекта базовой сущности для отчета (вызов первого варианта
		// функции)
		RptMatData reportData = manager.getDatReport(params);
		// Преобразование коллекции базовой сущности в источник данных для
		// отчета
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(reportData.data);
		// Для передачи параметров в отчет используем объект HashMap
		HashMap<String, Object> map = new HashMap<String, Object>();
		// Заполнение объекта значениями параметров
		map.put("dates", params.StartDate);
		map.put("datepo", params.EndDate);
		// Задаем расположение файла-отчета

		String reportName = "C:/Users/On_Luck-ASUST200TA/JaspersoftWorkspace/MyReports/report.jasper";
		// Заполнение отчета данными и параметрами
		try
		{
			jp = JasperFillManager.fillReport(reportName, map, ds);
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		// Вывод отчета в режиме предварительного просмотра
		JasperViewer viewer = new JasperViewer(jp, false);
		viewer.setTitle("Отчет о потребностях в материалах");
		// Вывод отчета
		viewer.setVisible(true);
	}

	// Метод тестирования соединения
	private void testConn()
	{
		// Вызов метода менеджера для получения версии PG
		String ver = manager.getVersion();
		// Вывод результата в консоль
		System.out.println(ver);
	}

	// метод создания меню
	private JMenuBar createMenu()
	{
		// Создаем строку основного меню
		mainMenu = new JMenuBar();
		// Создаем подменю и пункты меню
		mSpr = new JMenu("Справочники");
		miExit = new JMenuItem("Выход");
		miKli = new JMenuItem("Клиенты");
		miSpravMat = new JMenuItem("Саправочник материалов");
		miSklad = new JMenuItem("Склад");
		mOper = new JMenu("Операции");
		miZak = new JMenuItem("Заказы");
		miRpt = new JMenuItem("Отчет");

		// Формируем меню
		mSpr.add(miKli);
		mSpr.addSeparator();
		mSpr.add(miSpravMat);
		mSpr.addSeparator();
		mSpr.add(miSklad);

		mOper.add(miZak);
		mOper.add(miRpt);

		mainMenu.add(mSpr);
		mainMenu.add(mOper);
		mainMenu.add(miExit);

		return mainMenu;
	}

	// Создание графического интерфейса окна
	private void createGUI()
	{
		// Создаем панель
		JPanel pnlImg = new JPanel(new FlowLayout());
		// Размещаем на панели изображение
		// Изображение находится в папке src/images
		// и в папке bin\images
		URL url = this.getClass().getResource("/images/panda.jpg");
		BufferedImage wPic = null;
		try
		{
			wPic = ImageIO.read(url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		// Изображение размещаем на метке
		JLabel wIcon = new JLabel(new ImageIcon(new ImageIcon(wPic).getImage().getScaledInstance(367, 210, Image.SCALE_DEFAULT)));
		// Метку добавляем на панель
		pnlImg.add(wIcon);
		// Задаем менеджер расположения основного окна
		// Метка заполняет все пространство окна
		getContentPane().setLayout(new MigLayout("insets 0 2 0 2, gapy 0", "[grow, fill]", "[grow, fill]"));
		// Панель размещаем на фрейме
		getContentPane().add(pnlImg, "grow");
		// меню
		setJMenuBar(createMenu());
	}

	public void initialize()
	{
		Locale.setDefault(new Locale("ru"));
	}
}
