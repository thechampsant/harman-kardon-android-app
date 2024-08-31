package com.fieldforce.harmonkardonff.corona_survey_module.models.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("TileValue")
    @Expose
    private String tileValue;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTileValue() {
        return tileValue;
    }

    public void setTileValue(String tileValue) {
        this.tileValue = tileValue;
    }
}
