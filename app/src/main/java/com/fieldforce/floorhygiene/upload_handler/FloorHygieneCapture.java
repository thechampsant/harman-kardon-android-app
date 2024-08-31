package com.fieldforce.floorhygiene.upload_handler;

import com.fieldforce.harmonkardonff.MainActivity;

import app.core.entitymodels.ImageInfo;
import app.core.image.capture.ImageActivity;
import linq.ArrayList;

public class FloorHygieneCapture extends ImageActivity {
    @Override
    public String setImageDirectoryName() {
        return "AppImages";
    }

    @Override
    public String setImageUploadUrl() {

        return "http://harman.infield.co.in/FileUploader/ISD/Harman_Kardon/FloorHygieneHandler.ashx?";
    }

    @SuppressWarnings("static-access")
    @Override
    public ArrayList<ImageInfo> setImageData() {
        return MainActivity.Current.Database.LoadAllPendingImageFromDb();
    }
}
