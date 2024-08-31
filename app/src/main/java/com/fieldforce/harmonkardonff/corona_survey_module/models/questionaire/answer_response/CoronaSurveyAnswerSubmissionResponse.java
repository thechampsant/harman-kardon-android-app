package com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.answer_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoronaSurveyAnswerSubmissionResponse {
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
