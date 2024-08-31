package com.ariston.training_module.modules.training_module.models.tr_quize_model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question implements Serializable {

    @SerializedName("QuestionId")
    @Expose
    private Integer questionId;
    @SerializedName("Questions")
    @Expose
    private String questions;
    @SerializedName("Options")
    @Expose
    private List<Option> options = null;

    public Integer getQuestionId() {
        return questionId;
    }
    public int isSelectedpos=-1;

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }


    @Override
    public String toString() {
        return options.get(isSelectedpos).getOptionId().toString();
    }
}