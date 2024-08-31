package app.core.async;

import android.os.AsyncTask;

public class Background extends AsyncTask<Object, Object, Object> {

	private IProcess iprocess = null;
	public boolean IsError = false;
	public String Error = null;

	   @Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try {
			if (iprocess == null)
				return null;
			return iprocess.underProcess();
		} catch (Exception e) {
			e.printStackTrace();
			Error = e.getMessage();
			IsError = true;
			return null;
		}
	}

	   
	    @Override
	   protected void onPostExecute(Object response) {
		try {
			if (iprocess != null) {
				
				iprocess.processResponse(response);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			IsError = true;
			Error = ex.getMessage();
		}

	}

	 public Background setbackgroundProcess(IProcess _IProcess) {
		iprocess = _IProcess;
		return this;
	}

}
