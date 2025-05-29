package com.fieldforce.harmonkardonff.home.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bannerdata extends BaseResponse {
    @SerializedName("data")
    private List<Banner> mData;

    public Bannerdata(Boolean mStatus, String errorMessage) {
        super(mStatus, errorMessage);
    }


    public List<Banner> getData() {
        return mData;
    }

    public void setData(List<Banner> data) {
        mData = data;
    }
}
