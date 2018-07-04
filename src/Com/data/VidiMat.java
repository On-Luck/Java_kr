package Com.data;

import java.math.BigDecimal;

public class VidiMat
{
	private BigDecimal id_vida;
	private String name;
	
	public VidiMat (BigDecimal id_vida, String name)
	{
		this.setId_vida(id_vida);
		this.setName(name);
	}
	
	public VidiMat()
	{
		super();
	}

	public BigDecimal getId_vida()
	{
		return id_vida;
	}

	public void setId_vida(BigDecimal id_vida)
	{
		this.id_vida = id_vida;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return getName();
	}
}
