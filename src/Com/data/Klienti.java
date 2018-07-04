package Com.data;

import java.math.BigDecimal;

public class Klienti
{
	private BigDecimal id_klienta = null;
	private String klient = null;
	private String phone = null;

	public Klienti(BigDecimal id_klienta, String klient, String phone)
	{
		this.id_klienta = id_klienta;
		this.klient = klient;
		this.phone = phone;
	}
	public Klienti()
	{
		super();
	}

	public String getKlient()
	{
		return klient;
	}

	public void setKlient(String klient)
	{
		this.klient = klient;
	}

	public BigDecimal getId_klienta()
	{
		return id_klienta;
	}

	public void setId_klienta(BigDecimal id_klienta)
	{
		this.id_klienta = id_klienta;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String toString()
	{
		return getKlient();
	}
}
