package com.suveyform;
public class ImageResponse {
    int QID=0;

    public ImageResponse(int QID, String imageURl) {
        this.QID = QID;
        ImageURl = imageURl;
    }

    public int getQID() {
        return QID;
    }

    public void setQID(int QID) {
        this.QID = QID;
    }

    public String getImageURl() {
        return ImageURl;
    }

    public void setImageURl(String imageURl) {
        ImageURl = imageURl;
    }

    String ImageURl="";
}
