package com.ariston.training_module.modules.training_module.models.trainer_quize;


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnswerTamplate  implements Serializable {

    @SerializedName("Answer")
    @Expose
    private String answer;
    @SerializedName("Value")
    @Expose
    private String value;


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



}
