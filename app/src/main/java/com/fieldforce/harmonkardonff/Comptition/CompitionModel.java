package com.fieldforce.harmonkardonff.Comptition;

import java.io.Serializable;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class CompitionModel  extends DataEntity<CompitionModel> implements Serializable {

    static String TableName="CompitionModel";
    public CompitionModel() {
        super(TableName);
        // TODO Auto-generated constructor stub

    }
    public String ID;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String Date;
    public String Question;
    public String CompQuestID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getCompQuestID() {
        return CompQuestID;
    }

    public void setCompQuestID(String compQuestID) {
        CompQuestID = compQuestID;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getIsMandatory() {
        return IsMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        IsMandatory = isMandatory;
    }

    public String Qty="";
    public String Quantity="";
    public String IsMandatory;



    @Override
    public void RegisterMappings() {
        // TODO Auto-generated method stub
        this.RegisterMapping("ID", DataTypes.TEXT);
        this.RegisterMapping("CatName",DataTypes.TEXT);
        this.RegisterMapping("CatID",DataTypes.TEXT);
        this.RegisterMapping("Date",DataTypes.TEXT);
        this.RegisterMapping("PicUrl",DataTypes.TEXT);
        this.RegisterMapping("IsMandatory",DataTypes.TEXT);
        this.RegisterMapping("IsUpload",DataTypes.TEXT);

    }

    @Override
    public CompitionModel GetNewObject() {
        // TODO Auto-generated method stub
        return new CompitionModel();
    }

}

