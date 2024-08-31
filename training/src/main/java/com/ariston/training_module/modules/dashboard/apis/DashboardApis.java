package com.ariston.training_module.modules.dashboard.apis;


import com.ariston.training_module.modules.dashboard.models.graph_models.GraphParent;
import com.ariston.training_module.modules.dashboard.models.list_models.ListParentModel;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardApis {
    //https://aristonfieldforce.infield.co.in/ispmobile/GetTrainingQuizDashboard?TrainingId=1&LoginId=TR12345
    @GET("GetTrainingQuizDashboard")
    Flowable<GraphParent> getGraphDataFromServer(
            @Query("TrainingId") String trainingId,
            @Query("LoginId") String LoginId
    );

    //https://aristonfieldforce.infield.co.in/ISPMobile/GetQuizTraineeDetails?LoginID=TR12345&TrainingId=1&GraphKey=NotAttempted&PageNo=1&SearchKey=

    @GET("GetQuizTraineeDetails")
    Flowable<ListParentModel> getTraineeQuizStatusFromServer(
            @Query("LoginID") String loginId,
            @Query("TrainingId") String TrainingId,
            @Query("GraphKey") String GraphKey,
            @Query("PageNo") int PageNo,
            @Query("SearchKey") String SearchKey
    );

}
