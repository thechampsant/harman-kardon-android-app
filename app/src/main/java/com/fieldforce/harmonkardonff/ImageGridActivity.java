package com.fieldforce.harmonkardonff;

import linq.ArrayList;
import app.core.entitymodels.ImageInfo;
import app.core.image.slider.GridImageActivity;


public class ImageGridActivity extends GridImageActivity {




	@Override
	public String setImageUploadUrl() {
		//return "http://harmankardon.infield.co.in/UploadFileHandler.ashx?";
		return "http://harman.infield.co.in/UploadFileHandler.ashx?";
	}

	@Override
	public ArrayList<ImageInfo> setImageData() {
		return MainActivity.Database.LoadAllPendingImageFromDb();
	}

	@Override
	public void RegisterTableInfoForLocalDB() {


	}

}
