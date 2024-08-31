package com.ariston.training_module.modules.training_module.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainingParentModel {
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
    private String errormsg;
    @SerializedName("AppVersion")
    @Expose
    private Object appVersion;
    @SerializedName("data")
    @Expose
    private List<TrainingModel> data = null;
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

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public Object getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Object appVersion) {
        this.appVersion = appVersion;
    }

    public List<TrainingModel> getData() {
        return data;
    }

    public void setData(List<TrainingModel> data) {
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
