package com.fieldforce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class SeenNotificationResonse extends DataEntity<SeenNotificationResonse> {

    public Integer Id;
    public String Notification;
    public String NotificationType;
    public String NotificationAt;

    @Override
    public void RegisterMappings() {
        this.RegisterMapping("Id", DataTypes.TEXT);
        this.RegisterMapping("Notification", DataTypes.TEXT);
        this.RegisterMapping("NotificationType", DataTypes.TEXT);
        this.RegisterMapping("NotificationAt", DataTypes.TEXT);
    }

    @Override
    public SeenNotificationResonse GetNewObject() {
        return new SeenNotificationResonse();
    }
}
