package app.core.services;

import android.util.Log;
import app.core.base.BaseService;
import app.core.base.InnosolsActivity;

public class LoginProvider {

	private String RememberMeKey = "RememberMe";
	private String UserNameKey = "UserName";
	private String PasswordKey = "Password";
	private String LoginKey = "LOGIN_USER_AUTH_KEY";
	private InnosolsActivity context;
	private BaseService service;

	public LoginProvider(InnosolsActivity context) {
		this.context = context;
	}

	public LoginProvider(BaseService service) {
		this.service = service;
	}

	public boolean CanDoLocalLogin() {
		if (!context.isNetworkAvailable()) {
			if (IsRememberMe())
				return true;
			else
				return false;
		} else
			return false;
	}

	public boolean IsRememberMe() {
		if (context.getSharedPreference(RememberMeKey, "false") == "false")
			return false;
		else
			return true;
	}

	public boolean IsLocalLoginExist(String UserName, String Password) {
		if (CanDoLocalLogin()) {
			if (UserName.equalsIgnoreCase(GetUserName())
					&& Password.equalsIgnoreCase(GetPassword())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	public String GetUserName() {
		if (context != null)
		{
			Log.i("loginprovider", "GetUsername from context");
			return context.getSharedPreference(UserNameKey, "NA");
		}
		else if (service != null){
			Log.i("loginprovider", "GetUsername from service");
			return service.getPreference(UserNameKey, "NA");
		}
		return "NA";
	}

	/**
	 * Returns Stored password
	 * @return password
	 */
	public String GetPassword() {
		if (context != null)
			return context.getSharedPreference(PasswordKey, "NA");
		else
			return service.getPreference(PasswordKey, "NA");
	}

	public void SetRememberMe() {
		if (context != null)
			context.SavePreferences(RememberMeKey, "true");
		else
			service.SavePreferences(RememberMeKey, "true");
	}

	public void SetLocalLogin(String UserName, String Password) {

		if (context != null) {
			context.SavePreferences(UserNameKey, UserName);
			context.SavePreferences(PasswordKey, Password);
		} else {
			service.SavePreferences(UserNameKey, UserName);
			service.SavePreferences(PasswordKey, Password);
		}
	}

	public void SetLoginWithRememberMe(String UserName, String Password) {
		SetLocalLogin(UserName, Password);
		SetRememberMe();
	}

	public void ChangePassword(String Password) {
		if (context != null) {
			context.RemovePrefrences(PasswordKey);
			context.SavePreferences(this.PasswordKey, Password);
		} else {
			service.RemovePrefrences(PasswordKey);
			service.SavePreferences(this.PasswordKey, Password);
		}
	}

	public void RemoveLocalLogin() {
		if (context != null) {
			context.RemovePrefrences(UserNameKey);
			context.RemovePrefrences(PasswordKey);
			context.RemovePrefrences(RememberMeKey);
		} else {
			service.RemovePrefrences(UserNameKey);
			service.RemovePrefrences(PasswordKey);
			service.RemovePrefrences(RememberMeKey);
		}
	}

	public void setUserLogged(String UserName, boolean islogged) {
		context.RemovePrefrences(UserName);
		context.RemovePrefrences(LoginKey);
		context.SavePreferences(LoginKey, String.valueOf(islogged));
	}

	public void setUserLogged(boolean islogged) {
		context.SavePreferences(LoginKey, String.valueOf(islogged));
	}

	public boolean isUserLoggedIn(boolean haveuser) {
		return Boolean.parseBoolean(context.getSharedPreference(LoginKey,
				"false"));
	}

	public boolean isUserLoggedIn() {

		return Boolean.parseBoolean(context.getSharedPreference(LoginKey,
				"false"));
	}

}
