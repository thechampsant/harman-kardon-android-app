
package com.ariston.training_module.modules.training_module.models.tr_mat_model;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.google.gson.annotations.Expose;

import java.util.List;


public class TrainingMaterialResponse extends BaseResponse {
    @Expose
    private List<TrMatItem> data;

    public TrainingMaterialResponse(boolean mStatus, String mErrormsg) {
        super(mStatus, mErrormsg);
    }


    public List<TrMatItem> getData() {
        return data;
    }

    public void setData(List<TrMatItem> data) {
        this.data = data;
    }
}
