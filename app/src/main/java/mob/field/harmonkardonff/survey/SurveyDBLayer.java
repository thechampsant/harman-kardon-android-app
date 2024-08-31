package mob.field.harmonkardonff.survey;

import linq.ArrayList;
import mob.field.harmonkardonff.services.WHPL_MainService;
import app.core.base.IDB;

import com.fieldforce.harmonkardonff.MainActivity;

     public class SurveyDBLayer extends IDB {

	public ArrayList<Survey> MySurvey = new ArrayList<Survey>();

	public SurveyDBLayer() {
		super(WHPL_MainService.Current);
		
	}
	
	
	      public ArrayList<Survey> LoadMySurveys() {
		  // TODO Auto-generated method stub
		    if (this.getAMID() == null || this.getAMID() == "")
			return null;
		    if(Survey.db != null)
			this.MySurvey = Survey.db.FetchAllData(new Survey());//.where("UserID", this.getAMID());
		    return this.MySurvey;
	}

	    @SuppressWarnings("unchecked")
	    
	    
		public void InsertAllMySurvey(ArrayList data) {
		if(Survey.db != null)
		
			
		{
			Survey.db.DeleteAll(new Survey());
			Survey.db.InsertAll(data);
		}
		    this.MySurvey = null;
		    this.MySurvey = data;
	}

	     public void InsertAllMySurvey(ArrayList<Survey> data, Survey survey) {
		// TODO Auto-generated method stub
		   
		Survey temp = MySurvey.where("QuizID", survey.QuizID).OrderBy("QID").First();
		Survey surveyScore = data.where("QuizID", survey.QuizID).First();
		 temp.Score = surveyScore.Score;
	    	temp.IsPassed = surveyScore.IsPassed;
	    	temp.AnsweredOn = data.notEqual("AnsweredOn", "NA").First().AnsweredOn;
		    temp.IsUpdated = "true";
		    temp.InsertOrUpdate();
		     LoadMySurveys();
		
	}

	     
     public String getAMID() {
    	 return MainActivity.MyInfo.UserID;
	}

}
