package com.fieldforce.harmonkardonff.corona_survey_module.view_models;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.answer_response.CoronaSurveyAnswerResponseContainer;
import com.fieldforce.harmonkardonff.corona_survey_module.models.questionaire.question_response.CoronaSurveyQuestionResponseContainer;
import com.fieldforce.retrofit_setup.ServiceGenerator;
import com.fieldforce.utility.Resource;
import io.reactivex.functions.Function;
import mob.field.harmonkardonff.services.WebService;

public class CoronaSurveyViewModel extends ViewModel
{
    private MediatorLiveData<Resource<CoronaSurveyQuestionResponseContainer>> responseFromWebWhenGettingQuestions;
    private MediatorLiveData<Resource<CoronaSurveyAnswerResponseContainer>> responseFromWebWhenSubmittingAnswers = new MediatorLiveData<>();

    public LiveData<Resource<CoronaSurveyQuestionResponseContainer>> observeResponse(String username){
        if (responseFromWebWhenGettingQuestions ==null){
            responseFromWebWhenGettingQuestions = new MediatorLiveData<>();
            responseFromWebWhenGettingQuestions.setValue(Resource.loading((CoronaSurveyQuestionResponseContainer)null));

            final LiveData<Resource<CoronaSurveyQuestionResponseContainer>> source = LiveDataReactiveStreams.fromPublisher(
                    ServiceGenerator.getCoronaSurveyApi().getSurveyQuestions(username)
                    .onErrorReturn(new Function<Throwable, CoronaSurveyQuestionResponseContainer>() {
                        @Override
                        public CoronaSurveyQuestionResponseContainer apply(Throwable throwable) throws Exception {
                            CoronaSurveyQuestionResponseContainer obj = new CoronaSurveyQuestionResponseContainer();
                            obj.setStatus(false);
                            obj.setErrormsg(throwable.getMessage());
                            return obj;
                        }
                    })
                    .map(new Function<CoronaSurveyQuestionResponseContainer, Resource<CoronaSurveyQuestionResponseContainer>>() {
                        @Override
                        public Resource<CoronaSurveyQuestionResponseContainer> apply(CoronaSurveyQuestionResponseContainer coronaSurveyQuestionResponseContainer) throws Exception {
                            if (coronaSurveyQuestionResponseContainer!=null){
                                if (!coronaSurveyQuestionResponseContainer.getStatus()){
                                    return Resource.error(coronaSurveyQuestionResponseContainer.getErrormsg(),null);
                                }
                                else {
                                    if (!(coronaSurveyQuestionResponseContainer.getData().size()>0)){
                                        return Resource.error("No Data Found",null);
                                    }
                                }
                            }
                            else {
                                return Resource.error("Something went wrong",null);
                            }
                            return Resource.success(coronaSurveyQuestionResponseContainer);
                        }
                    })
            );

            responseFromWebWhenGettingQuestions.addSource(source, new Observer<Resource<CoronaSurveyQuestionResponseContainer>>() {
                @Override
                public void onChanged(@Nullable Resource<CoronaSurveyQuestionResponseContainer> coronaSurveyQuestionResponseContainerResource) {
                    responseFromWebWhenGettingQuestions.setValue(coronaSurveyQuestionResponseContainerResource);
                    responseFromWebWhenGettingQuestions.removeSource(source);
                }
            });

        }
        return responseFromWebWhenGettingQuestions;
    }

    public void submitAnswersToServer(String questionsIds, String answers)
    {
            //responseFromWebWhenSubmittingAnswers = new MediatorLiveData<>();
            responseFromWebWhenSubmittingAnswers.setValue(Resource.loading((CoronaSurveyAnswerResponseContainer) null));

            final LiveData<Resource<CoronaSurveyAnswerResponseContainer>> source = LiveDataReactiveStreams.fromPublisher(
                    ServiceGenerator.getCoronaSurveyApi().submitCoronaSurveyAnswersToServer(questionsIds,answers, WebService.UserName)
                            .onErrorReturn(new Function<Throwable, CoronaSurveyAnswerResponseContainer>() {
                                @Override
                                public CoronaSurveyAnswerResponseContainer apply(Throwable throwable) throws Exception {
                                    CoronaSurveyAnswerResponseContainer obj = new CoronaSurveyAnswerResponseContainer();
                                    obj.setStatus(false);
                                    obj.setErrormsg(throwable.getMessage());
                                    return obj;
                                }
                            })
                            .map(new Function<CoronaSurveyAnswerResponseContainer, Resource<CoronaSurveyAnswerResponseContainer>>() {
                                @Override
                                public Resource<CoronaSurveyAnswerResponseContainer> apply(CoronaSurveyAnswerResponseContainer obj) throws Exception {
                                    if (obj!=null)
                                    {
                                        if (!obj.getStatus()){
                                            return Resource.error(obj.getErrormsg(),null);
                                        }
                                        else {
                                            //currently after successfully submission of Answers we get status = true and data = null
                                            // so currently after the submission of Answers we have to show Resource.success but we showing Resource.error
                                            // b'coz of this if condition.
                                            if (obj.getData()==null){
                                                return Resource.error("Response data is null during answers submission.",null);
                                            }
                                        }
                                    }
                                    else {
                                        return Resource.error("Something Went Wrong",null);
                                    }
                                    return Resource.success(obj);
                                }
                            })
            );
            responseFromWebWhenSubmittingAnswers.addSource(source, new Observer<Resource<CoronaSurveyAnswerResponseContainer>>() {
                @Override
                public void onChanged(@Nullable Resource<CoronaSurveyAnswerResponseContainer> CoronaSurveyAnswerSubmissionResponseResource) {
                    responseFromWebWhenSubmittingAnswers.setValue(CoronaSurveyAnswerSubmissionResponseResource);
                    responseFromWebWhenSubmittingAnswers.removeSource(source);
                }
            });
        //return responseFromWebWhenSubmittingAnswers;
    }

    public LiveData<Resource<CoronaSurveyAnswerResponseContainer>> getAndObserveAnswerSubmittionResponse(){
        return responseFromWebWhenSubmittingAnswers;
    }

}
