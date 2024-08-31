package com.fieldforce.floorhygiene.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilePath {

    @SerializedName("FilePath")
    @Expose
    public String filePath;
    @SerializedName("TypeName")
    @Expose
    public String typeName;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}