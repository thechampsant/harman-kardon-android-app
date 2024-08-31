package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;


public class NewSaleModel  extends DataEntity<NewSaleModel>{

	
	static String TableName="NewSalesModel";
	public NewSaleModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
	}

	//For Local Use
	public String ProductName="";
	public String Cat1="";
	public String Cat2="";
	public String Cat3="";
	
	
	public String CustomerName="";
	public String MobileNo="";
	public String Email="";
	public String City="";
	public String Location="";
	public String Domenstration="";
	
	public String PID="";
	public String ForDate;
	public String Qty="";
	public int TotalQty=0;
	public int TotalAmount=0;
	public String Remarks;
	public String IsUpdated="false";
	public String SerialNo="";
	public String Prices="";
	public String Price="";
	public String CreatedOn;
	public String UserName=MainActivity.MyInfo.EmployeeCode;
	public String IsNoSale="false";
	public String ModeOfPayment = "";
	public String DocIDs="";
	public String PriceDocIDs="";
	public String InvoiceNumber="";
	public String SKU="";
	public String BarCodeValue="";
	public String OutStockDate="";
	public String StockQty="";
	public String CurrentStock="";
	public String DescribeIssue="";
	public String Longitude="";
	public String Latitude="";


	public String IsManual="false";

	
	

	@Override
	public NewSaleModel GetNewObject() {
		// TODO Auto-generated method stub
		return new NewSaleModel();
	}
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		
		this.RegisterMapping("CustomerName", DataTypes.TEXT);
		this.RegisterMapping("MobileNo", DataTypes.TEXT);
		this.RegisterMapping("Email", DataTypes.TEXT);
		this.RegisterMapping("City", DataTypes.TEXT);
		this.RegisterMapping("Location", DataTypes.TEXT);
		this.RegisterMapping("ProductName", DataTypes.TEXT);
		this.RegisterMapping("PID", DataTypes.TEXT);
		this.RegisterMapping("ForDate", DataTypes.TEXT);
		this.RegisterMapping("Qty", DataTypes.TEXT);		
		this.RegisterMapping("TotalQty", DataTypes.INT);	
		this.RegisterMapping("TotalAmount", DataTypes.INT);	
		this.RegisterMapping("PriceDocIDs", DataTypes.INT);
		this.RegisterMapping("Remarks", DataTypes.TEXT);
		this.RegisterMapping("IsUpdated", DataTypes.TEXT);
		this.RegisterMapping("SerialNo", DataTypes.TEXT);
		this.RegisterMapping("Prices", DataTypes.TEXT);
		this.RegisterMapping("Price", DataTypes.TEXT);
		this.RegisterMapping("CreatedOn", DataTypes.TEXT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("DocIDs",DataTypes.TEXT);
		this.RegisterMapping("Barcode",DataTypes.TEXT);

		this.RegisterMapping("Cat1", DataTypes.TEXT);
		this.RegisterMapping("Cat2", DataTypes.TEXT);
		this.RegisterMapping("Cat3", DataTypes.TEXT);
		this.RegisterMapping("IsNoSale", DataTypes.TEXT);
		this.RegisterMapping("ModeOfPayment", DataTypes.TEXT);
		this.RegisterMapping("SKU", DataTypes.TEXT);

		this.RegisterMapping("BarCodeValue", DataTypes.TEXT);
		this.RegisterMapping("IsManual", DataTypes.TEXT);
		this.RegisterMapping("Longitude", DataTypes.TEXT);
		this.RegisterMapping("Latitude", DataTypes.TEXT);
	}

}
