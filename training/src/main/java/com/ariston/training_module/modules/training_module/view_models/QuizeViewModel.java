package com.ariston.training_module.modules.training_module.view_models;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.tr_quize_model.QuizeResponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class QuizeViewModel extends ViewModel {
    private MutableLiveData<QuizeResponse> trQuizeLiveResponse = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getTrainingMaterialItems(String trnID,String loginID) {
        disposable.add(ServiceGenerator.getTrainingModuleApi().getQuizeList(trnID,loginID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getQuizeResponse().setValue(success)
                        , error -> getQuizeResponse().setValue(new QuizeResponse(false, error.getMessage()))
                ));
    }


    public MutableLiveData<QuizeResponse> getQuizeResponse() {
        return trQuizeLiveResponse;
    }

    public void setTrMatLiveResponse(MutableLiveData<QuizeResponse> trQuizeLiveResponse) {
        this.trQuizeLiveResponse = trQuizeLiveResponse;
    }

    public void dispose() {
        disposable.dispose();
    }
}
