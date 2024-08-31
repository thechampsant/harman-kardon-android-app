package com.ariston.training_module.modules.training_module.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.training_module.models.TrainingModel;
import com.ariston.training_module.modules.training_module.models.TrainingParentModel;
import com.ariston.training_module.networking.ServiceGenerator;
import com.ariston.training_module.utility.Resource;
import com.ariston.training_module.utility.TrainingConstants;

import java.text.SimpleDateFormat;
import java.util.List;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TrainingViewModel extends ViewModel {

    private MediatorLiveData<Resource<List<TrainingModel>>> trainingListLiveData;
    private static final String TAG = "TrainingViewModelO";
    private LiveData<Resource<List<TrainingModel>>> source;

    public TrainingViewModel() {
        trainingListLiveData = new MediatorLiveData<>();
        //getListFromServer("TR12345", TrainingConstants.GetCurrentDateInString(),TrainingConstants.GetCurrentDateInString(),"");
        //getListFromServer("TR12345", "2020-07-06","2020-07-06","");

    }

    public MediatorLiveData<Resource<List<TrainingModel>>> getListLiveDataToObserve() {
        if (trainingListLiveData == null) {
            trainingListLiveData = new MediatorLiveData<>();
        }
        return trainingListLiveData;
    }

    public void getListFromServer(String userName, String sDate, String eDate, String searchQuery, boolean aBoolean) {
        trainingListLiveData.setValue(Resource.loading(null));

        if (aBoolean) {

            source = LiveDataReactiveStreams.fromPublisher(


                    ServiceGenerator.getTrainingModuleApi().getTrainingListFromServer(userName, sDate, eDate, searchQuery)
                            .subscribeOn(Schedulers.io())
                            .onErrorReturn(new Function<Throwable, TrainingParentModel>() {
                                @Override
                                public TrainingParentModel apply(Throwable throwable) throws Exception {
                                    TrainingParentModel obj = new TrainingParentModel();
                                    String errorMessage = "";
                                    obj.setStatus(false);
                                    if (throwable.getMessage().contains(TrainingConstants.UNABLE_TO_RESOLVE_HOST))
                                        errorMessage = "No Internet/Weak Internet";
                                    else if (throwable.getMessage().contains(TrainingConstants.SOFTWARE_ABORT))
                                        errorMessage = "No Internet/Weak Internet";
                                    else
                                        errorMessage = throwable.getMessage();
                                    obj.setErrormsg(errorMessage);
                                    Log.d(TAG, "ERROR :  " + throwable.getMessage());
                                    return obj;
                                }
                            })
                            .map(new Function<TrainingParentModel, Resource<List<TrainingModel>>>() {
                                @Override
                                public Resource<List<TrainingModel>> apply(TrainingParentModel trainingParentModel) throws Exception {
                                    if (trainingParentModel != null) {
                                        if (!trainingParentModel.getStatus()) {
                                            return Resource.error(trainingParentModel.getErrormsg(), null);
                                        } else {
                                            if (trainingParentModel.getData().size() < 1) {
                                                return Resource.error("No training for selected date range", null);
                                            } else {
                                                return Resource.success(getListWithDates(trainingParentModel));
                                            }
                                        }
                                    } else {
                                        return Resource.error("Data is null", null);
                                    }
                                }
                            })
                            .observeOn(Schedulers.newThread())
            );
        } else {

            source = LiveDataReactiveStreams.fromPublisher(


                    ServiceGenerator.getTrainingModuleApi().getTraineeList(userName, sDate, eDate, searchQuery)
                            .subscribeOn(Schedulers.io())
                            .onErrorReturn(new Function<Throwable, TrainingParentModel>() {
                                @Override
                                public TrainingParentModel apply(Throwable throwable) throws Exception {
                                    TrainingParentModel obj = new TrainingParentModel();
                                    String errorMessage = "";
                                    obj.setStatus(false);
                                    if (throwable.getMessage().contains(TrainingConstants.UNABLE_TO_RESOLVE_HOST))
                                        errorMessage = "No Internet/Weak Internet";
                                    else if (throwable.getMessage().contains(TrainingConstants.SOFTWARE_ABORT))
                                        errorMessage = "No Internet/Weak Internet";
                                    else
                                        errorMessage = throwable.getMessage();
                                    obj.setErrormsg(errorMessage);
                                    Log.d(TAG, "ERROR :  " + throwable.getMessage());
                                    return obj;
                                }
                            })
                            .map(new Function<TrainingParentModel, Resource<List<TrainingModel>>>() {
                                @Override
                                public Resource<List<TrainingModel>> apply(TrainingParentModel trainingParentModel) throws Exception {
                                    if (trainingParentModel != null) {
                                        if (!trainingParentModel.getStatus()) {
                                            return Resource.error(trainingParentModel.getErrormsg(), null);
                                        } else {
                                            if (trainingParentModel.getData().size() < 1) {
                                                return Resource.error("No training for selected date range", null);
                                            } else {
                                                return Resource.success(getListWithDates(trainingParentModel));
                                            }
                                        }
                                    } else {
                                        return Resource.error("Data is null", null);
                                    }
                                }
                            })
                            .observeOn(Schedulers.newThread())
            );

        }

        trainingListLiveData.addSource(source, trList -> {
            trainingListLiveData.setValue(trList);
            trainingListLiveData.removeSource(source);
        });
    }

    private String pattern = "MM/dd/yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    private List<TrainingModel> getListWithDates(TrainingParentModel trainingParentModel) {
//        for (TrainingModel obj : trainingParentModel.getData()) {
//            String[] str = obj.getTrainingDate().split(" ");
//            try {
//                obj.setDate(simpleDateFormat.parse(str[0]));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        Log.d(TAG, "getListWithDates: " + trainingParentModel.getData());
        return trainingParentModel.getData();
    }
}
