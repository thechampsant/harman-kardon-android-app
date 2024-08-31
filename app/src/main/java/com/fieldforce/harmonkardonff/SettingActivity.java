package com.fieldforce.harmonkardonff;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.InnosolsActivity;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.BLL.ProductUpdater;
import mob.field.harmonkardonff.entitiymodels.ChangePasswordModel;
import mob.field.harmonkardonff.services.WebService;

public class SettingActivity extends InnosolsActivity {

    ChangePasswordModel cpModel = new ChangePasswordModel();
    EditText NewPassword = null;
    WebService webAPI = new WebService();
    ArrayList<String> Data = new ArrayList<String>();
    RelativeLayout iv_backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        iv_backView = findViewById(com.ariston.training_module.R.id.iv_backView);
        iv_backView.setOnClickListener(view -> onBackPressed());
        setData();
        setList();

    }

    private void setData() {
//		Data.add("My Profile");
        Data.add("Update Products");
        // Data.add("Update Applications");
        Data.add("Clean Application Data");
        Data.add("Change Password");
        Data.add("App Images");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setList() {
        ListView lv = this.getListView(R.id.lv_setting);
        if (lv == null)
            return;
        GenricAdapter adaptor = new GenricAdapter(this,
                R.layout.listitem_setting).setData(Data);
        adaptor.setGenricAdapter(new IAdapter<String>() {

            @Override
            public void setItemView(String item, View view, int index) {
                TextView Name = (TextView) view
                        .findViewById(R.id.txt_name_setting);
                Name.setText(item);

            }

        });

        lv.setAdapter(adaptor);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                handleOnClick(arg2);
            }
        });

    }

    public boolean handleOnClick(int pos) {
        switch (pos) {
            case 0:
                UpdateProducts(null);
                UpdateBrand(null);
                return true;
            // case 1:
            // UpdateApplication(null);
            // return true;
            case 1:
                CleanApplicationData(null);
                return true;
            case 2:
                ChangePasswordDialog(null);
                return true;
            case 3:
                gotoPendingImage();
                return true;
            default:
                return false;

        }
    }

    private void Gotoprofile() {
        Intent i = new Intent(this, MyInfoActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    public void UpdateProducts(View view) {
        new ProductUpdater(this).UpdateProducts();
    }

    public void UpdateBrand(View view) {
        new ProductUpdater(this).UpdateBrandMapping();
    }

    public void UpdateApplication(View view) {
        ShowToastLong("This functionality has been disabled", 0);
        // if (isNetworkFoundDialog())
        // new VersionHandler(this).ForceUpdateApp(
        // MainActivity.MyInfo.version, MainActivity.MyInfo.FileUrl);
    }

    public void CleanApplicationData(View view) {
        showAlert();
    }

    private void showAlert() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Clean application data")
                .setMessage(
                        "It will remove sales/Attendance/images/stock from your App!")
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                try {
                                    MainActivity.Database.ClearDataBase();
                                    ShowToast("Operation Ok!");
                                } catch (Exception ex) {
                                    ShowToast("Unable to clean!");
                                    ShowToastLong(ex.getMessage(), 0);
                                }
                            }

                        }).setNegativeButton(R.string.no, null).show();
    }

    public void ChangePasswordDialog(View view) {
        NewPassword = new EditText(this);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Enter New Password!")
                .setView(NewPassword)
                .setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                ChangePassword();
                            }
                        }).setNegativeButton("Cancel", null).show();
    }

    public void ChangePassword() {
        if (isNetworkFoundDialog() && validate()) {
            BackgroundProcess bp = new BackgroundProcess(this)
                    .setProgressMessage("changing password...");
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {

                    ProcesResponse((Response) arg0);
                }

                @Override
                public Object underProcess() throws Exception {

                    return webAPI.TryChangePassword(cpModel);
                }
            });
            bp.execute(null, null, null);
        }
    }

    @SuppressWarnings("rawtypes")
    public void ProcesResponse(Response response) {

        if (response.status.equalsIgnoreCase("true")) {
            // AppSettings.Current.ChangePassword(cpModel.NewPassword);
            User.ChangePassword(cpModel.NewPassword);
            this.ShowToast("Password changed successfully!");

        } else {

            new Dialog(this).setTitle("Error").show(response.errormsg);
            this.ShowToast("unable to change password!");
        }
    }

    public void homebutton(View v) {
        finish();
    }

    public boolean validate() {
        if (IsNullOrWhiteSpace(NewPassword.getText().toString())) {
            ShowToast("Please enter Password");
            NewPassword.setHint("Enter Valid Password");
            return false;
        } else if (NewPassword.getText().toString().length() < 6) {
            ShowToast("Password must be 6 character long");
            NewPassword.setHint("Password must be 6 character long");
            return false;
        } else {
            cpModel.UserName = MainActivity.MyInfo.EmployeeCode;
            cpModel.NewPassword = NewPassword.getText().toString();
            return true;

        }

    }

    public void gotoPendingImage() {

        this.startActivity(new Intent(this, ImageGridActivity.class));
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
        // TODO Auto-generated method stub

    }

}
