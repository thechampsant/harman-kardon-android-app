package com.fieldforce.harmonkardonff.demo_tracking_module.models;

import app.core.sqllite.DataEntity;

public class EnterDemoRequestModel extends DataEntity<EnterDemoRequestModel> {

    public static final String TableName ="EnterDemoRequestModel";

    //http://harman.infield.co.in/ispmobile/SubmitDemoTracking?UserName=50&PID=3&CustomerName=testCust2&CustomerEmail=testCust2@gmail.com
    // &CustomerPhone=0202020202&CustomerAge=29

    public String UserName ="";
    public String DemoProdId ="";

    public String CustomerName ="";
    public String CustomerEmail ="";
    public String CustomerMob ="";
    public String CustomerAge ="";
    public String NoDemo ="";

    public EnterDemoRequestModel() {
        super(TableName);
    }

    @Override
    public void RegisterMappings() {

    }

    @Override
    public EnterDemoRequestModel GetNewObject() {
        return null;
    }
}
