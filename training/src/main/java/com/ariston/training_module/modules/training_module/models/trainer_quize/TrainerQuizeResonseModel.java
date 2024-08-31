package com.ariston.training_module.modules.training_module.models.trainer_quize;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainerQuizeResonseModel extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<TrainerQuizeResonse> data = null;




    public List<TrainerQuizeResonse> getData() { return data;
    }

    public void setData(List<TrainerQuizeResonse> data) {
        this.data = data;
    }
    public TrainerQuizeResonseModel(boolean mStatus, String mErrormsg) {
        super(mStatus, mErrormsg);
    }
}
