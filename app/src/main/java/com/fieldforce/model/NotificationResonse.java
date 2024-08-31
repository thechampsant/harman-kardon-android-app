package com.fieldforce.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationResonse {

    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("FileUrl")
    @Expose
    private Object fileUrl;
    @SerializedName("errormsg")
    @Expose
    private Object errormsg;
    @SerializedName("AppVersion")
    @Expose
    private Object appVersion;
    @SerializedName("data")
    @Expose
    private List<NotificationResonseMode> data = null;
    @SerializedName("Edata")
    @Expose
    private Object edata;
    @SerializedName("Success")
    @Expose
    private Object success;

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(Object fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Object getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(Object errormsg) {
        this.errormsg = errormsg;
    }

    public Object getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Object appVersion) {
        this.appVersion = appVersion;
    }

    public List<NotificationResonseMode> getData() {
        return data;
    }

    public void setData(List<NotificationResonseMode> data) {
        this.data = data;
    }

    public Object getEdata() {
        return edata;
    }

    public void setEdata(Object edata) {
        this.edata = edata;
    }

    public Object getSuccess() {
        return success;
    }

    public void setSuccess(Object success) {
        this.success = success;
    }

}