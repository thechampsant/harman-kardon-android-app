package com.ariston.training_module.modules.training_module.models.faq_hep;

import com.ariston.training_module.modules.training_module.models.BaseResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpDeskResponse extends BaseResponse {
    @SerializedName("data")
    private List<GetTrainingHelpDeskModel> mData;

    public HelpDeskResponse(Boolean mStatus, String errorMessage) {
        super(mStatus, errorMessage);
    }


    public List<GetTrainingHelpDeskModel> getData() {
        return mData;
    }

    public void setData(List<GetTrainingHelpDeskModel> data) {
        mData = data;
    }
}


