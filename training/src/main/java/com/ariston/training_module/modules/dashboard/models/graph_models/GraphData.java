package com.ariston.training_module.modules.dashboard.models.graph_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GraphData {
    @SerializedName("QuizDashboard")
    @Expose
    private List<QuizDashboard> quizDashboard = null;
    @SerializedName("QuizAttempted")
    @Expose
    private String quizAttempted;
    @SerializedName("NotAttempted")
    @Expose
    private String notAttempted;
    @SerializedName("Passed")
    @Expose
    private String passed;
    @SerializedName("Failed")
    @Expose
    private String failed;

    public List<QuizDashboard> getQuizDashboard() {
        return quizDashboard;
    }

    public void setQuizDashboard(List<QuizDashboard> quizDashboard) {
        this.quizDashboard = quizDashboard;
    }

    public String getQuizAttempted() {
        return quizAttempted;
    }

    public void setQuizAttempted(String quizAttempted) {
        this.quizAttempted = quizAttempted;
    }

    public String getNotAttempted() {
        return notAttempted;
    }

    public void setNotAttempted(String notAttempted) {
        this.notAttempted = notAttempted;
    }

    public String getPassed() {
        return passed;
    }

    public void setPassed(String passed) {
        this.passed = passed;
    }

    public String getFailed() {
        return failed;
    }

    public void setFailed(String failed) {
        this.failed = failed;
    }
}
