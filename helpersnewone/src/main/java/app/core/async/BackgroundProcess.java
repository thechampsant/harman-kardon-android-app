package app.core.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import app.core.base.IFragment;
import app.core.base.InnosolsActivity;
import app.core.controls.Drawable;
import app.core.utils.GUID;

public class BackgroundProcess extends AsyncTask<Object, Object, Object> {

	private Context context;
	private IFragment fcontext;
	private ProgressDialog progressDialog = null;
	private BackgroundProcess process;

	private boolean IsError = false;
	private boolean cancellable=true;
	private IProcess iprocess = null;
	public String CancelMessage = "Operation cancalled!";
	public String SuccessMessage = "Operation ok";
	public String ProgressMessage = "loading...";
	public int progress = 0;
	private boolean ShowDialog = false;
	private boolean ShowToast = false;
	private boolean ShowCancelButton = false;
	public int DialogIcon = -1;
	private boolean ShowProgress = true;
	private boolean IsProcessingResponse = false;
	private boolean InProcess = false;
	private boolean ShowProgressTitle = true;
	private boolean ShowHorizantalProgress = false;
	private boolean OnErrorDiloag = true;
	private int above = 0;
	private int below = 0;
	private boolean IsScreenProgress = false;
	private boolean OnScreenProgress = false;
	private ProgressBar ScreenProgressBar = null;
	private int LayoutID = 0;
	private boolean IsFragment = false;
	private View ScreenView = null;

	public BackgroundProcess onErrorDiloag(boolean _showOnErrorDiloag) {
		OnErrorDiloag = _showOnErrorDiloag;
		return this;
	}

	public BackgroundProcess showMessage(boolean _ShowDialog, boolean _showToast) {
		ShowDialog = _ShowDialog;
		ShowToast = _showToast;
		return this;
	}

	public BackgroundProcess showMessage(boolean _ShowDialog) {
		ShowDialog = _ShowDialog;
		return this;
	}

	 public BackgroundProcess setProgressDialog(boolean _showCancelButton,
			boolean _ShowProgressTitle) {
		ShowCancelButton = _showCancelButton;
		ShowProgressTitle = _ShowProgressTitle;
		return this;
	}

	 
	public BackgroundProcess setProgressDialog(boolean _showCancelButton) {
		ShowCancelButton = _showCancelButton;
		return this;
	}

	   public BackgroundProcess showProgress(boolean _ShowProgress) {
		ShowProgress = _ShowProgress;
		return this;
	}

	  public BackgroundProcess showProgressType(boolean _ShowHorizantalProgress) {
		ShowHorizantalProgress = _ShowHorizantalProgress;
		ShowProgress = true;
		return this;
	}

	   public BackgroundProcess setProgress(int _progress) {
		if (ShowHorizantalProgress)
			progressDialog.setProgress(_progress);
		return this;
	}

	/**
	 * 
	 * @param ConatinerID
	 *            this is id of the layout that contain the resource id.Must be
	 *            a relative layout
	 * @param resource
	 *            this is id of control after which you want to show progress
	 * @param showSmallProgress
	 *            show small progress bar if it is true default is small
	 * @return
	 */
	
	
	   public BackgroundProcess setScreenProgress(int ConatinerID, int above,
			int below, boolean IsHorizantalProgress) {
		InitScreenProgerss(ConatinerID, above, below, IsHorizantalProgress);
		return this;
	}

	public BackgroundProcess setScreenProgress(int ConatinerID, int above,
			int below) {
		InitScreenProgerss(ConatinerID, above, below, false);
		return this;
	}

	public BackgroundProcess setHorizantalScreenProgress() {
		if (IsFragment)
			setScreenProgress(this.fcontext.getCurrentView(),true);
		else if (context instanceof InnosolsActivity) {
			InnosolsActivity act = (InnosolsActivity) context;
			InitScreenProgerss(act.getCurrentView().getId(), 0, 0, true);
		}
		return this;
	}

	public BackgroundProcess setScreenProgress(int ConatinerID,
			boolean IsHorizantalProgress) {
		InitScreenProgerss(ConatinerID, 0, 0, IsHorizantalProgress);
		return this;
	}

	public BackgroundProcess setScreenProgress(int ConatinerID, int below,
			boolean IsHorizantalProgress) {
		InitScreenProgerss(ConatinerID, 0, below, IsHorizantalProgress);
		return this;
	}

	public BackgroundProcess setScreenProgress(int ConatinerID, int below) {
		InitScreenProgerss(ConatinerID, 0, below, false);
		return this;
	}

	public BackgroundProcess setScreenProgress(View Conatiner,
			boolean IsHorizantalProgress) {
		this.IsFragment = true;
		this.ScreenView = Conatiner;
		InitScreenProgerss(Conatiner.getId(), 0, 0, IsHorizantalProgress);
		return this;
	}

	public BackgroundProcess setScreenProgress(View Conatiner, View above,
			View below, boolean IsHorizantalProgress) {
		this.IsFragment = true;
		this.ScreenView = Conatiner;
		InitScreenProgerss(Conatiner.getId(), above.getId(), below.getId(),
				IsHorizantalProgress);
		return this;
	}

	public BackgroundProcess setScreenProgress(View Conatiner, View below,
			boolean showSmallProgress) {
		this.IsFragment = true;
		this.ScreenView = Conatiner;
		InitScreenProgerss(Conatiner.getId(), 0, below.getId(),
				showSmallProgress);
		return this;
	}

	public BackgroundProcess setScreenProgress(View Conatiner, View below) {
		this.IsFragment = true;
		this.ScreenView = Conatiner;
		InitScreenProgerss(Conatiner.getId(), 0, below.getId(), false);
		return this;
	}

	private void InitScreenProgerss(int ConatinerID, int above, int below,
			boolean IsHorizantalProgress) {
		this.above = above;
		this.below = below;
		this.IsScreenProgress = true;
		this.ShowHorizantalProgress = IsHorizantalProgress;
		this.LayoutID = ConatinerID;
	}

	public BackgroundProcess setOnScreenProgress() {
		this.OnScreenProgress = true;
		return this;
	}

	public BackgroundProcess setbackgroundProcess(IProcess _IProcess) {
		iprocess = _IProcess;
		return this;
	}

	public BackgroundProcess(Activity _context, boolean _ShowProgress) {
		Init(_context, _ShowProgress);
	}
	
	public BackgroundProcess(Context _context) {
		this.context = _context;
		Init(context, true);
	}
	public BackgroundProcess(Context _context,boolean _ShowProgress) {
		this.context = _context;
		ShowProgress = _ShowProgress;
		Init(context, ShowProgress);
	}
	
	public BackgroundProcess(IFragment _context) {
		this.IsFragment = true;
		this.fcontext = _context;
		Init(_context.context, true);
	}

	public BackgroundProcess(IFragment _context, boolean _ShowProgress) {
		this.IsFragment = true;
		this.fcontext = _context;
		Init(_context.context, _ShowProgress);
	}

	public BackgroundProcess(Activity _context) {
		Init(_context, true);
	}

	private void Init(Context _context, boolean _ShowProgress) {
		context = _context;
		process = this;
		ShowProgress = _ShowProgress;
	}

	     private void setScreenProgress() {
		RelativeLayout.LayoutParams params = null;
		if (ShowHorizantalProgress) {
			ScreenProgressBar = new ProgressBar(context, null,
					android.R.attr.progressBarStyleHorizontal);
			ScreenProgressBar.setIndeterminate(true);
			params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 10, 0, 0);
			ScreenProgressBar.getProgressDrawable().setColorFilter(
					new Drawable().red(), Mode.SCREEN);
			
		} else {
			ScreenProgressBar = new ProgressBar(context, null,
					android.R.attr.progressBarStyleLarge);
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		ScreenProgressBar.setPadding(0, 15, 0, 10);
		ScreenProgressBar.setId(GUID.getId());

		if (above != 0) {
			params.addRule(RelativeLayout.ABOVE, above);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		if (below != 0) {
			params.addRule(RelativeLayout.BELOW, below);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		if (above == 0 && below == 0) {
			params.setMargins(0, 0, 0, 5);
			ScreenProgressBar.setPadding(0, 0, 0, 0);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		}

		ScreenProgressBar.setLayoutParams(params);
		ScreenProgressBar.setVisibility(View.VISIBLE);


		if (!IsFragment)
			ScreenView = ((Activity)context).findViewById(LayoutID);
		ViewGroup vgroup = ((ViewGroup) ScreenView);
		vgroup.addView(ScreenProgressBar);

	}
	     
	     
	     

	    private void getOnScreenProgress() {
		RelativeLayout layout = new RelativeLayout(this.context);
		ScreenProgressBar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleLarge);
		ScreenProgressBar.setIndeterminate(true);
		ScreenProgressBar.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				100, 100);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(ScreenProgressBar, params);
		((Activity)context).setContentView(layout);
	}

	     @SuppressWarnings("deprecation")
	     private void setProgressDiloag() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(ProgressMessage);
		progressDialog.setCancelable(cancellable);

		if (ShowProgressTitle)
			progressDialog.setTitle("Please Wait!!");
		if (ShowCancelButton)
			progressDialog.setButton("Cancel", ocl);
		if (ShowHorizantalProgress) {
			progressDialog.setProgress(1);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}

	}

	public ProgressBar getScreenProgress() {
		return this.ScreenProgressBar;
	}

	public BackgroundProcess setProgressTitle(String title) {
		progressDialog.setTitle(title);
		return this;
	}

	public BackgroundProcess setProgressMessage(String message) {
		ProgressMessage = message;
		return this;
	}

	/**
	 * Setting the cancellablity of the progress dailog
	 * @param cancellable true - Progress Dailog will bew cancellable  || false - progress Dailog will not be cancellable
	 * @return
	 */
	public BackgroundProcess setProgressDailogCancellable(boolean cancellable) {
		this.cancellable = cancellable;
		return this;
	}


	public BackgroundProcess setProgress(String progresstitle,
			String progressmessage) {
		ProgressMessage = progressmessage;
		progressDialog.setTitle(progresstitle);
		return this;
	}

	private void setloadingstatus() {
		if(IsFragment)
			 this.fcontext.setLoading(false);
		else if (context instanceof InnosolsActivity) {
			InnosolsActivity act = (InnosolsActivity) context;
			act.setLoading(false);
		}
	}

	private void dismiss() {
		if (IsScreenProgress) {
			setloadingstatus();
			ScreenProgressBar.setVisibility(View.GONE);
			KillProcess();
			return;
		}
		if (OnScreenProgress) {
			((Activity)context).finish();
			KillProcess();
			return;
		}
		if (progressDialog == null)
			return;
		progressDialog.dismiss();

	}

	private void KillProcess() {
		if (process == null)
			return;
		process.cancel(true);
		process = null;
		iprocess = null;
	}

	public ProgressDialog getProgessDialog() {
		return this.progressDialog;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		try {
			// process=(BackgroundProcess)arg0;
			InProcess = true;
			if (iprocess == null)
				return null;
			// progressDialog.setProgress(3);
			return iprocess.underProcess();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			IsError = true;
			InProcess = false;
			return e.toString();
		}
	}

	@Override
	protected void onPostExecute(Object response) {
		try {
			IsProcessingResponse = true;
			InProcess = false;
			if (IsError) {
				ProcessResult("Error", response.toString());
			} else if (iprocess != null) {
				iprocess.processResponse(response);
				// progressDialog.setProgress(100);
				ProcessResult("Message", SuccessMessage);
			} else {
				IsError = true;
				ProcessResult("Message", "No background work to do!");
			}
		} catch (Exception ex) {
			IsError = true;
			ProcessResult("Error", ex.toString());
		}

	}

	@Override
	protected void onPreExecute() {
		if (ShowProgress) {
			showProgress();
		}
	}

	@Override
	protected void onCancelled() {

		ProcessResult("Message", CancelMessage);
	}

	public boolean showProgress() {
		if (!IsProcessingResponse) {
			if (IsScreenProgress) {
				setScreenProgress();
			} else if (OnScreenProgress) {
				getOnScreenProgress();
			} else {
				setProgressDiloag();
				progressDialog.show();
			}
		}
		return InProcess;
	}

	private void ProcessResult(String title, String Message) {
		try {
			dismiss();
			ShowDialog(title, Message);
			InProcess = false;
		} catch (Exception ex) {
			InProcess = false;
			ex.printStackTrace();
		}
	}

	private void ShowToast(String Message) {

		Toast toast = Toast.makeText(context, Message, Toast.LENGTH_LONG);
		toast.show();
	}

	private void ShowDialog(String title, String Message) {
		// TODO Auto-generated method stub
		// Ask the user if they want to quit
		if (!OnErrorDiloag)
			return;
		if (ShowToast && !ShowDialog && !IsError)
			ShowToast(title + " :" + Message);
		if (!ShowDialog && !IsError)
			return;
		Builder dialog = new AlertDialog.Builder(context).setTitle(title)
				.setMessage(Message)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//
					}

				});
		if (DialogIcon != -1)
			
			dialog.setIcon(DialogIcon);
		if (IsError)
			
			dialog.setIcon(android.R.drawable.ic_dialog_alert);
		else
			dialog.setIcon(android.R.drawable.ic_dialog_email);

		// dialog show..
		dialog.show();
	}

	// --------listeners---------------//
	OnClickListener ocl = new OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			IsError = true;
			dismiss();
		}
	};

}
