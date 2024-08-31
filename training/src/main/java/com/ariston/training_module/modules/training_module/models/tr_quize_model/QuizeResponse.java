package com.ariston.training_module.modules.training_module.models.tr_quize_model;

import java.util.List;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizeResponse extends BaseResponse {


    @SerializedName("data")
    @Expose
    private List<Quize_Res> data = null;




    public List<Quize_Res> getData() { return data;
    }

    public void setData(List<Quize_Res> data) {
        this.data = data;
    }

    public QuizeResponse(boolean mStatus, String mErrormsg) {
        super(mStatus, mErrormsg);
    }
}