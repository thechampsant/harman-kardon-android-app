package com.fieldforce.harmonkardonff;

import com.fieldforce.harmonkardonff.demo_tracking_module.models.ViewDemoResponseModel;

import app.core.sqllite.DataEntity;

public class ViewModuleModel  extends DataEntity<ViewModuleModel> {
    public static final String TableName ="";

           /* "ProductName": "HKAURASTUDIO2BLKEU",
            "CustomerName": "javs",
            "CustomerPhone": "9638527410",
            "CustomerEmail": "cg@mail",
            "CustomerAge": 23,
            "SubmittedOn": "06/22/20  4:40:54 PM"*/

    public String DisplayRaiseDate="";
    public String Category="";
    public String SubCategory="";
    public String IsManual="false";
    public String Longitude="";
    public String Latitude="";
    public String SKU="";
    public String SKUID="";
    public String BarCodeValue="";
    public String DateOfOutStock="";
    public String StockRaiseDate="";
    public String StockQtyRequired="";
    public String CurrentStock="";
    public String DescribeIssue="";


    public ViewModuleModel() {
        super(TableName);
    }

    @Override
    public void RegisterMappings() {

    }

    @Override
    public ViewModuleModel GetNewObject() {
        return new ViewModuleModel();
    }
}

