package com.APIService;


import com.fieldforce.asyntask.WebService;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class MTDSalesValueModel extends DataEntity<MTDSalesValueModel> {
	
	static String TableName="MTDSalesValueModel";
	public MTDSalesValueModel() {
		super(TableName);
		// TODO Auto-generated constructor stub
		
	}
    public String Category;
	public String Value;
	public String Tgt;
	public String Ach;
	public String UserName= WebService.getUsername();
	
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("Category",DataTypes.TEXT);
		this.RegisterMapping("Value",DataTypes.TEXT);
		this.RegisterMapping("Tgt",DataTypes.TEXT);
		this.RegisterMapping("Ach",DataTypes.TEXT);
		this.RegisterMapping("UserName",DataTypes.TEXT);
	}

	@Override
	public MTDSalesValueModel GetNewObject() {
		// TODO Auto-generated method stub
		return new MTDSalesValueModel();
	}

}
