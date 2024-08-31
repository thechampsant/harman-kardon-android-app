
package com.ariston.training_module.modules.training_module.models.tr_mat_model;

import com.google.gson.annotations.SerializedName;


public class TrMatItem {

    @SerializedName("FilePath")
    private String filePath;
    @SerializedName("FileName")
    private String fileName;
    @SerializedName("FileType")
    private String fileType;
    @SerializedName("MaterialId")
    private Long materialId;
    @SerializedName("TrainingId")
    private Long trainingId;

    public String getIsSeen() {
        return IsSeen;
    }

    public void setIsSeen(String isSeen) {
        IsSeen = isSeen;
    }

    @SerializedName("IsSeen")
    private String IsSeen;
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(Long trainingId) {
        this.trainingId = trainingId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
