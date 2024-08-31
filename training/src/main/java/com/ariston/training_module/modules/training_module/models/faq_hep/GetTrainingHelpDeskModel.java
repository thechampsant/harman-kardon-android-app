package com.ariston.training_module.modules.training_module.models.faq_hep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTrainingHelpDeskModel  {
    @SerializedName("ContactNo")
    @Expose
    private String contactNo;
    @SerializedName("EmailId")
    @Expose
    private String emailId;


    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
