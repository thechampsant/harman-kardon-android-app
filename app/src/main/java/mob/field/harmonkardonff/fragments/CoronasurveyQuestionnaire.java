package mob.field.harmonkardonff.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.customAdapter.corona_adapters.VerticalItemDecorator;
import com.fieldforce.customAdapter.corona_adapters.questionnaire.CoronaSurveyAdapter;
import com.fieldforce.harmonkardonff.R;

import java.util.HashMap;

import app.core.async.BackgroundProcess;
import app.core.async.IProcess;
import app.core.base.IFragment;
import app.core.model.Response;
import app.core.utils.Dialog;
import linq.ArrayList;
import mob.field.harmonkardonff.entitiymodels.CoronaSurveyRequestModel;
import mob.field.harmonkardonff.services.WebService;

public class CoronasurveyQuestionnaire extends IFragment {

    WebService service = new WebService();
    RecyclerView recyclerView;
    private TextView textViewSubmit;
    private TextView textViewWarning;
    private CoronaSurveyAdapter mAdapter;

    public static HashMap<String,String> mHap = new HashMap<>();

    @Override
    public void Activate(View FragmentView)
    {
        recyclerView = (RecyclerView) findViewById(R.id.rv_coronaSurvey);
        textViewSubmit = (TextView) findViewById(R.id.tv_submitCoronaQuestionnaire);
        textViewWarning = (TextView) findViewById(R.id.tv_warning);
        mAdapter = new CoronaSurveyAdapter();
        initRecycler();
        getDataFromServer();
        textViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sendAnswerToServer();
                }
                catch (Exception exception){
                    new Dialog(getActivity()).setTitle("Exception").setMessage(exception.getMessage()+" in "+TAG+", "+exception.getStackTrace()[0]).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.InflateView(R.layout.fragment_questionaire_newcorona, inflater, container);
    }

    private void initRecycler(){
        recyclerView.addItemDecoration(new VerticalItemDecorator(30));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

    }

    private void getDataFromServer(){
        if (isNetworkAvailable()){
            BackgroundProcess backgroundProcess = new BackgroundProcess(this);
            backgroundProcess.setProgressMessage("Getting Data...");
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return service.getQuestionsFromServer();
                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response questionsResponseFromServer = (Response) response;
                    if (questionsResponseFromServer.isSuccess() && questionsResponseFromServer.data != null)
                    {
                        ArrayList<CoronaSurveyRequestModel> arrDocModel = questionsResponseFromServer.data;
                        prepareHashMap(arrDocModel);
                        mAdapter.setDataToAdapter(arrDocModel);
                        textViewSubmit.setVisibility(View.VISIBLE);
                        textViewWarning.setVisibility(View.GONE);
                    }
                    else {
                        new Dialog(getActivity()).setTitle("Error").setMessage(questionsResponseFromServer.errormsg).show();
                        textViewSubmit.setVisibility(View.GONE);
                        textViewWarning.setVisibility(View.VISIBLE);
                    }
                }
            });
            backgroundProcess.execute();
        }
        else {
            textViewWarning.setVisibility(View.VISIBLE);
            ShowToast("No internet...");
        }
    }

    private void prepareHashMap(ArrayList<CoronaSurveyRequestModel> arrDocModel){
        for (CoronaSurveyRequestModel obj : arrDocModel){
            mHap.put(obj.QID,"");
        }
        Log.d(TAG, "prepareHashMap: "+mHap);
    }

    private static final String TAG = "CoronasurveyQuestioO";
    private boolean allAnswersValidated = true;
    private boolean validate(){
        StringBuilder stringBuilder = new StringBuilder();
        String tempRange = "";
        try{
            allAnswersValidated = true;
            Log.d(TAG, "validate: "+mHap);
            java.util.ArrayList<CoronaSurveyRequestModel> questionListt = mAdapter.getRequestList();
            for (int i = 0;i<questionListt.size();i++){
                String value = mHap.get(questionListt.get(i).QID);
                if (value.equalsIgnoreCase(""))
                {
                    stringBuilder.append("Q"+(i+1)+",");
                    allAnswersValidated = false;
                }
                else {
                    if (questionListt.get(i).QID.equalsIgnoreCase("1")){
                        double db = Double.parseDouble(value);
                        if (db<96)
                        {
                            tempRange = "Temperature value must be in between 96 - 104.";
                            allAnswersValidated = false;
                        }
                        else {
                            if (db>104){
                                tempRange = "Temperature value must be in between 96 - 104.";
                                allAnswersValidated = false;
                            }
                        }
                    }
                }

            }
        }
        catch (Exception exception){
            allAnswersValidated = false;
            new Dialog(getActivity()).setTitle("Exception").setMessage(exception.getMessage()+" in "+TAG+", "+exception.getStackTrace()[0]).show();
        }
        Log.d(TAG, "No Answer found for : "+stringBuilder.toString());

        if (allAnswersValidated){
            return true;
        }
        else {
            if (stringBuilder.length()>0)
                new Dialog(getActivity()).setTitle("No Answer Found").setMessage("Please provide answers for Question : "+stringBuilder.substring(0, stringBuilder.length() - 1)).show();
            if (tempRange.length()>2)
                new Dialog(getActivity()).setTitle("Temperature Range").setMessage(tempRange).show();
            return false;
        }
    }

    private void sendAnswerToServer()
    {
        try{
            if (validate()){
                prepareQuestionIdsInAKey();
            }
            else {
                Log.d(TAG, "Something went wrong...");
            }
        }
        catch (Exception exception){
            Log.d(TAG, "sendAnswerToServer: "+exception.getMessage());
            new Dialog(getActivity()).setTitle("Exception").setMessage(exception.getMessage()+" in "+TAG+", "+exception.getStackTrace()[0]).show();
        }
    }

    private void prepareQuestionIdsInAKey()
    {
        ArrayList<CoronaSurveyRequestModel> questionListt = mAdapter.getRequestList();
        StringBuilder questionIds = new StringBuilder();
        StringBuilder answers = new StringBuilder();

        for (CoronaSurveyRequestModel obj : questionListt){
            if (obj!=null){
                questionIds.append(obj.QID).append(",");
                answers.append(mHap.get(obj.QID)).append(",");
            }
        }
        questionIds = new StringBuilder(questionIds.substring(0, questionIds.length() - 1));
        answers = new StringBuilder(answers.substring(0, answers.length() - 1));
        Log.d(TAG, "prepareQuestionIdsInAKey: QuestionIds -> "+questionIds);
        Log.d(TAG, "prepareQuestionIdsInAKey: Answers -> "+answers);

        hitApi(questionIds.toString(),answers.toString());

    }

    private void hitApi(final String questionIds, final String Answers){
        if (isNetworkAvailable()){
            BackgroundProcess backgroundProcess = new BackgroundProcess(this);
            backgroundProcess.setProgressMessage("Submitting Data...");
            backgroundProcess.setbackgroundProcess(new IProcess() {
                @Override
                public Object underProcess() throws Exception {
                    return service.sendAnswersToServer(questionIds,Answers);
                }

                @Override
                public void processResponse(Object response) throws Exception {
                    Response res = (Response) response;
                    if (res.status.equalsIgnoreCase("true")){
                        ShowToast("Data Submission successful");
                        CoronasurveyQuestionnaire.this.setTab(1);
                    }
                    else {
                        Log.d(TAG, "hitApi -> processResponse: "+res.errormsg);
                        new Dialog(getActivity()).setTitle("Error").setMessage(res.errormsg).show();
                    }
                }
            });
            backgroundProcess.execute();
        }
        else {
            ShowToast("No internet...");
        }
    }


}
