package mob.field.harmonkardonff.quiz;

import linq.ArrayList;
import mob.field.harmonkardonff.services.WHPL_MainService;
import android.os.AsyncTask;
import app.core.events.IEvent;
import app.core.model.Response;

public class QuizService {

	WebServiceQuiz wc = new WebServiceQuiz();
	public boolean IsSuccess = false;
	public String Message = "";
	InfoService info = new InfoService();

	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public void LoadQuizFromLocal(final IEvent event) {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				ArrayList<Quiz> data = WHPL_MainService.qdb.LoadMyQuizs();
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
					LoadQuizFromOnline(event);
				else
					event.Completed(QuizService.this, result);

				super.onPostExecute(result);
			}
		}.execute("");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void LoadQuestionFromLocal(final IEvent event, final Quiz quiz) {
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				ArrayList<Quiz> data = WHPL_MainService.qdb.LoadMyQuizs().where(
						"QuizID", quiz.QuizID);
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
				event.Completed(QuizService.this, result);
				super.onPostExecute(result);
			}
		}.execute("");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void LoadQuizFromOnline(final IEvent event) {
		if (!WHPL_MainService.service.isNetworkAvailable()) {
			Message = "No internet connection found!";
			event.Completed(QuizService.this, null);
			return;
		}
		final String AMID = info.getAMID();
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				Response response = wc.TryGetAMSOQuiz(AMID);
				if (response.status.equalsIgnoreCase("true")) {
					if (response.data != null) {
						WHPL_MainService.qdb.InsertAllMyQuiz(response.data);
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
				
				event.Completed(QuizService.this, response.data);
				
				super.onPostExecute(result);
			}
		}.execute("");
	}

	protected void removeLocalTimerDetails() {
		
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void submitQuizOnServer(final IEvent event, final Quiz quiz) {
		if (!WHPL_MainService.service.isNetworkAvailable()) {
			Message = "No internet connection found!";
			event.Completed(QuizService.this, null);
			return;
		}
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				ArrayList<Quiz> quizs = WHPL_MainService.qdb.MyQuiz.where("QuizID",
						quiz.QuizID).where("IsAnswered", "true");
				Response response = wc.TrySubmitAMSOQuiz(quizs);
				if (response.status.equalsIgnoreCase("true")) {
					WHPL_MainService.qdb.InsertAllMyQuiz(response.data, quiz);
					IsSuccess = true;
				} else
					Message = response.errormsg;
				return response;
			}

			@Override
			protected void onPostExecute(Object result) {
				event.Completed(QuizService.this, null);
				super.onPostExecute(result);
			}

		}.execute("");
	}

}
