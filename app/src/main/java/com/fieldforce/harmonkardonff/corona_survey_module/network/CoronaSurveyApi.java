package com.fieldforce.harmonkardonff.corona_survey_module.network;

import com.fieldforce.harmonkardonff.corona_survey_module.models.history.CoronaSurveyHistoryContainer;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.answer_response.CoronaSurveyAnswerResponseContainer;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.question_response.CoronaSurveyQuestionResponseContainer;
import com.fieldforce.model.NotificationResonse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoronaSurveyApi
{
    //http://harmankardon.infield.co.in/ispmobile/GetCOVIDQuesAnsTemplate?Username=50
    @GET("GetCOVIDQuesAnsTemplate")
    Flowable<CoronaSurveyQuestionResponseContainer> getSurveyQuestions(@Query("Username") String userName);

    //http://harmankardon.infield.co.in/ispmobile/SaveCOVIDQuestion?QuestionIds=1%2C2%2C3%2C4%2C5%2C6%2C7%2C8&Answers=97%2CNo%2CYes%2CNo%2CYes%2CNo%2CYes%2CNo&UserName=50
    @GET("SaveCOVIDQuestion")
    Flowable<CoronaSurveyAnswerResponseContainer> submitCoronaSurveyAnswersToServer(@Query("QuestionIds")String questionIds, @Query("Answers") String answers, @Query("UserName") String userName);


    //http://harmankardon.infield.co.in/ispmobile/GetCOVIDReport1?UserName=50&Sdate=2020-05-08&Enddate=2020-05-09
    @GET("GetCOVIDReport1")
    Flowable<CoronaSurveyHistoryContainer> getCoronaSurveyHistoryDataFromServer(@Query("UserName") String userName, @Query("Sdate") String startDate, @Query("Enddate") String endDate);

    @GET("GetTrainingNotification")
    Flowable<NotificationResonse> getTrainingNotification(@Query("LoginId") String LoginId);

}
