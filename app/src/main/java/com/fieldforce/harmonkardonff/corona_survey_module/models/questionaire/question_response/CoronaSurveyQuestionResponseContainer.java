package com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.question_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CoronaSurveyQuestionResponseContainer {

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
    private List<CoronaSurveyQuestionResponseData> data = null;
    @SerializedName("Edata")
    @Expose
    private String edata;
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

    public List<CoronaSurveyQuestionResponseData> getData() {
        return data;
    }

    public void setData(List<CoronaSurveyQuestionResponseData> data) {
        this.data = data;
    }

    public String getEdata() {
        return edata;
    }

    public void setEdata(String edata) {
        this.edata = edata;
    }

    public Object getSuccess() {
        return success;
    }

    public void setSuccess(Object success) {
        this.success = success;
    }
}
