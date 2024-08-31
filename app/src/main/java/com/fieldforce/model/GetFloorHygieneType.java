package com.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class GetFloorHygieneType extends DataEntity<GetFloorHygieneType> {

    public Integer ID;

    public String TypeName;


    @Override
    public void RegisterMappings() {
        this.RegisterMapping("ID", DataTypes.TEXT);
        this.RegisterMapping("TypeName", DataTypes.TEXT);


    }

    @Override
    public GetFloorHygieneType GetNewObject() {
        return new GetFloorHygieneType();
    }




}