package com.ariston.training_module.modules.training_module.models.trainer_quize;




import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class TrainerQuizeResonse  implements Serializable {

    @SerializedName("QuestionName")
    @Expose
    private String questionName;
    @SerializedName("AnswerTamplate")
    @Expose
    private List<AnswerTamplate> answerTamplate = null;

    public int isSelectedpos=-1;
    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public List<AnswerTamplate> getAnswerTamplate() {
        return answerTamplate;
    }

    public void setAnswerTamplate(List<AnswerTamplate> answerTamplate) {
        this.answerTamplate = answerTamplate;
    }

}