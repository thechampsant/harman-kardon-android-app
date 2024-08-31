package com.APIService;


import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class SessionModel extends DataEntity<SessionModel> {
    static String TableName="SessionModel";
    public SessionModel() {
        super(TableName);
        // TODO Auto-generated constructor stub
    }
    public String user;
    public String date;



    @Override
    public void RegisterMappings() {
        // TODO Auto-generated method stub
        this.RegisterMapping("user", DataTypes.TEXT);
        this.RegisterMapping("date",DataTypes.TEXT);

    }

    @Override
    public SessionModel GetNewObject() {
        // TODO Auto-generated method stub
        return new SessionModel();
    }

}
