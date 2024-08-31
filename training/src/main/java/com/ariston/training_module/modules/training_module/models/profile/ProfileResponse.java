package com.ariston.training_module.modules.training_module.models.profile;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileResponse extends BaseResponse {
    public ProfileResponse(boolean mStatus, String mErrormsg) {
        super(mStatus, mErrormsg);
    }


        @SerializedName("data")
        @Expose
        private List<ProfileModel> data = null;




        public List<ProfileModel> getData() {
            return data;
        }

        public void setData(List<ProfileModel> data) {
            this.data = data;
        }




}
