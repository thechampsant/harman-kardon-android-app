package com.fieldforce.asyntask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.APIService.DropDownModel;
import com.APIService.MTDSalesValueModel;
import com.APIService.MainService;
import com.APIService.QueryModel;
import com.APIService.SessionModel;
import com.fieldforce.harmonkardonff.Comptition.CompitionModel;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.networkconnection.Parser;

import app.core.model.Response;
import app.core.server.Encode;
import app.core.server.Server;
import app.core.services.LoginProvider;
import mob.field.harmonkardonff.entitiymodels.ChangePasswordModel;
import mob.field.harmonkardonff.entitiymodels.CheckoutModel;
import mob.field.harmonkardonff.entitiymodels.Complain;
import mob.field.harmonkardonff.entitiymodels.Feedback;
import mob.field.harmonkardonff.entitiymodels.LMTDSalesModel;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.MTDSalesModel;
import mob.field.harmonkardonff.entitiymodels.MyInfoModel;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
import mob.field.harmonkardonff.entitiymodels.OtherInfoModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.entitiymodels.StocksModel;

public class WebService {

	public static final String FileUrl = null;
	// static String Web = "http://localhost:4873/";
	// static String Web = "http://104.155.215.243:8098/";
	static String Web = "http://product.infield.co.in:8080/";
//	 static String Web = "http://192.168.0.131:8080/";
	static String WebController = "api/";

	// Actions
	static String loginAction = "login/?";
	static String DetailedAttendance = "attendance/datewise_details/?";
	static String loginAct = "login/";
	static String InfoDetails = "admin/users/user_info/?";
	static String Default_weekoff = "weeklyoff/getweeklyoff?";
	static String SaveDefault_weekoff = "weeklyoff/updateweeklyoff?";
	static String ForgetPassword = "login/forgot_password?";
	static String CheckOut = "attendance/checkout/?";
	static String CheckInReasons = "attendance/get_reasons/?";
	static String MarkAttendanceAction = "MarkAttendance?";
	static String NewSaleAction = "UpdateSale?";
	static String ChangePassAction = "change_password/?";
	static String NewStoreAction = "getMappedStores?";
	static String NewStoreAction1 = "ChangeAssignment?";
	static String LMTDSalesAction = "GetLMTDSales?";
	static String MTDSalesAction = "GetMTDSales?";
	static String NewFeedbackAction = "CreateFeedback?";
	static String NewComplainAction = "CreateComplain?";
	static String UpdateProductAction = "getSaleProducts?";
	static String ChangePasswordAction = "ChangePassword?";
	static String UdateOtherInfoAction = "UpdateOtherInfo?";
	static String NewStockAction = "UpdateStock?";
	public static String ApiUrl = Web + WebController;

	public static String WebServiceURL = "http://fitbitisdmobile.innosols.co.in/";
	// public static String
	// FileUrl="http://mobile.innosols.co.in/artl/RedApp.apk";
	public static String FileHandlerName = "UploadFileHandler.ashx";
	public static String UserName;
	public static String Password;
	public static String UserID;
	Server server = new Server();

	static String query = "queries/postquery/";
	static String query_spin = "options/query/typemaster";

	public static void setUser(String _UserName, String _Password) {
		UserName = _UserName;
		Password = _Password;
	}

	public String QueryForm() {
		String apiUrl = ApiUrl;
		if (MainActivity.feedbackURLConfig != null
				&& !MainActivity.feedbackURLConfig.submit_feedback
						.equalsIgnoreCase("")) {
			query = MainActivity.feedbackURLConfig.submit_feedback;
		}
		apiUrl += query;
		return apiUrl;
	}

	public static String convertToEncodedStirng(String text) {
		if (text == null || text.isEmpty())
			return "";
		try {

			return URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return text;
		}
	}

	public static String convertToDecodeStirng(String text) {
		try {
			byte[] data = Base64.decode(text, Base64.DEFAULT);
			String decode = new String(data, "UTF-8");

			return decode;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return text;
		}
	}

	public static String getUsernameForUrl() {
		if (UserName == null)
			return "";
		UserName = UserName.trim();
		String username = UserName;
		try {
			username = URLEncoder.encode(UserName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return username;
	}

	public static String getUsername() {
		if (UserName == null)
			return "";
		UserName = UserName.trim();
		return UserName;
	}

	@SuppressWarnings({ "rawtypes" })
	public String LoginApi(String Latitude, String Longitude) {
		String apiUrl = ApiUrl;
		try {
			apiUrl += loginAction + "username=" + getUsernameForUrl()
					+ "&password=" + URLEncoder.encode(Password, "UTF-8")
					+ "&lat=" + Latitude + "&long=" + Longitude + "&source="
					+ "APP" + "&AppVersion="
					+ MainActivity.Current.getCurrentVersion();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return apiUrl;
	}

	private String appConfig = "config/get_config/?";

	@SuppressWarnings({ "rawtypes" })
	public String loadAppConfiguration() {
		String apiUrl = ApiUrl;
		apiUrl += appConfig + "username=" + getUsernameForUrl();
		return apiUrl;
	}
	private String appConfig1 = "http://product.infield.co.in:8080";
	public Response fetchAllSku(String fatchAllSkuURL, String fatchAllSkuhost) {
		String apiUrl = fatchAllSkuhost;
		apiUrl += fatchAllSkuURL+"?" + "username=" + getUsernameForUrl()+"&in_detail=yes";
		return server.getResponse(apiUrl, new DropDownModel());
	}

	/////   Hp APIs ///////////////////////////////////////////////////
	String allDataHp = "hp_fos/getAllDropdownValues?";
	public String getAllRequiredData() {
		String apiUrl = ApiUrl;
		apiUrl+=allDataHp+"username="+getUsernameForUrl();
		return apiUrl;
	}
	String allDataVisitHp = "hp_fos/load_vls_sale_new?";
	public String getAllVlsData(int page,String searchedText) {
		String apiUrl = ApiUrl;
		apiUrl+=allDataVisitHp+"username="+getUsernameForUrl()+"&page="+page+"&search_text="+searchedText;
		return apiUrl;
	}

	//http://product.infield.co.in:8080/api/hp_fos/loadWfhVlsAccountData?username=hpfos_test&page=1&search_text
	String loadWfhVlsAccountData = "hp_fos/loadWfhVlsAccountData?";
	public String getAllwfhVlsData(int page,String searchedText) {
		String apiUrl = ApiUrl;
		apiUrl+=loadWfhVlsAccountData+"username="+getUsernameForUrl()+"&page="+page+"&search_text="+searchedText;
		return apiUrl;
	}

	String hpProducts = "hp_fos/get_products_and_subs?";
	public String getHpProductsUrl(){
		String apiUrl = ApiUrl;
		apiUrl+=hpProducts+"username="+getUsernameForUrl();
		return apiUrl;
	}
	String hpCity = "hp_fos/getAllCities?";
	public String getHpCityUrl(String state){
		String apiUrl = ApiUrl;
		apiUrl+=hpCity+"state="+getFormatedString(state)+"&username="+getUsernameForUrl();
		return apiUrl;
	}



	String hpStates = "hp_fos/getAllStates?";
	public String getHpAllStatesUrl(){
		String apiUrl = ApiUrl;
		apiUrl+=hpStates+"username="+getUsernameForUrl();
		return apiUrl;
	}
	String hpSourceOfLeadMethod = "hp_fos/getSourceOfLeads?";
	public String getHpSourceOfLead(){
		String apiUrl = ApiUrl;
		apiUrl+=hpSourceOfLeadMethod+"username="+getUsernameForUrl();
		return apiUrl;
	}
	String hpVerticalMethod = "hp_fos/getVerticals?";
	public String getVerticalUrl(){
		String apiUrl = ApiUrl;
		apiUrl+=hpVerticalMethod+"username="+getUsernameForUrl();
		return apiUrl;
	}


	////////////////////////////////////////////////////////////////////

	private String appGridFormConfig = "forms/getallforms/?";

	@SuppressWarnings({ "rawtypes" })
	public String loadAppGridFormConfiguration() {
		String apiUrl = ApiUrl;
		apiUrl += appGridFormConfig + "username=" + getUsernameForUrl();
		return apiUrl;
	}

	public String ChangeApi(String Pass, String NewPass) {
		String apiUrl = ApiUrl;
		apiUrl += loginAct;
		apiUrl += ChangePassAction + "username=" + getUsernameForUrl()
				+ "&password=" + Pass + "&newpassword=" + NewPass
				+ "&AppVersion=" + MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public String ViewAttendance(String ViewAttendance, String StartDate,
			String EndDate) {
		String apiUrl = ApiUrl;
		apiUrl += ViewAttendance + "sdate=" + StartDate + "&edate=" + EndDate
				+ "&username=" + getUsernameForUrl() + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		Log.d("ViewAttendance", apiUrl);
		return apiUrl;
	}

	public Response getPing() {
		String apiUrl = "http://product.infield.co.in:8080/api/session/ping?";
		// apiUrl += MTDSalesAction + Encode.ToObject(model);
		int android_os = MainActivity.Current.getAndroidVersion();

		apiUrl += "username=" + getUsernameForUrl()+ "&AppVersion="
				+ MainActivity.Current.getCurrentVersion()+"&platform="+"Android"+"&api_lvl="+android_os;
		Log.e("ApiURL",apiUrl);
		return server.getResponse(apiUrl, new SessionModel());
	}

	public String DetailedAttendance(String StartDate, String EndDate) {
		String apiUrl = ApiUrl;
		apiUrl += DetailedAttendance + "sdate=" + StartDate + "&edate="
				+ EndDate + "&username=" + getUsernameForUrl() + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public String detailedAttendance(String detailedAttendance, String date,
			LoginProvider user) {
		String apiUrl = ApiUrl;
		apiUrl += detailedAttendance + "date=" + date + "&username="
				+ user.GetUserName() + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public String MarkAttendance(String MarkAttendance, MDAT model) {
		String apiUrl = ApiUrl;
		apiUrl += MarkAttendance + Encode.ToObject(model) + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		Log.d("MarkAttendance", "MarkAttendance: "+apiUrl);
		return apiUrl;
	}



	String getGeoFencingUrl = "attendance/get_store_geofencing_tl/?";

	public String getGeoFencingUrlForTL() {
		String apiUrl = ApiUrl;
		apiUrl += getGeoFencingUrl + "&username=" + getUsernameForUrl()
				+ "&AppVersion=" + MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public String getGeoFencingUrl(String url) {
		String apiUrl = ApiUrl;
		apiUrl += url + "&username=" + getUsernameForUrl() + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public String getGeoFencingUrlTL(String url) {
		String apiUrl = ApiUrl;
		apiUrl += url + "&username=" + getUsernameForUrl() + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public String DefaultWeekOff() {
		String apiUrl = ApiUrl;
		apiUrl += Default_weekoff + "user_id=" + getUsernameForUrl();
		return apiUrl;
	}

	public String SaveDefaultWeekOff() {
		String apiUrl = ApiUrl;
		apiUrl += SaveDefault_weekoff + "user_id=" + getUsernameForUrl();
		return apiUrl;
	}

	public String Forget_Password(String UserName) {
		String apiUrl = ApiUrl;
		apiUrl += ForgetPassword + "loginid=" + getFormatedString(UserName);
		apiUrl += "&AppVersion=" + getCurrentVersion();
		return apiUrl;
	}

	public String CheckOut(CheckoutModel checkoutModel) {
		String apiUrl = ApiUrl;
		apiUrl += CheckOut + Encode.ToObject(checkoutModel) + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public String getCurrentVersion() {
		String version = "0.0";
		try {
			if (MainService.Current != null)
				version = MainService.Current
						.getPackageManager()
						.getPackageInfo(MainService.Current.getPackageName(), 0).versionName;
			else if (MainActivity.Current != null)
				version = MainActivity.Current.getPackageManager()
						.getPackageInfo(MainActivity.Current.getPackageName(),
								0).versionName;
			else
				version = "null";
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return version;
	}

	public String MyInfoDetail() {
		String apiUrl = ApiUrl;
		if (MainActivity.profileUrlModel != null) {
			if (MainActivity.profileUrlModel.get_profile != null
					&& !MainActivity.profileUrlModel.get_profile
							.equalsIgnoreCase("")) {
				InfoDetails = MainActivity.profileUrlModel.get_profile;
			}
		}
		apiUrl += InfoDetails + "&username=" + getUsernameForUrl()
				+ "&AppVersion=" + MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public Response TryLogin() {
		String apiUrl = ApiUrl;
		apiUrl += loginAction + "username=" + getUsernameForUrl()
				+ "&password=" + Password + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();

		return server.getResponse(apiUrl, new MyInfoModel());
	}

	public JSONObject getCustomLayout() {
		String apiUrl = ApiUrl;
		apiUrl += "forms/getform/?form_type=log_book&";
		apiUrl += "username=" + getUsernameForUrl();
		return Parser.parserGET(apiUrl);
	}

	public JSONObject saveCustomLayout(JSONObject jobj, String apiURL,
			String title) {
		String apiUrl = "";
		if (apiURL.contains("http")) {
			apiUrl = apiURL;
			if (!apiUrl.contains("?")) {
				apiUrl += "?";
			}
		} else {
			apiUrl = ApiUrl;
			apiUrl += "forms/postformdata/?";
		}
		apiUrl += "user=" + getUsernameForUrl();
		return Parser.parserPostRequest(apiUrl, jobj);
	}

	public String customLayoutSubmitUrl(String apiURL, String title) {
		String apiUrl = "";
		if (apiURL.contains("http")) {
			apiUrl = apiURL;
			if (!apiUrl.contains("?")) {
				apiUrl += "?";
			}
		} else {
			apiUrl = ApiUrl;
			apiUrl += "forms/postformdata/?";
		}
		apiUrl += "&user=" + getUsernameForUrl();
		return apiUrl;
	}

	public String saleModuleSubmitUrl(String apiURL, String title) {
		String apiUrl = "";
		if (apiURL.contains("http")) {
			apiUrl = apiURL;
			if (!apiUrl.contains("?")) {
				apiUrl += "?";
			}
		} else {
			apiUrl = ApiUrl;
			apiUrl += "forms/postformdata/?";
		}
		apiUrl += "&user=" + getUsernameForUrl();
		return apiUrl;
	}

	public String CheckInreason(String Attendance) {
		String apiUrl = ApiUrl;
		apiUrl += CheckInReasons + "&attendance=" + Attendance + "&AppVersion="
				+ MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public JSONObject listOfActivities1() {
		String apiUrl = ApiUrl;
		apiUrl += NewStoreAction1 + "UserName=" + getUsernameForUrl()
				+ "&StoreID=" + UserID;
		return Parser.parserGET(apiUrl);
	}

	public JSONObject listOfActivities() {
		String apiUrl = ApiUrl;
		apiUrl += NewStoreAction + "UserName=" + getUsernameForUrl();
		return Parser.parserGET(apiUrl);
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryUpdateSale(NewSaleModel model) {

		String apiUrl = ApiUrl;
		apiUrl += NewSaleAction + "UserName=" + getUsernameForUrl()
				+ Encode.ToObject(model);

		return server.getResponse(apiUrl);

	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryLoadMTDSales(String message) {
		String apiUrl = ApiUrl;
		Encode.ToValue(message);
		apiUrl += MTDSalesAction + "UserName="
				+ MainActivity.MyInfo.EmployeeCode + "&fdata=" + Encode.Data;

		return server.getResponse(apiUrl, new MTDSalesModel());
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryChangePassword(ChangePasswordModel model) {
		String apiUrl = ApiUrl;
		apiUrl += ChangePasswordAction + Encode.ToObject(model);

		return server.getResponse(apiUrl);
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryMarkAttendance(String MarkAttendance, MDAT model) {
		String apiUrl = ApiUrl;
		apiUrl += MarkAttendance + Encode.ToObject(model);
		return server.getResponse(apiUrl, new MDAT());
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryCheckoutAtt(String apiUrl) {
		return server.getResponse(apiUrl, new CheckoutModel());
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryCreateNewFeedback(Feedback model) {
		String apiUrl = ApiUrl;
		apiUrl += "UserName=" + MainActivity.MyInfo.EmployeeCode;
		apiUrl += NewFeedbackAction + Encode.ToObject(model);
		return server.getResponse(apiUrl);
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryCreateNewComplain(Complain model) {
		String apiUrl = ApiUrl;
		apiUrl += NewComplainAction + Encode.ToObject(model);

		return server.getResponse(apiUrl);
	}

	private static final String targetVsAchi = "GetTLTrgAch?";

	@SuppressWarnings({ "rawtypes" })
	public Response TryGetMTDSale(String type) {
		// String apiUrl = ApiUrl;
		String apiUrl = "http://eurekamobile.v5global.co.in/ispmobile/";
		// apiUrl += MTDSalesAction + Encode.ToObject(model);
		apiUrl += targetVsAchi + "username=" + getUsernameForUrl() + "&type="
				+ type;
		return server.getResponse(apiUrl, new MTDSalesModel());
	}

	String MTDSalesValueAction = "GetMTDSalesValue?";

	public Response TryGetMTDValue(String value) {
		// String apiUrl = ApiUrl;
		String apiUrl = "http://eurekamobile.v5global.co.in/ispmobile/";
		apiUrl += targetVsAchi + "username=" + getUsernameForUrl() + "&type="
				+ value;
		/*
		 * apiUrl += MTDSalesValueAction + "&AppVersion=" +
		 * MainActivity.Current.getCurrentVersion() + Encode.ToObject(model);
		 */

		return server.getResponse(apiUrl, new MTDSalesValueModel());
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryGetLMTDSale(Feedback model) {
		String apiUrl = ApiUrl;
		apiUrl += LMTDSalesAction + Encode.ToObject(model);

		return server.getResponse(apiUrl, new LMTDSalesModel());
	}

	@SuppressWarnings({ "rawtypes" })
	public Response TryUpdateProducts() {
		String apiUrl = ApiUrl;
		apiUrl += UpdateProductAction + "UserName="
				+ MainActivity.MyInfo.EmployeeCode;
		return server.getResponse(apiUrl, new ProductModel());
	}

	@SuppressWarnings("rawtypes")
	public Response QueryFeedbackList() {
		String apiUrl = ApiUrl;
		apiUrl += query_spin;
		return server.getResponse(apiUrl, new QueryModel());
	}

	@SuppressWarnings("rawtypes")
	public Response TryUpdateStock(StocksModel newStock) {
		String apiUrl = ApiUrl;
		apiUrl += NewStockAction + Encode.ToObject(newStock);

		return server.getResponse(apiUrl);
	}

	@SuppressWarnings("rawtypes")
	public Response TryUpdateOtherInfo(OtherInfoModel otherinfo) {
		String apiUrl = ApiUrl;
		apiUrl += UdateOtherInfoAction + Encode.ToObject(otherinfo);
		return server.getResponse(apiUrl);
	}

	private static final String TAG = "app.core.base.WebService";
	public JSONObject getJsonFromApi(String apiUrl) {
		//Log.d(TAG, "getJsonFromApi: "+apiUrl);
		return Parser.parserGET(apiUrl);
	}

	public String getFormatedUrl(String url) {
		String apiUrl = url + "&username=" + getUsernameForUrl()
				+ "&AppVersion=" + MainActivity.Current.getCurrentVersion();
		return apiUrl;
	}

	public static String getFormatedString(String str) {
		try {
			str = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public Response getWinner() {
		return null;
	}

	public Object getLeaderShipDetails(String getWeeklyWinners) {
		return null;
	}

	public Response getOffers() {
		return null;
	}

	public Response requestClaim(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Response requestEnroll(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	//String checkMinAppVersion = "CheckMinAppVersion/?";
	String getBlockVersion = "api_to_check_appversion/?";
	@SuppressWarnings({ "rawtypes" })
	public String checkMinAppVersionOnServer() {
		String apiUrl = ApiUrl;
		apiUrl += "login/" + getBlockVersion + "AppVersion="
				+ MainActivity.Current.getCurrentVersion() + "&username="
				+ getUsernameForUrl();
		return apiUrl;
	}

	String checkLatestConfigVersion = "CheckConfigVersion/?";

	@SuppressWarnings({ "rawtypes" })
	public String checkLatestConfigVersionOnServer() {
		String apiUrl = ApiUrl;
		apiUrl += "config/" + checkLatestConfigVersion + "AppVersion="
				+ MainActivity.Current.getCurrentVersion() + "&username="
				+ getUsernameForUrl();
		return apiUrl;
	}

	public String submitMotorolaTlData() {
		String apiUrl = ApiUrl;
		apiUrl += "motorola/submit_stock_data/?";
		apiUrl += "username=" + getUsernameForUrl();
		return apiUrl;
	}
	public String latestAppVersionOnPlaystore = "login/api_to_get_app_latest_version_on_playstore/?";
	public String checkLatestAppVersionOnPlaystore() {
		String apiUrl = ApiUrl;
		apiUrl+=latestAppVersionOnPlaystore;
		apiUrl += "username=" + getUsernameForUrl();
		return apiUrl;
	}

	public String allSchemeData = "http://product.infield.co.in:8080/api/retail/scheme_master/getSchemesOfUser?";
	public String getAllSchemeData() {
		String apiurl = allSchemeData+"username="+getUsernameForUrl();
		return apiurl;
	}

	public String profiledata = "http://product.infield.co.in:8080/api/admin/users/profileData?";
	public String getProfileData() {
		String apiurl = profiledata+"username="+getUsernameForUrl();
		return apiurl;
	}

	private String commonNotificationBaseUrl = "http://product.infield.co.in:8091";
	public Response getAllPendingNotification() {
		/*String url = commonNotificationBaseUrl+"/notification/get";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("loginid", UserName);
			jsonObject.put("project_code", MainActivity.notificationConfig.project_code);
			return server.getResponse(url, jsonObject, new CommonNotificationModel());
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		return null;
	}

	public Response notificationViewed(String _id) {
		String url = commonNotificationBaseUrl+"/notification/viewed";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("_id", _id);
			return server.getResponse(url, jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Response getAllNotification(){
		/*String url = commonNotificationBaseUrl+"/notification";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("loginid", UserName);
			jsonObject.put("project_code", MainActivity.notificationConfig.project_code);
			return server.getResponse(url, jsonObject, new CommonNotificationModel());
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		return null;
	}


}
