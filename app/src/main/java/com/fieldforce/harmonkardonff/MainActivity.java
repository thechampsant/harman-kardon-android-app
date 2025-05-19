package com.fieldforce.harmonkardonff;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
/*import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.PagerSnapHelper;
import androidx.appcompat.widget.RecyclerView;*/
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.APIService.FeedbackUrlConfigModel;
import com.APIService.ProfileURLModel;
import com.ariston.training_module.modules.training_module.ui.activities.ProfileActivity;
import com.ariston.training_module.utility.TrainingConstants;
import com.bumptech.glide.Glide;
import com.fieldforce.checkversion.AppVersionController;
import com.fieldforce.customAdapter.OnPopupReadButtonClickListener;
import com.fieldforce.customAdapter.RecyclerViewMainActivityPopupAdapter;
import com.fieldforce.customAdapter.corona_adapters.VerticalItemDecorator;
import com.fieldforce.entities.MainActivityPopupResponse;
import com.fieldforce.floorhygiene.HygieneStoreListActivity;
import com.fieldforce.harmonhelper.GPSTracker;
import com.fieldforce.harmonkardonff.Comptition.CompetitionTab;
import com.fieldforce.harmonkardonff.custom_adapters.NotificationAdpter;
import com.fieldforce.harmonkardonff.demo_tracking_module.ui.activities.DemoTrackingFragmentsContainer;
import com.fieldforce.harmonkardonff.homeTrainingDoc.AdapterTrainingMat;
import com.fieldforce.model.GetDoctypedata;
import com.fieldforce.model.NotificationResonseMode;
import com.fieldforce.profile.MyProfileModel;

import com.fieldforce.utility.CommonUtility;
import com.fieldforce.utility.Helper;
import com.fieldforce.utility.Storage;
import com.fieldforce.utility.clicklisteners.PushDownAnim;
import com.fieldforce.utility.widgets.RobotoBoldTextView;
import com.fieldforce.utility.widgets.RobotoTextView;
import com.grid.GridActivity;
import com.grid.GridItem;
import com.jmedeisis.draglinearlayout.DragLinearLayout;
import com.suveyform.SurveytypeActivity;

import java.util.List;

import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.entitymodels.ImageInfo;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.gcm.NotificationView;
import mob.field.gcm.NotificationViewListener;
import mob.field.harmonkardonff.BLL.ProductUpdater;
import mob.field.harmonkardonff.db.Database;
import mob.field.harmonkardonff.entitiymodels.AadharModel;
import mob.field.harmonkardonff.entitiymodels.AppConfigModel;
import mob.field.harmonkardonff.entitiymodels.BrandCategoryModel;
import mob.field.harmonkardonff.entitiymodels.BrandModel;
import mob.field.harmonkardonff.entitiymodels.CheckoutModel;
import mob.field.harmonkardonff.entitiymodels.CompProductModel;
import mob.field.harmonkardonff.entitiymodels.Complain;
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
import mob.field.harmonkardonff.entitiymodels.StoreImage;
import mob.field.harmonkardonff.entitiymodels.VersionUpdationModel;
import mob.field.harmonkardonff.entitiymodels.ViewDisplayModel;
import mob.field.harmonkardonff.entitiymodels.Viewsalemodel;
import mob.field.harmonkardonff.entitiymodels.attnd_model;
import mob.field.harmonkardonff.quiz.Quiz;
import mob.field.harmonkardonff.quiz.QuizIDTimeDetailModel;
import mob.field.harmonkardonff.services.WHPL_MainService;
import mob.field.harmonkardonff.services.WebService;
import mob.field.harmonkardonff.survey.Survey;
import mob.field.harmonkardonff.tabs.CoronaSurvey_Tabs;
import mob.field.harmonkardonff.tabs.DatTab;
import mob.field.harmonkardonff.tabs.DownStock;
import mob.field.harmonkardonff.tabs.ModuleTab;
import mob.field.harmonkardonff.tabs.SaleTabs;
import mob.field.harmonkardonff.tabs.StockTabs;

//import app.core.services.VersionHandler;

@SuppressLint("NewApi")
public class MainActivity extends GridActivity implements View.OnClickListener, NotificationAdpter.NotificationAdapterCallbacks {


    public final static String IS_SOURCE_DIALOG = "SOURCE_DIALOG";
    public static MainActivity Current;
    // For Database Operations
    public static Database Database = null;
    // Used ArrayLists
    public static GPSTracker gpsTracker;
    public static ArrayList<ProductModel> MyProductList = new ArrayList<ProductModel>();
    public static ArrayList<NewSaleModel> MySales = new ArrayList<NewSaleModel>();
    public static ArrayList<StocksModel> MyStocks = new ArrayList<StocksModel>();
    public static ArrayList<MDAT> MyAttendances = new ArrayList<MDAT>();
    public static MyInfoModel MyInfo = new MyInfoModel();
    public static ArrayList<ImageInfo> PendingImages = new ArrayList<ImageInfo>();
    public static ArrayList<OtherInfoModel> footfalls = new ArrayList<OtherInfoModel>();
    public static ArrayList<MOPModel> MyMOPs = new ArrayList<MOPModel>();
    public static ArrayList<CompProductModel> MyPLForMOP = new ArrayList<CompProductModel>();
    public static ArrayList<MDisplay> MyDisplays = new ArrayList<MDisplay>();
    public static ArrayList<BrandModel> MyBrand = new ArrayList<BrandModel>();
    public static ArrayList<BrandCategoryModel> MyBrandCat = new ArrayList<BrandCategoryModel>();
    public static ArrayList<ViewDisplayModel> displayModels = new ArrayList<ViewDisplayModel>();
    public static java.util.ArrayList<NotificationResonseMode> notificationViewModuleArrayList = new ArrayList<NotificationResonseMode>();
    boolean backpressActive = true;
    WebService web = new WebService();
    AppVersionController appVersionController;
    private Button message_button;
    private RobotoBoldTextView tvName,tvUSerID,tvV5ID;
    private ImageView iv_profile_pic;
    private RecyclerView notificationlist;
    private Bundle dataForTraining = new Bundle();
    NotificationAdpter notificationAdpter;
    public static FeedbackUrlConfigModel feedbackURLConfig = null;
    public static ProfileURLModel profileUrlModel = null;
    //------------------------------------ from Abhijai----------------------
    public static ArrayList<String> GetMOPSubCategories(String Cat1) {
        ArrayList<String> list = new ArrayList<String>();
        for (CompProductModel p : MyPLForMOP) {
            if (p.Cat1.equalsIgnoreCase(Cat1)) {
                if (list.contains(p.Cat2))
                    continue;
                list.add(p.Cat2);
            }
        }
        return list;
    }
    public static ArrayList<CompProductModel> GetMOPProducts(String Cat1, String Cat2) {
        ArrayList<CompProductModel> list = new ArrayList<CompProductModel>();
        for (CompProductModel p : MyPLForMOP) {
            if (p.Cat1.equalsIgnoreCase(Cat1) && p.Cat2.equalsIgnoreCase(Cat2)) {
                if (list.contains(p))
                    continue;
                list.add(p);
            }
        }
        return list;
    }
    public static ArrayList<String> GetMOPMasterCategories() {
        ArrayList<String> list = new ArrayList<String>();
        for (CompProductModel p : MyPLForMOP) {
            if (list.contains(p.Cat1))
                continue;
            list.add(p.Cat1);
        }
        return list;
    }
    public static ArrayList<String> GetMasterCategories() {
        return MyProductList.Select("Cat1").Distinct().ToString();
    }
    public static ArrayList<String> GetSelectMasterCategories() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Select");
        list.addAll(MyProductList.Select("Cat1").Distinct().ToString());
        return list;
    }
    public static ArrayList<String> GetSubCategories(String Cat1) {
        return MyProductList.where("Cat1", Cat1).Select("Cat2").Distinct()
                .ToString();
    }
    public static ArrayList<String> GetSelectSubCategories(String Cat1) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Select");
        list.addAll(MyProductList.where("Cat1", Cat1).Select("Cat2").Distinct().ToString());
        return list;
    }
    public static ArrayList<String> GetModels(String mastCat, String subCat) {

        return MyProductList.where("Cat1", mastCat).where("Cat2", subCat)
                .Select("Cat3").Distinct().ToString();
    }
    public static ArrayList<String> GetSelectModels(String mastCat, String subCat) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Select");
        list.addAll(MyProductList.where("Cat1", mastCat).where("Cat2", subCat)
                .Select("Cat3").Distinct().ToString());
        return list;
    }
    public static ArrayList<ProductModel> GetProducts(String Cat1, String Cat2, String Cat3) {
        return MyProductList.where("Cat1", Cat1).where("Cat2", Cat2)
                .where("Cat3", Cat3).Distinct();
    }
    public static ArrayList<ProductModel> GetProducts(String Cat1, String Cat2) {

        return MyProductList.where("Cat1", Cat1).where("Cat2", Cat2).Distinct();
    }
    public static ArrayList<String> GetDemostratorRequired() {
        ArrayList<String> modellist = new ArrayList<String>();
        modellist.add("Yes");
        modellist.add("No");
        return modellist;
    }
    public static ArrayList<ProductModel> GetProducts(String Cat1) {

        return MyProductList.where("Cat1", Cat1).Distinct();
    }
    public static ArrayList<ProductModel> GetSelectProducts(String Cat1, String Cat2, String Cat3) {
        ArrayList<ProductModel> list = new ArrayList<ProductModel>();
        ProductModel PM = new ProductModel();
        PM.Name = "Select";
        PM.PID = "0";
        list.add(PM);
        list.addAll(MyProductList.where("Cat1", Cat1).where("Cat2", Cat2)
                .where("Cat3", Cat3).Distinct());
        return list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Current = this;
        super.onCreate(savedInstanceState);
        SetPolicy();
        gpsTracker = new GPSTracker(MainActivity.this);
        this.EnableLocalDatabase("MyyDbb0055", 55);
        Database = new Database(this);
        startService(new Intent(this, WHPL_MainService.class));
        // new GCMResponseManager();
          /*new GcmService(this).setServerAPI(URL.getRegistrationUrl(),
		  MainActivity.MyInfo.EmployeeCode).TryRegisterDevice();*/
        checkForLogin();
        // initialize();
        //apiCallfordataofdocuments();
         saveVersionUpdationFromServerToLocal();
        // toastForLastestVersion();

        checkForPendingAttendance();
        grd.setPadding(20, 20, 20, 0);
        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("UserID", MyInfo.UserID);
                startActivity(intent);
            }
        });
        initializeNotificationsRecycler();


    }
    @Override
    public void TrysetGridHeader() {
        super.TrysetGridHeader();
        RelativeLayout rlHeader = (RelativeLayout) headerview.getRootView();
        ImageView ivBg = headerview.findViewById(R.id.iv_bg_top);
        tvName = headerview.findViewById(R.id.tv_name);
        tvUSerID = headerview.findViewById(R.id.tv_user_ID);
        tvV5ID = headerview.findViewById(R.id.tv_V5ID);
        iv_profile_pic = headerview.findViewById(R.id.iv_profile_pic);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivBg.getLayoutParams();
        layoutParams.height = (int) (Helper.getViewHeight(rlHeader) - Helper.getSizeInDp(this, 10));
        ivBg.setLayoutParams(layoutParams);
        ivBg.setImageResource(R.drawable.bg_top_home);


    }
    public void initializeNotificationsRecycler() {
        notificationAdpter = new NotificationAdpter();
        notificationAdpter.setCallbacks(this);
        notificationlist = new RecyclerView(this);
        notificationlist.setVisibility(View.GONE);
        new PagerSnapHelper().attachToRecyclerView(notificationlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        notificationlist.addItemDecoration(new VerticalItemDecorator(30));
        notificationlist.setLayoutManager(linearLayoutManager);
        notificationlist.setAdapter(notificationAdpter);
        mLayout.addView(notificationlist);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) notificationlist.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        notificationlist.setLayoutParams(layoutParams);

    }
    private ProgressBar progressBar;
    AlertDialog popBuilder;
    RecyclerView recyclerView;
    RecyclerViewMainActivityPopupAdapter mAdapter;
    private void preparePopupTask() {
        mAdapter = new RecyclerViewMainActivityPopupAdapter(new OnPopupReadButtonClickListener() {
            @Override
            public void submitPopupStatusToServer(String schemeId, String status) {
                submitPopupResponseToServer(schemeId, status);
            }
        });

        getDataForMultipleImageFromServer();
        View view = getLayoutInflater().inflate(R.layout.popup_on_main_activity, null, false);
        popBuilder = new AlertDialog.Builder(MainActivity.this).create();
        recyclerView = view.findViewById(R.id.rv_recyclerInDialog);

        popBuilder.setCanceledOnTouchOutside(false);
        popBuilder.setView(view);

    }

    private void getDataForMultipleImageFromServer() {
        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
        bp.setProgressMessage("Please wait...");
        bp.setbackgroundProcess(new IProcess() {

            @SuppressWarnings("rawtypes")
            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                Response res = (Response) arg0;
                if (res.status.equalsIgnoreCase("true")) {
                    //show popup dialog
                    ArrayList<MainActivityPopupResponse> list = res.data;
                    if (list.size() > 0) {
                        mAdapter.setData(list);
                        //popBuilder.show();
                    }
                } else {
                    new Dialog(MainActivity.this).setTitle("Error").setMessage(res.errormsg).show();
                }
            }

            @Override
            public Object underProcess() throws Exception {
                // TODO Auto-generated method stub
                // IsCatSelected();
                // GetDropDown();
                return web.getPopupData();
            }
        });

        bp.execute();
    }
    private void submitPopupResponseToServer(final String schemeId, final String status) {
        BackgroundProcess bp = new BackgroundProcess(this).showProgress(true);
        bp.setbackgroundProcess(new IProcess() {

            @SuppressWarnings("rawtypes")
            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                Response res = (Response) arg0;
                if (res.status.equalsIgnoreCase("true")) {
                    popBuilder.dismiss();
                } else {
                    ShowToast(res.errormsg);
                }
            }

            @Override
            public Object underProcess() throws Exception {
                return web.submitPopupStatusToServer(schemeId, status);
            }
        });

        bp.execute();
    }
    private void checkForPendingAttendance() {
        MainActivity.Database.LoadAttendancesFromDb();

        if (MainActivity.MyAttendances.Any("IsOfflineOnly", "true")) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Attendance Pending")
                    .setMessage("You have Attendance Pending ,Would You Like To Update Them Now ?")
                    .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    putDataAsBoolean(IS_SOURCE_DIALOG, true);
                                    startActivity(DatTab.class);
                                }
                            })
                    .setNegativeButton(android.R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // do nothing
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }
    private void saveVersionUpdationFromServerToLocal() {
        if (isNetworkAvailable()) {
            checkForAppVersionOnServer();
            checkForuserOnServer();
        }
        //loadVersionDetailAndReflect();
    }

    // -------------------Products fetching methods--------------//
    // -----------------------------------------------------//
    // -----------------------------------------------------//

    /*private void loadVersionDetailAndReflect() {
        ArrayList<VersionUpdationModel> versList = db.FetchAllData(new VersionUpdationModel());
        if (versList.size() <= 0)
            return;
        for (VersionUpdationModel arrVersion : versList) {
            if (ToDouble(getCurrentVersion()) > ToDouble(arrVersion.AppVersion))
                return;

            if (findViewById(R.id.updation_layout) == null) {
                View view = Storage.getView(getApplicationContext(),
                        R.layout.updation_layout);
                addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            }
            // System.out.println("Date in milli :: " + timeInMilliseconds);
            if (Integer.valueOf(arrVersion.Days) <= 0) {

                findViewById(R.id.update_mandatory).setVisibility(View.VISIBLE);
                findViewById(R.id.update_or_not).setVisibility(View.GONE);

                ((TextView) findViewById(R.id.version_msg_tv)).setText("" + arrVersion.Message);
                findViewById(R.id.update_version).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                goToPlayStoreToDownloadApp();

                            }
                        });

                backpressActive = false;
                *//*
                 * int dateLeft = getDateLeft(); String date =
                 * GetCurrentDateInString(); ShowToast(date);
                 *//*
            } else {
                findViewById(R.id.update_mandatory).setVisibility(View.GONE);
                findViewById(R.id.update_or_not).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.version_msg_tv1)).setText(""
                        + arrVersion.Message);
                findViewById(R.id.cancel).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                findViewById(R.id.updation_layout)
                                        .setVisibility(View.GONE);

                            }
                        });
                findViewById(R.id.update).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                goToPlayStoreToDownloadApp();
                            }
                        });
                backpressActive = true;
            }
        }
    }*/

    protected void checkLatestVersionAndShowAlert(VersionUpdationModel verResponse) {
        // ArrayList<VersionUpdationModel> versList = db.FetchAllData(new VersionUpdationModel());
        if (verResponse.force_update.equalsIgnoreCase("true"))
            showConfirmationDialog("1");
        else if (verResponse.soft_update.equalsIgnoreCase("true"))
            showConfirmationDialog("2");

    }
    AlertDialog.Builder alertDialogBuilder = null;
    private void showConfirmationDialog(String number) {
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("New version is available!");
        if(number.equalsIgnoreCase("1"))
            alertDialogBuilder.setCancelable(false);
        else
            alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setMessage("Click yes to install!")

                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {

                                goToPlayStoreToDownloadApp();

                            }
                        });
        if(number.equalsIgnoreCase("2"))
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    finish();
                }
            });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void checkForAppVersionOnServer() {
        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
        bp.setbackgroundProcess(new IProcess() {

            @SuppressWarnings("rawtypes")
            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                processMinAppVersionResponse((Response) arg0);

            }

            @Override
            public Object underProcess() throws Exception {

                return web.checkLatestAppVersionOnPlaystore(getCurrentVersion());
            }
        });

        bp.execute();

    }
    protected void processMinAppVersionResponse(Response response) {

            // TODO Auto-generated method stub
            Response res = (Response)response;
            ArrayList<VersionUpdationModel> resdata = (ArrayList<VersionUpdationModel>) res.data;
            if (resdata != null && resdata.size() > 0) {
                checkLatestVersionAndShowAlert(resdata.get(0));
                Log.e("daaaa", resdata.get(0).toString());

            hideProgress();
        } else {
            new Dialog(this).setTitle("Error").show(response.errormsg);
        }

    }
    private void checkForuserOnServer() {
        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
        bp.setbackgroundProcess(new IProcess() {

            @SuppressWarnings("rawtypes")
            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                processuseractiveResponse((Response) arg0);

            }

            @Override
            public Object underProcess() throws Exception {

                return web.GetISPActiveStatus();
            }
        });

        bp.execute();

    }
    protected void processuseractiveResponse(Response response) {

        // TODO Auto-generated method stub
        Response res = (Response)response;
        ArrayList<VersionUpdationModel> resdata = (ArrayList<VersionUpdationModel>) res.data;
        if (resdata != null && resdata.size() > 0) {
         Log.e("Daraaa",resdata.get(0).IsActive+"nio");
          if(resdata.get(0).IsActive.equalsIgnoreCase("false")) {

              goToLogin();
              hideProgress();
          }
        } else {
            new Dialog(this).setTitle("Error").show(response.errormsg);
        }

    }
    private void goToPlayStoreToDownloadApp() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + getPackageName())));
        }
    }
    public String GetDocIDs(String GUID) {
        String AllDocIDs = "";
        ArrayList<String> DocIDs = PendingImages.where("ID", GUID).Select(
                "DocID");
        for (String id : DocIDs) {
            AllDocIDs = "," + id + AllDocIDs;
        }
        return AllDocIDs;
    }
    private void checkForProducts() {
        if (MyProductList.Count() < 1)
            new ProductUpdater(this).UpdateProducts();

    }

    private void checkForBrand() {
        if (MyBrand.Count() < 1)
            new ProductUpdater(this).UpdateBrandMapping();

    }

    private static final String TAG = "MainActivity";

    private void checkForLogin() {
        if (User.isUserLoggedIn()) {
            Log.d(TAG, "checkForLogin: " + MyInfo);
            // Note -> if username which you have inserted into the EditText in LoginScreen does not match the
            // EmployeeCode(which we get from server when we hit login api) than LoadMyInfoFromLocalDb re initialize the
            // MyInfo() and MyInfo.EmployeeCode = "000000" becomes true and then we have to login again and again.
            if (!Database.LoadMyInfoFromLocalDb(User.GetUserName())) {
                ShowToast("Local login is not active!!!");
                User.RemoveLocalLogin();
                goToLogin();
                return;
            }
            if (MyInfo.EmployeeCode.equals("000000")) {
                goToLogin();
                return;
            }
            if (MyInfo.ISPName != null) {
                ShowToast("Welcome" + " " + MyInfo.V5ID);
                tvName.setText(MyInfo.ISPName);
                tvV5ID.setText(MyInfo.V5ID);
                tvUSerID.setText(MyInfo.UserID);
            }
            new WebService().setUser(User.GetUserName(), User.GetPassword());

            Database.LoadDbData();
            dataForTraining.putString(TrainingConstants.USER_ID, MyInfo.UserID);
            dataForTraining.putBoolean(TrainingConstants.IS_TRAINER, MyInfo.TrainingType.equalsIgnoreCase("Trainer"));
            checkForProducts();
//            checkForBrand();
            // checkForConfigUpdationRequire();
//            apiCallToGetAllNotification();
            if (MyInfo.TrainingType.equalsIgnoreCase("Trainer")) {
                gotToTraining();
            }

            return;
        } else
            goToLogin();
    }

    private void gotToTraining() {
        Intent intent = new Intent(this, com.ariston.training_module.modules.training_module.ui.activities.MainActivity.class);
        intent.putExtras(dataForTraining);
        startActivityForResult(intent,1);
       // finish();
    }

    private void apiCallToGetAllNotification() {
        BackgroundProcess bp = new BackgroundProcess(this)
                .setProgressMessage("loading...");
        bp.setbackgroundProcess(new IProcess() {

            @SuppressWarnings("rawtypes")
            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                processNotificationResponse((Response) arg0);
            }

            @Override
            public Object underProcess() throws Exception {
                // TODO Auto-generated method stub

                return web.getAllNotification();
            }
        });

        bp.execute();
    }

    private void processNotificationResponse(Response response) {
        if (response.isSuccess()) {
            ArrayList<MNotification> arrayList = response.data;
            if (arrayList != null && arrayList.size() > 0) {
                boolean isColored = true;
                for (MNotification mNotification : arrayList) {
                    try {
                        View view = CommonUtility.getView(getApplicationContext(),
                                R.layout.notification_onhomepfage);
                        addContentView(view, new LayoutParams(
                                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                        ((TextView) view.findViewById(R.id.title)).setText(mNotification.textTitle);
                        if (mNotification.imageUrl != null && mNotification.imageUrl.length() > 10)
                            try {
                                Glide.with(getApplicationContext()).load(mNotification.imageUrl).into((ImageView) view.findViewById(R.id.image));
                            } catch (Exception e) {

                            }
                        ((TextView) view.findViewById(R.id.text_content)).setText(Html.fromHtml("<html><body style=\"text-align:justify\">" + mNotification.Message + "</body></Html>"));
//                        ((TextView) view.findViewById(R.id.text_content)).setText(mNotification.Message);
                        if (!isColored) {
                            View viewBG = view.findViewById(R.id.background_trans);
                            viewBG.setBackground(null);
                        }
                        isColored = false;
                        View read = view.findViewById(R.id.read);
                        NotificationViewListener myClickListener = new NotificationViewListener(view, MainActivity.this);
                        read.setOnClickListener(myClickListener);
                        DragLinearLayout dragLinearLayout = (DragLinearLayout) view.findViewById(R.id.container);
                        for (int i = 0; i < dragLinearLayout.getChildCount(); i++) {
                            if (i == 2) {
                                View child = dragLinearLayout.getChildAt(i);
                                dragLinearLayout.setViewDraggable(child, child);
                            }// the child is its own drag handle
                        }
//                        dragLinearLayout.setOnViewSwapListener(myClickListener);

//                        dragLinearLayout.setOnDragListener(new MyClickListener(view));
                    } catch (Exception e) {
                        ShowToast(e.getMessage());
                    }
                }
            }
        }
    }


    private void checkForConfigUpdationRequire() {
        new ProductUpdater(this).checkIfConfigUpdateRequire();

    }

    private void goToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        // this.minimizeApp();

        if (backpressActive && findViewById(R.id.updation_layout) != null && findViewById(R.id.updation_layout).getVisibility() == View.VISIBLE) {
            findViewById(R.id.updation_layout).setVisibility(View.GONE);
        } else
            finish();
    }

    public void UpdateProducts(View view) {
        new ProductUpdater(this).UpdateProducts();
    }

    private void SetPolicy() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void gotoMyInfo() {
        Intent intent = new Intent(this, MyInfoActivity.class);
        this.startActivity(intent);
    }

    private void apiCallfordataofdocuments() {
        BackgroundProcess bp = new BackgroundProcess(this).showProgress(false);
        bp.setbackgroundProcess(new IProcess() {

            @SuppressWarnings("rawtypes")
            @Override
            public void processResponse(Object arg0) throws Exception {
                // TODO Auto-generated method stub
                processDoclistResponse((Response) arg0);

            }

            @Override
            public Object underProcess() throws Exception {

                return web.getDoclist();
            }
        });

        bp.execute();
    }

    private void processDoclistResponse(Response response) {
        if (response.isSuccess()) {
            ArrayList<GetDoctypedata> originalList = response.data;
            if (originalList != null && !originalList.isEmpty()) {

                List<GetDoctypedata> filteredList = new ArrayList<>();

                for (GetDoctypedata item : originalList) {
                    if (!"true".equalsIgnoreCase(item.IsSeen)) {
                        filteredList.add(item);
                    }
                }

                if (!filteredList.isEmpty()) {
                    showTrainingMaterialDialog(MainActivity.this, filteredList);
                }
            }
        }
    }

    public void showTrainingMaterialDialog(Context context, List<GetDoctypedata> dataList) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_training_material, null);

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_dialog);
        Button btnClose = dialogView.findViewById(R.id.btn_dialog_close);

        AdapterTrainingMat adapter = new AdapterTrainingMat();
        adapter.addData(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        btnClose.setOnClickListener(v -> dialog.dismiss());
        Log.e("Cww","c");
        dialog.show();
    }



    public void gotoPendingImage() {
        this.startActivityForResult(new Intent(this, ImageGridActivity.class),
                2);
    }

    public void setcolumns(int count) {
        this.setGridColumns(count);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_fullgrid:
                setcolumns(3);
                return true;
            case R.id.action_grd2:
                setcolumns(2);
                return true;
            case R.id.action_grd1:
                setcolumns(1);
                return true;
            case R.id.action_logout:
                this.setUserLogged(false);

                goToLogin();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
        this.RegisterTableForDataEntity(new ProductModel());
        this.RegisterTableForDataEntity(new Viewsalemodel());

        this.RegisterTableForDataEntity(new MDAT());
        this.RegisterTableForDataEntity(new Feedback());
        this.RegisterTableForDataEntity(new Complain());
        this.RegisterTableForDataEntity(new MyInfoModel());
        this.RegisterTableForDataEntity(new ImageInfo());
        this.RegisterTableForDataEntity(new NewSaleModel());
        this.RegisterTableForDataEntity(new MTDSalesModel());
        this.RegisterTableForDataEntity(new LMTDSalesModel());
        this.RegisterTableForDataEntity(new StocksModel());
        this.RegisterTableForDataEntity(new StoreImage());
        this.RegisterTableForDataEntity(new OtherInfoModel());
        this.RegisterTableForDataEntity(new Quiz());
        this.RegisterTableForDataEntity(new MDisplay());
        this.RegisterTableForDataEntity(new MNotification());
        this.RegisterTableForDataEntity(new BrandModel());
        this.RegisterTableForDataEntity(new DocumentModel());
        this.RegisterTableForDataEntity(new attnd_model());
        this.RegisterTableForDataEntity(new CheckoutModel());
        this.RegisterTableForDataEntity(new AadharModel());
        this.RegisterTableForDataEntity(new NeftModel());

        this.RegisterTableForDataEntity(new BrandCategoryModel());
        this.RegisterTableForDataEntity(new ViewDisplayModel());
        this.RegisterTableForDataEntity(new QuizIDTimeDetailModel());
        this.RegisterTableForDataEntity(new VersionUpdationModel());
        this.RegisterTableForDataEntity(new Survey());
        this.RegisterTableForDataEntity(new AppConfigModel());

        this.RegisterTableForDataEntity(new ProfileURLModel());
        this.RegisterTableForDataEntity(new FeedbackUrlConfigModel());
        this.RegisterTableForDataEntity(new MyProfileModel());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.floatmenu, menu);
    }


    @Override
    public int setGridFooter() {

        return R.layout.layout_home_grid_footer;
    }

    @Override
    public void setGridList() {


        if (!IsDataAvilable()) {
            Toast.makeText(this, "No items found to display", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        GenricAdapter adapter = new GenricAdapter(this, R.layout.layout_gv_home).setData(data);
        adapter.setGenricAdapter(new IAdapter() {
            @Override
            public void setItemView(Object item, View view, int index) {
                GridItem item_ = (GridItem) item;
                ImageView iv = view.findViewById(R.id.iv_grid);
                RobotoTextView tv = view.findViewById(R.id.tv_label);
                iv.setImageResource(item_.getIcon());
                tv.setText(item_.getTitle());

            }
        });
        grd.setAdapter(adapter);

        grd.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub


                Log.e("ssss","DDDDDD");
                gotToItemActivity(data.get(arg2));
            }
        });

    }

    @Override
    public void TrysetGridFooter() {
        super.TrysetGridFooter();

        /*PushDownAnim.setPushDownAnimTo(footerview.findViewById(R.id.iv_notification), footerview.findViewById(R.id.iv_survey),
                footerview.findViewById(R.id.iv_hr_helpline), footerview.findViewById(R.id.iv_settings), footerview.findViewById(R.id.iv_fl_log_out)
                , footerview.findViewById(R.id.bt_corona_bot)).setOnClickListener(this);*/
        PushDownAnim.setPushDownAnimTo(footerview.findViewById(R.id.iv_notification),
                footerview.findViewById(R.id.iv_hr_helpline), footerview.findViewById(R.id.iv_settings), footerview.findViewById(R.id.iv_fl_log_out)
                ).setOnClickListener(this);
    }


    @Override
    public int setGridHeader() {

        return R.layout.app_header;
    }

    @Override
    public int setGridbackground() {

        return android.R.color.white;
    }

    // private void initialize() {
    // message_button = (Button) findViewById(R.id.tvmsg);
    // }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ArrayList<GridItem> setGridItems() {

        ArrayList<GridItem> data = new ArrayList<GridItem>();

        data.add(new GridItem().setItem("Mark IN", MarkAttendanceActivity.class, R.drawable.ic_mark_in));
        data.add(new GridItem().setItem("Mark Out", CheckoutActivity.class, R.drawable.ic_mark_out));
        data.add(new GridItem().setItem("View Attendance", DatTab.class, R.drawable.ic_view_attendance));
        data.add(new GridItem().setItem("Enter Sale", SaleTabs.class, R.drawable.ic_enter_sale));
        data.add(new GridItem().setItem("Model Display", ModuleTab.class, R.drawable.ic_enter_sale));
        data.add(new GridItem().setItem("Down Stock", DownStock.class, R.drawable.ic_enter_sale));
        data.add(new GridItem().setItem("Stock", StockTabs.class, R.drawable.ic_enter_stock));
        //data.add(new GridItem().setItem("Notification", NotificationView.class, R.drawable.inbox));
        data.add(new GridItem().setItem("Training", com.ariston.training_module.modules.training_module.ui.activities.MainActivity.class, R.drawable.ic_training, dataForTraining));
        //data.add(new GridItem().setItem("Dashboard", com.fieldforce.training_module.modules.dashboard.ui.activities.DashboardActivity.class, R.drawable.target_grid_item, dataForTraining));
       data.add(new GridItem().setItem("Incentive", NewTrainingActivity.class, R.drawable.ic_incentive));
       data.add(new GridItem().setItem("Sales Pitch", SalesPitch.class, R.drawable.ic_sales_pitch));
       data.add(new GridItem().setItem("Target vs Achievements", TargetScreenManagerActivity.class, R.drawable.ic_target_vs_achievement));
       data.add(new GridItem().setItem("Display Compliance", HygieneStoreListActivity.class, R.drawable.ic_floor_hygiene));
       data.add(new GridItem().setItem("Survey Form", SurveytypeActivity.class, R.drawable.ic_floor_hygiene));
       data.add(new GridItem().setItem("Counter share\n(MTD)", CompetitionTab.class, R.drawable.ic_floor_hygiene));

 /* data.add(new GridItem().setItem("Branding", BrandingActivity.class,
                R.drawable.ic_branding));*/
 // data.add(new GridItem().setItem("Feedback", FeedbackActivity.class,
        // R.drawable.feedback_new));
 /* data.add(new GridItem().setItem("Last 7 days Sale",
                SaleScreenManagerActivity.class, R.drawable.seven_days_sale));*/
        data.add(new GridItem().setItem("Demo Tracking", DemoTrackingFragmentsContainer.class, R.drawable.ic_demo_tracking));


//        data.add(new GridItem().setItem("HR Helpline", HelpLine.class, R.drawable.help));

        /*data.add(new GridItem().setItem("Stock", ViewStockActivity.class,
                R.drawable.view_sale_new));*/


//        data.add(new GridItem().setItem("Corona Bot", CoronaActivity.class,
//                R.drawable.corona_icon));


//        data.add(new GridItem().setItem("Corona Survey", CoronaSurvey_Tabs.class, R.drawable.survey));

        // data.add(new GridItem().setItem("Corona Survey", CoronaSurveyTabsActivity.class, R.drawable.survey));

//        data.add(new GridItem().setItem("Training Module", com.fieldforce.training_module.modules.training_module.ui.activities.MainActivity.class, R.drawable.survey_icon));

        // data.add(new GridItem().setItem("My Profile", MyInfoActivity.class,
        // R.drawable.profile_new));
/*        data.add(new GridItem().setItem("Display", DisplayTab.class,
                R.drawable.display_icon));*/
        /*data.add(new GridItem().setItem("Update NEFT", NeftActivity.class,
                R.drawable.markattendance_new));
        data.add(new GridItem().setItem("Update Aadhar/Pan",
                AadharActivity.class, R.drawable.markattendance_new));
        data.add(new GridItem().setItem("My quiz", QuizDisplayActivity.class,
                R.drawable.quiz_new));*/
/*        data.add(new GridItem().setItem("Training Module",
                TrainningModule.class, R.drawable.survey_icon));*/

/*        data.add(new GridItem().setItem("Survey", SurveyDisplayActivity.class,
                R.drawable.survey_icon));

        data.add(new GridItem().setItem("Test Bar Code Scanner", null, R.drawable.barcode_scanner)
                .Call(new Callable() {
                    @Override
                    public Object call() throws Exception {

                        Intent i = new Intent(MainActivity.this, BarcodeScannerActivity.class);
                        startActivityForResult(i, BarcodeScannerActivity.REQUEST_CODE);

                        return null;
                    }
                }));*/

        /*data.add(new GridItem().setItem("Helpdesk", Helpdesk.class,
                R.drawable.feedback_new));*/

/*//        data.add(new GridItem().setItem("Settings", SettingActivity.class,
//                R.drawable.settings_new));

//        data.add(new GridItem().setItem("Logout", null, R.drawable.logout_new)
//                .Call(new Callable() {
//                    @Override
//                    public Object call() throws Exception {
//                        logout();
//                        return null;
//                    }
//                }));*/
        isFeedbackUrlConfigModelLoaded();
        isProfileUrlModelLoaded();
        return data;
    }

    public void message(View v) {
        Intent intent = new Intent(this, NotificationView.class);
        startActivity(intent);
    }

    private void logout() {
        setUserLogged(false);
        goToLogin();
    }

    private void capturePhoto() {
        try {
            Intent I = new Intent(MainActivity.this, ImageCaptureActivity.class);
            I.putExtra(ImageCaptureActivity.PARAMS_DOC_TYPE, "StoreImage");
            I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
            I.putExtra(ImageCaptureActivity.PARAMS_GUID, Database
                    .getStoreImageGUIDForToday(GetCurrentDateInString()).guid);
            startActivity(I);
        } catch (Exception ex) {
            ShowToastLong(ex.getMessage(), 0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //       notificationViewModule = ViewModelProviders.of(this).get(NotificationViewModule.class);
//        notificationViewModule.getSurveyHistoryData();

        /*if (!Fabric.isInitialized()) {
            Fabric.with(this, new Crashlytics());

            if (WebService.UserName != null) {
                Crashlytics.setUserName(WebService.UserName);
            }
        }*/
        LocalStorage localStorage=new LocalStorage(this);
        localStorage.setMessage("add","");
        saveVersionUpdationFromServerToLocal();
        if (isNetworkAvailable()) {
            apiCallfordataofdocuments();
            preparePopupTask();

        } else {
            ShowToast("No internet...");
        }

        appVersionController = new AppVersionController(this);
        appVersionController.checkPlayStoreForUpdate();


        //String str = response.errormsg;

        //Log.i("abhi",str);

        //Toast.makeText(this,""+str,Toast.LENGTH_SHORT).show();
        //web.getStockUpdateStatus(MainActivity.this);

        if (isNetworkAvailable()) {

            BackgroundProcess backgroundProcess = new BackgroundProcess(MainActivity.this);
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return web.getStockUpdateStatus();

                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response res = (Response) response;
                    if (res.status.equalsIgnoreCase("true"))
                        new Dialog(MainActivity.this).setTitle("Stock Notification").setMessage(res.errormsg).show();
                }
            });
            backgroundProcess.execute(null, null, null);

        }


        if (isNetworkAvailable()) {

            BackgroundProcess backgroundProcess = new BackgroundProcess(MainActivity.this);
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return web.getNotification();

                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response res = (Response) response;
                    if (res.status.equals("true")) {
                        ArrayList<NotificationResonseMode> arrDocModel = res.data;
                        if (arrDocModel != null && arrDocModel.size() > 0) {
                            notificationlist.setVisibility(View.VISIBLE);
                            notificationAdpter.setNotificationdata(arrDocModel);

                        } else {
                            notificationlist.setVisibility(View.GONE);

                        }


                    }


                }
            });
            backgroundProcess.execute(null, null, null);

        }





         // loadVersionDetailAndReflect();
        /*try {
            // toastForLastestVersion();
			new GetAppVersion(this, new AppIntrfce() {
				@Override
				public void dialog(String dialog) {
					showDialogBox(dialog);
				}
			}).execute();
			// new VersionHandler(this).tryUpdateApp(false);
		} catch (Exception ex) {
		}
		checkForConfigUpdationRequire();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==-1)
        {
           logout();
        }
        else
        {
            onBackPressed();
        }
        switch (requestCode) {

            //Response From Bar code Scanner

            case BarcodeScannerActivity.REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {

                    switch (data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_FORMAT)) {
                        case "CODE_128":

                            String scannedCode = data.getStringExtra(BarcodeScannerActivity.SCAN_RESULT_DATA);
                            new Dialog(MainActivity.this).show("Scanned Code : " + scannedCode);

                            break;



                        default:
                            new Dialog(MainActivity.this).show("Invalid Bar Code Scanned");
                    }

                }

                else if (resultCode == Activity.RESULT_CANCELED)
                    new Dialog(MainActivity.this).show("Bar Code Scanning Cancelled");


                break;
        }
    }

    private void showDialogBox(String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        openActivityOnPlayStore();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void openActivityOnPlayStore() {
        String packageName = getPackageName();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + packageName));
        } catch (android.content.ActivityNotFoundException anfe) {
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public void Activate() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_survey:
                Helper.openNextActivity(MainActivity.this, CoronaSurvey_Tabs.class, null, false, -1);
                break;
            case R.id.iv_hr_helpline:
                Helper.openNextActivity(MainActivity.this, HelpLine.class, null, false, -1);
                break;
            case R.id.iv_settings:
                Helper.openNextActivity(MainActivity.this, SettingActivity.class, null, false, -1);
                break;
            case R.id.iv_fl_log_out:
                logout();
                break;
            case R.id.bt_corona_bot:
                Helper.openNextActivity(MainActivity.this, CoronaActivity.class, null, false, -1);
                break;
            case R.id.iv_notification:
                Helper.openNextActivity(MainActivity.this, NotificationView.class, null, false, -1);
                break;

        }
    }

    @Override
    public void updateSeenData(NotificationResonseMode obj, int position) {
        if (isNetworkAvailable()) {

            BackgroundProcess backgroundProcess = new BackgroundProcess(MainActivity.this);
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return web.updateSeenNotification(obj, MyInfo.UserID);

                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response res = (Response) response;
                    if (res.status.equals("true")) {
                        notificationAdpter.removeItem(position);
                    }
                }
            });
            backgroundProcess.execute(null, null, null);

        }
    }

    private boolean isFeedbackUrlConfigModelLoaded() {
        try {
            ArrayList<FeedbackUrlConfigModel> feedbackUrlModelArr = db
                    .FetchAllData(new FeedbackUrlConfigModel()).where("username",
                            User.GetUserName());
            if (feedbackUrlModelArr != null && feedbackUrlModelArr.size() > 0) {
                feedbackURLConfig = feedbackUrlModelArr.Last();
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean isProfileUrlModelLoaded() {
        try {
            ArrayList<ProfileURLModel> profileUrlModelArr = db.FetchAllData(
                    new ProfileURLModel()).where("username", User.GetUserName());
            if (profileUrlModelArr != null && profileUrlModelArr.size() > 0) {
                profileUrlModel = profileUrlModelArr.Last();
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
