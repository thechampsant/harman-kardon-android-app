package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

public class TargetModelAll extends DataEntity<TargetModelAll>{
	/**
	 * @param _Name
	 */
	static String TableName="TargetModelAll";
	public TargetModelAll() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	public String category;
	public String target;
	public String sales;
	public String Ach_perc;
	public String remarks;
	public String till_Date;

	public String getLYsales() {
		return LYsales;
	}

	public void setLYsales(String LYsales) {
		this.LYsales = LYsales;
	}

	public String getContribution() {
		return Contribution;
	}

	public void setContribution(String contribution) {
		Contribution = contribution;
	}

	public String LYsales;
	public String Contribution;
	public String PremiumContribution;

	public String getPremiumContribution() {
		return PremiumContribution;
	}

	public void setPremiumContribution(String premiumContribution) {
		PremiumContribution = premiumContribution;
	}

	public void setISPCategory(String ISPCategory) {
		this.ISPCategory = ISPCategory;
	}
	public void setStoreGrade(String StoreGrade) {
		this.StoreGrade = StoreGrade;
	}

	public String getISPCategory() {
		return ISPCategory;
	}

	public String getStoreGrade() {
		return StoreGrade;
	}


	public String ISPCategory;
	public String StoreGrade;

	

	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getTarget() {
		return target;
	}


	public void setTarget(String target) {
		this.target = target;
	}


	public String getSales() {
		return sales;
	}


	public void setSales(String sales) {
		this.sales = sales;
	}


	public String getAch_perc() {
		return Ach_perc;
	}


	public void setAch_perc(String ach_perc) {
		Ach_perc = ach_perc;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTill_Date() {
		return till_Date;
	}

	public void setTill_Date(String till_Date) {
		this.till_Date = till_Date;
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public TargetModelAll GetNewObject() {
		// TODO Auto-generated method stub
		return new TargetModelAll();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		
		/*this.RegisterMapping("_ID", DataTypes.INTEGER_PRIMARY_KEY);
		this.RegisterMapping("PID", DataTypes.INT);
		this.RegisterMapping("Name", DataTypes.TEXT);
		this.RegisterMapping("Code", DataTypes.TEXT);
		this.RegisterMapping("ParentID", DataTypes.INT_NULL);
		this.RegisterMapping("Cat1", DataTypes.TEXT);
		this.RegisterMapping("Cat2", DataTypes.TEXT);
		this.RegisterMapping("Cat3", DataTypes.TEXT);
		this.RegisterMapping("Cat4", DataTypes.TEXT);
		this.RegisterMapping("IsBlocked", DataTypes.TEXT);*/
	}
}
