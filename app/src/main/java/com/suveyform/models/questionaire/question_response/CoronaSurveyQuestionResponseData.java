package com.suveyform.models.questionaire.question_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoronaSurveyQuestionResponseData {
    @SerializedName("Question")
    @Expose
    private String question;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @SerializedName("Image")
    @Expose
    private String Image;
    @SerializedName("QID")
    @Expose
    private String qID;
    @SerializedName("Option")
    @Expose
    private String option;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("IsMandatory")
    @Expose
    private String isMandatory;
    public String getSurveyType() {
        return SurveyType;
    }

    public void setSurveyType(String surveyType) {
        SurveyType = surveyType;
    }

    @SerializedName("SurveyType")
    @Expose
    private String SurveyType;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQID() {
        return qID;
    }

    public void setQID(String qID) {
        this.qID = qID;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

}
