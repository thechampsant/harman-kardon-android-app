package com.ariston.training_module.modules.training_module.models.faq_hep;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Faqresponse extends BaseResponse {
    public Faqresponse(boolean mStatus, String mErrormsg) {
        super(mStatus, mErrormsg);
    }

    @SerializedName("data")
    @Expose
    private List<FaqresponseModel> data = null;


    public List<FaqresponseModel> getData() {
        return data;
    }

    public void setData(List<FaqresponseModel> data) {
        this.data = data;
    }


}
