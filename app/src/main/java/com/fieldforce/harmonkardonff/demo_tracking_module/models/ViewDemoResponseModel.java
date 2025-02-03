package com.fieldforce.harmonkardonff.demo_tracking_module.models;

import app.core.sqllite.DataEntity;

public class ViewDemoResponseModel extends DataEntity<ViewDemoResponseModel> {
    public static final String TableName ="";

           /* "ProductName": "HKAURASTUDIO2BLKEU",
            "CustomerName": "javs",
            "CustomerPhone": "9638527410",
            "CustomerEmail": "cg@mail",
            "CustomerAge": 23,
            "SubmittedOn": "06/22/20  4:40:54 PM"*/

    public String ProductName="";
    public String CustomerName="";
    public String CustomerMob="";
    public String NoDemo="";
    public String CustomerEmail="";
    public String CustomerAge="";
    public String SubmittedOn="";

    public ViewDemoResponseModel() {
        super(TableName);
    }

    @Override
    public void RegisterMappings() {

    }

    @Override
    public ViewDemoResponseModel GetNewObject() {
        return new ViewDemoResponseModel();
    }
}
