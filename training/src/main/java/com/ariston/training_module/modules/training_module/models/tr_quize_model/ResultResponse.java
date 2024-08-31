package com.ariston.training_module.modules.training_module.models.tr_quize_model;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultResponse extends BaseResponse {


    @SerializedName("data")
    @Expose
    private ResultRes data;

    public ResultRes getData() {
        return data;
    }

    public void setData(ResultRes data) {
        this.data = data;
    }

    public ResultResponse(boolean mStatus, String mErrormsg) {
        super(mStatus, mErrormsg);
    }


}