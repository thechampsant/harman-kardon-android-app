package com.fieldforce.harmonkardonff.corona_survey_module.view_models;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.fieldforce.harmonkardonff.corona_survey_module.models.history.CoronaSurveyHistoryContainer;
import com.fieldforce.retrofit_setup.ServiceGenerator;
import com.fieldforce.utility.Resource;

import io.reactivex.functions.Function;
import mob.field.harmonkardonff.services.WebService;

public class CoronaSurveyHistoryViewModel extends ViewModel {
    private MediatorLiveData<Resource<CoronaSurveyHistoryContainer>> coronaHistoryDataResponse = new MediatorLiveData<>();

    public LiveData<Resource<CoronaSurveyHistoryContainer>> getCoronaHistoryDataResponse(){
        return coronaHistoryDataResponse;
    }

    public void getSurveyHistoryData(String startDate, String endDate){
        final LiveData<Resource<CoronaSurveyHistoryContainer>> source = LiveDataReactiveStreams.fromPublisher(
                ServiceGenerator.getCoronaSurveyApi().getCoronaSurveyHistoryDataFromServer(WebService.UserName,startDate,endDate)
                .onErrorReturn(new Function<Throwable, CoronaSurveyHistoryContainer>() {
                    @Override
                    public CoronaSurveyHistoryContainer apply(Throwable throwable) throws Exception {
                        CoronaSurveyHistoryContainer obj = new CoronaSurveyHistoryContainer();
                        obj.setStatus(false);
                        obj.setErrormsg(throwable.getMessage());
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
        );
        coronaHistoryDataResponse.addSource(source, new Observer<Resource<CoronaSurveyHistoryContainer>>() {
            @Override
            public void onChanged(@Nullable Resource<CoronaSurveyHistoryContainer> coronaSurveyHistoryContainerResource) {
                coronaHistoryDataResponse.setValue(coronaSurveyHistoryContainerResource);
                coronaHistoryDataResponse.removeSource(source);
            }
        });
    }
}
