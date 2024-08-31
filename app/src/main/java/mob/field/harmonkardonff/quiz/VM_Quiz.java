package mob.field.harmonkardonff.quiz;

import linq.ArrayList;
import app.core.events.IEvent;
import app.core.viewmodels.ViewModel;

public class VM_Quiz extends ViewModel {

	public ArrayList<Quiz> QuizQuestions = null;
	public ArrayList<Quiz> Holddata = null;
	public ArrayList<Quiz> CurrentQuizAnswer = null;

	public Quiz currentQuiz = null;
	private int Index = 0;
	private int SerialNo = 0;
	private InfoService info = new InfoService();
	
	
    public boolean IsQuizValid() {
		return this.currentQuiz != null;
	}

    public boolean IsQuizFound() 
    {
		if (this.Holddata == null) {
			setMessage("No Quiz found!", false);
			return false;
		} else if (this.Holddata.Count() < 1) {
			setMessage("No Quiz found!", false);
			return false;
		} else
			return true;
	}

       private void setQuizData() {
		   if (!IsQuizFound())
		    	return;
		 ArrayList<String> QuizIds = Holddata.Select("QuizID").Distinct()
				.ToString();
		     QuizQuestions = new ArrayList<Quiz>();
		         for (String qid : QuizIds) {
			QuizQuestions.add(Holddata.where("QuizID", qid).First());
		}
	}

   private void setQuestionData() 
   {
	     if (!IsQuizFound())
			return;
		   QuizQuestions = new ArrayList<Quiz>();
		    ArrayList<String> QIDs = Holddata.OrderBy("QID").Select("QID")
				.Distinct().ToString();
		        for (String qid : QIDs) {
			QuizQuestions.add(Holddata.where("QID", qid).First());
		}
	}

    public void loadQuizFromLocal(final IEvent event) {
	    new QuizService().LoadQuizFromLocal(new IEvent() {
			@SuppressWarnings("unchecked")
			@Override
			public void Completed(Object Sender, Object Args) {
				// TODO Auto-generated method stub
				QuizService service = (QuizService) Sender;
				setMessage(service.Message, service.IsSuccess);
				Holddata = (ArrayList<Quiz>) Args;
				setQuizData();
				event.Completed(this, null);
			}
		});
	}

	
    public void loadQuestionFromLocal(final IEvent event, final Quiz quiz) {
		if (quiz != null) {
			new QuizService().LoadQuestionFromLocal(new IEvent() {

				@SuppressWarnings("unchecked")
				@Override
				public void Completed(Object Sender, Object Args) {
					// TODO Auto-generated method stub
					QuizService service = (QuizService) Sender;
					setMessage(service.Message, service.IsSuccess);
					if (service.IsSuccess)
						Holddata = (ArrayList<Quiz>) Args;
					    setQuestionData();
					    event.Completed(this, null);
				}
			}, quiz);
		} else {
			setMessage("No question found", false);
			event.Completed(this, null);
		}
	}

    public void loadQuizFromOnline(final IEvent event) {
    	new QuizService().LoadQuizFromOnline(new IEvent() 
    	{
			@SuppressWarnings("unchecked")
			@Override
			public void Completed(Object Sender, Object Args) {
				QuizService service = (QuizService) Sender;
				setMessage(service.Message,service.IsSuccess);
				if (service.IsSuccess)
					Holddata = (ArrayList<Quiz>) Args;
				setQuizData();
				event.Completed(this, null);
			}
		});
	}

    private boolean isAnyQuetionAnswered() {
		return this.Holddata.Any("IsAnswered", "true");
	}

	public void submitQuizOnOnline(final IEvent event) {
		if (isAnyQuetionAnswered()) {
			new QuizService().submitQuizOnServer(new IEvent() {

				@Override
				public void Completed(Object Sender, Object Args) {
					// TODO Auto-generated method stub
					QuizService service = (QuizService) Sender;
					setMessage(service.Message, service.IsSuccess);
					event.Completed(this, null);
				}
			}, this.currentQuiz);
		} else {
			setMessage("Please attempt the quiz to submit!");
			event.Completed(this, null);
		}
	}

	   public void saveAnswer(String answerids) {
		// TODO Auto-generated method stub
		String[] AIDs = answerids.split(",");
		if(CurrentQuizAnswer==null)
		{
			return;
		}
		for (Quiz quiz : CurrentQuizAnswer) {
			quiz.IsAnswered = "false";
			quiz.InsertOrUpdate();
		}
		for (Quiz quiz : CurrentQuizAnswer.where("AID", AIDs)) {
			quiz.IsAnswered = "true";
			quiz.UserID = info.getAMID();
			quiz.InsertOrUpdate();
		}

	}

    public void resetAnswer() {
		// TODO Auto-generated method stub
		for (Quiz quiz : CurrentQuizAnswer) {
			quiz.IsAnswered = "false";
			quiz.InsertOrUpdate();
		}
	}

	public boolean IsAnswered() {
		if (!IsQuizValid())
			return false;
		else
			return Holddata.where("QID", currentQuiz.QID).Any("IsAnswered",
					"true");
	}

	public boolean IsAnswered(String QID) {

		return Holddata.where("QID", QID).Any("IsAnswered", "true");
	}

	public boolean isQuizCompleted() {
		// TODO Auto-generated method stub
		return this.isNextDisabled();
	}

	public boolean isQuizAttemptedBefore() {
		if (!this.IsQuizFound())
			return false;
		return this.Holddata.Any("IsQuizCompleted", "true");
	}

	public boolean isWarningAvialable() {
		// TODO Auto-generated method stub
		if (!IsQuizFound())
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

	public int getTotalQuizQuestion() {
		if (QuizQuestions == null)
			return 0;
		return this.QuizQuestions.Count();
	}

	public int getAnsweredQuiz() {
		if (!IsQuizFound())
			return 0;
		int sanswer = this.Holddata.where("IsMultipleChoice", "true")
				.where("IsAnswered", "true").Select("AID").Distinct().Count();
		int manswer = 0;
		return sanswer + manswer;
	}

	public int getPendingQuestion() {
		return this.getTotalQuizQuestion() - this.getAnsweredQuiz();
	}

	public int getPendingQuestion(String Quizid) {
		int sanswer = this.Holddata.where("IsMultipleChoice", "false")
				.where("IsAnswered", "true").where("QuizID", Quizid)
				.Select("AID").Distinct().Count();
		int manswer = 0;
		int TotalAnswered = sanswer + manswer;
		int TotalQuestion = this.Holddata.where("QuizID", Quizid).Select("QID")
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

	public ArrayList<Quiz> getAnswerOfCurrentQuestion() {
		CurrentQuizAnswer = Holddata.where("QID", currentQuiz.QID).OrderBy(
				"AID");
		return this.CurrentQuizAnswer;
	}

	public void setCurrentQuiz() {
		if (!this.isQuizCompleted())
			currentQuiz = QuizQuestions.get(Index);

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
		this.Index = this.QuizQuestions.Count();
		setSerialNo();
	}

	public boolean isNextDisabled() {
		if (IsQuizFound()) {
			return Index >= this.QuizQuestions.Count();
		} else
			return true;
	}

	   public boolean isBackDisabled() {
		if (IsQuizFound()) {
			return Index <= 0;
		} else
			return true;
	}

	public boolean isMultipleChoice() {
		// TODO Auto-generated method stub
		if (this.IsQuizValid())
			return this.currentQuiz.IsMultipleChoice.equalsIgnoreCase("true");
		else
			return false;
	}

	// -------------Quiz Display Methods-----------------------//

	public int getPendingQuestion(Quiz quiz) {
		if (this.IsQuizFound()) {
			return this.Holddata.where("QuizID", quiz.QuizID)
					.where("IsAnswered", "false").Select("QID").Distinct()
					.Count();
		} else
			return 0;
	}

	public boolean isQuizInCriticalZone(Quiz quiz) {
		return false;
	}

	public void setQuizToFinished() {
		// TODO Auto-generated method stub
		if (this.IsQuizValid()) {
			currentQuiz.IsQuizCompleted = "true";
			currentQuiz.InsertOrUpdate();
		}
	}

	public boolean isQuizUpdateOnline(String quizID) {
		// TODO Auto-generated method stub
		return this.Holddata.where("QuizID", quizID).Any("IsUpdated", "true");
	}

	public String getQuizScore(String quizID) {
		return Holddata.where("QuizID", quizID).OrderBy("QID").First().Score;
	}

	public String getQuizSubmitDate(String quizID) {
		// TODO Auto-generated method stub
		return Holddata.where("QuizID", quizID).OrderBy("QID").First().AnsweredOn;
	}

    public boolean isPassed(String quizID) {
		return Holddata.where("QuizID", quizID).OrderBy("QID").First().IsPassed
				.equalsIgnoreCase("true");
	}

    public String getQuizResultMessage() {
		boolean ispassed = this.info.getQuizForUser()
				.where("QuizID", this.currentQuiz.QuizID).OrderBy("QID")
				.First().IsPassed.equalsIgnoreCase("true");

		if (ispassed)
			return "Congratulations, you passed !";
		else
			return "Sorry, you failed !";
	}

}
