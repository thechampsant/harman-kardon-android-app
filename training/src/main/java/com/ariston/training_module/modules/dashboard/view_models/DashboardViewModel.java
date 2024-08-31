package com.ariston.training_module.modules.dashboard.view_models;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.ariston.training_module.modules.dashboard.models.graph_models.GraphParent;
import com.ariston.training_module.modules.dashboard.models.list_models.ListParentModel;
import com.ariston.training_module.modules.dashboard.models.list_models.TraineeDetailModel;
import com.ariston.training_module.networking.ServiceGenerator;
import com.ariston.training_module.utility.Resource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> graphKey = new MutableLiveData<>();

    private MutableLiveData<String> loginId = new MutableLiveData<>();

    private MutableLiveData<String> trainingId = new MutableLiveData<>();

    private MediatorLiveData<Resource<GraphParent>> mediatorLiveData;

    private MediatorLiveData<Resource<List<TraineeDetailModel>>> traineeListLiveData;

    public MutableLiveData<String> observeGraphKey(){
        if (graphKey == null){
            graphKey = new MutableLiveData<>();
        }
        return graphKey;
    }

    public MediatorLiveData<Resource<List<TraineeDetailModel>>> observeTraineeList(){
        if (traineeListLiveData == null){
            traineeListLiveData = new MediatorLiveData<>();
        }
        return traineeListLiveData;
    }

    public String getLoginId(){
        return loginId.getValue();
    }
    public String getGraphKey(){
        return graphKey.getValue();
    }
    public String getTrainingId(){
        return trainingId.getValue();
    }

    public void setLoginId(String Id){
        loginId.setValue(Id);
    }
    public void setGraphKey(String key){
        graphKey.setValue(key);
    }
    public void setTrainingId(String training){
        trainingId.setValue(training);
    }

    public MediatorLiveData<Resource<GraphParent>> getDataFromServerAndObserve(String trainingId, String loginId)
    {
        if (mediatorLiveData==null)
        {
            mediatorLiveData = new MediatorLiveData<>();
            mediatorLiveData.setValue(Resource.loading(null));

            final LiveData<Resource<GraphParent>> source = LiveDataReactiveStreams.fromPublisher(
                    ServiceGenerator.getDashboardModuleApis().getGraphDataFromServer(trainingId,loginId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.newThread())
                            .onErrorReturn(throwable -> {
                                GraphParent obj = new GraphParent();
                                obj.setStatus(false);
                                obj.setErrormsg(throwable.getMessage());
                                return obj;
                            })
                            .map(graphParent -> {
                                if (graphParent==null){
                                    return Resource.error("Data is null",null);
                                }
                                else {
                                    if (!graphParent.getStatus()){
                                        return Resource.error(graphParent.getErrormsg(),null);
                                    }
                                    else {
                                        if (graphParent.getData()!=null){
                                            if (graphParent.getData().size()<1){
                                                return Resource.error("No Data Found",null);
                                            }
                                            else {
                                                return Resource.success(graphParent);
                                            }
                                        }
                                        else {
                                            return Resource.error("Dashboard data is null",null);
                                        }
                                    }
                                }
                            })
            );
            mediatorLiveData.addSource(source, new Observer<Resource<GraphParent>>() {
                @Override
                public void onChanged(@Nullable Resource<GraphParent> graphParentResource) {
                    mediatorLiveData.setValue(graphParentResource);
                    mediatorLiveData.removeSource(source);
                }
            });
        }
        return mediatorLiveData;
    }


    public void getTraineesStatusDataFromServer(String loginId, String trainingId, String graphKey, int pageNumber, String search)
    {
        traineeListLiveData.setValue(Resource.loading(null));
        LiveData<Resource<List<TraineeDetailModel>>> source = LiveDataReactiveStreams.fromPublisher(
                ServiceGenerator.getDashboardModuleApis().getTraineeQuizStatusFromServer(loginId,trainingId,graphKey,pageNumber,search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, ListParentModel>() {
                    @Override
                    public ListParentModel apply(Throwable throwable) throws Exception {
                        ListParentModel obj = new ListParentModel();
                        obj.setStatus(false);
                        obj.setErrormsg(throwable.getMessage());
                        return obj;
                    }
                })
                .map(new Function<ListParentModel, Resource<List<TraineeDetailModel>>>() {
                    @Override
                    public Resource<List<TraineeDetailModel>> apply(ListParentModel listParentModel) throws Exception {
                        if (listParentModel==null){
                            return Resource.error("Data is null",null);
                        }
                        else {
                            if (!listParentModel.getStatus()){
                                return Resource.error(listParentModel.getErrormsg(),null);
                            }
                            else {
                                if (listParentModel.getData()==null){
                                    return Resource.error("No Data Found",null);
                                }
                                else {
                                    if (listParentModel.getData().size()<1){
                                        return Resource.error("No data found",null);
                                    }
                                    else {
                                        return Resource.success(listParentModel.getData());
                                    }
                                }
                            }
                        }
                    }
                })
        );
        traineeListLiveData.addSource(source, listResource -> {
            traineeListLiveData.setValue(listResource);
            traineeListLiveData.removeSource(source);
        });
    }

}
