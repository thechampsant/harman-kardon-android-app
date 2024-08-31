package com.ariston.training_module.modules.dashboard.models.graph_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizDashboard {
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Value")
    @Expose
    private String value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
