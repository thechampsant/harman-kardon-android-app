package mob.field.harmonkardonff.quiz;

import linq.ArrayList;
import mob.field.harmonkardonff.services.WHPL_MainService;
import app.core.base.IDB;

import com.fieldforce.harmonkardonff.MainActivity;

     public class QuizDBLayer extends IDB {

	public ArrayList<Quiz> MyQuiz = new ArrayList<Quiz>();

	public QuizDBLayer() {
		super(WHPL_MainService.Current);
		
	}
	
	
	      public ArrayList<Quiz> LoadMyQuizs() {
		  // TODO Auto-generated method stub
		    if (this.getAMID() == null || this.getAMID() == "")
			return null;
		    if(Quiz.db != null)
			this.MyQuiz = Quiz.db.FetchAllData(new Quiz());//.where("UserID", this.getAMID());
		    
		    
		    return this.MyQuiz;
	}

	    @SuppressWarnings("unchecked")
	    
	    
		public void InsertAllMyQuiz(ArrayList data) {
		if(Quiz.db != null)
		
			
		{
			Quiz.db.DeleteAll(new Quiz());
			Quiz.db.InsertAll(data);
		}
		    this.MyQuiz = null;
		    this.MyQuiz = data;
	}

	     public void InsertAllMyQuiz(ArrayList<Quiz> data, Quiz quiz) {
		// TODO Auto-generated method stub
		   
		Quiz temp = MyQuiz.where("QuizID", quiz.QuizID).OrderBy("QID").First();
		Quiz QuizScore = data.where("QuizID", quiz.QuizID).First();
		 temp.Score = QuizScore.Score;
	    	temp.IsPassed = QuizScore.IsPassed;
	    	temp.AnsweredOn = data.notEqual("AnsweredOn", "NA").First().AnsweredOn;
		    temp.IsUpdated = "true";
		    temp.InsertOrUpdate();
		     LoadMyQuizs();
		
	}

	     
     public String getAMID() {
    	 return MainActivity.MyInfo.UserID;
	}

}
