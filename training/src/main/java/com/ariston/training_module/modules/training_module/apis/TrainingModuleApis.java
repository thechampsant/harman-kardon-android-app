package com.ariston.training_module.modules.training_module.apis;

import com.ariston.training_module.modules.training_module.models.BaseResponse;
import com.ariston.training_module.modules.training_module.models.TrainingParentModel;
import com.ariston.training_module.modules.training_module.models.faq_hep.Faqresponse;
import com.ariston.training_module.modules.training_module.models.faq_hep.HelpDeskResponse;
import com.ariston.training_module.modules.training_module.models.profile.ProfileResponse;
import com.ariston.training_module.modules.training_module.models.tr_desc_model.TrainingDescResponse;
import com.ariston.training_module.modules.training_module.models.tr_mat_model.TrainingMaterialResponse;
import com.ariston.training_module.modules.training_module.models.tr_quize_model.QuizeResponse;
import com.ariston.training_module.modules.training_module.models.tr_quize_model.ResultResponse;
import com.ariston.training_module.modules.training_module.models.trainer_quize.TrainerQuizeResonseModel;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TrainingModuleApis {
    //https://aristonfieldforce.infield.co.in/ISPMobile/GetTrainingList?LoginID=TR12345&StartDate=2020-07-06&EndDate=2020-07-06&SearchKey=
    @GET("GetTrainingList")
    Flowable<TrainingParentModel> getTrainingListFromServer(
            @Query("LoginID") String query,
            @Query("StartDate") String sDate,
            @Query("EndDate") String eDate,
            @Query("SearchKey") String searchKey
    );

    @GET("GetTrainingListOfTrainee")
    Flowable<TrainingParentModel> getTraineeList(
            @Query("LoginID") String query,
            @Query("StartDate") String sDate,
            @Query("EndDate") String eDate,
            @Query("SearchKey") String searchKey
    );

    @GET("GetTrainingLinksOfUser")
    Single<TrainingDescResponse> getTrainingList(@Query("LoginID") String query);

    @GET("GetTrainingMaterials")
    Single<TrainingMaterialResponse> getTrainingMatList(@Query("TrainingId") String query,
                                                        @Query("LoginId") String query1);

    @GET("GetTrainingHelpDesk")
    Single<HelpDeskResponse> GetTrainingHelpDesk();

    @GET("GetTrainingFAQ")
    Single<Faqresponse> GetTrainingFAQ();

    @GET("GetQuizOfTraining")
    Single<QuizeResponse> getQuizeList(
            @Query("TrainingId") String query,
            @Query("LoginId") String query1
    );


    @GET("GetQuizQuestionOfTraining")
    Single<TrainerQuizeResonseModel> getQuizeTrainer(
            @Query("TrainingId") String query
    );



    @GET("GetUserProfile")
    Single<ProfileResponse> GetUserProfile(
            @Query("LoginId") String query1
    );

    @FormUrlEncoded
    @POST("SaveQuizOfTraining")
    Single<ResultResponse> setResult(
            @Field("LoginId") String query,
            @Field("OptionIds") String query1
    );

    @FormUrlEncoded
    @POST("SaveSeenTrainingMaterialByTrainee")
    Single<BaseResponse> trainingMatCompleted(@Field("LoginId") String query,
                                              @Field("MaterialId") Long query1,
                                              @Field("IsCompleted") Boolean query2);



}
