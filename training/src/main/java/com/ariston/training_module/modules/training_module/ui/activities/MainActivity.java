package com.ariston.training_module.modules.training_module.ui.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.models.TrainingModel;
import com.ariston.training_module.modules.training_module.view_models.TrainingViewModel;
import com.ariston.training_module.utility.ColorConstants;
import com.ariston.training_module.utility.EditTextValidationsRx;
import com.ariston.training_module.utility.GenericRecyclerAdapter;
import com.ariston.training_module.utility.Helper;
import com.ariston.training_module.utility.Resource;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.VerticalItemDecorator;
import com.ariston.training_module.utility.services.UserDetailsService;
import com.ariston.training_module.utility.widgets.RobotoBoldTextView;
import com.ariston.training_module.utility.widgets.RobotoEditText;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TrainingViewModel mViewModel;
    private GenericRecyclerAdapter<TrainingModel> mAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CardView cardViewStartDateContainer;
    private CardView cardViewEndDateContainer;
    private CardView cardViewNoDateContainer;
    private RobotoTextView textViewStartDate;
    private RobotoTextView textViewEndDate;
    private RobotoTextView textViewErrorMessage;
    private RobotoEditText editTextSearch;
    private ImageView imageViewFilters;
    private List<TrainingModel> mList = new ArrayList<>();

    //----------------recycler view itemViews ---------------

    View itemView;
    ConstraintLayout constraintLayout;
    RobotoBoldTextView trainingTitle;
    RobotoTextView trainerName;
    RobotoBoldTextView date;
    RobotoBoldTextView time;
    RobotoBoldTextView trainingType;
    ImageButton imageButtonRightArrow, logout;

    private static final String TAG = "MainActiOO";
    private UserDetailsService boundService;
    private boolean isBound;
    private ImageView ivChecked;
    private int screenWidth=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_training);
        screenWidth = Helper.getScreenWidth(this);
        initObjects();
        initWidgets();
        initListener();
        initRecycler();
        observeViewModel();
        initDateProcess();
        initService();
        Log.d(TAG, "ScreenWidth: "+Helper.getScreenWidth(this));
    }

    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
        }
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);


    }

    private void initObjects() {
        mViewModel = ViewModelProviders.of(this).get(TrainingViewModel.class);
        mAdapter = new GenericRecyclerAdapter<>(this, R.layout.training_item);
    }

    private void initWidgets() {
        imageViewFilters = findViewById(R.id.iv_filters);
        editTextSearch = findViewById(R.id.et_search);
        cardViewStartDateContainer = findViewById(R.id.cv_startDateContainerInTraining);
        cardViewEndDateContainer = findViewById(R.id.cv_endDateContainerInTraining);
        cardViewNoDateContainer = findViewById(R.id.cv_noDataContainer);
        textViewStartDate = findViewById(R.id.tv_selectDateTitle);
        textViewEndDate = findViewById(R.id.tv_selectEndDateTitle);
        textViewErrorMessage = findViewById(R.id.rtv_errorMessage);
        progressBar = findViewById(R.id.pb_inTraining);
        recyclerView = findViewById(R.id.rv_inTraining);
        logout = findViewById(R.id.iv_logout);
    }

    private void initListener() {
        imageViewFilters.setOnClickListener(v -> prepareFilterDialog());
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                onBackPressed();

            }
        });
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (screenWidth>500)
            recyclerView.addItemDecoration(new VerticalItemDecorator(30));
        else
            recyclerView.addItemDecoration(new VerticalItemDecorator(10));
        recyclerView.setAdapter(mAdapter);
    }

    private void observeViewModel() {


        mViewModel.getListLiveDataToObserve().observe((LifecycleOwner) this, new Observer<Resource<List<TrainingModel>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<TrainingModel>> listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING: {
                            showView(progressBar);
                            hideView(cardViewNoDateContainer);
                            hideView(recyclerView);
                            break;
                        }
                        case ERROR: {
                            hideView(progressBar);
                            textViewErrorMessage.setText(listResource.message);
                            showView(cardViewNoDateContainer);
                            hideView(recyclerView);
                            Log.d(TAG, "ERROR : " + listResource.message);
                            break;
                        }
                        case SUCCESS: {
                            mList = listResource.data;
                            mAdapter.addData(mList);
                            manageItemViews();
                            List<TrainingModel> copyList = new ArrayList<>(mList);
                            setSearchListener(copyList);
                            Log.d(TAG, "SUCCESS : " + mList.size());
                            hideView(progressBar);
                            hideView(cardViewNoDateContainer);
                            showView(recyclerView);
                            break;
                        }
                    }

                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void manageItemViews() {
            mAdapter.setBindListener((trainingObject, viewHolder) -> {
            itemView = viewHolder.itemView;
            CircleImageView circleImageView = itemView.findViewById(R.id.civ_training);
            constraintLayout = itemView.findViewById(R.id.cl_parentInTrainingItem);
            trainingTitle = itemView.findViewById(R.id.rbtv_trainingTitle);
            trainerName = itemView.findViewById(R.id.rtv_trainerName);
            date = itemView.findViewById(R.id.rtv_dateValueInTrainingItem);
            time = itemView.findViewById(R.id.rbtv_timeInTrainingItem);
            trainingType = itemView.findViewById(R.id.rbtv_trainingTypeInTrainingItem);
            imageButtonRightArrow = itemView.findViewById(R.id.ib_rightArrowInTrainingItem);
            ivChecked = itemView.findViewById(R.id.iv_check);
            ivChecked.setVisibility(trainingObject.getTrainingStatus() != null && trainingObject.getTrainingStatus().equals("Passed") ? View.VISIBLE : View.GONE);
            circleImageView.setBackgroundResource(R.drawable.ball);
            trainingTitle.setText(trainingObject.getTrainingName());
            trainerName.setText(String.format("Trainer Name : %s", trainingObject.getTrainerName()));
            if(!trainingObject.getEndDate().equals(""))
             date.setText(trainingObject.getStartDate() + "-" + trainingObject.getEndDate());
            else
                date.setText(trainingObject.getStartDate());
            time.setText(trainingObject.getDuration());
            trainingType.setText("Training type: " + trainingObject.getTrainingType());
            manageItemViewsColor(viewHolder.getAdapterPosition());

        });
        mAdapter.setItemClickListener((obj, holderObject) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(TrainingConstants.TR_DESC_ITEM, obj);
            boundService.getUserBundle().putString(TrainingConstants.TRN_ID, obj.getTrainingId());
            if(!obj.getTrainingType().equals("Calender"))
            {
                if (boundService.getUserBundle().getBoolean(TrainingConstants.IS_TRAINER)) {
                    Helper.openNextActivity(MainActivity.this, TrainingDescriptionActivity.class, bundle, false, false, -1);
                } else {
                    Helper.openNextActivity(MainActivity.this, TrainingDescriptionActivity.class, bundle, true, false, 1087);
                }
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_calender_type, viewGroup, false);
                TextView messge=dialogView.findViewById(R.id.type);
                Button buttonOk=dialogView.findViewById(R.id.buttonOk);
                messge.setText(" Kindly attend the training session as per the mentioned schedule");
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.hide();
                    }
                });

                alertDialog.show();
            }

        });
    }

    private void manageItemViewsColor(int position) {
        switch (position % 4) {
            case 0: {
                changingViewsColor(ColorConstants.LIGHT_BLUE, ColorConstants.DARK_BLUE);
                break;
            }
            case 1: {
                changingViewsColor(ColorConstants.LIGHT_PURPLE, ColorConstants.DARK_PURPLE);
                break;
            }
            case 2: {
                changingViewsColor(ColorConstants.LIGHT_ORANGE, ColorConstants.DARK_ORANGE);
                break;
            }
            case 3: {
                changingViewsColor(ColorConstants.LIGHT_GREEN, ColorConstants.DARK_GREEN);
                break;
            }
        }
    }

    private void changingViewsColor(String lightColor, String darkColor) {
        constraintLayout.setBackgroundColor(Color.parseColor(lightColor));
        imageButtonRightArrow.setBackgroundColor(Color.parseColor(lightColor));
        trainingTitle.setTextColor(Color.parseColor(darkColor));
        trainerName.setTextColor(Color.parseColor(darkColor));
        date.setTextColor(Color.parseColor(darkColor));
        time.setTextColor(Color.parseColor(darkColor));
        trainingType.setTextColor(Color.parseColor(darkColor));
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }


    //------------------------------------- Date Process Start ------------------------------------

    private String startDate;
    private String endDate;

    private void initDateProcess() {
        initializeDatesAndServerRequest();
        startDateProcess(R.id.tv_selectDateTitle, R.id.cv_startDateContainerInTraining);
        endDateProcessed(R.id.tv_selectEndDateTitle, R.id.cv_endDateContainerInTraining);
    }


    private void initializeDatesAndServerRequest() {
        // TODO Auto-generated method stub
        initializeStartDate();
        initializeEndDate();

    }

    private void initializeStartDate() {
        String date = TrainingConstants.GetStartDateInString();
        startDate = date;
        textViewStartDate.setText("From: " + date);
    }

    private void initializeEndDate() {

        String date = TrainingConstants.GetEndDateInString();
        endDate = date;
        textViewEndDate.setText("To: " + endDate);
    }


    private void startDateProcess(int idForDateTxt, int idForDateButton) {
        final TextView dateText = (TextView) findViewById(idForDateTxt);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.US);
        final SimpleDateFormat apiFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText("From: " + dateFormatter.format(newDate.getTime()));
                startDate = dateFormatter.format(newDate.getTime());

                if (endDate == null || endDate.equalsIgnoreCase(""))
                    showToast("Please Enter To Date");

                else {
                    hitapi(startDate, endDate);
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar
                .get(Calendar.MONTH), newCalendar
                .get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.setCancelable(false);
        findViewById(idForDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        fromDatePickerDialog.show();
                    }
                });
    }

    private void endDateProcessed(int idForDateTxt, int idForDateButton) {
        final TextView dateText = (TextView) findViewById(idForDateTxt);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.US);
        final SimpleDateFormat apiFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(String.format("To: %s", dateFormatter.format(newDate.getTime())));
                endDate = dateFormatter.format(newDate.getTime());
                if (startDate != null
                        && !startDate.equalsIgnoreCase(""))
                    hitapi(startDate, endDate);
                else
                    showToast("Please Enter From Date");

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar
                .get(Calendar.MONTH), newCalendar
                .get(Calendar.DAY_OF_MONTH));
        endDatePickerDialog.setCancelable(false);
        findViewById(idForDateButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        endDatePickerDialog.show();
                    }
                });
    }


    //------------------------------------- Date Process End -------------------------------------------**********************


    private RobotoTextView textViewAscending;
    private RobotoTextView textViewDescending;
    private TextView textViewOk;

    private void prepareFilterDialog() {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.asc_desc_custom_dialog, null);
        textViewAscending = view.findViewById(R.id.rtv_ascending);
        textViewDescending = view.findViewById(R.id.rtv_descending);
        textViewOk = view.findViewById(R.id.tv_okInDialog);
        dialogBuilder.setView(view);
        textViewAscending.setOnClickListener(v -> {
            sortListInAscendingOrder();
            dialogBuilder.dismiss();
        });
        textViewDescending.setOnClickListener(v -> {
            sortListInDescendingOrder();
            dialogBuilder.dismiss();
        });
        textViewOk.setOnClickListener(v -> {
            dialogBuilder.dismiss();
        });
        dialogBuilder.show();
    }

    private void sortListInAscendingOrder() {
        try {
            if (mList != null) {
                if (mList.size() > 1) {
                    Collections.sort(mList);
                    mAdapter.addData(mList);
                    manageItemViews();
                }
            }
        } catch (Exception ex) {
            showToast(ex.getMessage());
        }
    }

    private void sortListInDescendingOrder() {
        if (mList != null) {
            if (mList.size() > 1) {
                List<TrainingModel> newList = new ArrayList<>(mList);
                Collections.reverse(newList);
                mAdapter.addData(newList);
                manageItemViews();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1087) {
            if (data != null && data.getBooleanExtra(TrainingConstants.REFRESH_LIST, false)) {
                hitapi(startDate, endDate);
            }
        }

    }

    private void hitapi(String sDate, String eDate) {
        final SimpleDateFormat apiFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        final SimpleDateFormat normal = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.US);
        try {

            if (boundService.getUserBundle().getBoolean(TrainingConstants.IS_TRAINER))
                logout.setVisibility(View.VISIBLE);

            mViewModel.getListFromServer(boundService.getUserBundle().getString(TrainingConstants.USER_ID), apiFormat.format(normal.parse(sDate)), apiFormat.format(normal.parse(eDate)), "", boundService.getUserBundle().getBoolean(TrainingConstants.IS_TRAINER));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setSearchListener(List<TrainingModel> copyOfList) {
        Observable<String> textWatcherObservable = EditTextValidationsRx.getTextWatcherObservable(editTextSearch);
        try {
            textWatcherObservable.debounce(800, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorReturn(throwable -> {
                        Log.e(TAG, "apply: " + throwable.getMessage());
                        return null;
                    })
                    .subscribe(string -> Observable.fromIterable(copyOfList)
                            .filter(itemInner -> (itemInner.getTrainingName() + itemInner.getTrainerName() + itemInner.getTrainingId()
                                    + itemInner.getTrainingType()).toLowerCase().contains(string.toLowerCase()))
                            .toList()
                            .subscribe(success -> {
                                mAdapter.addData(success);
                                manageItemViews();
                            }, throwable -> {
                            }), throwable -> {
                    });
        } catch (Exception ex) {
            Log.e(TAG, "setSearchListener: " + ex.getMessage());
        }
    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;
            hitapi(startDate, endDate);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            boundService = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(boundServiceConnection);
            Intent intent = new Intent(this, UserDetailsService.class);
            stopService(intent);
            isBound = false;
        }
    }

}
