package mob.field.harmonkardonff.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.customAdapter.corona_adapters.VerticalItemDecorator;
import com.fieldforce.customAdapter.corona_adapters.history.CoronaHistoryRecyclerAdapter;
import com.fieldforce.harmonkardonff.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.CoronaHistoryModal;
import mob.field.harmonkardonff.services.WebService;

public class CoronaSurveyHistoryFragment extends IFragment {

    private static final String TAG = "CoronaSurveyHistoryFrag";
    private RecyclerView recyclerView;
    private CoronaHistoryRecyclerAdapter mAdapter;
    private WebService service = new WebService();
    private String startDate;
    private String endDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.fragment_corona_survey_history, inflater, container);
    }



    @Override
    public void Activate(View FragmentView) {
        recyclerView = (RecyclerView) findViewById(R.id.rv_coronaHistory);
        mAdapter = new CoronaHistoryRecyclerAdapter();
        initRecycler();

        initializeDatesAndServerRequest();
        startDateProcess(R.id.txt_for_dates, R.id.btn_for_dates);
        endDateProcessed(R.id.txt_to_dates, R.id.btn_to_dates);

    }

    private void initRecycler(){
        recyclerView.addItemDecoration(new VerticalItemDecorator(30));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
    }

    private void getDataFromServer(final String startDate, final String endDate){
        if (isNetworkAvailable()){
            BackgroundProcess backgroundProcess = new BackgroundProcess(this);
            backgroundProcess.setProgressMessage("Getting Data...");
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return service.getCoronaSurveyHistoryFromWeb(startDate,endDate);
                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response questionsResponseFromServer = (Response) response;
                    if (questionsResponseFromServer.isSuccess() && questionsResponseFromServer.data != null)
                    {
                        ArrayList<CoronaHistoryModal> arrDocModel = questionsResponseFromServer.data;
                        Log.d(TAG, "processResponse: "+arrDocModel);
                        mAdapter.setHistoryData(arrDocModel);
                    }
                    else {
                        new Dialog(getActivity()).setTitle("Error").setMessage(questionsResponseFromServer.errormsg).show();
                    }
                }
            });
            backgroundProcess.execute();
        }
        else {
            ShowToast("No internet...");
        }
    }

    private void initializeDatesAndServerRequest() {
        // TODO Auto-generated method stub
        initializeStartDate();
        initializeEndDate();
        if (isNetworkAvailable())
            hitapi(startDate, endDate);

        else {
            ShowToast("No Network available for View attendance");

        }
    }

    private void initializeStartDate() {
        String date = GetCurrentDateInString();
        startDate = date;
        SetTextViewAsString(R.id.txt_for_dates, date);
    }

    private void initializeEndDate() {

        String date = GetCurrentDateInString();
        endDate = date;
        SetTextViewAsString(R.id.txt_to_dates, date);
    }

    private void startDateProcess(int idForDateTxt, int idForDateButton) {
        final TextView dateText = (TextView) findViewById(idForDateTxt);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
                startDate = dateFormatter.format(newDate.getTime());

                if (endDate == null || endDate.equalsIgnoreCase(""))
                    ShowToast("Please Enter To Date");

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
                "yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText.setText(dateFormatter.format(newDate.getTime()));
                endDate = dateFormatter.format(newDate.getTime());
                if (startDate != null
                        && !startDate.equalsIgnoreCase(""))
                    hitapi(startDate, endDate);
                else
                    ShowToast("Please Enter From Date");

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

    private void hitapi(final String start, final String end) {
        if (!isNetworkFoundToast()) {
            return;
        }
        BackgroundProcess bp = new BackgroundProcess(this)
                .setHorizantalScreenProgress().setbackgroundProcess(
                        new IProcess()
                        {
                            @Override
                            public Object underProcess() throws Exception {
                                return service.getCoronaSurveyHistoryFromWeb(start,end);
                            }

                            @Override
                            public void processResponse(Object response) throws Exception {
                                Response questionsResponseFromServer = (Response) response;
                                if (questionsResponseFromServer.isSuccess() && questionsResponseFromServer.data != null)
                                {
                                    ArrayList<CoronaHistoryModal> arrDocModel = questionsResponseFromServer.data;
                                    Log.d(TAG, "processResponse: "+arrDocModel);
                                    mAdapter.setHistoryData(arrDocModel);
                                }
                                else {
                                    new Dialog(getActivity()).setTitle("Error").setMessage(questionsResponseFromServer.errormsg).show();
                                }
                            }
                        });
        bp.execute();

    }
}
