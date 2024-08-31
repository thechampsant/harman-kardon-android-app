package app.core.entitymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class ProductCategory extends DataEntity<ProductCategory> {

	public ProductCategory() {
		super("ProductCategory");

	}

	public String ID;
	public String ParentID="0";
	public String IsProduct;
	public String Name;
	public String IsBlocked;

	public boolean isBlocked() {
		return this.IsBlocked.trim().equalsIgnoreCase("true");
	}
	public boolean isProduct() {
		return this.IsProduct.trim().equalsIgnoreCase("true");
	}

	@Override
	public void RegisterMappings() {

		this.RegisterMapping("ID", DataTypes.TEXT);
		this.RegisterMapping("ParentID", DataTypes.TEXT);
		this.RegisterMapping("IsProduct", DataTypes.TEXT);
		this.RegisterMapping("Name", DataTypes.TEXT);
		this.RegisterMapping("IsBlocked", DataTypes.TEXT);
	}

	@Override
	public ProductCategory GetNewObject() {
		return new ProductCategory();
	}

}
