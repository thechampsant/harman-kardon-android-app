package com.fieldforce.floorhygiene.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepakkanyan on 22/07/20 at 4:03 PM.
 */
public class ModelHeadStore {

    @SerializedName("type")
    @Expose
    public Object type;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("FileUrl")
    @Expose
    public Object fileUrl;
    @SerializedName("errormsg")
    @Expose
    public Object errormsg;
    @SerializedName("AppVersion")
    @Expose
    public Object appVersion;
    @SerializedName("data")
    @Expose
    public List<ModelStoreData> data = null;
    @SerializedName("Edata")
    @Expose
    public Object edata;
    @SerializedName("Success")
    @Expose
    public Object success;
}
