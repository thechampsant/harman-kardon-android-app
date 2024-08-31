package mob.field.harmonkardonff.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.ImageCaptureActivity;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.SimpleCameraActivity;
import com.fieldforce.utility.Helper;
import com.fieldforce.utility.Storage;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import app.core.utils.Seprator;
import app.core.utils.onDateSetListener;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.NewSaleModel;
import mob.field.harmonkardonff.entitiymodels.ProductModel;
import mob.field.harmonkardonff.services.WebService;

public class SaleEnter extends IFragment {

    CharSequence lastamount = "0";
    ArrayList<EditText> TextBoxes = new ArrayList<EditText>();
    ArrayList<EditText> barBoxes = new ArrayList<EditText>();
    String Products = ""; // Products Separated by Comma (i.e p1,p2,p3)
    NewSaleModel CurrentSalesModel = new NewSaleModel();
    ArrayList<Integer> Prices = new ArrayList<Integer>();
    ArrayList<String> barcodes = new ArrayList<String>();

    String MastCat = "";
    String SubCat = "";
    // ProductModel SelectedProduct = null;
    ProductModel SelectedProduct = new ProductModel();
    String SelectedDemonstration = null;
    WebService server = new WebService();
    LinearLayout linearLayout = null;
    LinearLayout linear_invc = null;
    CardView cvUploadInvoice,cv_upload_price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.activity_enter_sale, inflater, container);
    }

    @Override
    public void Activate(View arg0) {
        try {
            linearLayout = this.getLinearLayout(R.id.rll);
            linear_invc = this.getLinearLayout(R.id.lnr_invoc);
            cvUploadInvoice = (CardView) findViewById(R.id.cv_upload_invoice);
            cv_upload_price = (CardView) findViewById(R.id.cv_upload_price);

            float radius = Math.max(Helper.getViewHeight(cvUploadInvoice), Helper.getViewWidth(cvUploadInvoice));
            radius = radius % 2 == 0 ? radius : radius + 1;
            cvUploadInvoice.setRadius(radius / 2);
            LinearLayout buttonInvoice = (LinearLayout) findViewById(R.id.btn_invc);

          /*  float radius1 = Math.max(Helper.getViewHeight(cv_upload_price), Helper.getViewWidth(cv_upload_price));
            radius1 = radius1 % 2 == 0 ? radius1 : radius1 + 1;
            cv_upload_price.setRadius(radius1 / 2);
            */
            LinearLayout buttonInvoice1 = (LinearLayout) findViewById(R.id.btn_price);
            buttonInvoice1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowToast("Button clicked...");
                    uploadimage1();
                }
            });
            buttonInvoice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowToast("Button clicked...");
                    uploadimage();
                }
            });
			/*
			 * CurrentSalesModel.ForDate = GetCurrentDateInString();
			 * SetTextViewAsString(R.id.txt_for_date,
			 * CurrentSalesModel.ForDate);
			 SetQtyTextChangeEvent();*/
            // setvisibiltyofsaleimg();
            SetOutletName();
            AttachOnSubmitClick();
            AttachAddMoreSaleClick();
            AttachNoSaleSubmitClick();
            AttachNewMethodClick();
            MastCat = MainActivity.GetMasterCategories().get(0);
            //	AttachOnItemSelectedEventOnAutocompleteMastCat();
            AttachOnItemSelectedEventOnAutocompleteSubCat();
            AttachOnItemSelectedEventOnAutocompleteModel();
            // AttachOnItemSelectedEventOnAutocompleteProductD();
            AttachOnItemSelectedEventOnAutocompleteProduct();
            // AttachOnItemSelectedEventOnDemosntrator();
            FillModeOfPayment();
             attachDatePicker();
            // Storage.startDateProcess(getActivity(), R.id.txt_for_date,
            // R.id.btn_for_date);
            startDateProcess(R.id.txt_for_date, R.id.btn_for_date);
//			FillMastCatCombo();
            FillSubCatCombo();
            FillDemostrationCombo();
            // AttachNewMethodClick();
        } catch (Exception ex) {
            ShowToastLong(ex.getMessage(), 0);
            this.context.finish();
        }


    }

    LinearLayout saleimage;

    // private void setvisibiltyofsaleimg() {
    // saleimage=(LinearLayout) this.findViewById(R.id.lnr_upld);
    // saleimage.setVisibility(View.GONE);
    //
    // }

    private void startDateProcess(int idForDateTxt, int idForDateButton) {
        final TextView dateText = (TextView) findViewById(idForDateTxt);
        dateText.setText("" + Storage.getCurrentDateYMD());
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        CurrentSalesModel.ForDate = dateFormatter.format(newCalendar.getTime());
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                getActivity(), new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
                CurrentSalesModel.ForDate = dateFormatter
                        .format(newDate.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar
                .get(Calendar.MONTH), newCalendar
                .get(Calendar.DAY_OF_MONTH));

        findViewById(idForDateButton).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        fromDatePickerDialog.show();
                    }
                });
    }

    private void attachDatePicker() {

        this.attachDatePicker(R.id.btn_for_date, new onDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, Date date, String sdate) {
                SetTextViewAsString(R.id.txt_for_date, sdate);
                CurrentSalesModel.ForDate = sdate;
            }
        });
    }

    private void AttachNewMethodClick() {
        this.SetOnClickListenerOnButton(R.id.btn_new_sale, new OnClickListener() {
            @Override
            public void onClick(View v) {
                newmethod();
            }
        });
    }

    private void AttachNoSaleSubmitClick() {
        this.SetOnClickListenerOnButton(R.id.btn_no_sale,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ShowDialog();
                    }
                });
    }

    private void ShowDialog() {
        new AlertDialog.Builder(this.context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("No sale")
                .setMessage("Are you sure for zero sale?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UpdateSalesModel();
                                if (validateDates())
                                    submitZeroSale();

                            }
                        }).setNegativeButton("No", null).show();

    }

    private void submitZeroSale() {
        this.CurrentSalesModel.IsNoSale = "true";
        if (isNetworkAvailable()) {
            BackgroundProcess bp = new BackgroundProcess(this.context).setProgressMessage("sending to server..");
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {
                    Response response = (Response) arg0;
                    if (response.isSuccess()) {
                        ShowToast("sales updated successfully!");
                        CurrentSalesModel.ProductName = "Zero Sale";
                        CurrentSalesModel.TotalQty = 0;
                        CurrentSalesModel.IsUpdated = "true";
                        AddNewSaleLocally(CurrentSalesModel);
                    } else
                        ShowToastLong(response.errormsg, 0);

                    gotoSaleDisplay(true);
                }

                @Override
                public Object underProcess() throws Exception {

                    return server.TryUpdateSale(CurrentSalesModel);
                }
            });

            bp.execute(null, null, null);
        } else {
            new Dialog(this.context).setTitle("Message").show(
                    "No network found!Try later");
        }

    }

    private void AttachAddMoreSaleClick() {

        /*
         * this.SetOnClickListenerOnButton(R.id.btn_addmore_sale, new
         * OnClickListener() {
         *
         * @Override public void onClick(View v) { addMoreSale(); } });
         */
    }

    private void AttachOnSubmitClick() {
        this.SetOnClickListenerOnButton(R.id.btn_submit_sale,
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        OnSubmitClick();

                    }
                });
    }

    public void OnSubmitClick() {

        try {
            if (validate(true)) {
             //   SubmitSaleOnServer();
                Log.e("Storetype",MainActivity.MyInfo.Storetype);
             //   CurrentSalesModel.Price= GetEditTextAsString(R.id.tbxqty).trim();
                if(MainActivity.MyInfo.Storetype.equalsIgnoreCase("GT")||MainActivity.MyInfo.Storetype.equalsIgnoreCase("AI RR"))
				if (CurrentSalesModel.DocIDs.equalsIgnoreCase("")){
					ShowToast("Please upload invoice first");
				}
				else {
					SubmitSaleOnServer();
				}
                else
                    SubmitSaleOnServer();

            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SubmitSaleOnServer() {
        if (isNetworkAvailable()) {
            BackgroundProcess bp = new BackgroundProcess(this.context)
                    .setProgressMessage("sending to server..");
            bp.setProgressDailogCancellable(false);
            bp.setbackgroundProcess(new IProcess() {

                @SuppressWarnings("rawtypes")
                @Override
                public void processResponse(Object arg0) throws Exception {
                    ProcessNewSaleResponse((Response) arg0);
                }

                @Override
                public Object underProcess() throws Exception {
                    return server.TryUpdateSale(CurrentSalesModel);
                }
            });

            bp.execute(null, null, null);
        } else {
            new Dialog(this.context).setTitle("Message").show(
                    "No network found!Try later");
            AddNewSaleLocally(CurrentSalesModel);
            gotoSaleDisplay(true);
        }
    }

    @SuppressWarnings("rawtypes")
    private void ProcessNewSaleResponse(Response response) {
        if (response.status.equalsIgnoreCase("true")) {
            this.ShowToast("Sale updated successfully!");
            CurrentSalesModel.IsUpdated = "true";
          //  AddNewSaleLocally(CurrentSalesModel);
            gotoSaleDisplay(true);
        } else {
           // AddNewSaleLocally(CurrentSalesModel);
            new Dialog(this.context).show(response.errormsg);
            //gotoSaleDisplay(true);

        }

    }

    private void gotoPendingSale() {

    }

    private void gotoSaleDisplay(boolean Status) {
        if (Status == true) {
            setTab(2);
        }
    }

    private void AddNewSaleLocally(NewSaleModel newSale) {
        newSale.InsertOrUpdate();

    }

    private boolean addMoreSale() {
        if (!validate(false))
            return false;
        UpdateSalesModel();
        removetextbox(true);
        findViewById(R.id.spmastcat).requestFocusFromTouch();
        return true;
    }

    private boolean validateDates() {
        int backday = 0;
        if (CurrentSalesModel.ForDate == null) {
            ShowToast("Please select the date..");
            return false;
        }
        if (ConvertStringToDate(CurrentSalesModel.ForDate).after(
                GetCurrentDate())) {
            ShowToast("Future Dates are not allowed");
            return false;
        } else if (ConvertStringToDate(CurrentSalesModel.ForDate).before(
                getBackDate(backday))) {
            ShowToast("Past Dates are not allowed");
            //ShowToast("Only past " + backday + " days sale is allowed");
            return false;
        } else
            return true;
    }

    private boolean validate(boolean canSubmit) {
        if (validateDates() && validateCustomer() && validateQty(canSubmit) && validatePrice() /*&& validatebar()*/) {
            if (canSubmit) {
                UpdateSalesModel();
            }
            return true;
        }
        return false;

    }

    private boolean validatebar() {

        boolean Isbarcodeok = true;
        if (validateMsgTv.size() == 0) {
            ShowToastLong("There should be minimum one sale!!", 0);
            return false;
        }
		/*for (TextView tv : validateMsgTv) {
			String str = tv.getTag().toString();
			if (str.equalsIgnoreCase("false")) {
				ShowToastLong("All Barcode should be validate!!", 0);
				return false;
			}
		}*/
        for (EditText tbx : barBoxes) {
            if (!tbx.getText().toString().trim().isEmpty()) {
                barcodes.add(tbx.getText().toString().trim());
            } else {
                Isbarcodeok = false;
                break;
            }
        }
        if (!Isbarcodeok)
            ShowToastLong("Please enter valid Barcode!", 0);
        return Isbarcodeok;
    }

    boolean invoiceimage = false;

    // private boolean validateserial() {
    // if (CurrentSalesModel.SerialNo.equalsIgnoreCase("")
    // ) {
    // if (serialview.Count() != 0) {
    // for (int i = 0; i <serialview.Count(); i++) {
    // linearLayout.removeView(serialview.get(i));
    //
    // }
    // serialview.clear();
    // saleimage.setVisibility(View.VISIBLE);
    // saleimage.setOnClickListener(new OnClickListener() {
    //
    // @Override
    // public void onClick(View v) {
    // captureimage();
    //
    // }
    // });
    //
    // this.ShowToast("plz upload image");
    // return false;
    // }
    //
    //
    // }
    //
    // return true;
    // }

    protected void captureimage() {
        Intent I = new Intent(this.context, ImageCaptureActivity.class);
        I.putExtra(ImageCaptureActivity.PARAMS_DOC_TYPE, "Sale");
        I.putExtra(ImageCaptureActivity.PARAMS_USERNAME, User.GetUserName());
        I.putExtra(ImageCaptureActivity.PARAMS_GUID, CurrentSalesModel.guid);

        // For getting the result in the parent activity
        // this.context.startActivityForResult(I, 1);

        // for getting the result in the fragment
        startActivityForResult(I, 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            String GUID = data.getExtras().getString(SimpleCameraActivity.PARAMS_GUID);
            String IMAGE_CAPTURED = data.getExtras().getString("IMAGE_CAPTURED") + "";
            String DocID = data.getExtras().getString("DocID") + "";
            showImageToastAfterImageCapture(IMAGE_CAPTURED);
            if (!IMAGE_CAPTURED.equals("")) {
                setDocsIDs(DocID, true);
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            String GUID = data.getExtras().getString(SimpleCameraActivity.PARAMS_GUID);
            String IMAGE_CAPTURED = data.getExtras().getString("IMAGE_CAPTURED") + "";
            String DocID = data.getExtras().getString("DocID") + "";
            showImageToastAfterImageCapture(IMAGE_CAPTURED);
            if (!IMAGE_CAPTURED.equals("")) {
                setDocsIDs1(DocID, true);
            }

        } else if (requestCode == 2 && resultCode == Activity.RESULT_CANCELED) {
            new Dialog(getActivity()).show("" + "Barcode Canceled");
        }
    }

    public void setDocsIDs(String docsIDs, boolean IsImageUploaded) {
        this.CurrentSalesModel.DocIDs = docsIDs;
        this.invoiceimage = IsImageUploaded;

    }

    public void setDocsIDs1(String docsIDs, boolean IsImageUploaded) {
        this.CurrentSalesModel.PriceDocIDs = docsIDs;
        this.invoiceimage = IsImageUploaded;

    }

    private void showImageToastAfterImageCapture(String result) {

        if (!result.equals(""))
            ShowToastLong(" image is attached", 0);
        else
            ShowToastLong("No image found to attach", 0);


    }

    private boolean isZeroSale() {
        return CurrentSalesModel.Qty.length() < 1 && GetEditTextAsString(R.id.tbxqty).trim().equalsIgnoreCase("0");
    }

    private boolean validateQty(boolean cansubmit) {
        if (cansubmit) {
			/*if (GetEditTextAsInt(R.id.tbxqty) > 3) {
				GetEditText(R.id.tbxqty).setError("Invalid Qty");
				this.ShowToast("Please Enter valid  Quantity less than 4");
				return false;
			} else*/
            if (GetEditTextAsInt(R.id.tbxqty) == 0) {
                GetEditText(R.id.tbxqty).setError("Invalid Qty");
                this.ShowToast("quantity cannot be zero");
                return false;
            } else
                return true;
        } else {
            if (isZeroSale()) {
                return true;
                // }else if(CurrentSalesModel.Qty.length() > 26 ||
                // CurrentSalesModel.Qty.length() == 0){
                // this.ShowToast("Please Enter value less then 26");
                // GetEditText(R.id.tbxqty).setError("Invalid Entry");
                // return false;
                // }else if(CurrentSalesModel.Qty.length() < 1){
                // this.ShowToast("Please Enter Greater Value");
                // GetEditText(R.id.tbxqty).setError("Invalid Entry");
                // return false;
            } else {
                GetEditText(R.id.tbxqty).setError("Invalid Qty");
                this.ShowToast("Please Enter Quantity");
                return true;
            }
        }

    }

    private boolean validatePrice() {
        boolean IsPriceOK = true;
        for (EditText tbx : TextBoxes) {
            int price = 0;
            price = ToInt(tbx.getText().toString().trim());

            if (price > 1000)
                Prices.add(price);
            else {
                IsPriceOK = false;
            }
        }
        if (!IsPriceOK)
            ShowToastLong("Please enter valid amount!", 0);
        return IsPriceOK;
    }

    private boolean validateCustomer() {
        /*
         * if (!isZeroSale()) { if
         * (TextUtils.isEmpty(GetEditTextAsString(R.id.edt_CustomerName))) {
         * this.GetEditText(R.id.edt_CustomerName).setError(
         * "Customer name requried");
         * this.ShowToast("Customer name is required!"); return false; }
         */

        // if (TextUtils.isEmpty(CurrentSalesModel.City)) {
        // this.ShowToast("City is required!");
        // return false;
        // } else if
        // (TextUtils.isEmpty(GetEditTextAsString(R.id.edt_location)))
        // {
        // this.ShowToast("location is required!");
        // return false;
        // }
        CurrentSalesModel.Price=GetEditTextAsString(R.id.tbxprice);
        CurrentSalesModel.InvoiceNumber=GetEditTextAsString(R.id.tbxInvoiceNumber);
       /* if(TextUtils.isEmpty(GetEditTextAsString(R.id.edt_email))&&(TextUtils.isEmpty(GetEditTextAsString(R.id.edt_mobile))))
        {
            ShowToast("MobileNo and EmailID is required!");
            return false;
        }
        else*/ if (CurrentSalesModel.Price.equalsIgnoreCase("")){
            ShowToast("Please Enter Price");
            return false;
        }
        else if (CurrentSalesModel.InvoiceNumber.equalsIgnoreCase("")){
            ShowToast("Please Enter Invoice Number");
            return false;
        }
       /* else if (TextUtils.isEmpty(GetEditTextAsString(R.id.edt_email))&&(GetEditTextAsString(R.id.edt_mobile).length() < 10)) {
                GetEditText(R.id.edt_mobile).setError("Invalid Mobile");
                ShowToast("MobileNo is invalid!");
                return false;
        }*/ /*else if(TextUtils.isEmpty(GetEditTextAsString(R.id.edt_mobile))&&!isValid(GetEditTextAsString(R.id.edt_email))){
            this.ShowToast("Email is required!");
            return false;
        }else if((GetEditTextAsString(R.id.edt_mobile).length() < 10)&&!isValid(GetEditTextAsString(R.id.edt_email)))
        {
            ShowToast("MobileNo and EmailID is invalid!");
            return false;
        }*/
        else
            return true;
        /*
         * } else return true;
         */
    }
    public static boolean isValid(String email)
    {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private void UpdateSalesModel() {
        CurrentSalesModel.CustomerName = GetEditTextAsString(R.id.edt_CustomerName);
        CurrentSalesModel.MobileNo = GetEditTextAsString(R.id.edt_mobile);
        CurrentSalesModel.City = "NA";
        CurrentSalesModel.Location = "NA";
        CurrentSalesModel.Email = GetEditTextAsString(R.id.edt_email);
        CurrentSalesModel.MobileNo = CurrentSalesModel.MobileNo.length() > 0 ? CurrentSalesModel.MobileNo : "NA";
        // CurrentSalesModel.SerialNo += "000000";
        CurrentSalesModel.CreatedOn = this.GetCurrentDateTimeInString();
        CurrentSalesModel.TotalQty = GetEditTextAsInt(R.id.tbxqty);
        CurrentSalesModel.Qty = GetEditTextAsString(R.id.tbxqty);
        CurrentSalesModel.InvoiceNumber = GetEditTextAsString(R.id.tbxInvoiceNumber);
        CurrentSalesModel.Domenstration = SelectedDemonstration;
//        CurrentSalesModel.ModeOfPayment = getSpinnerAsString(R.id.mode_of_payment_spinner);
        CurrentSalesModel.ModeOfPayment = getSpinnerAsString(R.id.searchableSpinner_mode_of_payment_spinner);

        setPrices();
        addbar();
        addProduct();
    }

    public void addbar() {
        int i = 0;
        if (barcodes.Count() > 0) {
            for (String barcode : barcodes) {

                if (i == 0)
                    CurrentSalesModel.SerialNo += barcode;
                else
                    CurrentSalesModel.SerialNo += Seprator.com + barcode;

                i++;
                // sb.append(price).append(Seprator.com);
                // CurrentSalesModel.TotalAmount += price;
            }
            // CurrentSalesModel.Prices += sb.toString();
        }
    }

    private void setPrices() {

        int i = 0;
        if (Prices.Count() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int price : Prices) {

                if (i == 0)
                    CurrentSalesModel.Prices += price;
                else
                    CurrentSalesModel.Prices += Seprator.com + price;

                i++;
                // sb.append(price).append(Seprator.com);
                // CurrentSalesModel.TotalAmount += price;
            }
            CurrentSalesModel.Prices += sb.toString();
        }

    }

    private void addProduct() {
        //CurrentSalesModel.PID += SelectedProduct.PID;
        CurrentSalesModel.PID = SelectedProduct.PID;
        CurrentSalesModel.ProductName = SelectedProduct.Name;
        CurrentSalesModel.Cat1 = SelectedProduct.Cat1;
        CurrentSalesModel.Cat2 = SelectedProduct.Cat2;
        CurrentSalesModel.Cat3 = SelectedProduct.Cat3;
    }

    protected void removetextbox(boolean isclear) {
        linearLayout.removeAllViews();
        Prices = new ArrayList<Integer>();
        barcodes = new ArrayList<String>();
        linearLayout.setVisibility(View.INVISIBLE);
        if (isclear)
            SetEditTextAsString(R.id.tbxqty, "");
    }

    ArrayList<TextView> validateMsgTv = new ArrayList<TextView>();

    protected void ShowRequiredTextBoxes(CharSequence s) {
        linear_invc.setVisibility(View.VISIBLE);
        ((LinearLayout) linear_invc.findViewById(R.id.btn_invc))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        uploadimage();
                    }
                });

        if (s.length() < 1) {
            removetextbox(false);
            linear_invc.setVisibility(View.GONE);
            return;
        }
        TextBoxes = new ArrayList<EditText>();
        Integer qty = this.ToInt(s.toString());
        removetextbox(false);
        if (qty < 1)
            return;

        if (qty >= 25)
            return;
        linearLayout.setVisibility(View.VISIBLE);
        barBoxes.clear();
        validateMsgTv.clear();
        for (int i = 0; i < qty; i++) {
            /*
             * LinearLayout relatedView = getverticallinear();
             * relatedView.setBackgroundDrawable(getResources().getDrawable(
             * R.drawable.rect));
             */
            View view = getInflatedView(R.layout.relatedview_ll);
            LinearLayout relatedView = (LinearLayout) view
                    .findViewById(R.id.relatedviewll);
            // if(qty>=25)
            // {
            // Toast.makeText(context, "Quantity must be less than 25",
            // Toast.LENGTH_LONG).show();
            // }
            // else
            // {
            LinearLayout ll = gethorizontallinear(3);
            ll.addView(gettextview("Price" + (i + 1) + ":-", 1));
            // linearLayout.addView(gettextview("Price" + (i + 1) + ":-"));

            /*
             * EditText tbx = new EditText(this.context);
             * tbx.setInputType(InputType.TYPE_CLASS_NUMBER); tbx.setHint("0");
             */

            EditText tbx = getediitext(2);
            if (i == 0) {
                SetPriceTextChangeEvent(tbx);
            }
            TextBoxes.add(tbx);
            ll.addView(tbx);
            // linearLayout.addView(tbx);

            relatedView.addView(ll);
            addbarcode(i, relatedView, view);

        }

        // TextView serialtitle = new TextView(context);
        // serialtitle.setText("Enter Serial No :-");
        // serialtitle.setTextAppearance(context,
        // android.R.style.TextAppearance_Medium);
        // serialtitle.setTypeface(null, Typeface.BOLD);
        // linearLayout.addView(serialtitle);
        // EditText serialtextbx = new EditText(this.context);
        // serialtextbx.setInputType(InputType.TYPE_CLASS_NUMBER);
        // serialtextbx.setFilters(new InputFilter[] { new
        // InputFilter.LengthFilter(
        // 16) });
        // setseriallistener(serialtextbx);
        // linearLayout.addView(serialtextbx);
        // getserialviews(serialtitle, serialtextbx);
        // }
    }

    protected void uploadimage() {
        Intent I = new Intent(this.context, SimpleCameraActivity.class);
        I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "SaleEnter");
        I.putExtra(SimpleCameraActivity.PARAMS_USERNAME, User.GetUserName());
        I.putExtra(SimpleCameraActivity.PARAMS_GUID, CurrentSalesModel.guid);
        I.putExtra(SimpleCameraActivity.PARAMS_MODEL_ID, "");
        I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
        // For getting the result in the parent activity
        // this.context.startActivityForResult(I, 1);

        // for getting the result in the fragment
        startActivityForResult(I, 1);

    } protected void uploadimage1() {
        Intent I = new Intent(this.context, SimpleCameraActivity.class);
        I.putExtra(SimpleCameraActivity.PARAMS_DOC_TYPE, "Sale Price Protection");
        I.putExtra(SimpleCameraActivity.PARAMS_USERNAME, User.GetUserName());
        I.putExtra(SimpleCameraActivity.PARAMS_GUID, CurrentSalesModel.guid);
        I.putExtra(SimpleCameraActivity.PARAMS_MODEL_ID, "");
        I.putExtra(SimpleCameraActivity.ACTION_ENABLE_GPS, true);
        // For getting the result in the parent activity
        // this.context.startActivityForResult(I, 1);

        // for getting the result in the fragment
        startActivityForResult(I, 2);

    }

    private void addbarcode(final int position, LinearLayout relatedView,
                            View relatedviewtopview) {
        LinearLayout linrvrtcl = getverticallinear();
        LinearLayout linrhrzntl = gethorizontallinear(3);
        linrhrzntl.addView(gettextview("IMEI No. " + (position + 1) + ":-", 1));
        EditText et = getediitext(2);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(15);
        et.setFilters(FilterArray);
        barBoxes.add(et);
        linrhrzntl.addView(et);
        linrvrtcl.addView(linrhrzntl);
        LinearLayout linearscannervalidate = gethorizontallinear(6);
        View view = getEmptyView(2);
        linearscannervalidate.addView(view);
        ImageView img = GetImagevw(2);
        img.setOnClickListener(new BarscanClickListener(position));
        linearscannervalidate.addView(img);
        View view1 = getEmptyView(1);
        linearscannervalidate.addView(view1);
//		View tv = getbuttonview("Validate");

//		linearscannervalidate.addView(tv);
        linrvrtcl.addView(linearscannervalidate);
        final TextView tbx = gettextviewremarks();
//		tv.setOnClickListener(new ValidateBarcodeListener(et, tbx, position));
        linrvrtcl.addView(tbx);
        validateMsgTv.add(tbx);
        relatedView.addView(linrvrtcl);
        linearLayout.addView(relatedviewtopview);
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                validateMsgTv.get(position).setTag("false");
                tbx.setText("");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }


        });
    }

    class ValidateBarcodeListener implements OnClickListener {

        EditText barcodetext;
        TextView validateBarcodeMsg;
        int position;

        public ValidateBarcodeListener(EditText barcodetext,
                                       TextView validateBarcodeMsg, int position) {
            this.barcodetext = barcodetext;
            this.validateBarcodeMsg = validateBarcodeMsg;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            final String str = barcodetext.getText().toString();
            if (str.length() != 15) {
                validateBarcodeMsg
                        .setText("Barcode text length is not 15!!");
                return;
            }
            if (isNetworkAvailable()) {

                BackgroundProcess bp = new BackgroundProcess(SaleEnter.this)
                        .setProgressMessage("validation from server..");
                bp.setbackgroundProcess(new IProcess() {

                    @SuppressWarnings("rawtypes")
                    @Override
                    public void processResponse(Object arg0) throws Exception {
                        Response res = (Response) arg0;
                        if (res != null && res.isSuccess()) {
                            validateMsgTv.get(position).setTag("true");
                            validateBarcodeMsg
                                    .setText("Barcode is validate from server.");
                        } else {
                            validateMsgTv.get(position).setTag("false");
                            validateBarcodeMsg.setText("" + res.getMessage());
                        }
                        // validateBarcodeMsg.setText("" + res.getMessage());
                        // validateBarcodeMsg.setError("" + res.getMessage());
                    }

                    @Override
                    public Object underProcess() throws Exception {

                        return server.checkforIMEIValidation(str);
                    }
                });

                bp.execute(null, null, null);
            }

        }

    }

    class BarscanClickListener implements OnClickListener {
        int position;

        public BarscanClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(
                        "com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
                startActivityForResult(intent, 2);
                SELECTED_BARCODE_ICON = position;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
				/*Toast.makeText(getActivity().getApplicationContext(),
						"ERROR:" + e, 1).show();*/

            }
        }

    }

    int SELECTED_BARCODE_ICON = -1;

    private ImageView GetImagevw(int f) {
        ImageView img = new ImageView(context);
        LayoutParams param = new LayoutParams(0, 80,
                f);
        img.setLayoutParams(param);
        img.setBackgroundResource(R.drawable.scaner_img);
        return img;

    }

    private EditText getediitext(int value) {
        EditText edt = new EditText(context);
        LayoutParams param = new LayoutParams(0,
                LayoutParams.WRAP_CONTENT, value);
        edt.setLayoutParams(param);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt.setHint("0");

        return edt;
    }

    private EditText getediitext() {
        EditText edt = new EditText(context);
        LayoutParams param = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        edt.setLayoutParams(param);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt.setHint("0");

        return edt;
    }

    private TextView gettextviewremarks() {
        TextView edt = new TextView(context);
        LayoutParams param = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        edt.setLayoutParams(param);
        return edt;
    }

    private View getEmptyView(int value) {
        View edt = new View(context);
        LayoutParams param = new LayoutParams(0,
                LayoutParams.WRAP_CONTENT, value);
        edt.setLayoutParams(param);
        return edt;
    }

    public LinearLayout getverticallinear() {
        LinearLayout Linr_vrtcl = new LinearLayout(context);
        Linr_vrtcl.setBackgroundColor(Color.WHITE);
        Linr_vrtcl.setOrientation(LinearLayout.VERTICAL);

        LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        Linr_vrtcl.setLayoutParams(LLParams);
        return Linr_vrtcl;
    }

    public LinearLayout gethorizontallinear() {
        LinearLayout Linr_hrzntl = new LinearLayout(context);
        Linr_hrzntl.setBackgroundColor(Color.WHITE);
        Linr_hrzntl.setOrientation(LinearLayout.HORIZONTAL);
        Linr_hrzntl.setWeightSum(5);
        LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        Linr_hrzntl.setLayoutParams(LLParams);
        return Linr_hrzntl;
    }

    public LinearLayout gethorizontallinear(int weightsum) {
        LinearLayout Linr_hrzntl = new LinearLayout(context);
        Linr_hrzntl.setBackgroundColor(Color.WHITE);
        Linr_hrzntl.setOrientation(LinearLayout.HORIZONTAL);
        Linr_hrzntl.setWeightSum(weightsum);
        LayoutParams LLParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        Linr_hrzntl.setLayoutParams(LLParams);
        return Linr_hrzntl;
    }

    ArrayList<View> serialview = new ArrayList<View>();

    private void getserialviews(TextView serialtitle, EditText serialtbx) {

        serialview.add(serialtitle);
        serialview.add(serialtbx);

    }

    public View gettextview(String text) {
        TextView title = new TextView(context);
        title.setText(text);
        title.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        title.setTypeface(null, Typeface.BOLD);
        return title;
    }

    public View getbuttonview(String text) {
        Button button = new Button(context);
        button.setText(text);
        button.setPadding(2, 4, 2, 4);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 4, 0, 0);
        button.setLayoutParams(params);
        // title.setTextAppearance(context,
        // android.R.style.TextAppearance_Medium);
        // title.setTypeface(null, Typeface.BOLD);
        return button;
    }

    public View gettextview(String text, int weight) {
        TextView title = new TextView(context);
        title.setText(text);
        LayoutParams param = new LayoutParams(0,
                LayoutParams.WRAP_CONTENT, weight);
        title.setLayoutParams(param);
        title.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        title.setTypeface(null, Typeface.BOLD);
        return title;
    }

    private void setseriallistener(EditText serialtbx) {
        serialtbx.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                CurrentSalesModel.SerialNo = s.toString();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void SetPriceTextChangeEvent(EditText tbx) {
        tbx.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                for (int i = 0; i < TextBoxes.Count(); i++) {
                    if (i != 0) {
                        EditText edittext = TextBoxes.get(i);
                        edittext.setText(TextBoxes.get(0).getText());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

        });
    }

    private void SetQtyTextChangeEvent() {

        TextView tbxqty = ((TextView) this.findViewById(R.id.tbxqty));
        tbxqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                ShowRequiredTextBoxes(s);

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

        });
    }

    private void SetOutletName() {
        ((TextView) this.findViewById(R.id.txt_outletname))
                .setText(MainActivity.MyInfo.CurrentStore);
    }

    private void AttachOnItemSelectedEventOnAutocompleteProduct() {

//        Spinner spinner = (Spinner) this.findViewById(R.id.spproducts);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                spinner = (SearchableSpinner) findViewById(R.id.searchableSpinner_spproducts);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SelectedProduct = (ProductModel) arg0.getSelectedItem();
                // CurrentSalesModel.PID = SelectedProduct.PID;
                // fillmop(SelectedProduct);

                /*
                 * CurrentSalesModel.PID = _SelectedProduct.PID;
                 * CurrentSalesModel.ProductName = _SelectedProduct.Name;
                 * CurrentSalesModel.Cat1 = _SelectedProduct.Cat1;
                 * CurrentSalesModel.Cat2 = _SelectedProduct.Cat2;
                 * CurrentSalesModel.Cat3 = _SelectedProduct.Cat3;
                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

    }

	/*protected void fillmop(ProductModel selectedProduct2) {
		EditText edt_mop = this.GetEditText(R.id.edtxt_mop);
		edt_mop.setText(selectedProduct2.MOP);
		edt_mop.setEnabled(false);

	}*/

    private void AttachOnItemSelectedEventOnAutocompleteProductD() {

        // TODO Auto-generated method stub

//        Spinner spinner = (Spinner) this.findViewById(R.id.tbxdemonst);
        // Spinner spinner = (Spinner) this.findViewById(R.id.spmodels);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
        spinner = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                SelectedProduct = (ProductModel) arg0.getSelectedItem();
                // CurrentSalesModel.PID = SelectedProduct.PID;
                // String model = (String) arg0.getItemAtPosition(arg2);
                // FillProductcombo(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void AttachOnItemSelectedEventOnAutocompleteModel() {
        // TODO Auto-generated method stub
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                spinner = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
//        Spinner spinner = (Spinner) this.findViewById(R.id.spmodels);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String model = (String) arg0.getItemAtPosition(arg2);
                FillProductcombo(model);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }

    private void AttachOnItemSelectedEventOnAutocompleteSubCat() {

        /*Spinner spinner = (Spinner) this.findViewById(R.id.spsubcat);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SubCat = (String) arg0.getItemAtPosition(arg2);
                FillModelcombo();
                // FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });*/
        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spsubcat);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SubCat = (String) arg0.getItemAtPosition(arg2);
                FillModelcombo();
                // FillProductcombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

    private void FillModelcombo() {
        // TODO Auto-generated method stub
//        Spinner sp = (Spinner) this.findViewById(R.id.spmodels);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spmodels);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetModels(MastCat, SubCat));
        sp.setAdapter(spinnerArrayAdapter);
    }

    private void AttachOnItemSelectedEventOnDemosntrator() {
        Spinner spinner = (Spinner) this.findViewById(R.id.tbxdemonst);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                SelectedDemonstration = (String) arg0.getItemAtPosition(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void AttachOnItemSelectedEventOnAutocompleteMastCat() {

        Spinner spinner = (Spinner) this.findViewById(R.id.spmastcat);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                MastCat = (String) arg0.getItemAtPosition(arg2);
                // FillProductcombo();
                FillSubCatCombo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

    }

    private void FillMastCatCombo() {
        Spinner sp = (Spinner) this.findViewById(R.id.spmastcat);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetMasterCategories());

        sp.setAdapter(spinnerArrayAdapter);

    }

    private void FillSubCatCombo() {
//        Spinner sp = (Spinner) this.findViewById(R.id.spsubcat);
        com.toptoche.searchablespinnerlibrary.SearchableSpinner sp;
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spsubcat);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetSubCategories(MainActivity.GetMasterCategories().get(0)));
        sp.setAdapter(spinnerArrayAdapter);


    }

    private void FillProductcombo(String model) {
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
        sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_spproducts);

//        Spinner sp = (Spinner) this.findViewById(R.id.spproducts);
        ArrayAdapter<ProductModel> spinnerArrayAdapter = new ArrayAdapter<ProductModel>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetProducts(MastCat, SubCat, model));
        sp.setAdapter(spinnerArrayAdapter);
    }

    private void FillModeOfPayment() {
        ArrayList<String> modeOfPaymentList = new ArrayList<String>();
        modeOfPaymentList.add("Cash");
        modeOfPaymentList.add("Cheque/DD");
        modeOfPaymentList.add("Card (Credit/Debit)");
        modeOfPaymentList.add("Net banking");
        modeOfPaymentList.add("Finance Scheme");
        modeOfPaymentList.add("Other");
        com.toptoche.searchablespinnerlibrary.SearchableSpinner
                sp = (SearchableSpinner) findViewById(R.id.searchableSpinner_mode_of_payment_spinner);
//        Spinner sp = (Spinner) this.findViewById(R.id.mode_of_payment_spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                modeOfPaymentList);
        sp.setAdapter(spinnerArrayAdapter);
    }

    private void FillDemostrationCombo() {
        Spinner sp = (Spinner) this.findViewById(R.id.tbxdemonst);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this.context, android.R.layout.simple_spinner_dropdown_item,
                MainActivity.GetDemostratorRequired());
        sp.setAdapter(spinnerArrayAdapter);

    }

    public void newmethod() {
        LinearLayout relav = (LinearLayout) findViewById(R.id.newsalecontent);
        relav.setVisibility(View.VISIBLE);

        LinearLayout lyt = (LinearLayout) findViewById(R.id.lyt_sale);
        lyt.setVisibility(View.VISIBLE);
    }

}
