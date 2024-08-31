package com.ariston.training_module.modules.training_module.view_models;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.profile.ProfileResponse;
import com.ariston.training_module.networking.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class ProfileViewModel extends ViewModel {
    private MutableLiveData<ProfileResponse> trQuizeLiveResponse = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getProfile(String loginID) {
        disposable.add(ServiceGenerator.getTrainingModuleApi().GetUserProfile(loginID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> getProfileResponse().setValue(success)
                        , error -> getProfileResponse().setValue(new ProfileResponse(false, error.getMessage()))
                ));
    }


    public MutableLiveData<ProfileResponse> getProfileResponse() {
        return trQuizeLiveResponse;
    }

    public void setTrMatLiveResponse(MutableLiveData<ProfileResponse> trQuizeLiveResponse) {
        this.trQuizeLiveResponse = trQuizeLiveResponse;
    }

    public void dispose() {
        disposable.dispose();
    }
}