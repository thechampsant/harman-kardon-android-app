/*
package com.fieldforce.training_module.modules.training_module.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fieldforce.training_module.R;
import com.fieldforce.training_module.modules.training_module.adapters.AdpterQuestAnswerList;
import com.fieldforce.training_module.modules.training_module.adapters.TrainerAdpterQuestAnswerList;
import com.fieldforce.training_module.modules.training_module.models.tr_quize_model.Question;
import com.fieldforce.training_module.modules.training_module.models.trainer_quize.TrainerQuizeResonse;
import com.ariston.training_module.utility.widgets.RobotoTextView;

public class AnswerFragmentTrainer extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TrainerQuizeResonse quizModel;
    RobotoTextView question,question_number;
    private RecyclerView rv_quetionanslist;
    private TrainerAdpterQuestAnswerList adapter;
    public static AnswerFragmentTrainer Instance(TrainerQuizeResonse model){
        // Required empty public constructor
        AnswerFragmentTrainer instance = new AnswerFragmentTrainer();
        Bundle bundle=new Bundle();
        bundle.putSerializable("model",model);
        instance.setArguments(bundle);
        return  instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_answer, container, false);

        quizModel=(TrainerQuizeResonse)getArguments().getSerializable("model");
        question=view.findViewById(R.id.question);
        question_number=view.findViewById(R.id.question_number);
        rv_quetionanslist=view.findViewById(R.id.rv_quetionanslist_tr);
        //question.setText("Quesdtion"+quizModel.getQuestions());
        question.setText(quizModel.getQuestionName());
        setupRecycler();
        adapter.addData(quizModel.getAnswerTamplate(),getActivity(),quizModel.isSelectedpos);
        return view;
    }

    private void setupRecycler() {
        adapter = new TrainerAdpterQuestAnswerList();
        rv_quetionanslist.setAdapter(adapter);
        rv_quetionanslist.setLayoutManager(new LinearLayoutManager(getActivity()));
        // rv_quetionanslist.addItemDecoration(new ItemDecorationAlbumColumns((int) Helper.getSizeInDp(getActivity(), 16), 3));
    }


}*/
