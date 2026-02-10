package mob.field.harmonkardonff.services;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.fieldforce.entities.MainActivityPopupResponse;
import com.fieldforce.harmonkardonff.Comptition.CompitionModel;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.ViewModuleModel;
import com.fieldforce.harmonkardonff.demo_tracking_module.models.EnterDemoRequestModel;
import com.fieldforce.harmonkardonff.demo_tracking_module.models.ViewDemoResponseModel;
import com.fieldforce.model.GetDoctypedata;
import com.fieldforce.model.GetFloorHygieneType;
import com.fieldforce.model.NotificationResonseMode;
import com.fieldforce.model.SeenNotificationResonse;
import com.fieldforce.networkconnection.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.core.model.Response;
import app.core.server.Encode;
import app.core.server.Server;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.entitiymodels.AadharModel;
import mob.field.harmonkardonff.entitiymodels.AllPendingPushNotificationMsg;
import mob.field.harmonkardonff.entitiymodels.AllSalesDetailModel;
import mob.field.harmonkardonff.entitiymodels.AllSalesSKUDetailModel;
import mob.field.harmonkardonff.entitiymodels.BrandCategoryModel;
import mob.field.harmonkardonff.entitiymodels.BrandModel;
import mob.field.harmonkardonff.entitiymodels.BrandingImageModel;
import mob.field.harmonkardonff.entitiymodels.ChangePasswordModel;
import mob.field.harmonkardonff.entitiymodels.CheckoutModel;
import mob.field.harmonkardonff.entitiymodels.CompProductModel;
import mob.field.harmonkardonff.entitiymodels.Complain;
import mob.field.harmonkardonff.entitiymodels.CoronaHistoryModal;
import mob.field.harmonkardonff.entitiymodels.CoronaSurveyRequestModel;
import mob.field.harmonkardonff.entitiymodels.DocumentModel;
import mob.field.harmonkardonff.entitiymodels.Feedback;
import mob.field.harmonkardonff.entitiymodels.LMTDSalesModel;
import mob.field.harmonkardonff.entitiymodels.MDAT;
import mob.field.harmonkardonff.entitiymodels.MDisplay;
import mob.field.harmonkardonff.entitiymodels.MNotification;
import mob.field.harmonkardonff.entitiymodels.MOPModel;
import mob.field.harmonkardonff.entitiymodels.MTDSalesModel;
import mob.field.harmonkardonff.entitiymodels.MyInfoModel;
import mob.field.harmonkardonff.entitiymodels.NeftModel;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
import mob.field.harmonkardonff.entitiymodels.OtherInfoModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.entitiymodels.StocksModel;
import mob.field.harmonkardonff.entitiymodels.StringModel;
import mob.field.harmonkardonff.entitiymodels.TargetModelAll;
import mob.field.harmonkardonff.entitiymodels.VersionUpdationModel;
import mob.field.harmonkardonff.entitiymodels.Viewsalemodel;
import mob.field.harmonkardonff.entitiymodels.attnd_model;

public class WebService {
    MainActivity BaseActivity = null;
    public static final String FileUrl = null;
    // static String Web = "http://localhost:4873/";
    //public static final String Web = "http://harmankardon.infield.co.in/";//old
    public static final String Web = "http://harman.infield.co.in/";
    // static String Web = "http://test.mobile.innosols.co.in/";
    public static final String WebController = "ispmobile/";

    public static final String getStockApiUrlcomplete = "https://api3.zed-axis.in/zedsales/api/User/RetailerStock";

    // Actions
    static String loginAction = "TRLogin?";
    static String MarkAttendanceAction = "MarkAttendance2?";
    static String MarkAttendanceAction1 = "MarkAttendance?";
    static String NewSaleAction = "UpdateSaleLatest?";
    public static String SubmitDisplayModel = "SubmitDisplayModel2?";
    static String SubmitDownStock = "SubmitDownStock2?";
    static String MTDSalesAction = "GetMTDSales?";
    static String LMTDSalesAction = "GetLMTDSales?";
    static String NewFeedbackAction = "CreateFeedback?";
    static String Neftadd = "SubmitBankDetails?";
    static String aadhar = "SubmitAadharPanDetails/?";
    static String GetDisplayPicNotification = "GetDisplayPicNotification/?";

    final static String FORGOT_PASSOWRD_ENDPOINT = "ForgotPassword?";
    final static String CHECK_ATTENDANCE = "CheckAttendence?";
    final static String UPLOAD_BRANDING_IMAGE_HANDLER = "UploadBrandingFile.ashx?";
    final static String SUBMIT_BRANDING = "SubmitBranding?";


    static String NewComplainAction = "CreateComplain?";
    static String UpdateProductAction = "getSaleProducts?";
    static String GetDemoProducts = "GetDemoProducts?";
    static String ChangePasswordAction = "ChangePassword?";
    static String UdateOtherInfoAction = "UpdateOtherInfo?";
    static String UpdateDisplayWithOtherBrand = "UpdateDisplayWithOtherBrand?";
    static String AttendanceCheckOut = "AttendanceCheckOut?";
    static String Getpresentation = "getDocumentsURLs?";
    static String Getattendance = "GetAttendanceBtwDate?";
    static String Getsale = "GetSalesBtwDate?";

    static String NewStockAction = "UpdateStock?";
    public static final String ApiUrl = Web + WebController;

    //public static String WebServiceURL = "http://harmankardon.infield.co.in/"; old
    public static String WebServiceURL = "http://harman.infield.co.in/";

    //public static String UploaderUrl = "http://harmankardon.infield.co.in/FileUploader/ISD/Comio/";old
    public static String UploaderUrl = "http://harman.infield.co.in/FileUploader/ISD/Comio/";

    // public static String
    // FileUrl="http://mobile.innosols.co.in/artl/RedApp.apk";
    public static String FileHandlerName = "UploadFileHandler.ashx";
    static String registerDeviceAction = "RegisterDevice?";
    public static String UserName;
    public static String Password;
    public static String UserID;
    Server server = new Server();

    public void setUser(String _UserName, String _Password) {
        UserName = _UserName;
        Password = _Password;
    }

    public String getUsername() {
        try {
            String username = URLEncoder.encode(MainActivity.MyInfo.EmployeeCode,
                    "UTF-8");
            return username;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            return "";

        }
    }

    public static String getBrandingImageUploadUrl() {
        return UploaderUrl + UPLOAD_BRANDING_IMAGE_HANDLER;
    }

    public WebService(MainActivity _mainActivity) {
        // TODO Auto-generated constructor stub
        BaseActivity = _mainActivity;

    }

    public WebService() {
        // TODO Auto-generated constructor stub

    }

    public String hitdisplayRequest() {
        String apiUrl = ApiUrl;
        apiUrl += "GetDisplayView?";
        apiUrl += "username=" + MainActivity.MyInfo.EmployeeCode;
        apiUrl += "&date=" + MainActivity.Current.GetCurrentDateInString();
        return apiUrl;
    }


    public Response submitBrandingData(String storeId, ArrayList<BrandingImageModel> imageDetails) throws Exception {
        String apiUrl = ApiUrl;
        apiUrl += SUBMIT_BRANDING;
        apiUrl += "Username=" + MainActivity.MyInfo.EmployeeCode;
        apiUrl += "&StoreID=" + URLEncoder.encode(storeId, "UTF-8");

        String name = "", status = "", docId = "";

        for (BrandingImageModel image : imageDetails) {
            name += image.brandingName + ":";
            status += image.YesSelected + ":";
            docId += image.docId + ":";
        }

        apiUrl += "&Name=" + URLEncoder.encode(name.substring(0, name.length() - 1), "UTF-8");
        apiUrl += "&Status=" + URLEncoder.encode(status.substring(0, status.length() - 1), "UTF-8");
        apiUrl += "&DocID=" + URLEncoder.encode(docId.substring(0, docId.length() - 1), "UTF-8");
        return server.getResponse(apiUrl);
    }


    @SuppressWarnings("rawtypes")
    public Response checkPreviousAttendance(String date) {
        String apiUrl = ApiUrl;
        apiUrl += CHECK_ATTENDANCE +
                "username=" + MainActivity.MyInfo.EmployeeCode +
                "&ForDate=" + date +
                "&AppVersion=" + MainActivity.Current.getCurrentVersion();

        Response r = server.getResponse(apiUrl);
        Log.d("check", "check");
        return r;
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
    public Response GetPresentation() {
        String apiUrl = ApiUrl;
        apiUrl += Getpresentation + "&AppVersion="
                + MainActivity.Current.getCurrentVersion();
        return server.getResponse(apiUrl, new DocumentModel());
    }

    public Response getattendance(String startdate, String enddate) {
        String apiUrl = ApiUrl;
        apiUrl += Getattendance + "AppVersion="
                + MainActivity.Current.getCurrentVersion() + "&fuserid="
                + UserName;
        apiUrl += "&startdate=" + startdate;
        apiUrl += "&EndDate=" + enddate;

        return server.getResponse(apiUrl, new attnd_model());
    }

    public Response getsale(String startdate, String enddate) {
        String apiUrl = ApiUrl;
        apiUrl += Getsale + "AppVersion="
                + MainActivity.Current.getCurrentVersion() + "&username="
                + UserName;
        apiUrl += "&startdate=" + startdate;
        apiUrl += "&EndDate=" + enddate;

        return server.getResponse(apiUrl, new Viewsalemodel());

    }

    public Response CreateNewDP(MDisplay model) {
        String apiurl = ApiUrl;
        try {

            apiurl += UpdateDisplayWithOtherBrand;
            apiurl += "UserName="
                    + URLEncoder.encode(MainActivity.MyInfo.EmployeeCode,
                    "UTF-8");
            apiurl += "&AppVersion=" + MainActivity.Current.getCurrentVersion();
            apiurl += Encode.ToObject(model);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub

        /*
         * String APIURL = MainActivity.WebServiceURL; // domain String apiURL =
         * APIURL + "/" + MainActivity.WebServiceControler + "/";// Controler //
         * name
         */

        Response res = server.getResponse(apiurl);
        return res;

        /*
         * JSONArray jArray = ResponseHandler.GetServerResponse(ApiUrl);
         *
         * String Status = (String) jArray.getJSONObject(0).getString("status");
         * MainActivity.currenterror = (String)
         * jArray.getJSONObject(0).getString( "errormsg");
         *
         * String data = jArray.getJSONObject(0).getString("data"); JSONArray
         * products = new JSONArray(data);
         *
         *
         * if (Status.equalsIgnoreCase("true")) { return true; } else { return
         * false; }
         */
    }

    public Response CreateNewMOP(MOPModel model) throws IOException,
            JSONException {
        // TODO Auto-generated method stub

        CalendarView s;

        /*
         * String APIURL = MainActivity.WebServiceURL; // domain String apiURL =
         * APIURL + "/" + MainActivity.WebServiceControler + "/";// Controler //
         * name
         */
        String DocIDNew = model.DocIDs;
        if (DocIDNew == null)
            DocIDNew = "0";
        else
            DocIDNew = URLEncoder.encode(model.DocIDs, "UTF-8");
        String apiUrl = ApiUrl;
        apiUrl = apiUrl + "UpdateMOP";// Method Name

        apiUrl += "?UserName="
                + URLEncoder.encode(MainActivity.MyInfo.EmployeeCode, "UTF-8");
        apiUrl += "&date=" + URLEncoder.encode(model.ForDate, "UTF-8");
        apiUrl += "&PID="
                + URLEncoder.encode(String.valueOf(model.PID), "UTF-8");
        apiUrl += "&Price="
                + URLEncoder.encode(String.valueOf(model.Price), "UTF-8");
        apiUrl += "&ID=" + URLEncoder.encode(model.guid, "UTF-8");
        apiUrl += "&DocIDs=" + DocIDNew;
        apiUrl += "&AppVersion=" + MainActivity.Current.getCurrentVersion();

        Response res = server.getResponse(apiUrl);
        return res;

        /*
         * JSONArray jArray = ResponseHandler.GetServerResponse(ApiUrl);
         *
         * String Status = (String) jArray.getJSONObject(0).getString("status");
         * MainActivity.currenterror = (String)
         * jArray.getJSONObject(0).getString( "errormsg");
         *
         * String data = jArray.getJSONObject(0).getString("data"); JSONArray
         * products = new JSONArray(data);
         *
         *
         * if (Status.equalsIgnoreCase("true")) { return true; } else { return
         * false; }
         */
    }

    public static String getRegistrationUrl() {
        return ApiUrl + registerDeviceAction;
    }

    public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryLogin(String IMEINo) {

        String apiUrl = ApiUrl;
        apiUrl += loginAction + "UserName=" + UserName + "&Password="
                + Password + "&AppType=" + "ANDROID" + "&AppVersion="
                + MainActivity.Current.getCurrentVersion() + "&IMEINO="
                + IMEINo;

        Response res = server.getResponse(apiUrl, new MyInfoModel());
        Log.i("TryLogin", apiUrl);
        return res;

    }

    @SuppressWarnings({"rawtypes"})
    public Response TryUpdateSale(NewSaleModel model) {
        String apiUrl = ApiUrl;
        apiUrl += NewSaleAction + "UserName=" + UserName + "&AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);
        Log.i("TryUpdateSale", apiUrl);
        return server.getResponse(apiUrl);
    }

    @SuppressWarnings({"rawtypes"})
    public Response SubmitDisplayModel(NewSaleModel model) {
        String apiUrl = ApiUrl;
        apiUrl += SubmitDisplayModel + "Username=" + UserName + "&AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);

        Log.i("TryUpdateSale", apiUrl);
        return server.getResponse(apiUrl);
    }
    public Response GetCompetitionQuestion() {
        String apiUrl = ApiUrl;
        apiUrl += "GetCompetitionQuestion";
        return server.getResponse(apiUrl, new CompitionModel());
    }
    public Response ViewCompetitionData(String startDate, String endDate) {
        String apiUrl = ApiUrl;
        apiUrl += "ViewCompetitionData"+
                 "?UserName=" + MainActivity.MyInfo.EmployeeCode
                + "&SDate=" + startDate
                + "&EDate=" + endDate;;
        return server.getResponse(apiUrl, new CompitionModel());
    }
    public Response SubmitDownStock(NewSaleModel model) {
        String apiUrl = ApiUrl;
        apiUrl += SubmitDownStock + "Username=" + UserName
                + Encode.ToObject(model);
        Log.i("TryUpdateSale", apiUrl);
        return server.getResponse(apiUrl);
    }
    // http://localhost:4422/ISPmobile/CheckBarcodeFromDMS?username=11&IMEI=12312312
    String imeivalidation = "/CheckBarcodeFromDMS?";

    public Response checkforIMEIValidation(String imeino) {
        String apiUrl = ApiUrl;
        apiUrl += imeivalidation;
        try {
            String imei = URLEncoder.encode(imeino, "UTF-8");
            apiUrl += "username=" + MainActivity.MyInfo.EmployeeCode + "&IMEI="
                    + imei;
            return server.getResponse(apiUrl);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryLoadMTDSales(String message) {
        String apiUrl = ApiUrl;
        Encode.ToValue(message);
        apiUrl += MTDSalesAction + "UserName="
                + MainActivity.MyInfo.EmployeeCode + "&AppVersion="
                + MainActivity.Current.getCurrentVersion() + "&fdata="
                + Encode.Data;
        return server.getResponse(apiUrl, new MTDSalesModel());
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryChangePassword(ChangePasswordModel model) {
        String apiUrl = ApiUrl;
        apiUrl += ChangePasswordAction + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);

        return server.getResponse(apiUrl);
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryMarkAttendance(MDAT model) {

        String apiUrl = ApiUrl;
        apiUrl += MarkAttendanceAction + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);
        Log.i("TryMarkAttendance", apiUrl);
        return server.getResponse(apiUrl, new MDAT());
    }
    @SuppressWarnings({"rawtypes"})
    public Response TryMarkAttendance1(MDAT model) {

        String apiUrl = ApiUrl;
        apiUrl += MarkAttendanceAction1 + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);
        Log.i("TryMarkAttendance", apiUrl);
        return server.getResponse(apiUrl, new MDAT());
    }

    public Response getCurrentAttendanceStatus(String url) {
        return server.getResponse(url, new MDAT());
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryCreateNewFeedback(Feedback model) {
        String apiUrl = ApiUrl;
        apiUrl += NewFeedbackAction + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);
        return server.getResponse(apiUrl);
    }

    public Response TryaddNeftdetails(NeftModel model) {
        String apiUrl = ApiUrl;
        apiUrl += Neftadd + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);
        return server.getResponse(apiUrl);
    }

    public Response TryaddAdhardetails(AadharModel model) {
        String apiUrl = ApiUrl;
        apiUrl += aadhar + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);
        return server.getResponse(apiUrl);
    }

    String checkMinAppVersion = "CheckMinAppVersion?";
    String GetISPActiveStatus = "GetISPActiveStatus?";

    @SuppressWarnings({"rawtypes"})
    public Response checkMinAppVersionOnServer() {
        String apiUrl = ApiUrl;
        apiUrl += checkMinAppVersion + "AppVersion="
                + MainActivity.Current.getCurrentVersion() + "&UserName="
                + MainActivity.MyInfo.EmployeeCode;
        return server.getResponse(apiUrl, new VersionUpdationModel());
    }

    public String latestAppVersionOnPlaystore = "http://product.infield.co.in:8080/api/login/api_to_get_app_latest_version_on_playstore/?";

    public Response GetISPActiveStatus() {
        String apiUrl = ApiUrl;
        apiUrl += GetISPActiveStatus;
        apiUrl += "LoginId="+MainActivity.MyInfo.UserID;

        Log.d("GetISPActiveStatus",apiUrl);
        return server.getResponse(apiUrl,new VersionUpdationModel());
    }

    public Response checkLatestAppVersionOnPlaystore(String version) {
        String apiUrl = "";
        apiUrl += latestAppVersionOnPlaystore;
        apiUrl += "project_code=harman_isp_dotnet";
        apiUrl += "&app_version="+version;
        apiUrl += "&device_type=Android";
        Log.d("checkForVersion",apiUrl);
        return server.getResponse(apiUrl,new VersionUpdationModel());
    }

    String MOPData = "getmopdata?";

    @SuppressWarnings({"rawtypes"})
    public Response callForMOPCatAndBrand() {
        String apiUrl = ApiUrl;
        apiUrl += MOPData;// + Encode.ToObject(model);
        apiUrl += "UserName=" + MainActivity.MyInfo.EmployeeCode
                + "&AppVersion=" + MainActivity.Current.getCurrentVersion();
        return server.getResponse(apiUrl, new CompProductModel());
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryCreateNewComplain(Complain model) {
        String apiUrl = ApiUrl;
        String lasturl = Encode.ToObject(model);
        lasturl = lasturl.substring(1, lasturl.length());
        apiUrl += NewComplainAction + lasturl;
        return server.getResponse(apiUrl);
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryGetMTDSale(Feedback model) {
        String apiUrl = ApiUrl;
        apiUrl += MTDSalesAction + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);

        return server.getResponse(apiUrl, new MTDSalesModel());
    }

    public Response TryGetstock(Feedback model) {
        String apiUrl = ApiUrl;
        apiUrl += MTDSalesAction + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);

        return server.getResponse(apiUrl, new MTDSalesModel());
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryGetLMTDSale(Feedback model) {
        String apiUrl = ApiUrl;
        apiUrl += LMTDSalesAction + "AppVersion="
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);

        return server.getResponse(apiUrl, new LMTDSalesModel());
    }

    private String getLatestProductConfigVersion = "GetLatestProductVersion?";

    @SuppressWarnings({"rawtypes"})
    public Response getLatestProductConfigVer() {
        String apiUrl = ApiUrl;
        apiUrl += getLatestProductConfigVersion;
        try {
            apiUrl += "UserName="
                    + URLEncoder.encode(MainActivity.MyInfo.EmployeeCode + "",
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return server.getResponse(apiUrl, new BrandCategoryModel());
    }

    public Response tryForgotpassword(String userId) throws Exception {
        String apiUrl = ApiUrl;
        apiUrl += FORGOT_PASSOWRD_ENDPOINT;
        apiUrl += "UserName=" + URLEncoder.encode(userId, "utf-8");
        Log.i("tryForgotpassword", apiUrl);
        return server.getResponse(apiUrl);
    }


    private String getBrandCategory = "GetOtherBrandCategory";

    @SuppressWarnings({"rawtypes"})
    public Response TryUpdateBrandCategory() {
        String apiUrl = ApiUrl;
        apiUrl += getBrandCategory;
        /*
         * + "UserName=" + MainActivity.MyInfo.EmployeeCode;
         */
        return server.getResponse(apiUrl, new BrandCategoryModel());
    }

    private String getBrand = "GetBrandList";

    @SuppressWarnings({"rawtypes"})
    public Response TryUpdateBrand() {
        String apiUrl = ApiUrl;
        apiUrl += getBrand;
        /*
         * + "UserName=" + MainActivity.MyInfo.EmployeeCode;
         */
        return server.getResponse(apiUrl, new BrandModel());
    }

    private String getViewDisplay = "GetDisplayView?";
    private String currentDate = "2015-12-21";

    @SuppressWarnings({"rawtypes"})
    public Response TryUpdateViewDisplay() {
        String apiUrl = ApiUrl;
        apiUrl += getViewDisplay + "&fdata=" + currentDate;
        /*
         * + "UserName=" + MainActivity.MyInfo.EmployeeCode;
         */
        return server.getResponse(apiUrl, new BrandModel());
    }

    @SuppressWarnings({"rawtypes"})
    public Response TryUpdateProducts() {
        String apiUrl = ApiUrl;
        apiUrl += UpdateProductAction + "UserName="
                + MainActivity.MyInfo.EmployeeCode;
        return server.getResponse(apiUrl, new ProductModel());
    }

    public Response TryUpdateDemoProducts() {
        String apiUrl = ApiUrl;
        apiUrl += GetDemoProducts;
        return server.getResponse(apiUrl, new ProductModel());
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

    public JSONObject tryGetStock(String retailerCode) throws Exception {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("UserName", "APIBusy");
        bodyJson.put("Password", "test2");
        bodyJson.put("AccessKey", "ComioSales");
        bodyJson.put("RetailerCode", retailerCode);

        return Parser.parserPostRequest(getStockApiUrlcomplete, bodyJson);

    }

    static String lastSevenSale = "Last7DaysCategorySales?";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Response getSales(int UserID) {
        try {
            // TODO Auto-generated method stub
            String apiUrl = ApiUrl;
            apiUrl += lastSevenSale;
            apiUrl += "UserName="
                    + URLEncoder.encode(MainActivity.MyInfo.EmployeeCode + "",
                    "UTF-8");
            apiUrl += "&AppVersion=" + MainActivity.Current.getCurrentVersion();
            Log.e("salesurl", "-----------------------------\n" + apiUrl);
            // @SuppressWarnings("static-access")
            /*
             * JSONObject jobj = (JSONObject)
             * server.GetServerResponse(apiUrl).get(0); Response resp = new
             * JsonConverter().ConvertToResponse( new AllSalesDetailModel(),
             * jobj);
             */
            return server.getResponse(apiUrl, new AllSalesDetailModel());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static String targetVSAchievementLatest = "targetVSAchievementLatest?";
    static String targetVSAchievementQtyWise = "targetVSAchievementQtyWise?";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Response getTargetAchievementFromWeb() {
        try {
            String apiUrl = ApiUrl;
            apiUrl += targetVSAchievementLatest;
            apiUrl += "UserName=";
            apiUrl += URLEncoder.encode(MainActivity.MyInfo.EmployeeCode + "", "UTF-8");
            return server.getResponse(apiUrl, new TargetModelAll());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(e.getMessage());
        }
    }
    public Response getTargetAchievementFromWebQTY() {
        try {
            String apiUrl = ApiUrl;
            apiUrl += targetVSAchievementQtyWise;
            apiUrl += "UserName=";
            apiUrl += URLEncoder.encode(MainActivity.MyInfo.EmployeeCode + "", "UTF-8");
            return server.getResponse(apiUrl, new TargetModelAll());
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(e.getMessage());
        }
    }

    static String lastSevenSKUSale = "Last7DaysSKUSalesForCategory?";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Response getSalesSKUDetails(String CatID) {
        try {
            // TODO Auto-generated method stub
            String apiUrl = ApiUrl;
            apiUrl += lastSevenSKUSale;
            apiUrl += "UserName=";
            // apiUrl += "&AppVersion="
            // + MainActivity.Current.getCurrentVersion();
            apiUrl += ""
                    + URLEncoder.encode(MainActivity.MyInfo.EmployeeCode + "",
                    "UTF-8");
            apiUrl += "&PID=" + CatID;
            Log.e("salesurl", "-----------------------------\n" + apiUrl);
            /*
             * @SuppressWarnings("static-access") JSONObject jobj = (JSONObject)
             * server.GetServerResponse(apiUrl) .get(0); Response resp = new
             * JsonConverter().ConvertToResponse( new AllSalesSKUDetailModel(),
             * jobj);
             */
            return server.getResponse(apiUrl, new AllSalesSKUDetailModel());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPushNotificationMsg = "GetRegisterDevice?";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Response requestForAllMessage() {

        try {
            // TODO Auto-generated method stub
            String apiUrl = ApiUrl;
            apiUrl += getPushNotificationMsg;
            apiUrl += "UserName=";
            // apiUrl += "&AppVersion="
            // + MainActivity.Current.getCurrentVersion();
            apiUrl += ""
                    + URLEncoder.encode(MainActivity.MyInfo.EmployeeCode + "",
                    "UTF-8");
            Log.e("salesurl", "-----------------------------\n" + apiUrl);

            return server.getResponse(apiUrl,
                    new AllPendingPushNotificationMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String achknowledge = "UpdatePushNotificationStatus?";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Response sendAcknowledgement(String headerID) {

        try {
            String apiUrl = ApiUrl;
            apiUrl += achknowledge;
            apiUrl += "UserName=";
            apiUrl += ""
                    + URLEncoder.encode(MainActivity.MyInfo.EmployeeCode + "",
                    "UTF-8");
            apiUrl += "&PID=" + headerID;
            // apiUrl += "&AppVersion="
            // + MainActivity.Current.getCurrentVersion();
            /*
             * @SuppressWarnings("static-access") JSONObject jobj = (JSONObject)
             * server.GetServerResponse(apiUrl).get(0);
             */
            return server.getResponse(apiUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response Trycheckout(CheckoutModel checkout) {
        String apiUrl = ApiUrl;
        apiUrl += AttendanceCheckOut + Encode.ToObject(checkout);
        return server.getResponse(apiUrl);

    }

    String getNotificationMethod = "getPushNotificationMsgsForRE/?";

    public Response getAllNotification() {
        String apiUrl = ApiUrl;
        apiUrl += getNotificationMethod + "username=" + getUsername();
        return server.getResponse(apiUrl, new MNotification());
    }
    String GetISPConfuguredData = "GetISPConfuguredData?";
    public Response getDoclist() {
        String apiUrl = ApiUrl;
        apiUrl += GetISPConfuguredData + "LoginId=" + getUsername();
        return server.getResponse(apiUrl, new GetDoctypedata());
    }


    String checkstockstatus = "checkstockstatus?";

    public Response getStockUpdateStatus() {
        String apiUrl = ApiUrl;
        apiUrl += checkstockstatus + "userid=" + getUsername();
        //Log.i("apiWa",""+apiUrl);
        return server.getResponse(apiUrl);
    }

    String GetTrainingNotification = "GetTrainingNotification?";

    public Response getNotification() {
        String apiUrl = ApiUrl;
        apiUrl += GetTrainingNotification + "LoginId=" + getUsername();
        //Log.i("apiWa",""+apiUrl);
        return server.getResponse(apiUrl, new NotificationResonseMode());
    }
    public Response GetDisplayPicNotification() {
        String apiUrl = ApiUrl;
        apiUrl += GetDisplayPicNotification + "Username=" + getUsername();
        //Log.i("apiWa",""+apiUrl);
        return server.getResponse(apiUrl, new NotificationResonseMode());
    }
    String GetFloorHygieneType = "GetFloorHygieneType?";
    public Response getFloorHygieneType() {
        String apiUrl = ApiUrl;
        apiUrl += GetFloorHygieneType + "LoginId=" + getUsername();
        //Log.i("apiWa",""+apiUrl);
        return server.getResponse(apiUrl, new GetFloorHygieneType());
    }





    //http://harman.infield.co.in/ispmobile/geSchemesDataURLs?DocType=SalePic
    public Response GetSchemes(String DocType) {
        String apiUrl = ApiUrl;
        apiUrl += "geSchemesDataURLs?";
        apiUrl += "DocType="+DocType;
        Log.i("GetSchemesO", apiUrl);
        return server.getResponse(apiUrl, new DocumentModel());
    }

    //http://harman.infield.co.in/ispmobile/GetScheamPopupByISP?Username=T123456
    public Response getPopupData() {
        String apiUrl = ApiUrl;
        apiUrl += "GetScheamPopupByISP?Username=" + getUsername();
        Log.i("getPopupData", apiUrl);
        return server.getResponse(apiUrl, new MainActivityPopupResponse());
    }

    //http://harman.infield.co.in/ispmobile/SaveReadScheamByISP?Username=T123456&ScheamId=3&IsRead=true
    public Response submitPopupStatusToServer(String schemeId, String status) {
        String apiUrl = ApiUrl;
        apiUrl += "SaveReadScheamByISP?Username=" + getUsername() + "&ScheamId=" + schemeId + "&IsRead=" + status;
        Log.i("submitPopupStatus", apiUrl);
        return server.getResponse(apiUrl);
    }


    //------------------------------ CoronaSurvey -------------------------------

    //http://harman.infield.co.in/ispmobile/GetCOVIDQuesAnsTemplate?Username=v5/test
    public Response getQuestionsFromServer() {
        String apiUrl = ApiUrl;
        apiUrl += "GetCOVIDQuesAnsTemplate?Username=" + UserName;
        Log.i("getQuestionsFromServer", apiUrl);
        return server.getResponse(apiUrl, new CoronaSurveyRequestModel());
    }

    //http://harman.infield.co.in/ispmobile/SaveCOVIDQuestion?QuestionIds=1,2,3,4,5&Answers=111,true,false,true,Not%20Bad&UserName=v5/test
    public Response sendAnswersToServer(String questionIds, String Answers) {
        String apiUrl = ApiUrl;
        try {
            apiUrl += "SaveCOVIDQuestion?QuestionIds=" + URLEncoder.encode(questionIds, "UTF-8") + "&Answers=" +
                    URLEncoder.encode(Answers, "UTF-8") + "&UserName=" + UserName;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("sendAnswersToServer", apiUrl);
        return server.getResponse(apiUrl);
    }

    //http://harman.infield.co.in/ispmobile/GetCOVIDReport?Username=v5/test&Sdate=2020-05-01&Enddate=2020-05-04
    public Response getCoronaSurveyHistoryFromWeb(String startDate, String endDate) {
        String apiUrl = ApiUrl;
        apiUrl += "GetCOVIDReport?Username=" + UserName + "&Sdate=" + startDate + "&Enddate=" + endDate;
        Log.i("getCoronaHistory", apiUrl);
        return server.getResponse(apiUrl, new CoronaHistoryModal());
    }

    //http://harman.infield.co.in/ispmobile/COVIDNotification?UserName=2233
    public Response getPopupForCoronaSurveyOnMainScreen() {
        String apiUrl = ApiUrl;
        apiUrl += "COVIDNotification?UserName=" + UserName;
        Log.i("getPopupOnMainScreen", apiUrl);
        return server.getResponse(apiUrl, new StringModel());
    }


    //http://harman.infield.co.in/ispmobile/SubmitDemoTracking?UserName=50&PID=3&CustomerName=testCust2&CustomerEmail=testCust2@gmail.com&CustomerPhone=0202020202&CustomerAge=29
    @SuppressWarnings({"rawtypes"})
    public Response SubmitDemoToServer(EnterDemoRequestModel model) {
        String apiUrl = ApiUrl;
        apiUrl += "SubmitDemoTracking?"
                + MainActivity.Current.getCurrentVersion()
                + Encode.ToObject(model);
        Log.i("SubmitDemoToServer", apiUrl);
        return server.getResponse(apiUrl);
    }

    public Response SubmitDemoToServerNew(EnterDemoRequestModel model) {
        String apiUrl = ApiUrl;
        apiUrl += "SubmitDemoProductData?"
                + Encode.ToObject(model);
        Log.i("SubmitDemoToServer", apiUrl);
        return server.getResponse(apiUrl);
    }


    //http://harman.infield.co.in/ispmobile/GetISPDemoTracking?Username=50&StartDate=2020-06-19&EndDate=2020-06-22
    //http://harman.infield.co.in/ispmobile/GetISPDemoTrackingAppVersion=2.8&Username=50&StartDate=2020-06-22&EndDate=2020-06-22
    public Response getDemoDataFromServer(String startdate, String enddate) {
        String apiUrl = ApiUrl;
        apiUrl += "ViewDemoProductData?" + "AppVersion="
                + MainActivity.Current.getCurrentVersion() + "&UserName="
                + UserName;
        apiUrl += "&SDate=" + startdate;
        apiUrl += "&EDate=" + enddate;
        Log.i("getDemoDataFromServer", apiUrl);
        return server.getResponse(apiUrl, new ViewDemoResponseModel());

    }

    public Response getModuleFromServer(String startdate, String enddate) {
        String apiUrl = ApiUrl;
        apiUrl += "ViewDisplayModel?"  + "&StoreId="
                + MainActivity.MyInfo.StoreID;
        apiUrl += "&SDate=" + startdate;
        apiUrl += "&EDate=" + enddate;
        Log.i("getModuleFromServer", apiUrl);
        return server.getResponse(apiUrl, new ViewModuleModel());

    }
    public Response getModuleFromServer() {
        String apiUrl = ApiUrl;
        apiUrl += "ViewDisplayModelLatest?"  + "&StoreId="
                + MainActivity.MyInfo.StoreID;
        Log.i("getModuleFromServer", apiUrl);
        return server.getResponse(apiUrl, new ViewModuleModel());

    }

    public Response ViewDownStock(String startdate, String enddate) {
        String apiUrl = ApiUrl;
        apiUrl += "ViewDownStock?"  + "&Username="
                + UserName;
        apiUrl += "&SDate=" + startdate;
        apiUrl += "&EDate=" + enddate;
        Log.i("getModuleFromServer", apiUrl);
        return server.getResponse(apiUrl, new ViewModuleModel());

    }

    //Update Seen Notification
    public Response updateSeenNotification(NotificationResonseMode seenNotification, String LoginId) {
        String apiUrl = ApiUrl;
        apiUrl += "UpdateSeenNotification?" + "Id="
                + seenNotification.Id + "&NotificationType="
                + "";
        apiUrl += "&LoginId=" + LoginId;

        return server.getResponse(apiUrl);

    }


	@SuppressWarnings({ "rawtypes" })
	public Response getHygieneStoreInfo(MyInfoModel model) {
		String apiUrl = ApiUrl;

		apiUrl += "GetStoresFloorHygiene?";
		apiUrl += "&StoreId=" + model.StoreID;
		apiUrl += "&LoginId=" + model.UserID;
		Log.i("getHygieneStoreInfo",apiUrl);

		return server.getResponse(apiUrl);
	}
	public Response addStoreHygieneData(String  loginId,String docids,String storeId,String date) {
		String apiUrl = ApiUrl;

		apiUrl += "SaveStoresFloorHygiene?";
		apiUrl += "&StoreId=" + storeId;
		apiUrl += "&LoginId=" + loginId;
		apiUrl += "&DocIds=" + docids.replaceAll(" ","%20");
		apiUrl += "&DateFor=" + date;




		return server.getResponse(apiUrl);
	}



    String GeTrainingSeenNotification = "GeTrainingSeenNotification?";
    public Response getSeenNotification() {
        String apiUrl = ApiUrl;
        apiUrl += GeTrainingSeenNotification + "LoginId=" + getUsername();
        //Log.i("apiWa",""+apiUrl);
        return server.getResponse(apiUrl, new SeenNotificationResonse());
    }

}
