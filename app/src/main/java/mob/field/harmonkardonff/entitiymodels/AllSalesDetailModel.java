package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

public class AllSalesDetailModel extends DataEntity<AllSalesDetailModel>{

	
	public String CatID;
	public String Category;
    public String Qty;
	/**
	 * @param _Name
	 */
	static String TableName="allSalesDetailModel";
	public AllSalesDetailModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	public String getCatID() {
		return CatID;
	}

	public void setCatID(String catID) {
		CatID = catID;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getQty() {
		return Qty;
	}


	public void setQty(String qty) {
		Qty = qty;
	}


	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public AllSalesDetailModel GetNewObject() {
		// TODO Auto-generated method stub
		return new AllSalesDetailModel();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		}
}
