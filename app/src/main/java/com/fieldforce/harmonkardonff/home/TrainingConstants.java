package com.fieldforce.harmonkardonff.home;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrainingConstants {

    //https://aristonfieldforce.infield.co.in/
    public static final String UNABLE_TO_RESOLVE_HOST = "Unable to resolve host \"aristonfieldforce.infield.co.in\": No address associated with hostname";
    public static final String SOFTWARE_ABORT = "Software caused connection abort";
    public static final String NO_DATA_FOUND1 = "No Data Found";
    public static final String NO_DATA_FOUND2 = "Data not found";
    public static final String NO_INTERNET = "No Internet Found...";
    public static final String WEAK_INTERNET = "Weak Network...";
    public static final String TR_DESC_ITEM = "TR_DESC_ITEM";
    public static final String USER_DETAILS = "USER_DETAILS";
    public static final String USER_ID = "USER_ID";
    public static final String TRN_ID = "TRN_ID";
    public static final String MEDIA_URL = "MEDIA_URL";
    public static final String IS_TRAINER = "IS_TRAINER";

    public static final String DASH_BOARD_GRAPH_FRAGMENT_TAG = "DashboardGraphFragment";
    public static final String DASH_BOARD_LIST_FRAGMENT_TAG = "DashboardListFragment";

    //NotAttempted ,Passed and Failed
    public static final String SLICE_1 = "Passed";
    public static final String SLICE_2 = "Failed";
    public static final String SLICE_3 = "NotAttempted";
    public static final String DOC_URL = "DOC_URL";
    public static final String MAT_ID = "MAT_ID";
    public static final String REFRESH_LIST = "REFRESH_LIST";


    public static String GetEndDateInString() {

        // TODO Auto-generated method stub
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 15);
        String MyDate = dateFormat.format(new Date(cal.getTimeInMillis()));
        return MyDate;
    }

    public static String GetStartDateInString() {

        // TODO Auto-generated method stub
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -16);
        String MyDate = dateFormat.format(new Date(cal.getTimeInMillis()));
        return MyDate;
    }

}
