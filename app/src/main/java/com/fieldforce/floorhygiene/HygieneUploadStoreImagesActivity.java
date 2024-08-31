package com.fieldforce.floorhygiene;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fieldforce.harmonkardonff.ImageCaptureActivity;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SimpleCameraActivity;
import com.fieldforce.model.GetFloorHygieneType;
import com.ariston.training_module.utility.widgets.RobotoTextView;
import com.fieldforce.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.ImageCaptureResult;
import app.core.model.Response;
import linq.ArrayList;
import mob.field.harmonkardonff.services.WebService;

/**
 * Created by deepakkanyan on 21/07/20 at 2:56 PM.
 */
public class HygieneUploadStoreImagesActivity extends InnosolsActivity {


    private ImageView imageCap1;
    private ImageView imageCap2;
    private ImageView imageCap3;
    private TextView tvImg1;
    private TextView tvImg2;
    private TextView tvImg3;
    private ImageView selectedView;
    private Spinner selectedType;
    private RobotoTextView tvStoreName;
    //
    Spinner sp3, sp2, sp;
    private String docIDs = "";
    private WebService webService;
    private RobotoTextView tvDate;
    ArrayList<String> GetFloorHygieneType = new ArrayList<>();
    ArrayList<GetFloorHygieneType> arrDocModel = new ArrayList<>();

    @Override
    public void RegisterTableInfoForLocalDB() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_hygiene_edit);


        imageCap1 = findViewById(R.id.image1Cap);
        imageCap2 = findViewById(R.id.image2Cap);
        imageCap3 = findViewById(R.id.image3Cap);
        tvStoreName = findViewById(R.id.tv_store_name);
        tvDate = findViewById(R.id.txtDate);
        tvImg1 = findViewById(R.id.tv_img_1);
        tvImg2 = findViewById(R.id.tv_img_2);
        tvImg3 = findViewById(R.id.tv_img_3);


        imageCap1.setOnClickListener(selectedView1 -> capturePhoto((ImageView) selectedView1));
        imageCap2.setOnClickListener(selectedView2 -> capturePhoto((ImageView) selectedView2));
        imageCap3.setOnClickListener(selectedView3 -> capturePhoto((ImageView) selectedView3));
        initDatePicker(R.id.rlDateSelect);
        tvStoreName.setText(MainActivity.MyInfo.CurrentStore);
        if (webService == null) {
            webService = new WebService();
        }

        if (isNetworkAvailable()) {

            BackgroundProcess backgroundProcess = new BackgroundProcess(HygieneUploadStoreImagesActivity.this);
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return webService.getFloorHygieneType();

                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response res = (Response) response;
                    if (res.status.equals("true")) {
                        arrDocModel = res.data;
                        for (GetFloorHygieneType getFloorHygieneType : arrDocModel) {
                            GetFloorHygieneType.add(getFloorHygieneType.TypeName);
                        }
                        getType();
                    }


                }
            });
            backgroundProcess.execute(null, null, null);

        }


        findViewById(R.id.cv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDate.getText().toString().equals("Select Date")) {
                    ShowToast("Please Select Date To Continue!!!!");
                    return;
                }
          if(docIDs!=null&&docIDs.isEmpty()){
              ShowToast("Please Upload At Least A Single Image to Continue!!");
              return;
          }

                if (isNetworkAvailable()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    BackgroundProcess backgroundProcess = new BackgroundProcess(HygieneUploadStoreImagesActivity.this);
                    backgroundProcess.setbackgroundProcess(new IProcess() {
                        @Override
                        public Object underProcess() throws Exception {
                            return webService.addStoreHygieneData(MainActivity.MyInfo.UserID, docIDs,
                                    MainActivity.MyInfo.StoreID, simpleDateFormat1.format(simpleDateFormat.parse(tvDate.getText().toString())));

                        }

                        @Override
                        public void processResponse(Object response) throws Exception {
                            Response res = (Response) response;
                            if (res.status.equalsIgnoreCase("true")) {
                                Intent intent = new Intent();
                                intent.putExtra(Constants.REFRESH_LIST, true);
                                setResult(Activity.RESULT_OK, intent);
                                HygieneUploadStoreImagesActivity.this.finish();
                            }
                            if (res.errormsg != null && !res.errormsg.isEmpty()) {
                                ShowToast(res.errormsg);
                            }
                        }
                    });
                    backgroundProcess.execute(null, null, null);

                }


            }
        });
        findViewById(R.id.icon_back_btn).setOnClickListener(v -> finish());


    }


    private void capturePhoto(ImageView selectedView) {
        try {

            this.selectedView = selectedView;

            Intent I = new Intent(this, SimpleCameraActivity.class);
            I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Hygiene");
            I.putExtra(SimpleCameraActivity.PARAMS_USERNAME, User.GetUserName());
            I.putExtra(SimpleCameraActivity.PARAMS_GUID, MainActivity.MyInfo.guid);
            startActivityForResult(I, 1);
        } catch (Exception ex) {
            ShowToastLong(ex.getMessage(), 0);
        }

    }

    private void getType() {
        sp = (Spinner) this.findViewById(R.id.spin1Cap);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GetFloorHygieneType);
        sp.setAdapter(spinnerArrayAdapter);

        sp2 = (Spinner) this.findViewById(R.id.spin2Cap);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GetFloorHygieneType);
        sp2.setAdapter(spinnerArrayAdapter2);

        sp3 = (Spinner) this.findViewById(R.id.spin3Cap);
        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, GetFloorHygieneType);
        sp3.setAdapter(spinnerArrayAdapter3);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            ImageCaptureResult result = getData(ImageCaptureActivity.IMAGE_CAPTURE_RESULT);

                if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

                    String GUID = data.getExtras().getString(SimpleCameraActivity.PARAMS_GUID);
                    String IMAGE_CAPTURED = data.getExtras().getString("IMAGE_CAPTURED") + "";
                    String DocID = data.getExtras().getString("DocID") + "";

                if(!IMAGE_CAPTURED.equals("")) {
                    selectedView.setOnClickListener(null);
                    selectedView.setImageResource(R.drawable.done);
                }
                String TypeID = "";
                if (selectedView.getId() == R.id.image1Cap &&!IMAGE_CAPTURED.equals("")) {
                    TypeID = sp.getSelectedItem().toString();
                    findViewById(R.id.ll_sp1).setVisibility(View.GONE);
                    tvImg1.setText("Uploaded");
                }
                if (selectedView.getId() == R.id.image2Cap) {
                    findViewById(R.id.ll_sp2).setVisibility(View.GONE);
                    TypeID = sp2.getSelectedItem().toString();
                    tvImg2.setText("Uploaded");
                }
                if (selectedView.getId() == R.id.image3Cap) {
                    findViewById(R.id.ll_sp3).setVisibility(View.GONE);
                    TypeID = sp3.getSelectedItem().toString();
                    tvImg3.setText("Uploaded");
                }
                docIDs += docIDs.isEmpty() ? DocID + "-" + TypeID : "," + DocID + "-" + TypeID;


            } else {
                ShowToast("No Image Uploaded!!!");
            }

        }
    }

    private void initDatePicker(int idForDateButton) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.US);

        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar
                .get(Calendar.MONTH), newCalendar
                .get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.setCancelable(false);
        findViewById(idForDateButton).setOnClickListener(
                arg0 -> fromDatePickerDialog.show());
    }
}





