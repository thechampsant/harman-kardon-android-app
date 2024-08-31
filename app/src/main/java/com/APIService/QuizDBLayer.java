package com.APIService;

import com.fieldforce.asyntask.WebService;

import linq.ArrayList;
import app.core.base.IDB;
import app.core.sqllite.DataEntity;
import mob.field.harmonkardonff.quiz.Quiz;

public class QuizDBLayer extends IDB {

	public ArrayList<Quiz> MyQuiz = new ArrayList<Quiz>();

	public QuizDBLayer() {
		super(MainService.Current);
		
	}
	
	
	      public ArrayList<Quiz> LoadMyQuizs() {
		  // TODO Auto-generated method stub
		    if (this.getAMID() == null || this.getAMID() == "")
			return null;
		    if(DataEntity.db != null)
			this.MyQuiz = DataEntity.db.FetchAllData(new Quiz());//.where("UserID", this.getAMID());
		    
		    
		    return this.MyQuiz;
	}

	    @SuppressWarnings("unchecked")
	    
	    
		public void InsertAllMyQuiz(ArrayList data) {
		if(DataEntity.db != null)
		
			
		{
			DataEntity.db.DeleteAll(new Quiz());
			DataEntity.db.InsertAll(data);
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
 //   	 return MainActivity.MyInfo.empcode;
    	 return WebService.getUsername();
	}

}
