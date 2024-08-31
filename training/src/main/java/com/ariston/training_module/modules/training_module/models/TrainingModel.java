package com.ariston.training_module.modules.training_module.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TrainingModel implements Parcelable, Comparable<TrainingModel> {

    @SerializedName("TrainingId")
    @Expose
    private String trainingId;
    @SerializedName("TrainingName")
    @Expose
    private String trainingName;
    //    @SerializedName("TrainingDate")
//    @Expose
//    private String trainingDate;
    @SerializedName("TrainingType")
    @Expose
    private String trainingType;
    @SerializedName("TrainerName")
    @Expose
    private String trainerName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("TrainingStatus")
    private String trainingStatus;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("Duration")
    private String duration;


    private Date date;


    protected TrainingModel(Parcel in) {
        trainingId = in.readString();
        trainingName = in.readString();
        trainingType = in.readString();
        trainerName = in.readString();
        description = in.readString();
        trainingStatus = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        duration = in.readString();
    }

    public static final Creator<TrainingModel> CREATOR = new Creator<TrainingModel>() {
        @Override
        public TrainingModel createFromParcel(Parcel in) {
            return new TrainingModel(in);
        }

        @Override
        public TrainingModel[] newArray(int size) {
            return new TrainingModel[size];
        }
    };

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TrainingModel() {

    }



    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

//    public String getTrainingDate() {
//        return trainingDate;
//    }
//
//    public void setTrainingDate(String trainingDate) {
//        this.trainingDate = trainingDate;
//    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TrainingModel{" +
                "trainingId='" + trainingId + '\'' +
                ", trainingName='" + trainingName + '\'' +
//                ", trainingDate='" + trainingDate + '\'' +
                ", trainingType='" + trainingType + '\'' +
                ", trainerName='" + trainerName + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }


    public void setTrainingStatus(String trainingStatus) {
        this.trainingStatus = trainingStatus;
    }

    public String getTrainingStatus() {
        return trainingStatus;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trainingId);
        dest.writeString(trainingName);
        dest.writeString(trainingType);
        dest.writeString(trainerName);
        dest.writeString(description);
        dest.writeString(trainingStatus);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(duration);
    }

    @Override
    public int compareTo(TrainingModel o) {
        return 0;
    }
}
