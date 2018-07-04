package Com;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Com.data.*;
import Com.helper.HelperConverter;
import Com.rpt.RptMatData;
import Com.rpt.RptParam;

/*
 *  Менеджер взаимодействия с БД
*/
public class DBManager
{
	private Connection conn = null;

	public DBManager() throws SQLException
	{
	}

	public Connection getConnection()
	{
		return conn;
	}

	public void setConnection(Connection conn)
	{
		this.conn = conn;
	}

	// Метод тестирования соединения
	public String getVersion()
	{
		String ver = null;
		Statement stmt;
		ResultSet rset;
		try
		{
			stmt = conn.createStatement();
			rset = stmt.executeQuery("SELECT VERSION()");
			while (rset.next())
			{
				ver = rset.getString(1);
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ver;
	}

	public void setPath()
	{
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			stmt.executeUpdate("SET SEARCH_PATH=prod");
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	// ==== Область данных ====

	// КЛИЕНТЫ
	public ArrayList<Klienti> loadKlients()
	{
		ArrayList<Klienti> klienti = new ArrayList<Klienti>();
		
		// Получение объекта соединения
		Connection con = this.getConnection();
		try
		{
			// Формирование запроса к БД
			Statement stmt = con.createStatement();
			// Выполнение запроса и получение набора данных
			ResultSet ress = stmt.executeQuery("SELECT id_klienta, klient, phone FROM public.klienti;");
			// Создание объекта – список продуктов
			klienti = new ArrayList<Klienti>();
			// В цикле просмотра набора данных формируем список
			while (ress.next())
			{
				// Создаем объект продукции
				Klienti kl = new Klienti();
				// Заполняем поля объекта данными из строки набора
				kl.setId_klienta(ress.getBigDecimal(1));
				kl.setKlient(ress.getString(2));
				kl.setPhone(ress.getString(3));
				// Добавляем объект к списку
				klienti.add(kl);
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		// Возврат списка
		return klienti;
	}

	public boolean addKlient(Klienti kl)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "INSERT INTO public.klienti(id_klienta, klient, phone)	" + "VALUES (?, ?, ?);";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, getId("public.klienti_id_klienta_seq"));
			pst.setString(2, kl.getKlient());
			pst.setString(3, kl.getPhone());
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			// В случае ошибки – отмена транзакции
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean updateKlient(Klienti kl, BigDecimal key)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "UPDATE public.klienti SET id_klienta=?, klient=?, phone=?	" + "WHERE id_klienta = ?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, kl.getId_klienta());
			pst.setString(2, kl.getKlient());
			pst.setString(3, kl.getPhone());
			pst.setBigDecimal(4, key);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
		}
		catch (SQLException ex)
		{
			// В случае ошибки – отмена транзакции
			RollBack();
			// и вывод сообщения об ошибке
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean deleteKlient(BigDecimal kod)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "DELETE FROM public.klienti" + " WHERE id_klienta=?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, kod);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	// ВИДЫ МАТЕРИАЛОВ
	public ArrayList<VidiMat> loadVidForCmb()
	{
		ArrayList<VidiMat> vidmat = new ArrayList<VidiMat>();
		Connection con = this.getConnection();
		try
		{
			Statement stmt = con.createStatement();
			// Выполнение запроса и получение набора данных
			ResultSet ress = stmt.executeQuery("SELECT * FROM public.vidi_materialov");
			// Создание объекта – список продуктов
			vidmat = new ArrayList<VidiMat>();
			// В цикле просмотра набора данных формируем список
			while (ress.next())
			{
				// Создаем объект продукции
				VidiMat vm = new VidiMat();
				// Заполняем поля объекта данными из строки набо
				vm.setId_vida(ress.getBigDecimal(1));
				vm.setName(ress.getString(2));
				// Добавляем объект к списку
				vidmat.add(vm);
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		// Возврат списка
		return vidmat;
	}

	// СПРАВОЧНИК МАТЕРИАЛОВ
	public ArrayList<Sprav_mat> loadSpravMat()
	{
		ArrayList<Sprav_mat> sprmat = new ArrayList<Sprav_mat>();
		// Получение объекта соединения
		Connection con = this.getConnection();
		try
		{
			// Формирование запроса к БД
			Statement stmt = con.createStatement();
			// Выполнение запроса и получение набора данных
			ResultSet ress = stmt
					.executeQuery("SELECT id_materiala, name, zakup_cena, otpusk_cena, rashod_na_1kvm, id_vida, v_name "
							+ "FROM public.spravmat_v");
			// Создание объекта – список продуктов
			sprmat = new ArrayList<Sprav_mat>();
			// В цикле просмотра набора данных формируем список
			while (ress.next())
			{
				// Создаем объект продукции
				Sprav_mat sm = new Sprav_mat();
				VidiMat vm = new VidiMat();
				// Заполняем поля объекта данными из строки набора
				sm.setId_sprav_mat(ress.getBigDecimal(1));
				sm.setName(ress.getString(2));
				sm.setZakup_cena(ress.getBigDecimal(3));
				sm.setOtpusk_cena(ress.getBigDecimal(4));
				sm.setRashod(ress.getBigDecimal(5));
				vm.setId_vida(ress.getBigDecimal(6));
				vm.setName(ress.getString(7));
				sm.setVidmat(vm);
				// Добавляем объект к списку
				sprmat.add(sm);
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		// Возврат списка
		return sprmat;
	}

	public boolean addSpravMat(Sprav_mat sprmat)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "INSERT INTO public.sprav_mat(id_materiala, name, zakup_cena, otpusk_cena, rashod_na_1kvm, id_vida) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, getId("public.sprav_mat_id_materiala_seq_1"));
			pst.setString(2, sprmat.getName());
			pst.setBigDecimal(3, sprmat.getZakup_cena());
			pst.setBigDecimal(4, sprmat.getOtpusk_cena());
			pst.setBigDecimal(5, sprmat.getRashod());
			pst.setBigDecimal(6, sprmat.getVidmat().getId_vida());
			// Выполнение оператора
			pst.executeUpdate();

			// Завершение транзакции – сохранение изменений
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			// В случае ошибки – отмена транзакции
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean updateSpravMat(Sprav_mat sprmat, BigDecimal key)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "UPDATE public.sprav_mat set " + "id_materiala=?," + "name=?," + "zakup_cena=?," + "otpusk_cena=?,"
				+ "rashod_na_1kvm=?," + "id_vida=?" + " WHERE id_materiala=?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, sprmat.getId_sprav_mat());
			pst.setString(2, sprmat.getName());
			pst.setBigDecimal(3, sprmat.getZakup_cena());
			pst.setBigDecimal(4, sprmat.getOtpusk_cena());
			pst.setBigDecimal(5, sprmat.getRashod());
			pst.setBigDecimal(6, sprmat.getVidmat().getId_vida());
			pst.setBigDecimal(7, key);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
		}
		catch (SQLException ex)
		{
			// В случае ошибки – отмена транзакции
			RollBack();
			// и вывод сообщения об ошибке
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean deleteSpravMat(BigDecimal kod)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "DELETE FROM public.sprav_mat" + " WHERE id_materiala=?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, kod);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	// ЗАКАЗЫ
	public ArrayList<Zakaz> loadZakaz()

	{
		ArrayList<Zakaz> zakazi = new ArrayList<Zakaz>();
		// Получение объекта соединения
		Connection con = this.getConnection();
		try
		{
			// Формирование запроса к БД
			Statement stmt = con.createStatement();
			// Выполнение запроса и получение набора данных
			ResultSet res = stmt.executeQuery("SELECT id_zakaza, nomer, data_zakaza, data_nachala_rabot, ob_stoim,"
					+ " plosh_pom, public.klienti.id_klienta, klient, phone "
					+ "FROM public.zakazi JOIN public.klienti "
					+ "ON public.zakazi.id_klienta = public.klienti.id_klienta " + "ORDER BY nomer;");
			// Создание объекта – список продуктов
			zakazi = new ArrayList<Zakaz>();
			// В цикле просмотра набора данных формируем список
			while (res.next())
			{
				// Создаем объект продукции
				Zakaz zk = new Zakaz();
				Klienti kl = new Klienti();
				// Заполняем поля объекта данными из строки набора
				zk.setId_zakaza(res.getBigDecimal(1));
				zk.setNomer(res.getBigDecimal(2));
				zk.setData_zakaza(res.getDate(3));
				zk.setData_nachala_rabot(res.getDate(4));
				zk.setOb_stoim(res.getBigDecimal(5));
				zk.setPlosh_pom(res.getBigDecimal(6));
				zk.setId_klienta(res.getBigDecimal(7));
				kl.setKlient(res.getString(8));
				kl.setPhone(res.getString(9));
				zk.setKlient(kl);
				// Добавляем объект к списку
				zakazi.add(zk);
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		// Возврат списка
		return zakazi;
	}

	public boolean addZakaz(Zakaz zk)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "INSERT INTO public.zakazi(id_zakaza, nomer, data_zakaza, data_nachala_rabot, "
				+ "ob_stoim, plosh_pom, id_klienta)	" + "VALUES (?, ?, ?, ?, ?, ?, ?);";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, getId("public.zakazi_id_zakaza_seq"));
			pst.setBigDecimal(2, zk.getNomer());
			pst.setDate(3, HelperConverter.convertFromJavaDateToSQLDate(zk.getData_zakaza()));
			pst.setDate(4, HelperConverter.convertFromJavaDateToSQLDate(zk.getData_nachala_rabot()));
			pst.setBigDecimal(5, zk.getOb_stoim());
			pst.setBigDecimal(6, zk.getPlosh_pom());
			pst.setBigDecimal(7, zk.getKlient().getId_klienta());
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			// В случае ошибки – отмена транзакции
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean updateZakaz(Zakaz zk, BigDecimal key)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "UPDATE public.zakazi SET id_zakaza=?, nomer=?, data_zakaza=?, data_nachala_rabot=?, "
				+ "ob_stoim=?, plosh_pom=?, id_klienta=?" + "WHERE id_zakaza = ?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, zk.getId_zakaza());
			pst.setBigDecimal(2, zk.getNomer());
			pst.setDate(3, HelperConverter.convertFromJavaDateToSQLDate(zk.getData_zakaza()));
			pst.setDate(4, HelperConverter.convertFromJavaDateToSQLDate(zk.getData_nachala_rabot()));
			pst.setBigDecimal(5, zk.getOb_stoim());
			pst.setBigDecimal(6, zk.getPlosh_pom());
			pst.setBigDecimal(7, zk.getKlient().getId_klienta());
			// Выполнение оператора
			pst.setBigDecimal(8, key);
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
		}
		catch (SQLException ex)
		{
			// В случае ошибки – отмена транзакции
			RollBack();
			// и вывод сообщения об ошибке
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean deleteZakaz(BigDecimal kod)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "DELETE FROM public.zakazi" + " WHERE id_zakaza=?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, kod);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	public void refreshZakazRow(Zakaz zk)
	{
		if (zk.getId_zakaza() == null)
			return;
		Connection conn = this.getConnection();
		try
		{
			// Формирование запроса к БД
			Statement stmt = conn.createStatement();
			// Выполнение запроса и получение набора данных
			ResultSet res = stmt.executeQuery(
					"SELECT SUM(cena) FROM public.smeta WHERE id_zakaza = " + zk.getId_zakaza().toString());
			// В цикле просмотра набора данных формируем список
			while (res.next())
			{
				zk.setOb_stoim(res.getBigDecimal(1));
			}
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка получения данных 0",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// СМЕТА
	public ArrayList<Smeta> loadSmeta(BigDecimal id_zak)
	{
		ArrayList<Smeta> smeta = new ArrayList<Smeta>();

		Connection con = this.getConnection();
		PreparedStatement pst = null;
		try
		{
			// Формирование запроса к БД
			Statement stmt = con.createStatement();
			// Выполнение запроса и получение набора данных
			String stm = "SELECT id_smeti, kolvo_mat, cena, public.smeta.id_materiala, id_zakaza , "
					+ "public.sprav_mat.name, public.vidi_materialov.name "
					+ "FROM public.smeta JOIN public.sprav_mat ON "
					+ "public.smeta.id_materiala = public.sprav_mat.id_materiala "
					+ "JOIN public.vidi_materialov ON public.sprav_mat.id_vida = public.vidi_materialov.id_vida "
					+ "WHERE id_zakaza = ?;";
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, id_zak);

			ResultSet res = pst.executeQuery();
			// Создание объекта – список продуктов
			smeta = new ArrayList<Smeta>();
			// В цикле просмотра набора данных формируем список
			while (res.next())
			{
				Sprav_mat spr = new Sprav_mat();
				Smeta sm = new Smeta();
				VidiMat vm = new VidiMat();
				sm.setId_smeti(res.getBigDecimal(1));
				sm.setKolvo(res.getBigDecimal(2));
				sm.setCena(res.getBigDecimal(3));
				sm.setId_mat(res.getBigDecimal(4));
				spr.setId_sprav_mat(res.getBigDecimal(4));
				sm.setId_zakaza(res.getBigDecimal(5));
				spr.setName(res.getString(6));
				vm.setName(res.getString(7));
				spr.setVidmat(vm);
				sm.setSpr(spr);
				// Добавляем объект к списку
				smeta.add(sm);
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		// Возврат списка
		return smeta;
	}

	public boolean addSmeta(Smeta sm)
	{
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "INSERT INTO public.smeta(id_smeti, kolvo_mat, cena, id_materiala, id_zakaza) VALUES (?, ?, ?, ?, ?);";
		try
		{
			pst = con.prepareStatement(stm);
			sm.setId_smeti(getId("public.smeta_id_smeti_seq"));
			pst.setBigDecimal(1, sm.getId_smeti());
			pst.setBigDecimal(2, sm.getKolvo());
			pst.setBigDecimal(3, sm.getKolvo().multiply(sm.getSpr().getOtpusk_cena()));
			pst.setBigDecimal(4, sm.getSpr().getId_sprav_mat());
			pst.setBigDecimal(5, sm.getId_zakaza());
			pst.executeUpdate();
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean updateSmeta(Smeta sm, BigDecimal key)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Создание объекта «Оператор с параметрами»
		String stm = "UPDATE public.smeta SET id_smeti=?, kolvo_mat=?, cena=?, id_materiala=?, id_zakaza=? WHERE id_smeti = ?;";
		try
		{
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, sm.getId_smeti());
			pst.setBigDecimal(2, sm.getKolvo());
			pst.setBigDecimal(3, sm.getKolvo().multiply(sm.getSpr().getOtpusk_cena()));
			pst.setBigDecimal(4, sm.getSpr().getId_sprav_mat());
			pst.setBigDecimal(5, sm.getId_zakaza());
			pst.setBigDecimal(6, key);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
		}
		catch (SQLException ex)
		{
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean deleteSmeta(BigDecimal kod)
	{
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "DELETE FROM public.smeta WHERE id_smeti=?";
		try
		{
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка удаления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	public void refreshSmetaRow(Smeta smeta)
	{
		if (smeta.getId_smeti() == null)
			return;
		Connection conn = this.getConnection();
		try
		{
			// Формирование запроса к БД
			Statement stmt = conn.createStatement();
			// Выполнение запроса и получение набора данных
			ResultSet res = stmt.executeQuery(
					"SELECT cena FROM public.smeta" + " where id_smeti = " + smeta.getId_smeti().toString());
			// В цикле просмотра набора данных формируем список
			while (res.next())
			{
				smeta.setCena(res.getBigDecimal(1));
			}
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}

	}

	// СКЛАД
	public ArrayList<Sklad> loadSklad()
	{
		ArrayList<Sklad> sklad = new ArrayList<Sklad>();
		;
		// Получение объекта соединения
		Connection con = this.getConnection();
		try
		{
			// Формирование запроса к БД
			Statement stmt = con.createStatement();
			// Выполнение запроса и получение набора данных
			ResultSet ress = stmt.executeQuery("SELECT id_materiala, name, zakup_cena, otpusk_cena, "
					+ "rashod_na_1kvm, id_vida, v_name, id_mat_sklad, kolvo " + "FROM public.spravmat_v;");
			// Создание объекта – список продуктов
			sklad = new ArrayList<Sklad>();
			// В цикле просмотра набора данных формируем список
			while (ress.next())
			{
				// Создаем объект продукции
				Sklad skl = new Sklad();
				Sprav_mat spr = new Sprav_mat();
				VidiMat vm = new VidiMat();

				// Заполняем поля объекта данными из строки набора
				spr.setId_sprav_mat(ress.getBigDecimal(1));
				spr.setName(ress.getString(2));
				spr.setZakup_cena(ress.getBigDecimal(3));
				spr.setOtpusk_cena(ress.getBigDecimal(4));
				spr.setRashod(ress.getBigDecimal(5));
				vm.setId_vida(ress.getBigDecimal(6));
				vm.setName(ress.getString(7));
				spr.setVidmat(vm);
				skl.setId_sklad(ress.getBigDecimal(8));
				skl.setKolvo(ress.getBigDecimal(9));
				skl.setSpr(spr);
				sklad.add(skl);
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		// Возврат списка
		return sklad;
	}

	public boolean addSklad(Sklad sklad)
	{
		PreparedStatement pst = null;

		Connection con = this.getConnection();

		String stm = "INSERT INTO public.mat_na_sklade(id_mat_sklad, kolvo, id_materiala) VALUES (?, ?, ?);";
		try
		{
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, getId("public.mat_na_sklade_id_mat_sklad_seq"));
			pst.setBigDecimal(2, sklad.getKolvo());
			pst.setBigDecimal(3, sklad.getSpr().getId_sprav_mat());
			pst.executeUpdate();
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean updateSklad(Sklad sklad, BigDecimal key)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "UPDATE public.mat_na_sklade SET id_mat_sklad=?, kolvo=?, id_materiala=? WHERE id_mat_sklad=?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, sklad.getId_sklad());
			pst.setBigDecimal(2, sklad.getKolvo());
			pst.setBigDecimal(3, sklad.getSpr().getId_sprav_mat());
			pst.setBigDecimal(4, key);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
		}
		catch (SQLException ex)
		{
			// В случае ошибки – отмена транзакции
			RollBack();
			// и вывод сообщения об ошибке
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean deleteSklad(BigDecimal kod)
	{
		PreparedStatement pst = null;
		// Получение объекта соединения
		Connection con = this.getConnection();
		// Строка с текстом оператора (? – параметры)
		String stm = "DELETE FROM public.mat_na_sklade" + " WHERE id_mat_sklad=?";
		try
		{
			// Создание объекта «Оператор с параметрами»
			pst = con.prepareStatement(stm);
			// Задаем значения параметров оператора
			pst.setBigDecimal(1, kod);
			// Выполнение оператора
			pst.executeUpdate();
			// Завершение транзакции – сохранение изменений
			con.commit();
			return true;
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}

	// ОТКАТ ИЗМЕНЕНИЙ В СЛУЧАЕ ОШИБКИ
	private void RollBack()
	{
		try
		{
			conn.rollback();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		// Установка путей к схемам
		setPath();
	}

	// ПОЛУЧАЕМ СЛЕДУЮЩИЙ ID ДЛЯ ДОБАВЛЕНИЯ
	public BigDecimal getId(String seqName)
	{
		BigDecimal id = null;
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "SELECT nextval(?)";
		try
		{
			// Формирование запроса к БД
			// Выполнение запроса и получение набора данных
			pst = con.prepareStatement(stm);
			// seqName - имя последовательности
			pst.setString(1, seqName);
			ResultSet res = pst.executeQuery();
			while (res.next())
			{
				id = res.getBigDecimal(1);
			}
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка получения идентификатора",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		return id;
	}

	// РАБОТА С ОТЧЕТОМ
	public RptMatData getDatReport(RptParam params)
	{
		Connection con = this.getConnection();
		PreparedStatement pst = null;
		String stm = "SELECT * FROM public.report_mat(?::date,?::date)";
		try
		{
			pst = con.prepareStatement(stm);
			if (params.StartDate == null)
				pst.setDate(1, null);
			else
				pst.setDate(1, HelperConverter.convertFromJavaDateToSQLDate(params.StartDate));
			if (params.EndDate == null)
				pst.setDate(2, null);
			else
				pst.setDate(2, HelperConverter.convertFromJavaDateToSQLDate(params.EndDate));

			ResultSet res = pst.executeQuery();

			RptMatData rptData = new RptMatData();

			while (res.next())
			{
				RptMatData req = new RptMatData();

				req.setVid(res.getString(1));
				req.setMat(res.getString(2));
				req.setTreb(res.getBigDecimal(3));
				req.setOpl(res.getBigDecimal(4));
				req.setNedost(res.getBigDecimal(5));
				req.setStoim(res.getBigDecimal(6));
				rptData.data.add(req);
			}

			res.close();

			return rptData;
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			RollBack();
			return null;
		}
		finally
		{
			try
			{
				pst.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}

}
