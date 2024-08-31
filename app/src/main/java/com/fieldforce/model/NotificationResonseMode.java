package com.fieldforce.model;


import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class NotificationResonseMode extends DataEntity<NotificationResonseMode> {


    public String Id;
    public String Notification;
    public String NotificationType;


    @Override
    public void RegisterMappings() {
        this.RegisterMapping("Id", DataTypes.TEXT);
        this.RegisterMapping("Notification", DataTypes.TEXT);
        this.RegisterMapping("NotificationType", DataTypes.TEXT);

    }

    @Override
    public NotificationResonseMode GetNewObject() {
        return new NotificationResonseMode();
    }
}