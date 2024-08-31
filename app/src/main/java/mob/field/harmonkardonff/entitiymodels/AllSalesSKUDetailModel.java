package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

public class AllSalesSKUDetailModel extends DataEntity<AllSalesSKUDetailModel>{

	
	public String SaleFor;
    public String SKUID;
    public String SKU;
    public String Qty;
	
	
	/**
	 * @param _Name
	 */
	static String TableName="allSalesDetailModel";
	public AllSalesSKUDetailModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}



	public String getSaleFor() {
		return SaleFor;
	}



	public void setSaleFor(String saleFor) {
		SaleFor = saleFor;
	}



	public String getSKUID() {
		return SKUID;
	}



	public void setSKUID(String sKUID) {
		SKUID = sKUID;
	}



	public String getSKU() {
		return SKU;
	}



	public void setSKU(String sKU) {
		SKU = sKU;
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
	public AllSalesSKUDetailModel GetNewObject() {
		// TODO Auto-generated method stub
		return new AllSalesSKUDetailModel();
	}

	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#RegisterMappings()
	 */
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		}
}
