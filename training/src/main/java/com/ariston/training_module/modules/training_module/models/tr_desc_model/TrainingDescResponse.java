
package com.ariston.training_module.modules.training_module.models.tr_desc_model;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TrainingDescResponse extends BaseResponse {


    @SerializedName("data")
    private List<TrainingDescItem> mData;

    public TrainingDescResponse(Boolean mStatus, String errorMessage) {
        super(mStatus, errorMessage);
    }


    public List<TrainingDescItem> getData() {
        return mData;
    }

    public void setData(List<TrainingDescItem> data) {
        mData = data;
    }


}
