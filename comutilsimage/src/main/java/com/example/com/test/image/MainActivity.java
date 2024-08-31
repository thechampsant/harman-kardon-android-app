package com.example.com.test.image;

import linq.ArrayList;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import app.core.base.InnosolsActivity;
import app.core.entitymodels.ImageInfo;

public class MainActivity extends InnosolsActivity {
	
	
	  
 public static ArrayList<ImageInfo> PendingImages = new ArrayList<ImageInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// this.EnableLocalDatabase("MyyDbb02",05);
		// setOnClick();
		// LoadPendingImage();

	}

	      public void LoadPendingImage() {

		PendingImages = db.FetchAllData(new ImageInfo());
	}

	// private void setOnClick()
	// {
	// this.SetOnClickListenerOnButton(R.id.btn_go_To_Image,new
	// OnClickListener(){
	//
	// @Override
	// public void onClick(View v) {
	// startCam();
	// }});
	// this.SetOnClickListenerOnButton(R.id.btn_img_slider,new
	// OnClickListener(){
	//
	// @Override
	// public void onClick(View v) {
	// viewImages();
	// }});
	// }

	// private void viewImages()
	// {
	// Intent i= new Intent(MainActivity.this,ImageSlider.class);
	// i.putExtra("GUID","234adasd234");
	// i.putExtra("DocType","DAT");
	// i.putExtra("UserName","Vipin");
	// MainActivity.this.startActivityForResult(i,1);
	// }
	// private void startCam()
	// {
	// Intent i= new Intent(this,ImageCaptureActivity.class);
	// i.putExtra("GUID","234adasd234");
	// i.putExtra("DocType","DAT");
	// i.putExtra("UserName","Vipin");
	// this.startActivityForResult(i,1);
	// }

	  @Override
	      public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.main, menu);
		     return true;
		 
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		this.RegisterTableForDataEntity(new ImageInfo());
	}

}
