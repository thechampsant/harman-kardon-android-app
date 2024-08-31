package com.ariston.training_module.modules.training_module.ui.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ariston.training_module.R;
import com.ariston.training_module.modules.training_module.adapters.WrongQuestionAdpter;
import com.ariston.training_module.modules.training_module.models.tr_quize_model.Question;
import com.ariston.training_module.modules.training_module.ui.adapters.ViewPagerAdapter;
import com.ariston.training_module.modules.training_module.ui.fragment.AnswerFragment;
import com.ariston.training_module.modules.training_module.view_models.QuizeViewModel;
import com.ariston.training_module.modules.training_module.view_models.ResultViewModel;
import com.ariston.training_module.utility.ConnectionDetector;
import com.ariston.training_module.utility.TrainingConstants;
import com.ariston.training_module.utility.services.UserDetailsService;
import com.ariston.training_module.utility.widgets.CustomViewPager;
import com.ariston.training_module.utility.widgets.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private RecyclerView rv_quetionanslist;
    private WrongQuestionAdpter adapter;
    private QuizeViewModel quizeViewModel;
    private ResultViewModel resultViewModel;
    private UserDetailsService boundService;
    private boolean isBound;
    private ProgressBar progressBar, progressBar_pr;
    private final List<Question> quizeResponses = new ArrayList<>();
    private CustomViewPager viewPager;
    private CardView cv_previous, cv_next, cv_submit;
    RobotoTextView tv_submit, question_number, totalAttamptnumber, totalcorrectnumber, totalwrongnumber, scorepercentage, rtv_errorMessage,tv_wrong_Question;
    private int sizeofQuize;
    private String OptionID = "";
    private String LoginID;
    private RelativeLayout takeexamview, resultView, examsubmitView;
    private RecyclerView quetionList;
    private ImageView iv_backView;
    private  CardView cv_noDataContainer;
    private String ScorePercentage;
    private ConnectionDetector _conn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_tr);
        quizeViewModel = ViewModelProviders.of(this).get(QuizeViewModel.class);
        resultViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        _conn = new ConnectionDetector(this);
        initViews();

        if (_conn.isConnectingToInternet()) {
            initObserver();
        } else {
            examsubmitView.setVisibility(View.GONE);
            cv_noDataContainer.setVisibility(View.VISIBLE);
            rtv_errorMessage.setText("No Internet Connection!!");
            progressBar.setVisibility(View.GONE);
        }

        initService();
        setupRecycler();
        if (viewPager.getCurrentItem() == 0) {
            cv_previous.setVisibility(View.GONE);
            cv_submit.setVisibility(View.GONE);
        }

        cv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizeResponses.get(viewPager.getCurrentItem()).isSelectedpos == -1) {
                    Toast.makeText(QuizActivity.this, "One option is Required", Toast.LENGTH_LONG).show();
                    return;
                }
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                cv_previous.setVisibility(View.VISIBLE);
                cv_submit.setVisibility(View.VISIBLE);
                cv_next.setVisibility(View.GONE);
                tv_submit.setText("Next");
                question_number.setText("Question " + (viewPager.getCurrentItem() + 1) + "/" + sizeofQuize);
            }
        });
        cv_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                cv_next.setVisibility(View.GONE);
                question_number.setText("Question " + (viewPager.getCurrentItem() + 1) + "/" + sizeofQuize);
            }
        });
        cv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizeResponses.get(viewPager.getCurrentItem()).isSelectedpos == -1) {
                    Toast.makeText(QuizActivity.this, "One option is Required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (viewPager.getCurrentItem() == sizeofQuize - 1) {
                    tv_submit.setText("Submit");
                }
                if (tv_submit.getText().equals("Submit")) {
                    progressBar.setVisibility(View.VISIBLE);
                    OptionID = TextUtils.join(",", quizeResponses);
                    resultViewModel.getResult(LoginID, OptionID);
                    setResult();
                }
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                question_number.setText("Question " + (viewPager.getCurrentItem() + 1) + "/" + sizeofQuize);

            }
        });
        iv_backView.setOnClickListener(view -> onBackPressed());

    }


    private void initService() {
        Intent intent = new Intent(this, UserDetailsService.class);
        startService(intent);
        bindService(intent, boundServiceConnection, BIND_AUTO_CREATE);
    }


    private void initObserver() {
        quizeViewModel.getQuizeResponse().observe(this, trainingMaterialResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingMaterialResponse.getmStatus()) {
                //    adapter.addData(trainingMaterialResponse.getData().get(0).getQuestions().get(0).getOptions());
                sizeofQuize = trainingMaterialResponse.getData().get(0).getQuestions().size();

                quizeResponses.addAll(trainingMaterialResponse.getData().get(0).getQuestions());
                intalizeViewpager();

            } else {
                question_number.setVisibility(View.GONE);
                examsubmitView.setVisibility(View.GONE);
                cv_noDataContainer.setVisibility(View.VISIBLE);
                rtv_errorMessage.setText(trainingMaterialResponse.getmErrormsg()+"");
               // Toast.makeText(boundService, trainingMaterialResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void setupRecycler() {
        adapter = new WrongQuestionAdpter();
        quetionList.setAdapter(adapter);
        quetionList.setLayoutManager(new LinearLayoutManager(this));
        //  faqlist.addItemDecoration(new ItemDecorationAlbumColumns((int) Helper.getSizeInDp(this, 16), 3));
    }

    private void setResult() {
        resultViewModel.getResultResponse().observe(this, trainingMaterialResponse -> {
            progressBar.setVisibility(View.GONE);
            if (trainingMaterialResponse.getmStatus()) {
                Log.e("Data", trainingMaterialResponse.getData().getIncorrectQuestion().size() + "Null");



                takeexamview.setVisibility(View.GONE);
                resultView.setVisibility(View.VISIBLE);
                tv_wrong_Question.setVisibility(View.VISIBLE);
                quetionList.setVisibility(View.VISIBLE);
                adapter.addData(trainingMaterialResponse.getData().getIncorrectQuestion());
                totalAttamptnumber.setText(trainingMaterialResponse.getData().getAttempted().toString());
                totalcorrectnumber.setText(trainingMaterialResponse.getData().getCorrect().toString());
                totalwrongnumber.setText(trainingMaterialResponse.getData().getWrong().toString());
                progressBar_pr.setProgress(Integer.parseInt(trainingMaterialResponse.getData().getPercentage().replace("%", "")));
                scorepercentage.setText(trainingMaterialResponse.getData().getPercentage());
                if(trainingMaterialResponse.getData().getPercentage().contains("100"))
                    tv_wrong_Question.setVisibility(View.GONE);
                ScorePercentage=trainingMaterialResponse.getData().getPercentage().replaceAll("%","")+"";
            } else {
                Toast.makeText(boundService, trainingMaterialResponse.getmErrormsg(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void intalizeViewpager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Question model : quizeResponses) {
            adapter.addFragment(AnswerFragment.Instance(model), "");
        }
        viewPager.setAdapter(adapter);
    }


    private void initViews() {
        //rv_quetionanslist = findViewById(R.id.rv_quetionanslist_tr);
        progressBar = findViewById(R.id.pbload);
        progressBar_pr = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.rv_quetionanslist_pager);
        cv_next = findViewById(R.id.cv_next);
        cv_previous = findViewById(R.id.cv_previous);
        cv_submit = findViewById(R.id.cv_submit);
        tv_submit = findViewById(R.id.tv_submit);
        question_number = findViewById(R.id.question_number);
        takeexamview = findViewById(R.id.takeexamview);
        quetionList=findViewById(R.id.lv_quetionList);
        resultView = findViewById(R.id.resultView);
        iv_backView = findViewById(R.id.iv_back);
        totalAttamptnumber = findViewById(R.id.totalAttamptnumber_tr);
        totalcorrectnumber = findViewById(R.id.totalcorrectnumber_tr);
        totalwrongnumber = findViewById(R.id.totalwrongnumber_tr);
        scorepercentage = findViewById(R.id.scorepercentage1);
        tv_wrong_Question=findViewById(R.id.tv_wrong_Question);
        examsubmitView = findViewById(R.id.examsubmitView);
        cv_noDataContainer = findViewById(R.id.cv_noDataContainer);
        rtv_errorMessage = findViewById(R.id.rtv_errorMessage);
        question_number.setText("Question 1/1");

    }

    private ServiceConnection boundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UserDetailsService.MyBinder binderBridge = (UserDetailsService.MyBinder) service;
            boundService = binderBridge.getService();
            isBound = true;
            LoginID = boundService.getUserBundle().getString(TrainingConstants.USER_ID);
            quizeViewModel.getTrainingMaterialItems(boundService.getUserBundle().getString(TrainingConstants.TRN_ID), boundService.getUserBundle().getString(TrainingConstants.USER_ID));

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(boundServiceConnection);
            isBound = false;
        }
        quizeViewModel.dispose();

    }

    public void selectAnswer(int i) {
        quizeResponses.get(viewPager.getCurrentItem()).isSelectedpos = i;

    }

    @Override
    public void onBackPressed() {
        if(resultView.getVisibility()==View.VISIBLE){
            Intent intent=new Intent();
            intent.putExtra("Percentage",ScorePercentage);
            setResult(Activity.RESULT_OK,intent);
            super.onBackPressed();
        }else{
            super.onBackPressed();

        }
    }
}