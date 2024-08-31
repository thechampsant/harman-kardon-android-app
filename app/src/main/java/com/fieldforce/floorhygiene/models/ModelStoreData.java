package com.fieldforce.floorhygiene.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepakkanyan on 22/07/20 at 4:04 PM.
 */
public class ModelStoreData {
    @SerializedName("Rating")
    private String rating;
    @SerializedName("DateFor")
    @Expose
    public String dateFor;
    @SerializedName("StoreName")
    @Expose
    public String storeName;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("FilePath")
    @Expose
    public List<FilePath> filePath = null;

    public String getRating() {
        return rating;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
