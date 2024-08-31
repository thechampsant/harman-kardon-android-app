package mob.field.harmonkardonff.survey;

import linq.ArrayList;
import mob.field.harmonkardonff.services.WHPL_MainService;
import android.os.AsyncTask;
import app.core.events.IEvent;
import app.core.model.Response;

public class SurveyService {

	WebServiceSurvey wc = new WebServiceSurvey();
	public boolean IsSuccess = false;
	public String Message = "";
	SurveyInfoService info = new SurveyInfoService();

	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public void LoadSurveyFromLocal(final IEvent event) {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				ArrayList<Survey> data = WHPL_MainService.sdb.LoadMySurveys();
				if (data == null) {
					Message = "No data found";
					return null;
				} else if (data.Count() < 1) {
					Message = "No data found";
					return null;
				} else {
					IsSuccess = true;
					return data;
				}
			}

			@Override
			protected void onPostExecute(Object result) {
				if (!IsSuccess)
					LoadSurveyFromOnline(event);
				else
					event.Completed(SurveyService.this, result);

				super.onPostExecute(result);
			}
		}.execute("");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void LoadQuestionFromLocal(final IEvent event, final Survey survey) {
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				ArrayList<Survey> data = WHPL_MainService.sdb.LoadMySurveys().where(
						"QuizID", survey.QuizID);
				if (data == null) {
					Message = "No data found";
					return null;
				} else if (data.Count() < 1) {
					Message = "No data found";
					return null;
				} else {
					IsSuccess = true;
					return data;
				}
			}

			@Override
			protected void onPostExecute(Object result) {
				event.Completed(SurveyService.this, result);
				super.onPostExecute(result);
			}
		}.execute("");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void LoadSurveyFromOnline(final IEvent event) {
		if (!WHPL_MainService.service.isNetworkAvailable()) {
			Message = "No internet connection found!";
			event.Completed(SurveyService.this, null);
			return;
		}
		final String AMID = info.getAMID();
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				Response response = wc.TryGetAMSOSurvey(AMID);
				if (response.status.equalsIgnoreCase("true")) {
					if (response.data != null) {
						WHPL_MainService.sdb.InsertAllMySurvey(response.data);
						IsSuccess = true;
					}
				} else
					Message = response.errormsg;
				return response;
			}

			@Override
			protected void onPostExecute(Object result) {

				removeLocalTimerDetails();
				Response response = (Response) result;
				
				event.Completed(SurveyService.this, response.data);
				
				super.onPostExecute(result);
			}
		}.execute("");
	}

	protected void removeLocalTimerDetails() {
		
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void submitSurveyOnServer(final IEvent event, final Survey survey) {
		if (!WHPL_MainService.service.isNetworkAvailable()) {
			Message = "No internet connection found!";
			event.Completed(SurveyService.this, null);
			return;
		}
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				ArrayList<Survey> surveys = WHPL_MainService.sdb.MySurvey.where("QuizID",
						survey.QuizID).where("IsAnswered", "true");
				Response response = wc.TrySubmitAMSOSurvey(surveys);
				if (response.status.equalsIgnoreCase("true")) {
					WHPL_MainService.sdb.InsertAllMySurvey(response.data, survey);
					IsSuccess = true;
				} else
					Message = response.errormsg;
				return response;
			}

			@Override
			protected void onPostExecute(Object result) {
				event.Completed(SurveyService.this, null);
				super.onPostExecute(result);
			}

		}.execute("");
	}

}
