package com.ariston.training_module.modules.training_module.models.faq_hep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqresponseModel {
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Answer")
    @Expose
    private String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
