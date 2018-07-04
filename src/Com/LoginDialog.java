package Com;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.Locale;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class LoginDialog extends JRDialog
{
	private JTextField txtLogin;
	// Поля класса
	private JPasswordField txtPassword;
	private JButton btnEnter;
	private JButton btnCancel;
	private Connection conn;
	private DBManager manager;
	private String url_db;

	// Конструктор класса
	public LoginDialog(DBManager manager)
	{
		this.manager = manager;
		createGUI();
		setTitle("Соединение с БД");
		// Чтение файла свойств проекта
		FileInputStream fprop;
		Properties property = new Properties();
		try
		{
			// Файл свойств находится в корневой папке проекта
			fprop = new FileInputStream("src/config.prop");
			property.load(fprop);
			// Чтение строки соединения из файла свойств
			url_db = property.getProperty("URL_DB");
		}
		catch (IOException e)
		{
			System.err.println("ОШИБКА: Файл свойств отсуствует!");
		}
	}

	// Формирование графического интерфейса окна класса
	private void createGUI()
	{
		MigLayout layout = new MigLayout("insets 12, gapy 5", "[]12[grow, fill][]", "");
		JPanel res = new JPanel(layout);
		// поле с логином
		res.add(new JLabel("Логин:"));
		txtLogin = new JTextField(15);
		res.add(txtLogin, "span");
		// поле с паролем
		res.add(new JLabel("Пароль:"));
		txtPassword = new JPasswordField(15);
		res.add(txtPassword, "span");
		// кнопки
		btnEnter = new JButton("Войти");
		btnCancel = new JButton("Отмена");
		res.add(btnEnter, "gaptop 8,span,split 2,right");
		res.add(btnCancel);
		getContentPane().add(res);
		// установка кнопки по умолчанию для окна
		getRootPane().setDefaultButton(btnEnter);
		// пакуем окно
		pack();
		// запрет изменения размеров
		setResizable(false);
		// привязка обработчиков событий - нажатия кнопок
		bindListeners();
	}

	// Добавление слушателей для компонентов окна
	private void bindListeners()
	{
		// Для кнопки отмена – закрытие окна
		btnCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		});
		// Для кнопки Войти – получение соединения
		btnEnter.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (authenticate())
				{
					setDialogResult(JDialogResult.OK);
					close();
				}
			}
		});
	}

	// Метод аутентификации
	protected boolean authenticate()
	{
		String login = null;
		char[] password = null;
		try
		{
			login = txtLogin.getText().trim();
			password = txtPassword.getPassword();
			if (login.length() == 0 || password.length == 0)
				throw new Exception("Логин и/или пароль не указаны.");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка ввода данных", JOptionPane.ERROR_MESSAGE);
			txtPassword.setText(null);
			return false;
		}
		// пробуем авторизоваться
		try
		{
			String pwd = new String(password);
			conn = connect(login, pwd);
			manager.setConnection(conn);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка входа в систему", JOptionPane.ERROR_MESSAGE);
			txtPassword.setText(null);
			return false;
		}
		return true;
	}

	// Метод соединения с БД
	private Connection connect(String log, String pass)
	{
		Locale.setDefault(Locale.ENGLISH);
		try
		{
			try
			{
				Class.forName("org.postgresql.Driver");
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			conn = DriverManager.getConnection(url_db, log, pass);
			conn.setAutoCommit(false);
			return conn;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
