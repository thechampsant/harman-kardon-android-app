package com.fieldforce.harmonkardonff;

import mob.field.harmonkardonff.services.WebService;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.Response;
import app.core.utils.Dialog;

public class ForgotPasswordActivity extends InnosolsActivity {

	WebService webCall = new WebService();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		findViewById(R.id.forgot_password_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				forgotPasswordButtonClicked();
			}
		});
	}
	
	@Override
	public void RegisterTableInfoForLocalDB() {}
	
	String userID = null;
	public void forgotPasswordButtonClicked()
	{
		userID = ((EditText) findViewById(R.id.forgot_password_username_edittext)).getText().toString();
		
		if(userID.isEmpty())
		{
			new Dialog(this).setTitle("Empty User ID").setMessage("User Id is Empty").show();
			return;
		}
	
		BackgroundProcess forgotPasswordBP = new BackgroundProcess(this);
		forgotPasswordBP.setProgressMessage("Reseting Password...");
		forgotPasswordBP.setbackgroundProcess(new IProcess() {
			
			@Override
			public Object underProcess() throws Exception {
				return webCall.tryForgotpassword(userID);
				
			}
			
			@Override
			public void processResponse(Object response) throws Exception {
				Response forgotPasswordResponse = (Response) response;
				
				if(forgotPasswordResponse.status.equalsIgnoreCase("true"))
				{
					ShowToastLong("Your New Password Has Been Sent To Your Registered Phone", 0);
					onBackPressed();
				}
				else
				{
					ShowToastLong(forgotPasswordResponse.errormsg, 0);
				}
				
			}
		});
		forgotPasswordBP.execute();
	}

}
