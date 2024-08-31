package com.APIService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitEmpDetail {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isError")
    @Expose
    private Boolean isError;
    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
