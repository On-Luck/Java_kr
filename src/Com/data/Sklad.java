package Com.data;

import java.math.BigDecimal;

public class Sklad
{
	private BigDecimal id_sklad = null;
	private BigDecimal kolvo = null;
	private BigDecimal id_mat = null;
	private Sprav_mat spr = null;

	public BigDecimal getId_sklad()
	{
		return id_sklad;
	}

	public void setId_sklad(BigDecimal id_sklad)
	{
		this.id_sklad = id_sklad;
	}

	public BigDecimal getKolvo()
	{
		return kolvo;
	}

	public void setKolvo(BigDecimal kolvo)
	{
		this.kolvo = kolvo;
	}

	public BigDecimal getId_mat()
	{
		return id_mat;
	}

	public void setId_mat(BigDecimal id_mat)
	{
		this.id_mat = id_mat;
	}

	public Sprav_mat getSpr()
	{
		return spr;
	}

	public void setSpr(Sprav_mat spr)
	{
		this.spr = spr;
	}

	public String toString()
	{
		return getId_sklad() + "," + getKolvo() + "," + getSpr().getName();
	}
}
