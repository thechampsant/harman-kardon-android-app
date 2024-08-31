package com.APIService;

import com.fieldforce.asyntask.WebService;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;


public class DropDownModel extends DataEntity<DropDownModel> {
    /**
     * @param _Name
     */
    static String TableName = "DropDownModel";

    public DropDownModel() {
        super(TableName);
    }

    public String parent_label;
    public String parent_value;
    public String current_label;
    public String child_label;
    public String tree_level;
    public String key;
    public String value;
    public String url;
    public String circleColorCode = "";
    public String form_title;
    public String username = WebService.getUsername();

    public DropDownModel(String current_label) {
        super(TableName);
        this.current_label = current_label;
    }

    @Override
    public DropDownModel GetNewObject() {
        return new DropDownModel();
    }

    @Override
    public void RegisterMappings() {
        this.RegisterMapping("parent_label", DataTypes.TEXT);
        this.RegisterMapping("parent_value", DataTypes.TEXT);
        this.RegisterMapping("current_label", DataTypes.TEXT);
        this.RegisterMapping("child_label", DataTypes.TEXT);
        this.RegisterMapping("tree_level", DataTypes.TEXT);
        this.RegisterMapping("key", DataTypes.TEXT);
        this.RegisterMapping("value", DataTypes.TEXT);
        this.RegisterMapping("url", DataTypes.TEXT);
        this.RegisterMapping("circleColorCode", DataTypes.TEXT);
        this.RegisterMapping("form_title", DataTypes.TEXT);
        this.RegisterMapping("username", DataTypes.TEXT);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DropDownModel that = (DropDownModel) o;
        return that.current_label.equals(((DropDownModel) o).current_label);
    }
}