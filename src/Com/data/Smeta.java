package Com.data;

import java.math.BigDecimal;

public class Smeta
{
	private BigDecimal id_smeti = null;
	private BigDecimal kolvo = null;
	private BigDecimal cena = null;
	private BigDecimal id_mat = null;
	private Sprav_mat spr = null;
	private BigDecimal id_zakaza = null;

	public Smeta()
	{
		super();
	}

	public BigDecimal getId_smeti()
	{
		return id_smeti;
	}

	public void setId_smeti(BigDecimal id_smeti)
	{
		this.id_smeti = id_smeti;
	}

	public BigDecimal getKolvo()
	{
		return kolvo;
	}

	public void setKolvo(BigDecimal kolvo)
	{
		this.kolvo = kolvo;
	}

	public BigDecimal getCena()
	{
		return cena;
	}

	public void setCena(BigDecimal cena)
	{
		this.cena = cena;
	}

	public Sprav_mat getSpr()
	{
		return spr;
	}

	public void setSpr(Sprav_mat spr)
	{
		this.spr = spr;
	}

	public BigDecimal getId_zakaza()
	{
		return id_zakaza;
	}

	public void setId_zakaza(BigDecimal id_zakaza)
	{
		this.id_zakaza = id_zakaza;
	}
	public BigDecimal getId_mat()
	{
		return id_mat;
	}

	public void setId_mat(BigDecimal id_mat)
	{
		this.id_mat = id_mat;
	}

	public String toString()
	{
		return getSpr() + "," + getKolvo() + "," + getCena();
	}
}
