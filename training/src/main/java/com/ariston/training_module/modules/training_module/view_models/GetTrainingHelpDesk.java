package com.ariston.training_module.modules.training_module.view_models;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.faq_hep.HelpDeskResponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class GetTrainingHelpDesk extends ViewModel {
    private MutableLiveData<HelpDeskResponse> trQuizeLiveResponse = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getHelpDesk() {
        disposable.add(ServiceGenerator.getTrainingModuleApi().GetTrainingHelpDesk()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getHelpDeskResponse().setValue(success)
                        , error -> getHelpDeskResponse().setValue(new HelpDeskResponse(false, error.getMessage()))
                ));
    }


    public MutableLiveData<HelpDeskResponse> getHelpDeskResponse() {
        return trQuizeLiveResponse;
    }

    public void setTrMatLiveResponse(MutableLiveData<HelpDeskResponse> trQuizeLiveResponse) {
        this.trQuizeLiveResponse = trQuizeLiveResponse;
    }

    public void dispose() {
        disposable.dispose();
    }
}
