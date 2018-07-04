package Com.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

public class Zakaz
{
	private BigDecimal id_zakaza = null;;
	private BigDecimal nomer = null;
	private Date data_zakaza = null;
	private Date data_nachala_rabot = null;
	private BigDecimal ob_stoim = null;
	private BigDecimal plosh_pom = null;
	private BigDecimal id_klienta = null;
	private Klienti klient = null;

	public Zakaz(BigDecimal id_zakaza, BigDecimal nomer, Integer data_zakaza_year, Integer data_zakaza_month,
			Integer data_zakaza_day, Integer data_nachala_rabot_year, Integer data_nachala_rabot_month,
			Integer data_nachala_rabot_day, BigDecimal ob_stoim, BigDecimal plosh_pom, BigDecimal id_klienta)
	{
		this.setId_zakaza(id_zakaza);
		this.setNomer(nomer);
		this.setData_zakaza(
				(new GregorianCalendar(data_zakaza_year, data_zakaza_month - 1, data_zakaza_day)).getTime());
		this.setData_nachala_rabot(
				(new GregorianCalendar(data_nachala_rabot_year, data_nachala_rabot_month - 1, data_nachala_rabot_day))
						.getTime());
		this.setOb_stoim(ob_stoim);
		this.setPlosh_pom(plosh_pom);
		this.setId_klienta(id_klienta);
	}

	public Zakaz()
	{
		super();
	}

	public BigDecimal getId_zakaza()
	{
		return id_zakaza;
	}

	public void setId_zakaza(BigDecimal id_zakaza)
	{
		this.id_zakaza = id_zakaza;
	}

	public Date getData_zakaza()
	{
		return data_zakaza;
	}

	public void setData_zakaza(Date data_zakaza)
	{
		this.data_zakaza = data_zakaza;
	}

	public BigDecimal getNomer()
	{
		return nomer;
	}

	public void setNomer(BigDecimal nomer)
	{
		this.nomer = nomer;
	}

	public BigDecimal getOb_stoim()
	{
		return ob_stoim;
	}

	public void setOb_stoim(BigDecimal ob_stoim)
	{
		this.ob_stoim = ob_stoim;
	}

	public Date getData_nachala_rabot()
	{
		return data_nachala_rabot;
	}

	public void setData_nachala_rabot(Date data_nachala_rabot)
	{
		this.data_nachala_rabot = data_nachala_rabot;
	}

	public BigDecimal getPlosh_pom()
	{
		return plosh_pom;
	}

	public void setPlosh_pom(BigDecimal plosh_pom)
	{
		this.plosh_pom = plosh_pom;
	}

	public BigDecimal getId_klienta()
	{
		return id_klienta;
	}

	public void setId_klienta(BigDecimal id_klienta)
	{
		this.id_klienta = id_klienta;
	}

	public Klienti getKlient()
	{
		return klient;
	}

	public void setKlient(Klienti klient)
	{
		this.klient = klient;
	}

	public String toString()
	{
		return getId_zakaza() + "," + getNomer() + "," + getData_zakaza() + "," + getData_nachala_rabot() + ","
				+ getOb_stoim() + "," + getPlosh_pom() + "," + getId_klienta();
	}
}
