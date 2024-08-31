package mob.field.harmonkardonff.quiz;

import linq.ArrayList;
import mob.field.harmonkardonff.services.WHPL_MainService;

public class InfoService {

	public Object getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAMID() {
		// TODO Auto-generated method stub
		return WHPL_MainService.qdb.getAMID();
	}

	public ArrayList<Quiz> getQuizForUser() {
		// TODO Auto-generated method stub
		return WHPL_MainService.qdb.MyQuiz;
	}

}
