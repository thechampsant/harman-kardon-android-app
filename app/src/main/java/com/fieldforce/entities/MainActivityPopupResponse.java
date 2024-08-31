package com.fieldforce.entities;

import app.core.sqllite.DataEntity;

public class MainActivityPopupResponse extends DataEntity<MainActivityPopupResponse>
{
    public static final String TableName ="MainActivityPopupResponse";
    public String ScheamId="";
    public String DocName="";
    public String DocURL="";
    public String Thumbnail="";
    public String IsRead="";
    public MainActivityPopupResponse(){
        super(TableName);
    }

    @Override
    public void RegisterMappings() {

    }

    @Override
    public MainActivityPopupResponse GetNewObject() {
        return new MainActivityPopupResponse();
    }

    @Override
    public String toString() {
        return "MainActivityPopupResponse{" +
                "ScheamId='" + ScheamId + '\'' +
                ", DocName='" + DocName + '\'' +
                ", DocURL='" + DocURL + '\'' +
                ", Thumbnail='" + Thumbnail + '\'' +
                ", IsRead='" + IsRead + '\'' +
                '}';
    }
}
