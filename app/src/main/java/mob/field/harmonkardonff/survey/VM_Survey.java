package mob.field.harmonkardonff.survey;

import linq.ArrayList;
import app.core.events.IEvent;
import app.core.viewmodels.ViewModel;

public class VM_Survey extends ViewModel {

	public ArrayList<Survey> SurveyQuestions = null;
	public ArrayList<Survey> Holddata = null;
	public ArrayList<Survey> CurrentSurveyAnswer = null;

	public Survey currentSurvey = null;
	private int Index = 0;
	private int SerialNo = 0;
	private SurveyInfoService info = new SurveyInfoService();
	
	
    public boolean IsSurveyValid() {
		return this.currentSurvey != null;
	}

    public boolean IsSurveyFound() 
    {
		if (this.Holddata == null) {
			setMessage("No Survey found!", false);
			return false;
		} else if (this.Holddata.Count() < 1) {
			setMessage("No Survey found!", false);
			return false;
		} else
			return true;
	}

       private void setSurveyData() {
		   if (!IsSurveyFound())
		    	return;
		 ArrayList<String> SurveyIds = Holddata.Select("QuizID").Distinct()
				.ToString();
		     SurveyQuestions = new ArrayList<Survey>();
		         for (String qid : SurveyIds) {
			SurveyQuestions.add(Holddata.where("QuizID", qid).First());
		}
	}

   private void setQuestionData() 
   {
	     if (!IsSurveyFound())
			return;
		   SurveyQuestions = new ArrayList<Survey>();
		    ArrayList<String> QIDs = Holddata.OrderBy("QID").Select("QID")
				.Distinct().ToString();
		        for (String qid : QIDs) {
			SurveyQuestions.add(Holddata.where("QID", qid).First());
		}
	}

    public void loadSurveyFromLocal(final IEvent event) {
	    new SurveyService().LoadSurveyFromLocal(new IEvent() {
			@SuppressWarnings("unchecked")
			@Override
			public void Completed(Object Sender, Object Args) {
				// TODO Auto-generated method stub
				SurveyService service = (SurveyService) Sender;
				setMessage(service.Message, service.IsSuccess);
				Holddata = (ArrayList<Survey>) Args;
				setSurveyData();
				event.Completed(this, null);
			}
		});
	}

	
    public void loadQuestionFromLocal(final IEvent event, final Survey survey) {
		if (survey != null) {
			new SurveyService().LoadQuestionFromLocal(new IEvent() {

				@SuppressWarnings("unchecked")
				@Override
				public void Completed(Object Sender, Object Args) {
					// TODO Auto-generated method stub
					SurveyService service = (SurveyService) Sender;
					setMessage(service.Message, service.IsSuccess);
					if (service.IsSuccess)
						Holddata = (ArrayList<Survey>) Args;
					    setQuestionData();
					    event.Completed(this, null);
				}
			}, survey);
		} else {
			setMessage("No question found", false);
			event.Completed(this, null);
		}
	}

    public void loadSurveyFromOnline(final IEvent event) {
    	new SurveyService().LoadSurveyFromOnline(new IEvent() 
    	{
			@SuppressWarnings("unchecked")
			@Override
			public void Completed(Object Sender, Object Args) {
				SurveyService service = (SurveyService) Sender;
				setMessage(service.Message,service.IsSuccess);
				if (service.IsSuccess)
					Holddata = (ArrayList<Survey>) Args;
				setSurveyData();
				event.Completed(this, null);
			}
		});
	}

    private boolean isAnyQuetionAnswered() {
		return this.Holddata.Any("IsAnswered", "true");
	}

	public void submitSurveyOnOnline(final IEvent event) {
		if (isAnyQuetionAnswered()) {
			new SurveyService().submitSurveyOnServer(new IEvent() {

				@Override
				public void Completed(Object Sender, Object Args) {
					// TODO Auto-generated method stub
					SurveyService service = (SurveyService) Sender;
					setMessage(service.Message, service.IsSuccess);
					event.Completed(this, null);
				}
			}, this.currentSurvey);
		} else {
			setMessage("Please attempt the Survey to submit!");
			event.Completed(this, null);
		}
	}

	   public void saveAnswer(String answerids) {
		// TODO Auto-generated method stub
		String[] AIDs = answerids.split(",");
		if(CurrentSurveyAnswer==null)
		{
			return;
		}
		for (Survey survey : CurrentSurveyAnswer) {
			survey.IsAnswered = "false";
			survey.InsertOrUpdate();
		}
		for (Survey survey : CurrentSurveyAnswer.where("AID", AIDs)) {
			survey.IsAnswered = "true";
			survey.UserID = info.getAMID();
			survey.InsertOrUpdate();
		}

	}

    public void resetAnswer() {
		// TODO Auto-generated method stub
		for (Survey survey : CurrentSurveyAnswer) {
			survey.IsAnswered = "false";
			survey.InsertOrUpdate();
		}
	}

	public boolean IsAnswered() {
		if (!IsSurveyValid())
			return false;
		else
			return Holddata.where("QID", currentSurvey.QID).Any("IsAnswered",
					"true");
	}

	public boolean IsAnswered(String QID) {

		return Holddata.where("QID", QID).Any("IsAnswered", "true");
	}

	public boolean isSurveyCompleted() {
		// TODO Auto-generated method stub
		return this.isNextDisabled();
	}

	public boolean isSurveyAttemptedBefore() {
		if (!this.IsSurveyFound())
			return false;
		return this.Holddata.Any("IsQuizCompleted", "true");
	}

	public boolean isWarningAvialable() {
		// TODO Auto-generated method stub
		if (!IsSurveyFound())
			return false;
		else {
			return this.getPendingQuestion() > 0;
		}

	}

	public String getWarningText() {
		return getPendingQuestion()
				+ " questions are pending \n Do you want to submit this quiz?";
	}

	public boolean isQuestionOverViewAvialable() {
		return false;
	}

	public int getTotalSurveyQuestion() {
		if (SurveyQuestions == null)
			return 0;
		return this.SurveyQuestions.Count();
	}

	public int getAnsweredSurvey() {
		if (!IsSurveyFound())
			return 0;
		int sanswer = this.Holddata.where("IsMultipleChoice", "true")
				.where("IsAnswered", "true").Select("AID").Distinct().Count();
		int manswer = 0;
		return sanswer + manswer;
	}

	public int getPendingQuestion() {
		return this.getTotalSurveyQuestion() - this.getAnsweredSurvey();
	}

	public int getPendingQuestion(String Surveyid) {
		int sanswer = this.Holddata.where("IsMultipleChoice", "false")
				.where("IsAnswered", "true").where("QuizID", Surveyid)
				.Select("AID").Distinct().Count();
		int manswer = 0;
		int TotalAnswered = sanswer + manswer;
		int TotalQuestion = this.Holddata.where("QuizID", Surveyid).Select("QID")
				.Distinct().Count();
		return TotalQuestion - TotalAnswered;

	}

    public int getSerialNo() {
		if (this.Index == 0)
			return 1;
		return this.SerialNo;
	}

	private void setSerialNo() {
		SerialNo = this.Index + 1;
	}

	public ArrayList<Survey> getAnswerOfCurrentQuestion() {
		CurrentSurveyAnswer = Holddata.where("QID", currentSurvey.QID).OrderBy(
				"AID");
		return this.CurrentSurveyAnswer;
	}

	public void setCurrentSurvey() {
		if (!this.isSurveyCompleted())
			currentSurvey = SurveyQuestions.get(Index);

	}

	public void setIndex(boolean isnext) {
		if (Index < 0)
			Index = 0;
		if (isnext)
			Index += 1;
		else
			Index -= 1;
		setSerialNo();
	}

	public void setIndex(int index) {
		this.Index = index;
		setSerialNo();
	}

	public void setIndexToFinal() {
		this.Index = this.SurveyQuestions.Count();
		setSerialNo();
	}

	public boolean isNextDisabled() {
		if (IsSurveyFound()) {
			return Index >= this.SurveyQuestions.Count();
		} else
			return true;
	}

	   public boolean isBackDisabled() {
		if (IsSurveyFound()) {
			return Index <= 0;
		} else
			return true;
	}

	public boolean isMultipleChoice() {
		// TODO Auto-generated method stub
		if (this.IsSurveyValid())
			return this.currentSurvey.IsMultipleChoice.equalsIgnoreCase("true");
		else
			return false;
	}

	// -------------Quiz Display Methods-----------------------//

	public int getPendingQuestion(Survey survey) {
		if (this.IsSurveyFound()) {
			return this.Holddata.where("QuizID", survey.QuizID)
					.where("IsAnswered", "false").Select("QID").Distinct()
					.Count();
		} else
			return 0;
	}

	public boolean isSurveyInCriticalZone(Survey survey) {
		return false;
	}

	public void setSurveyToFinished() {
		// TODO Auto-generated method stub
		if (this.IsSurveyValid()) {
			currentSurvey.IsQuizCompleted = "true";
			currentSurvey.InsertOrUpdate();
		}
	}

	public boolean isSurveyUpdateOnline(String surveyID) {
		// TODO Auto-generated method stub
		return this.Holddata.where("QuizID", surveyID).Any("IsUpdated", "true");
	}

	public String getSurveyScore(String surveyID) {
		return Holddata.where("QuizID", surveyID).OrderBy("QID").First().Score;
	}

	public String getSurveySubmitDate(String surveyID) {
		// TODO Auto-generated method stub
		return Holddata.where("QuizID", surveyID).OrderBy("QID").First().AnsweredOn;
	}

    public boolean isPassed(String surveyID) {
		return Holddata.where("QuizID", surveyID).OrderBy("QID").First().IsPassed
				.equalsIgnoreCase("true");
	}

    public String getSurveyResultMessage() {
		boolean ispassed = this.info.getSurveyForUser()
				.where("QuizID", this.currentSurvey.QuizID).OrderBy("QID")
				.First().IsPassed.equalsIgnoreCase("true");

		if (ispassed)
			return "Congratulations, you passed !";
		else
			return "Sorry, you failed !";
	}

}
