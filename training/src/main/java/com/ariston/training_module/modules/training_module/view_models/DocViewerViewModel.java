package com.ariston.training_module.modules.training_module.view_models;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DocViewerViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<BaseResponse> trainingMatCompleteResponse = new MutableLiveData<>();


    public MutableLiveData<BaseResponse> getTrainingMatLiveData() {
        return trainingMatCompleteResponse;
    }

    public void trainMatCompleted(String loginId, Long materialId) {
        compositeDisposable.add(ServiceGenerator.getTrainingModuleApi().trainingMatCompleted(loginId, materialId, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getTrainingMatLiveData().setValue(success)
                        , error -> getTrainingMatLiveData().setValue(new BaseResponse(false, error.getMessage()))

                ));

    }

    public void disposeApis() {
        compositeDisposable.dispose();
    }
}
