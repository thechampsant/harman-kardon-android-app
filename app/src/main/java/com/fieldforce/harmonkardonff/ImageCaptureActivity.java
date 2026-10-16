package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import mob.field.harmonkardonff.db.Database;
import app.core.entitymodels.ImageInfo;
import app.core.image.capture.ImageActivity;


public class ImageCaptureActivity extends ImageActivity {
	@Override
	public String setImageDirectoryName() {
		return "AppImages";
	}

	@Override
	public String setImageUploadUrl() {
		return "http://harman.infield.co.in/UploadFileHandler.ashx?";
	}

	@SuppressWarnings("static-access")
	@Override
	public ArrayList<ImageInfo> setImageData() {
		if (MainActivity.Database == null && MainActivity.Current != null) {
			MainActivity.Database = new Database(MainActivity.Current);
		}
		if (MainActivity.Database == null || MainActivity.Current == null) {
			return new ArrayList<ImageInfo>();
		}
		return MainActivity.Database.LoadAllPendingImageFromDb();
	}

}
