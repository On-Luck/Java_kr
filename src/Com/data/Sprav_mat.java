package Com.data;

import java.math.BigDecimal;

public class Sprav_mat
{
	private BigDecimal id_sprav_mat = null;
	private String name = null;
	private BigDecimal zakup_cena = null;
	private BigDecimal otpusk_cena = null;
	private BigDecimal rashod = null;
	private BigDecimal id_vida = null;
	private VidiMat vidmat = null;

	public Sprav_mat(BigDecimal id_sprav_mat, String name, BigDecimal zakup_cena, BigDecimal otpusk_cena,
			BigDecimal rashod, BigDecimal id_vida)
	{
		this.setId_sprav_mat(id_sprav_mat);
		this.setName(name);
		this.setZakup_cena(zakup_cena);
		this.setOtpusk_cena(otpusk_cena);
		this.setRashod(rashod);
		this.setId_vida(id_vida);
	}

	public Sprav_mat()
	{
		super();
	}

	public BigDecimal getId_sprav_mat()
	{
		return id_sprav_mat;
	}

	public void setId_sprav_mat(BigDecimal id_sprav_mat)
	{
		this.id_sprav_mat = id_sprav_mat;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getZakup_cena()
	{
		return zakup_cena;
	}

	public void setZakup_cena(BigDecimal zakup_cena)
	{
		this.zakup_cena = zakup_cena;
	}

	public BigDecimal getOtpusk_cena()
	{
		return otpusk_cena;
	}

	public void setOtpusk_cena(BigDecimal otpusk_cena)
	{
		this.otpusk_cena = otpusk_cena;
	}

	public BigDecimal getRashod()
	{
		return rashod;
	}

	public void setRashod(BigDecimal rashod)
	{
		this.rashod = rashod;
	}

	public BigDecimal getId_vida()
	{
		return id_vida;
	}

	public void setId_vida(BigDecimal id_vida)
	{
		this.id_vida = id_vida;
	}

	public String toString()
	{
		return  getName();
	}

	public VidiMat getVidmat()
	{
		return vidmat;
	}

	public void setVidmat(VidiMat vidmat)
	{
		this.vidmat = vidmat;
	}
}
