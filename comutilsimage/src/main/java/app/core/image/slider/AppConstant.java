package app.core.image.slider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AppConstant {

//	// Number of columns of Grid View
	public static final int NUM_OF_COLUMNS = 3;
//
//	// Gridview image padding
	public static final int GRID_PADDING = 8; // in dp
//
//	// SD card image directory
	public static final String PHOTO_ALBUM = "AppPhoto";
//
//	// supported file formats
	public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
			"png");

	//public static final String PDF_PREFIX="https://docs.google.com/viewer?url=";
	public static final String PDF_PREFIX="https://docs.google.com/viewer?embedded=true&url=";
	public static final int PERM_REQ_CODE = 1001;
	public static final String ENTER_STOCK_TASK ="Enter Stock";
	public static final String UNABLE_TO_RESOLVE_HOST ="Unable to resolve host: No address associated with hostname";
	public static final String SOFTWARE_ABORT ="Software caused connection abort";

	public static String GetCurrentDateInString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String MyDate = dateFormat.format(date);
		return MyDate;
	}
}
