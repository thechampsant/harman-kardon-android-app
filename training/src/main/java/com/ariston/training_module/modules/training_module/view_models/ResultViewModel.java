package com.ariston.training_module.modules.training_module.view_models;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.tr_quize_model.ResultResponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class ResultViewModel extends ViewModel {
    private MutableLiveData<ResultResponse> trQuizeLiveResponse = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getResult(String trnID,String loginID) {
        disposable.add(ServiceGenerator.getTrainingModuleApi().setResult(trnID,loginID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getResultResponse().setValue(success)
                        , error -> getResultResponse().setValue(new ResultResponse(false, error.getMessage()))
                ));
    }


    public MutableLiveData<ResultResponse> getResultResponse() {
        return trQuizeLiveResponse;
    }

    public void setResult(MutableLiveData<ResultResponse> trQuizeLiveResponse) {
        this.trQuizeLiveResponse = trQuizeLiveResponse;
    }

    public void dispose() {
        disposable.dispose();
    }
}
