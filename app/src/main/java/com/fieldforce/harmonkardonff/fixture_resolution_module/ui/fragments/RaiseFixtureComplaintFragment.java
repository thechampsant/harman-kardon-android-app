package com.fieldforce.harmonkardonff.fixture_resolution_module.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.SimpleCameraActivity;
import com.fieldforce.harmonkardonff.fixture_resolution_module.ui.activities.FixtureResolutionTabActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import mob.field.harmonkardonff.services.WebService;

public class RaiseFixtureComplaintFragment extends IFragment {

    private static final int REQUEST_CAPTURE_PHOTO = 701;

    private Spinner spinnerFixtureType;
    private Spinner spinnerIssueType;
    private EditText editRemark;
    private TextView tvStoreInfo;
    private TextView tvPhotoStatus;
    private Button btnCapturePhoto;
    private Button btnSubmit;

    private WebService webService = new WebService();
    private String docId = "";
    private boolean photoUploaded = false;

    private List<String> fixtureTypes = new ArrayList<>();
    private List<String> issueTypes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return InflateView(R.layout.fragment_raise_fixture_complaint, inflater, container);
    }

    @Override
    public void Activate(View fragmentView) {
        spinnerFixtureType = getSpinner(R.id.spinner_fixture_type);
        spinnerIssueType = getSpinner(R.id.spinner_issue_type);
        editRemark = GetEditText(R.id.et_fixture_remark);
        tvStoreInfo = GetTextView(R.id.tv_fixture_store_info);
        tvPhotoStatus = GetTextView(R.id.tv_fixture_photo_status);
        btnCapturePhoto = GetButton(R.id.btn_fixture_capture_photo);
        btnSubmit = GetButton(R.id.btn_fixture_submit);

        if (MainActivity.MyInfo != null) {
            String storeLabel = MainActivity.MyInfo.CurrentStore;
            if (!TextUtils.isEmpty(MainActivity.MyInfo.StoreID)) {
                storeLabel = storeLabel + " (Store ID: " + MainActivity.MyInfo.StoreID + ")";
            }
            tvStoreInfo.setText(storeLabel);
        }

        btnCapturePhoto.setOnClickListener(v -> openCamera());
        btnSubmit.setOnClickListener(v -> submitComplaint());

        loadMasterData();
    }

    private void loadMasterData() {
        if (!isNetworkFoundToast()) {
            return;
        }
        BackgroundProcess bp = new BackgroundProcess(this)
                .setProgressMessage("Loading...")
                .setbackgroundProcess(new IProcess() {
                    @Override
                    public Object underProcess() throws Exception {
                        return webService.getFixtureMasterDataWrapper();
                    }

                    @Override
                    public void processResponse(Object response) throws Exception {
                        JSONObject wrapper = (JSONObject) response;
                        if (wrapper == null) {
                            new Dialog(getActivity()).setTitle("Error").show("Unable to load dropdown data.");
                            return;
                        }
                        if (!wrapper.optBoolean("status", false)) {
                            new Dialog(getActivity()).setTitle("Error")
                                    .show(wrapper.optString("errormsg", "Unable to load dropdown data."));
                            return;
                        }
                        JSONObject data = wrapper.optJSONObject("data");
                        if (data == null) {
                            new Dialog(getActivity()).setTitle("Error").show("Invalid master data response.");
                            return;
                        }
                        fixtureTypes = jsonArrayToList(data.optJSONArray("FixtureTypes"));
                        issueTypes = jsonArrayToList(data.optJSONArray("IssueTypes"));
                        bindSpinners();
                    }
                });
        bp.execute();
    }

    private List<String> jsonArrayToList(JSONArray array) throws Exception {
        List<String> list = new ArrayList<>();
        if (array == null) {
            return list;
        }
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    private void bindSpinners() {
        ArrayAdapter<String> fixtureAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, fixtureTypes);
        fixtureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFixtureType.setAdapter(fixtureAdapter);

        ArrayAdapter<String> issueAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, issueTypes);
        issueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIssueType.setAdapter(issueAdapter);
    }

    private void openCamera() {
        docId = UUID.randomUUID().toString();
        photoUploaded = false;
        tvPhotoStatus.setText("Photo: pending capture & upload");

        Intent intent = new Intent(getActivity(), SimpleCameraActivity.class);
        intent.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Fixture");
        intent.putExtra(SimpleCameraActivity.PARAMS_USERNAME, User.GetUserName());
        intent.putExtra(SimpleCameraActivity.PARAMS_GUID, docId);
        intent.putExtra(SimpleCameraActivity.PARAMS_DOC_ID, docId);
        intent.putExtra(SimpleCameraActivity.PARAMS_CAMERA_ONLY, true);
        intent.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
        startActivityForResult(intent, REQUEST_CAPTURE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            String captured = data.getStringExtra("IMAGE_CAPTURED");
            String returnedDocId = data.getStringExtra("DocID");
            boolean uploaded = data.getBooleanExtra("IMAGE_UPLOADED", false);
            if (!TextUtils.isEmpty(returnedDocId)) {
                docId = returnedDocId;
            }
            if (uploaded && !TextUtils.isEmpty(captured) && !TextUtils.isEmpty(docId)) {
                photoUploaded = true;
                tvPhotoStatus.setText("Photo: uploaded");
            } else {
                photoUploaded = false;
                tvPhotoStatus.setText("Photo: not uploaded — please capture again");
            }
        }
    }

    private void submitComplaint() {
        if (fixtureTypes.isEmpty() || issueTypes.isEmpty()) {
            ShowToast("Please wait for dropdown data to load.");
            return;
        }
        String remark = editRemark.getText().toString().trim();
        if (TextUtils.isEmpty(remark)) {
            ShowToast("Remark is mandatory.");
            return;
        }
        if (!photoUploaded || TextUtils.isEmpty(docId)) {
            ShowToast("Photo is mandatory. Capture and upload a photo first.");
            return;
        }
        if (!isNetworkFoundToast()) {
            return;
        }

        final String fixtureType = spinnerFixtureType.getSelectedItem().toString();
        final String issueType = spinnerIssueType.getSelectedItem().toString();
        final String finalDocId = docId;
        final String finalRemark = remark;

        BackgroundProcess bp = new BackgroundProcess(this)
                .setProgressMessage("Submitting complaint...")
                .setbackgroundProcess(new IProcess() {
                    @Override
                    public Object underProcess() throws Exception {
                        return webService.raiseFixtureComplaint(
                                fixtureType, issueType, finalRemark, finalDocId);
                    }

                    @Override
                    public void processResponse(Object response) throws Exception {
                        Response resp = (Response) response;
                        if (resp == null) {
                            new Dialog(getActivity()).setTitle("Error").show("No response from server.");
                            return;
                        }
                        if (resp.isSuccess()) {
                            editRemark.setText("");
                            photoUploaded = false;
                            docId = "";
                            tvPhotoStatus.setText("Photo: not captured");
                            showSuccessAndOpenLogs();
                        } else {
                            new Dialog(getActivity()).setTitle("Error").show(resp.errormsg);
                        }
                    }
                });
        bp.execute();
    }

    private void showSuccessAndOpenLogs() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        new AlertDialog.Builder(activity)
                .setTitle("Success")
                .setMessage("Complaint raised successfully.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (activity instanceof FixtureResolutionTabActivity) {
                            ((FixtureResolutionTabActivity) activity).showViewLogsAfterSubmit();
                        } else {
                            setTabInternal(1);
                        }
                    }
                })
                .show();
    }
}
