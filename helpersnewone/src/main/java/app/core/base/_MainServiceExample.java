package app.core.base;

import android.content.Intent;
import android.os.IBinder;

/*
 * 	T: SELF REFERENCE FOR SERVICE
 * 	E: DB REFERENCE
 */
public class _MainServiceExample extends BaseService<_MainServiceExample, _MainServiceExample> {

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
