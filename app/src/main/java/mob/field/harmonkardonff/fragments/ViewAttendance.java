package mob.field.harmonkardonff.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.attnd_model;
import mob.field.harmonkardonff.services.WebService;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;

import com.fieldforce.harmonkardonff.AttandanceRegularize;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.harmonkardonff.R;

public class ViewAttendance extends IFragment {

    private String startDate;
    private String endDate;
    WebService webapi;
    private GenricAdapter<attnd_model> adapter;
    ArrayList<attnd_model> attnd_array = new ArrayList<attnd_model>();
    public static String datee="";
    @Override
    public void Activate(View FragmentView) {

        /**
         * User came After Clicking Update Pending Attendance on MainActivity,
         * so we will route him to Pending Attendance Tab
         */
        if (getDataAsBoolean(MainActivity.IS_SOURCE_DIALOG)) {
            putDataAsBoolean(MainActivity.IS_SOURCE_DIALOG, false);
            setTab(1);
        }

        webapi = new WebService();
        initializeDatesAndServerRequest();
        startDateProcess(R.id.txt_for_dates, R.id.btn_for_dates);
        endDateProcessed(R.id.txt_to_dates, R.id.btn_to_dates);

        /**
         * Showing Current User's Name
         */
        SetTextViewAsString(R.id.view_attendance_current_user_textview, "Current User : " + WebService.UserName);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return this.InflateView(R.layout.actvty_viewattndsl, inflater,
                container);
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
                getActivity(), new OnDateSetListener() {

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
                getActivity(), new OnDateSetListener() {

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
                        new IProcess() {

                            @Override
                            public Object underProcess() throws Exception {
                                // TODO Auto-generated method stub
                                return webapi.getattendance(start, end);
                            }

                            @Override
                            public void processResponse(Object response)
                                    throws Exception {
                                Response resp = (Response) response;

                                if (response == null) {
                                    return;
                                }
                                if (resp.isSuccess()) {
                                    removeAllRows();
                                    setTableData(resp.data);
                                    /*
                                     * clearlist(); setdataonlist(resp.data);
                                     */
                                } else {
                                    removeAllRows();
                                    // clearlist();
                                    new Dialog(getActivity())
                                            .show(resp.errormsg);
                                    // ShowToast("error");
                                }
                            }
                        });
        bp.execute();

    }

    TableLayout tableLayout;

    protected void setTableData(ArrayList<attnd_model> data) {
        if (tableLayout == null)
            initializeTableLayout();


        for (int i = 0; i < data.size(); i++) {
            attnd_model model = data.get(i);
            View view = inflator.inflate(R.layout.attendance_table_row, null);

            TextView date = view.findViewById(R.id.date);
            TextView attend = view.findViewById(R.id.attendance);
            TextView checkIN = view.findViewById(R.id.checkin);
            TextView checkout = view.findViewById(R.id.checkout);
            TextView working_hours = view.findViewById(R.id.working_hours);
            TextView update = view.findViewById(R.id.update);
            date.setText("" + model.Date);
            attend.setText("" + model.Attendance);
            checkIN.setText("" + model.CheckIn);
            checkout.setText("" + model.CheckOut);
            working_hours.setText("" + model.WorkingHours);

            if(!model.Attendance.equalsIgnoreCase("Not Marked"))
                update.setVisibility(View.GONE);
            else
                update.setVisibility(View.VISIBLE);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datee = model.Date;

                            /*SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yyyy");
                            Date date = null;
                            try {
                                date = dt.parse(date_s);
                               Log.e("Dateee", ConvertDateToString(date));
                                datee=ConvertDateToString(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/

                    if(!model.Date.equalsIgnoreCase(GetCurrentDateInString())) {
                        Intent intent = new Intent(context,AttandanceRegularize.class);
                        intent.putExtra("CheckInTime", model.CheckIn.toString());
                        startActivity(intent);
                    }else
                        ShowToast("You can not Regularize Attendance of Current Date");
                }
            });


            if (i % 2 == 0) {
                date.setBackgroundResource(R.drawable.cell_shape_row);
                attend.setBackgroundResource(R.drawable.cell_shape_row);
                checkIN.setBackgroundResource(R.drawable.cell_shape_row);
                checkout.setBackgroundResource(R.drawable.cell_shape_row);
                working_hours.setBackgroundResource(R.drawable.cell_shape_row);
            } else {
                date.setBackgroundResource(R.drawable.cell_row_white);
                attend.setBackgroundResource(R.drawable.cell_row_white);
                checkIN.setBackgroundResource(R.drawable.cell_row_white);
                checkout.setBackgroundResource(R.drawable.cell_row_white);
                working_hours.setBackgroundResource(R.drawable.cell_row_white);
            }


            tableLayout.addView(view);


        }

		/*for (attnd_model model : data) {
			View view = inflator.inflate(R.layout.attendance_table_row, null);
			((TextView) view.findViewById(R.id.date)).setText("" + model.Date);
			((TextView) view.findViewById(R.id.attendance)).setText(""
					+ model.Attendance);
			((TextView) view.findViewById(R.id.checkin)).setText(""
					+ model.CheckIn);
			((TextView) view.findViewById(R.id.checkout)).setText(""
					+ model.CheckOut);
			((TextView) view.findViewById(R.id.working_hours)).setText(""
					+ model.WorkingHours);

			tableLayout.addView(view);
		}*/
    }

    private void initializeTableLayout() {
        tableLayout = (TableLayout) findViewById(R.id.table_layout);

    }

    protected void removeAllRows() {
        if (tableLayout == null)
            initializeTableLayout();
        // ShowToast(tableLayout.getChildCount()+"");
        if (tableLayout.getChildCount() > 1) {
            while (tableLayout.getChildCount() > 1) {
                tableLayout.removeViewAt(1);
            }
        }

    }

    protected void setdataonlist(ArrayList data) {
        attnd_array = data;

        ListView lv = getListView(R.id.list);
        lv.setClickable(true);
        if (adapter == null) {
            adapter = new GenricAdapter<attnd_model>(getActivity(),
                    R.layout.activity_viewattnd);

            adapter.setGenricAdapter(new IAdapter<attnd_model>() {

                @Override
                public void setItemView(final attnd_model item, View view,
                                        int index) {
                    if (item.Date != null) {
                        ((TextView) view.findViewById(R.id.date))
                                .setText(item.Date);

                    }
                    if (item.Attendance != null) {
                        ((TextView) view.findViewById(R.id.attendance))
                                .setText(item.Attendance);

                    }

                }
            });
            lv.setAdapter(adapter);
        }
        adapter.setData(attnd_array);
        adapter.notifyDataSetChanged();

    }

    protected void clearlist() {
        attnd_array.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();

        }
    }

}
