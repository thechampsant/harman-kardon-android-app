package com.fieldforce.viewModel;




import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.fieldforce.model.NotificationResonse;
import com.fieldforce.retrofit_setup.ServiceGenerator;
import com.fieldforce.utility.Resource;

import io.reactivex.functions.Function;
import mob.field.harmonkardonff.services.WebService;



public class NotificationViewModule extends ViewModel {
    private MediatorLiveData<Resource<NotificationResonse>> coronaHistoryDataResponse = new MediatorLiveData<>();

    public LiveData<Resource<NotificationResonse>> getCoronaHistoryDataResponse(){
        return coronaHistoryDataResponse;
    }

    public void getSurveyHistoryData(){
        final LiveData<Resource<NotificationResonse>> source = LiveDataReactiveStreams.fromPublisher(
                ServiceGenerator.getCoronaSurveyApi().getTrainingNotification(WebService.UserID)
                        .onErrorReturn(new Function<Throwable, NotificationResonse>() {
                            @Override
                            public NotificationResonse apply(Throwable throwable) throws Exception {
                                NotificationResonse obj = new NotificationResonse();
                                obj.setStatus(false);
                                obj.setErrormsg(throwable.getMessage());
                                return obj;
                            }
                        })
                        .map(new Function<NotificationResonse, Resource<NotificationResonse>>() {
                            @Override
                            public Resource<NotificationResonse> apply(NotificationResonse object) throws Exception {
                                if (object!=null){
                                    if (!object.getStatus()){
                                        return Resource.error(object.getErrormsg().toString(),null);
                                    }
                                    else {
                                        if (object.getData()!=null){
                                            if (!(object.getData().size()>0)){
                                                return Resource.error("No Data Found",null);
                                            }
                                        }
                                        else {
                                            return Resource.error("Response111 Data is null during fetching of History Data.",null);
                                        }
                                    }
                                }
                                else {
                                    return Resource.error("Response11111 Data is null during fetching of History Data.",null);
                                }
                                return Resource.success(object);
                            }
                        })
        );
        coronaHistoryDataResponse.addSource(source, new Observer<Resource<NotificationResonse>>() {
            @Override
            public void onChanged(@Nullable Resource<NotificationResonse> NotificationResonseResource) {
                coronaHistoryDataResponse.setValue(NotificationResonseResource);
                coronaHistoryDataResponse.removeSource(source);
            }
        });
    }
}
