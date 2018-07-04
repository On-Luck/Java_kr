package Com.rpt;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RptMatData
{
		private String vid;
		private String mat;
		private BigDecimal treb;
		private BigDecimal opl;
		private BigDecimal nedost;
		private BigDecimal stoim;
	// Коллекция – источника данных отчета
		public ArrayList<RptMatData> data 
	      = new ArrayList<RptMatData>();
	//  Методы установки и извлечения значений полей
		public String getVid()
		{
			return vid;
		}
		public void setVid(String vid)
		{
			this.vid = vid;
		}
		public String getMat()
		{
			return mat;
		}
		public void setMat(String mat)
		{
			this.mat = mat;
		}
		public BigDecimal getTreb()
		{
			return treb;
		}
		public void setTreb(BigDecimal treb)
		{
			this.treb = treb;
		}
		public BigDecimal getOpl()
		{
			return opl;
		}
		public void setOpl(BigDecimal opl)
		{
			this.opl = opl;
		}
		public BigDecimal getNedost()
		{
			return nedost;
		}
		public void setNedost(BigDecimal nedost)
		{
			this.nedost = nedost;
		}
		public BigDecimal getStoim()
		{
			return stoim;
		}
		public void setStoim(BigDecimal stoim)
		{
			this.stoim = stoim;
		}
}
