package com.suveyform.view_models;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.fieldforce.asyntask.WebService;
import com.fieldforce.harmonkardonff.MainActivity;
import com.fieldforce.retrofit_setup.ServiceGenerator;
import com.suveyform.models.history.CoronaSurveyHistoryContainer;
import com.suveyform.utils.Resource;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.Function;

public class SurveyFormHistoryViewModel extends ViewModel {
    private static final String TAG = "CoronaSurveyHistoryView";
    private MediatorLiveData<Resource<CoronaSurveyHistoryContainer>> coronaHistoryDataResponse = new MediatorLiveData<>();

    private static CoronaSurveyHistoryContainer apply(Throwable throwable) {
        CoronaSurveyHistoryContainer obj = new CoronaSurveyHistoryContainer();
        obj.setStatus(false);
        if (throwable.getMessage().contains("Unable to resolve host \"eurekamobile.v5global.co.in\": No address associated with hostname")) {
            obj.setErrormsg("No Internet Connection...");
        } else {
            obj.setErrormsg(throwable.getMessage());
        }
        Log.d(TAG, "ERROR -> " + throwable.getMessage());
        return obj;
    }

    public LiveData<Resource<CoronaSurveyHistoryContainer>> getCoronaHistoryDataResponse(){
        return coronaHistoryDataResponse;
    }

    public void getSurveyHistoryData(String startDate, String endDate){
        Log.d(TAG, "getSurveyHistoryData: "+ MainActivity.MyInfo.UserID.toString());
        coronaHistoryDataResponse.setValue(Resource.loading((CoronaSurveyHistoryContainer) null));
        final LiveData<Resource<CoronaSurveyHistoryContainer>> source = LiveDataReactiveStreams.fromPublisher(
                ServiceGenerator.getCoronaSurveyApi1().getCoronaSurveyHistoryDataFromServer(MainActivity.MyInfo.UserID.toString(),startDate,endDate)
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(new Function<Throwable, CoronaSurveyHistoryContainer>() {
                            @Override
                            public CoronaSurveyHistoryContainer apply(Throwable throwable) throws Exception {
                                CoronaSurveyHistoryContainer obj = new CoronaSurveyHistoryContainer();
                                obj.setStatus(false);
                                if (throwable.getMessage().contains("Unable to resolve host \"eurekamobile.v5global.co.in\": No address associated with hostname")){
                                    obj.setErrormsg("No Internet Connection...");
                                }
                                else {
                                    obj.setErrormsg(throwable.getMessage());
                                }
                                Log.d(TAG, "ERROR -> "+throwable.getMessage());
                                return obj;
                            }
                        })
                        .map(new Function<CoronaSurveyHistoryContainer, Resource<CoronaSurveyHistoryContainer>>() {
                            @Override
                            public Resource<CoronaSurveyHistoryContainer> apply(CoronaSurveyHistoryContainer object) throws Exception {
                                if (object!=null){
                                    if (!object.getStatus()){
                                        return Resource.error(object.getErrormsg(),null);
                                    }
                                    else {
                                        if (object.getData()!=null){
                                            if (!(object.getData().size()>0)){
                                                return Resource.error("No Data Found",null);
                                            }
                                        }
                                        else {
                                            return Resource.error("Response Data is null during fetching of History Data.",null);
                                        }
                                    }
                                }
                                else {
                                    return Resource.error("Response Data is null during fetching of History Data.",null);
                                }
                                return Resource.success(object);
                            }
                        })
                        .observeOn(Schedulers.newThread())
        );
        coronaHistoryDataResponse.addSource(source, new Observer<Resource<CoronaSurveyHistoryContainer>>() {
            @Override
            public void onChanged(@Nullable Resource<CoronaSurveyHistoryContainer> coronaSurveyHistoryContainerResource) {
                coronaHistoryDataResponse.removeSource(source);
                coronaHistoryDataResponse.setValue(coronaSurveyHistoryContainerResource);
            }
        });
    }
}
