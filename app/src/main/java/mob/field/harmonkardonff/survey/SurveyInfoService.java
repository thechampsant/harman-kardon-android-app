package mob.field.harmonkardonff.survey;

import linq.ArrayList;
import mob.field.harmonkardonff.services.WHPL_MainService;

public class SurveyInfoService {

	public Object getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAMID() {
		// TODO Auto-generated method stub
		return WHPL_MainService.sdb.getAMID();
	}

	public ArrayList<Survey> getSurveyForUser() {
		// TODO Auto-generated method stub
		return WHPL_MainService.sdb.MySurvey;
	}

}
