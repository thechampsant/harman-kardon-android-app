package com.fieldforce.harmonkardonff.home.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {

    public Banner(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @SerializedName("Path")
    @Expose
    private String path;

    public String getIncentivePoints() {
        return IncentivePoints;
    }

    public void setIncentivePoints(String incentivePoints) {
        IncentivePoints = incentivePoints;
    }

    @SerializedName("IncentivePoints")
    @Expose
    private String IncentivePoints;

    public String getAchTgt() {
        return AchTgt;
    }

    public void setAchTgt(String achTgt) {
        AchTgt = achTgt;
    }

    @SerializedName("AchTgt")
    @Expose
    private String AchTgt;

    @SerializedName("IsSeen")
    @Expose
    private String IsSeen;

    public String getIsSeen() {
        return IsSeen;
    }

    public void setIsSeen(String isSeen) {
        IsSeen = isSeen;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getDocUrl() {
        return DocUrl;
    }

    public void setDocUrl(String docUrl) {
        DocUrl = docUrl;
    }

    public String getDocType() {
        return DocType;
    }

    public void setDocType(String docType) {
        DocType = docType;
    }

    @SerializedName("DocID")
    @Expose
    private String DocID;


    @SerializedName("DocUrl")
    @Expose
    private String DocUrl;


    @SerializedName("DocType")
    @Expose
    private String DocType;


    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    @SerializedName("IsActive")
    @Expose
    private String IsActive;
}
