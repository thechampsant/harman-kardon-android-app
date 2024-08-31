
package com.ariston.training_module.modules.training_module.models.tr_desc_model;

import com.google.gson.annotations.SerializedName;


public class TrainingDescItem {

    @SerializedName("IconPath")
    private String mIconPath;
    @SerializedName("LinkName")
    private String mLinkName;
    @SerializedName("LoginId")
    private String mLoginId;
    @SerializedName("TypeName")
    private String mTypeName;

    public String getIconPath() {
        return mIconPath;
    }

    public void setIconPath(String iconPath) {
        mIconPath = iconPath;
    }

    public String getLinkName() {
        return mLinkName;
    }

    public void setLinkName(String linkName) {
        mLinkName = linkName;
    }

    public String getLoginId() {
        return mLoginId;
    }

    public void setLoginId(String loginId) {
        mLoginId = loginId;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String typeName) {
        mTypeName = typeName;
    }

}
