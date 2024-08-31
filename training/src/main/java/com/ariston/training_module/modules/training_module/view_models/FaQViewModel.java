package com.ariston.training_module.modules.training_module.view_models;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.faq_hep.Faqresponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;



public class FaQViewModel extends ViewModel {
    private MutableLiveData<Faqresponse> trQuizeLiveResponse = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getHelpDesk() {
        disposable.add(ServiceGenerator.getTrainingModuleApi().GetTrainingFAQ()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getFaqresponse().setValue(success)
                        , error -> getFaqresponse().setValue(new Faqresponse(false, error.getMessage()))
                ));
    }


    public MutableLiveData<Faqresponse> getFaqresponse() {
        return trQuizeLiveResponse;
    }

    public void setTrMatLiveResponse(MutableLiveData<Faqresponse> trQuizeLiveResponse) {
        this.trQuizeLiveResponse = trQuizeLiveResponse;
    }

    public void dispose() {
        disposable.dispose();
    }
}
