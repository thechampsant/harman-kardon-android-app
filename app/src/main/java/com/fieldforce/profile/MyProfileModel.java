package com.fieldforce.profile;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

/**
 * Created by Amit Dhiman on 1/4/2019.
 */

public class MyProfileModel extends DataEntity<MyProfileModel> {
    static String TableName="MyProfileModel";
    public String loginid;
    public String name;
    public String empcode;
    public String mobile;
    public String email;
    public String designation;
    public String weekly_off;
    public String profile_pic_path;
    public MyProfileModel() {
        super(TableName);
    }

    @Override
    public void RegisterMappings() {
        this.RegisterMapping("loginid", DataTypes.TEXT);
        this.RegisterMapping("name", DataTypes.TEXT);
        this.RegisterMapping("empcode", DataTypes.TEXT);
        this.RegisterMapping("mobile", DataTypes.TEXT);
        this.RegisterMapping("email", DataTypes.TEXT);
        this.RegisterMapping("designation", DataTypes.TEXT);
        this.RegisterMapping("weekly_off", DataTypes.TEXT);
        this.RegisterMapping("profile_pic_path", DataTypes.TEXT);
    }

    @Override
    public MyProfileModel GetNewObject() {
        return new MyProfileModel();
    }
}
