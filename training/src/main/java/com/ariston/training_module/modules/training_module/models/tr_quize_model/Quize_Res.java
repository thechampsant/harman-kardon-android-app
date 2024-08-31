package com.ariston.training_module.modules.training_module.models.tr_quize_model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quize_Res implements Serializable {

    @SerializedName("TrainingId")
    @Expose
    private Integer trainingId;
    @SerializedName("QuizId")
    @Expose
    private Integer quizId;
    @SerializedName("QuizName")
    @Expose
    private String quizName;
    @SerializedName("Questions")
    @Expose
    private List<Question> questions = null;

    public Integer getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}