package app.core.image.slider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import linq.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import app.core.action.image.IWebClient;
import app.core.adapter.GenricAdapter;
import app.core.adapter.ICustomAdapter;
import app.core.base.InnosolsActivity;
import app.core.entitymodels.ImageInfo;
import app.core.utils.IntentFactory;

import com.example.com.test.image.R;

public abstract class GridImageActivity extends InnosolsActivity implements IWebClient {

	private GenricAdapter<ImageInfo> adaptor;
	private GridView gridView;
	private int columnWidth;
	private ArrayList<ImageInfo> Images = new ArrayList<ImageInfo>();
	public abstract ArrayList<ImageInfo> setImageData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_grid_view);
			gridView = (GridView)findViewById(R.id.grid_view);
			Images = setImageData();
			InitilizeGridLayout();
			setGridList();
		} catch (Exception ex) {
			ShowToastLong("Error: " + ex.getMessage(), 0);
		}
	}

	private boolean checkandExit() {
		if (!isImagesFound()) {
			ShowToastLong("No image to display!", 0);
			this.finish();
			return false;
		} else
			return true;
	}

	@SuppressWarnings("unchecked")
	private void setGridList() {

		if (!checkandExit())
			return;
		adaptor = new GenricAdapter<ImageInfo>(this).setData(Images);
		adaptor.setCustomAdapter(new ICustomAdapter<ImageInfo>() {

			@Override
			public View setItemView(ImageInfo item, int index) {
				ImageView imageView = new ImageView(GridImageActivity.this);
				Bitmap image = decodeFile(Images.get(index).LocalUrl,
						columnWidth, columnWidth);

				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(new GridView.LayoutParams(
						columnWidth, columnWidth));
				imageView.setImageBitmap(image);
				imageView.setOnClickListener(new OnImageClickListener(index));

				return imageView;
			}
		});
		
		gridView.setAdapter(adaptor);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

			if (!checkandExit())
				return;
			adaptor.setData(Images);
			gridView.setAdapter(adaptor);
		}
	}

	private boolean isImagesFound() {
		if (this.Images == null)
			return false;
		else if (this.Images.Count() < 1)
			return false;
		else
			return true;
	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				AppConstant.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

		gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	class OnImageClickListener implements OnClickListener {

		int _postion;

		// constructor
		public OnImageClickListener(int position) {
			this._postion = position;
		}

		@Override
		public void onClick(View v) {
			// on selecting grid view image
			// launch full screen activity
			Intent i = new Intent(GridImageActivity.this,FullScreenImageActivity.class);
			i.putExtra("position", _postion);
			i.putExtra("WEB_UPLOAD_URL",setImageUploadUrl());
			IntentFactory.putDataArrayList("IMAGES_APP_VIEW_ACTION",setImageData());
			GridImageActivity.this.startActivityForResult(i, 1);
		}

	}

	@SuppressWarnings("deprecation")
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}

}
