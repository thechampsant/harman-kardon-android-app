package com.ariston.training_module.modules.training_module.view_models;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.tr_mat_model.TrainingMaterialResponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewModelTrainingMat extends ViewModel {
    private MutableLiveData<TrainingMaterialResponse> trMatLiveResponse = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getTrainingMaterialItems(String trnID,String LoginID) {
        disposable.add(ServiceGenerator.getTrainingModuleApi().getTrainingMatList(trnID,LoginID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getTrMatLiveResponse().setValue(success)
                        , error -> getTrMatLiveResponse().setValue(new TrainingMaterialResponse(false, error.getMessage()))
                ));
    }


    public MutableLiveData<TrainingMaterialResponse> getTrMatLiveResponse() {
        return trMatLiveResponse;
    }

    public void setTrMatLiveResponse(MutableLiveData<TrainingMaterialResponse> trMatLiveResponse) {
        this.trMatLiveResponse = trMatLiveResponse;
    }

    public void dispose() {
        disposable.dispose();
    }
}
