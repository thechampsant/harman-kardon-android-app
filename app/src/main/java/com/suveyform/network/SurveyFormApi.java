package com.suveyform.network;

import com.suveyform.models.history.CoronaSurveyHistoryContainer;
import com.suveyform.models.questionaire.answer_response.CoronaSurveyAnswerResponseContainer;
import com.suveyform.models.questionaire.question_response.CoronaSurveyQuestionResponseContainer;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SurveyFormApi
{
    //http://harmankardon.infield.co.in/ispmobile/GetCOVIDQuesAnsTemplate?Username=50
    @GET("GetsurveyQuesAnsTemplate")
    Flowable<CoronaSurveyQuestionResponseContainer> getSurveyQuestions(@Query("Username") String userName);

    //http://harmankardon.infield.co.in/ispmobile/SaveCOVIDQuestion?QuestionIds=1%2C2%2C3%2C4%2C5%2C6%2C7%2C8&Answers=97%2CNo%2CYes%2CNo%2CYes%2CNo%2CYes%2CNo&UserName=50
    @GET("SavesurveyQuestion")
    Flowable<CoronaSurveyAnswerResponseContainer> submitCoronaSurveyAnswersToServer(@Query("SDate") String Sdate, @Query("QuestionIds") String questionIds, @Query("Answers") String answers, @Query("UserName") String userName);


    //http://eurekamobile.v5global.co.in/ISPMobile/GetCOVIDReport1?Username=v5/test&Sdate=2020-06-17&Enddate=2020-06-17
    @GET("GetsurveyReport")
    Flowable<CoronaSurveyHistoryContainer> getCoronaSurveyHistoryDataFromServer(@Query("Username") String userName, @Query("Sdate") String startDate, @Query("Enddate") String endDate);
}
