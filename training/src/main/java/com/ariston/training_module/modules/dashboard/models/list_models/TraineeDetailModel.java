package com.ariston.training_module.modules.dashboard.models.list_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TraineeDetailModel {

    @SerializedName("TraineeId")
    @Expose
    private String traineeId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("V5Id")
    @Expose
    private String v5Id;
    @SerializedName("QuizAttempted")
    @Expose
    private String quizAttempted;
    @SerializedName("Education")
    @Expose
    private String education;
    @SerializedName("QuizStatus")
    @Expose
    private String quizStatus;
    @SerializedName("Percentage")
    @Expose
    private String percentage;

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getV5Id() {
        return v5Id;
    }

    public void setV5Id(String v5Id) {
        this.v5Id = v5Id;
    }

    public String getQuizAttempted() {
        return quizAttempted;
    }

    public void setQuizAttempted(String quizAttempted) {
        this.quizAttempted = quizAttempted;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getQuizStatus() {
        return quizStatus;
    }

    public void setQuizStatus(String quizStatus) {
        this.quizStatus = quizStatus;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

}
