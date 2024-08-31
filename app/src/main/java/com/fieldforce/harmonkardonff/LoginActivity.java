package com.fieldforce.harmonkardonff;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fieldforce.floorhygiene.HygieneStoreListActivity;
import com.fieldforce.utility.Storage;

import app.core.base.InnosolsActivity;
import app.core.model.Response;
import linq.ArrayList;
import mob.field.gcm.GcmRegistration;
import mob.field.harmonkardonff.entitiymodels.MyInfoModel;
import mob.field.harmonkardonff.services.WebService;

@SuppressWarnings("rawtypes")
public class LoginActivity extends InnosolsActivity {

    String username = null;
    String password = null;
    ProgressBar pbr;

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private CheckBox rememberMe;
    // private boolean IsRemember=false;
    private TextView mLoginStatusMessageView;
    private Response response = new Response();
    WebService webAPI = new WebService();

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private UserLoginTask mAuthTask = null;
    private String LoginMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Show the Up button in the action bar.
        Activate();
    }

    public void Activate() {
        // SmsReceiver.LM = this;
        this.SetOnClickListenerOnButton(R.id.btn_login, new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Login(null);
            }
        });
        SetTextViewAsString(R.id.txt_version1, "Version: "
                + getCurrentVersion());
        setLayoutControl();
        setInitialForLogin();

        findViewById(R.id.login_forgot_password).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                //startActivity(new Intent(LoginActivity.this, HygieneStoreListActivity.class));
            }
        });

    }

    public void setLayoutControl() {
        Button btn_Login = (Button) this.findViewById(R.id.btn_login);
        btn_Login.requestFocus();
        mPasswordView = (EditText) this.findViewById(R.id.password);
        mUserNameView = (EditText) this.findViewById(R.id.username);
        rememberMe = GetCheckBox(R.id.cbxrme);
        mLoginFormView = this.findViewById(R.id.login_form);
        mLoginStatusView = this.findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) this
                .findViewById(R.id.login_status_message);
    }

    // private void HandleGCMReg() {
    // GcmService gcms = new GcmService(this);
    // gcms.setServerAPI(WebService.getRegistrationUrl(), username);
    // gcms.TryRegisterDevice();
    // }

    private void setInitialForLogin() {
        if (User.IsRememberMe()) {
            rememberMe.setChecked(true);
            mPasswordView.setText(User.GetPassword());
            mUserNameView.setText(User.GetUserName());
        } else {
            mPasswordView.setHint("Password");
            mUserNameView.setHint("Username");
            rememberMe.setChecked(false);
            User.RemoveLocalLogin();
        }
    }

    public void Login(View view) {
        // TODO Auto-generated method stub
        GetDataFromPage();
        if (mAuthTask != null) {
            return;
        }
        if (validate()) {
            loadMyInfoFromDb(username);
            if (this.isNetworkFoundToast())
                DO_GPRS_Communication();
            else if (this.LocalLogin()) {
                setUserLogged(true);
                gotToMain();
            }
        }

    }

    private void GetDataFromPage() {
        // TODO Auto-generated method stub
        this.LoginMessage = "";
        LoginMessage = "v5gs wp1 ";
        username = this.GetEditTextAsString(R.id.username);
        password = this.GetEditTextAsString(R.id.password);
    }

    private boolean LocalLogin() {
        // TODO Auto-generated method stub
        if (User.IsLocalLoginExist(username, password)) {
            SaveLoginPrefrence();
            // HandleGCMReg();
            return true;

        } else {
            return false;
        }
    }

    private boolean validate() {
        // TODO Auto-generated method stub
        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError("Please Enter UserName");
            mUserNameView.requestFocus();

            return false;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Please Enter Password");
            mPasswordView.requestFocus();
            return false;
        } else
            return true;

    }

    String phoneIMEI;

    private void DO_GPRS_Communication() {
        // TODO Auto-generated method stub
        /*
         * TelephonyManager mngr =
         * (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); IMEINo
         * = mngr.getDeviceId(); int type = mngr.getLine1Number();
         */
        try {

            phoneIMEI = Storage.getPhoneIMEI(this);
        } catch (Exception e) {
            Log.e("IMEI Error", e.getMessage().toString());
        }
        mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
        showProgressWait(true);
        mAuthTask = new UserLoginTask();
        mAuthTask.execute((Void) null);

    }

    private void loadMyInfoFromDb(String UserName) {
        MainActivity.Database.LoadMyInfoFromLocalDb(UserName);
    }

    private void setMyInfoToDb() {
        @SuppressWarnings("unchecked")
        ArrayList<MyInfoModel> myinfo = (ArrayList<MyInfoModel>) response.data;
        if (myinfo == null)
            return;
        if (myinfo.Count() > 0) {
            MainActivity.MyInfo = myinfo.First();
            MainActivity.MyInfo.EmployeeCode = myinfo.First().UserID;
            MainActivity.MyInfo.Storetype = myinfo.First().Storetype;
            MainActivity.MyInfo.TrainingType = myinfo.First().TrainingType;
            MainActivity.MyInfo.InsertOrUpdate();
        }
        SaveLoginPrefrence();
    }

    private void gotToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

    private void ProcesssResult(boolean status) {

        try {
            if (status) {
                setMyInfoToDb();
                User.SetLoginWithRememberMe(GetEditTextAsString(R.id.username), GetEditTextAsString(R.id.password));
                User.setUserLogged(true);
                showProgressWait(false);
                gotToMain();
                // HandleGCMReg();
            } else {
                String msg = "Login Failed\n" + response.errormsg;
                ShowToast(msg);
                SetMessage(msg);
                showProgressWait(false);

            }
        } catch (Exception ex) {
            showProgressWait(false);
            ShowToastLong(ex.getMessage(), 0);
        }
    }

    private void SaveLoginPrefrence() {

        if (rememberMe.isChecked()) {
            User.RemoveLocalLogin();
            User.SetLoginWithRememberMe(username, password);
        } else {
            User.RemoveLocalLogin();
            User.SetLocalLogin(username, password);
            User.setUserLogged(false);
        }

    }

    public void SetMessage(String _msg) {
        TextView txt = this.GetTextView(R.id.loginmsg);
        txt.setText(_msg);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgressWait(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = this.getResources().getInteger(
                    android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.INVISIBLE
                                    : View.VISIBLE);
                        }
                    });

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE
                                    : View.GONE);
                        }
                    });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);

        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {


            try {
                // Simulate network access.
                webAPI.UserName = username;
                webAPI.Password = password;
                response = webAPI.TryLogin(phoneIMEI);
                Log.v("loginResponse", response.toString());

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Void st) {
            mAuthTask = null;
            if (response.isSuccess()) {
                ProcesssResult(response.getRequestStatus());
                LocalStorage localStrObj = new LocalStorage(LoginActivity.this);
                String regId = localStrObj.getMessage(LocalStorage.REGID);
                /*if (regId == "0") {
                    new GcmRegistration(LoginActivity.this).execute();
                }*/
            } else {
                ProcesssResult(response.getRequestStatus());
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgressWait(false);
        }
    }

    @Override
    public void onBackPressed() {
/*		MainActivity.Current.finish();
		this.finish();
		this.minimizeApp();*/
        finish();
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
        // TODO Auto-generated method stub

    }

}
