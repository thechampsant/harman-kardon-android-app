package com.ariston.training_module.modules.training_module.view_models;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.tr_desc_model.TrainingDescResponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TrDescViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<TrainingDescResponse> trainingDescRes = new MutableLiveData<>();


    public MutableLiveData<TrainingDescResponse> getTrainingDescRes() {
        return trainingDescRes;
    }

    public void getTrainingItems(String loginId) {
        compositeDisposable.add(ServiceGenerator.getTrainingModuleApi().getTrainingList(loginId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getTrainingDescRes().setValue(success)
                        , error -> getTrainingDescRes().setValue(new TrainingDescResponse(false, error.getMessage()))

                ));

    }

    public void disposeApis() {
        compositeDisposable.dispose();
    }
}
