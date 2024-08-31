package com.ariston.training_module.modules.training_module.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.ariston.training_module.modules.training_module.models.trainer_quize.TrainerQuizeResonseModel;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TrainerQuizeViewModel extends ViewModel {
    private MutableLiveData<TrainerQuizeResonseModel> trQuizeLiveResponse = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getTrainnerQuize(String trnID) {
        disposable.add(ServiceGenerator.getTrainingModuleApi().getQuizeTrainer(trnID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getTrainerQuizeResonse().setValue(success)
                        , error -> getTrainerQuizeResonse().setValue(new TrainerQuizeResonseModel(false, error.getMessage()))
                ));
    }


    public MutableLiveData<TrainerQuizeResonseModel> getTrainerQuizeResonse() {
        return trQuizeLiveResponse;
    }

    public void setTrMatLiveResponse(MutableLiveData<TrainerQuizeResonseModel> trQuizeLiveResponse) {
        this.trQuizeLiveResponse = trQuizeLiveResponse;
    }

    public void dispose() {
        disposable.dispose();
    }
}
