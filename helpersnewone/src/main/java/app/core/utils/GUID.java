
package app.core.utils;

import java.util.Random;

import android.util.Log;
public class GUID {

	public static Random random = null;
	static int ID=1111;
		
	private static void init() {
		if (random == null) {
			random = new Random();
		}
	}
	public static int getId() {
		init();
		int id = random.nextInt();
		if (id<0) {
			//getId();
			return getId();
		} else {
			Log.i("GUID pack:",""+id);
			return id;
		}

	}
//	public static int getId() {
//
//		int id= ID+1;
//		Log.i("GUID pack:",""+id);
//		return id;
//
//	}
}
