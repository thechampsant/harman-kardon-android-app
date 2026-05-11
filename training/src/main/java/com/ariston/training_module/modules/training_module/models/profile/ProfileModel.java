package com.ariston.training_module.modules.training_module.models.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModel {
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Education")
    @Expose
    private Object education;
    @SerializedName("Counter")
    @Expose
    private String counter;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("AssignedOn")
    @Expose
    private String assignedOn;
    @SerializedName("Helpline")
    @Expose
    private String helpline;

    @SerializedName("ProfilePicUrl")
    @Expose
    private String profilePicUrl;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getEducation() {
        return education;
    }

    public void setEducation(Object education) {
        this.education = education;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAssignedOn() {
        return assignedOn;
    }

    public void setAssignedOn(String assignedOn) {
        this.assignedOn = assignedOn;
    }

    public String getHelpline() {
        return helpline;
    }

    public void setHelpline(String helpline) {
        this.helpline = helpline;
    }
}
