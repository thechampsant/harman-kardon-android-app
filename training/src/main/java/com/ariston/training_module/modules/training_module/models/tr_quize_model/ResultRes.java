package com.ariston.training_module.modules.training_module.models.tr_quize_model;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultRes {

    @SerializedName("Attempted")
    @Expose
    private Integer attempted;
    @SerializedName("Correct")
    @Expose
    private Integer correct;
    @SerializedName("Wrong")
    @Expose
    private Integer wrong;
    @SerializedName("Percentage")
    @Expose
    private String percentage;

    public List<String> getIncorrectQuestion() {
        return incorrectQuestion;
    }

    public void setIncorrectQuestion(List<String> incorrectQuestion) {
        this.incorrectQuestion = incorrectQuestion;
    }

    @SerializedName("IncorrectQuestion")
    @Expose
    private List<String> incorrectQuestion = null;

    public Integer getAttempted() {
        return attempted;
    }

    public void setAttempted(Integer attempted) {
        this.attempted = attempted;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getWrong() {
        return wrong;
    }

    public void setWrong(Integer wrong) {
        this.wrong = wrong;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

}