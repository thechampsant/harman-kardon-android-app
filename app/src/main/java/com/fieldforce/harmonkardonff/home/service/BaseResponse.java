package com.fieldforce.harmonkardonff.home.service;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("AppVersion")
    private String mAppVersion;
    @SerializedName("Edata")
    private String mEdata;
    @SerializedName("errormsg")
    private String mErrormsg;
    @SerializedName("FileUrl")
    private String mFileUrl;
    @SerializedName("status")
    private boolean mStatus;
    @SerializedName("Success")
    private String mSuccess;
    @SerializedName("type")
    private String mType;

    public BaseResponse(boolean mStatus, String mErrormsg) {
        this.mStatus = mStatus;
        this.mErrormsg = mErrormsg;
    }

    public String getmAppVersion() {
        return mAppVersion;
    }

    public void setmAppVersion(String mAppVersion) {
        this.mAppVersion = mAppVersion;
    }

    public String getmEdata() {
        return mEdata;
    }

    public void setmEdata(String mEdata) {
        this.mEdata = mEdata;
    }

    public String getmErrormsg() {
        return mErrormsg;
    }

    public void setmErrormsg(String mErrormsg) {
        this.mErrormsg = mErrormsg;
    }

    public String getmFileUrl() {
        return mFileUrl;
    }

    public void setmFileUrl(String mFileUrl) {
        this.mFileUrl = mFileUrl;
    }

    public boolean getmStatus() {
        return mStatus;
    }

    public void setmStatus(boolean mStatus) {
        this.mStatus = mStatus;
    }

    public String getmSuccess() {
        return mSuccess;
    }

    public void setmSuccess(String mSuccess) {
        this.mSuccess = mSuccess;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
