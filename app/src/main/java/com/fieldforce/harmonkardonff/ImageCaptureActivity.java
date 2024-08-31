package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import app.core.entitymodels.ImageInfo;
import app.core.image.capture.ImageActivity;


public class ImageCaptureActivity extends ImageActivity {
	@Override
	public String setImageDirectoryName() {
		return "AppImages";
	}

	@Override
	public String setImageUploadUrl() {
		//return "http://harmankardon.infield.co.in/UploadFileHandler.ashx?";
		return "http://harman.infield.co.in/UploadFileHandler.ashx?";
	}

	@SuppressWarnings("static-access")
	@Override
	public ArrayList<ImageInfo> setImageData() {
		return MainActivity.Current.Database.LoadAllPendingImageFromDb();
	}

}
