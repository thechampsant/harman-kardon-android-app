package mob.field.harmonkardonff.survey;

import java.util.Timer;

import linq.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.base.InnosolsActivity;
import app.core.events.IEvent;
import app.core.utils.Dialog;
import app.core.utils.IntentFactory;

import com.fieldforce.harmonkardonff.R;

//import com.smartinfield.amso.GcmBroadcastReceiver.AppTracker;

public class SurveyActivity extends InnosolsActivity {

	private VM_Survey model = new VM_Survey();
	private RadioGroup rg = null;
	private LinearLayout rgm = null;
	Timer timer = new Timer();

	private void setTracker() {
		/*
		 * Tracker t = this.getBase().getTracker(AppTracker.TRACKER);
		 * t.setScreenName("My Quiz"); t.send(new
		 * HitBuilders.AppViewBuilder().build()); t.send(new
		 * HitBuilders.EventBuilder().setNewSession()
		 * .setCategory("Training").setAction("Open").setLabel("My Quiz")
		 * .setValue(1).build());
		 * 
		 * t.set("USER_ID", User.GetUserName()); t.setSampleRate(0.01); //
		 * t.enableAdvertisingIdCollection(true);
		 * 
		 * t.enableAutoActivityTracking(true);
		 */
	}

/*	private int minutes = 19;
	private int seconds = 0;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_quiz);
			setTracker();
			initilizeControls();
			attachClickable();
			LoadQuestionFromLocal((Survey) IntentFactory.getData(SurveyKeys.SURVEY));
			// startTimer();

		} catch (Exception ex) {
			ShowToastLong(ex.toString(), 0);
			finish();
		}
	}

/*	private void startTimer() {
		if (model.IsQuizValid()) {
			minutes = ToInt(model.currentQuiz.Time);
			ArrayList<SurveyIDTimeDetailModel> arrQuizIDModel = MainActivity.Current.db
					.FetchAllData(new SurveyIDTimeDetailModel()).where(
							"UserName", MainActivity.MyInfo.EmployeeCode);

			if (arrQuizIDModel != null && arrQuizIDModel.size() > 0) {
				for (SurveyIDTimeDetailModel quizIDModel : arrQuizIDModel) {
					if (quizIDModel.quizID
							.equalsIgnoreCase(model.currentQuiz.QuizID)) {
						minutes = ToInt(quizIDModel.QuizMinute);
						seconds = ToInt(quizIDModel.quizSecond);
						long lastTime = Long
								.valueOf(quizIDModel.currentMilli);
						long currTime = System.currentTimeMillis();
						long milliSecDiff = currTime - lastTime;
						int minuteDiff = (int)(milliSecDiff/(1000*60));
						int secondDiff = (int)((milliSecDiff%(1000*60))/1000);

						if (seconds < secondDiff) {
							seconds = seconds + 60;
							minutes = minutes - 1;
						}
						seconds = seconds - secondDiff;
						minutes = minutes - minuteDiff;
						if(minutes<0)
						{
							seconds = 2;
							minutes = 0;
						}
						break;
					}
				}
			}

		}
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						TextView tv = (TextView) findViewById(R.id.timer);
						if(tv==null)
							return;
						tv.setText(String.valueOf(minutes) + ":"
								+ String.valueOf(seconds));
						if (seconds == 0) {
							seconds = 60;
							minutes = minutes - 1;
						}
						seconds -= 1;
						ArrayList<SurveyIDTimeDetailModel> arrQuizIDModel = MainActivity.Current.db
								.FetchAllData(new SurveyIDTimeDetailModel())
								.where("UserName",
										MainActivity.MyInfo.EmployeeCode);

						if (arrQuizIDModel != null && arrQuizIDModel.size() > 0) {
							for (SurveyIDTimeDetailModel quizIDModel : arrQuizIDModel) {
								if (quizIDModel.quizID
										.equalsIgnoreCase(model.currentQuiz.QuizID))
									quizIDModel.Delete();
							}
						}
						SurveyIDTimeDetailModel quizIDTime = new SurveyIDTimeDetailModel();
						quizIDTime.quizID = model.currentQuiz.QuizID;
						quizIDTime.QuizMinute = minutes + "";
						quizIDTime.quizSecond = seconds + "";
						quizIDTime.currentMilli = System.currentTimeMillis()
								+ "";
						quizIDTime.InsertOrUpdate();
						if (minutes == -1) {
							timer.cancel();
							SurveyActivity.this.goToSubmitScreen();
							submitQuiz();
						}
						// Log.e("key","value");
					}

				});
			}

		}, 0, 1000);

	}*/

	private void LoadQuestionFromLocal(final Survey survey) {
		model.loadQuestionFromLocal(new IEvent() {

			@Override
			public void Completed(Object Sender, Object Args) {
				if (model.IsSuccess) {
					setFirstQuestion();
					setSurveyTitle();
//					startTimer();
				} else
					ShowToastLong(model.Message, 0);
			}
		}, survey);
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// private void setList() {
	// GenricAdapter adapter = new GenricAdapter(this,
	// R.layout.item_pager_quiz).setData(model.QuizQuestions);
	// adapter.setGenricAdapter(new IAdapter() {
	//
	// @Override
	// public void setItemView(Object obj, View view, int index) {
	// // TODO Auto-generated method stub
	// Quiz quiz=(Quiz) obj;
	// view.setTag(obj);
	// view.findViewById(R.id.lyt_question).setVisibility(View.VISIBLE);
	// TextView question=(TextView) view.findViewById(R.id.txt_question);
	// question.setText(quiz.Question);
	// }
	// }).setPageAdapter();
	//
	// pager.setAdapter(adapter.getPageAdapter());
	// pager.setOnPageChangeListener(new OnPageChangeListener() {
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// // TODO Auto-generated method stub
	// ShowToast("Question No" + (arg0 + 1));
	// Quiz quiz=model.QuizQuestions.get(arg0);
	// View view=pager.findViewWithTag(quiz);
	// if(quiz==null)return;
	// if(view==null)return;
	// TextView question=(TextView) view.findViewById(R.id.txt_question);
	// question.setText(model.QuizQuestions.get(arg0).Question);
	//
	// }
	// });
	// }

	private void initilizeControls() {
		rg = this.getRadioGroup(R.id.rg_answer);
		rgm = this.getLinearLayout(R.id.lyt_choice_multiple);
	}

	private void attachClickable() {
		getView(R.id.lyt_back).setClickable(true);
		getView(R.id.lyt_next).setClickable(true);
		getView(R.id.lyt_save_quiz).setClickable(true);
		getView(R.id.lyt_submit).setClickable(true);
		getView(R.id.lyt_reset_answer).setClickable(true);

		getView(R.id.lyt_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SurveyActivity.this.goToBackQuestion();
			}
		});

		getView(R.id.lyt_next).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SurveyActivity.this.goToNextQuestion();
			}
		});
		getView(R.id.lyt_save_quiz).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SurveyActivity.this.goToSubmitScreen();
			}
		});

		getView(R.id.lyt_submit).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SurveyActivity.this.confirmSubmitSurvey();
			}
		});
		getView(R.id.lyt_reset_answer).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SurveyActivity.this.resetAnswer();
					}
				});
	}

	private void resetAnswer() {
		// TODO Auto-generated method stub
		if (model.IsSurveyValid()) {
			model.resetAnswer();
			setSurveyStatusheader();
			if (rg != null)
				rg.clearCheck();
		}
	}

	private void setAnswerScreenVisibility() {
		if (model.isMultipleChoice()) {
			this.setVisibility(R.id.scrl_choice_multiple, View.VISIBLE);
			this.setVisibility(R.id.scrl_choice_single, View.GONE);
		} else {
			this.setVisibility(R.id.scrl_choice_multiple, View.GONE);
			this.setVisibility(R.id.scrl_choice_single, View.VISIBLE);
		}
	}

	private void setSurveyStatusheader() {

		this.SetTextViewAsString(R.id.txt_total_quiz,
				"Total: " + model.getTotalSurveyQuestion());
		this.SetTextViewAsString(R.id.txt_answered,
				"Answered: " + model.getAnsweredSurvey());
		this.SetTextViewAsString(R.id.txt_pending_quiz,
				"Pending: " + model.getPendingQuestion());
	}

	private void setSurveyQuestionSerial() {
		if (!model.isSurveyCompleted()) {
			this.getView(R.id.lyt_quiz_serial).setVisibility(View.VISIBLE);
			this.SetTextViewAsString(R.id.txt_question_serial, "Question No: "
					+ model.getSerialNo());
			this.SetTextViewAsString(R.id.txt_qid, "QID: "
					+ model.currentSurvey.QID);
		} else {
			this.getView(R.id.lyt_quiz_serial).setVisibility(View.GONE);
		}
	}

	private void setQuestionText() {
		// TODO Auto-generated method stub
		if (model.IsSurveyValid() && !model.isNextDisabled()) {
			this.SetTextViewAsString(R.id.txt_question,
					model.currentSurvey.Question);
		}

	}

	private void setQuestionStatusVisibility() {
		if (model.IsAnswered() || model.isSurveyCompleted())
			this.setVisibility(R.id.lyt_quiz_status, View.GONE);
		else
			this.setVisibility(R.id.lyt_quiz_status, View.VISIBLE);
	}

	private void setSubmitButtonVisibility() {
		if (model.isSurveyCompleted()) {
			this.setVisibility(R.id.lyt_submit, View.VISIBLE);
			this.setVisibility(R.id.lyt_next, View.GONE);
		} else {
			this.setVisibility(R.id.lyt_submit, View.GONE);
			this.setVisibility(R.id.lyt_next, View.VISIBLE);
		}
		if (model.isBackDisabled())
			this.setVisibility(R.id.lyt_back, View.GONE);
		else
			this.setVisibility(R.id.lyt_back, View.VISIBLE);
	}

	private void setWarningForSurvey() {
		setSurveyQuestionStatusList();
		if (model.isWarningAvialable()) {
			this.setVisibility(R.id.lyt_warning, View.VISIBLE);
			this.SetTextViewAsString(R.id.txt_warning, model.getWarningText());

		} else {
			this.setVisibility(R.id.lyt_warning, View.GONE);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setSurveyQuestionStatusList() {
		if (model.isSurveyCompleted()) {
			setVisibility(R.id.lyt_quiz_overview, View.VISIBLE);
			ListView lv = this.getListView(R.id.lv_quiz_status);
			if (lv == null)
				return;
			GenricAdapter adaptor = new GenricAdapter(this,
					R.layout.listitem_quiz_status).setData(model.SurveyQuestions);
			adaptor.setGenricAdapter(new IAdapter() {

				@Override
				public void setItemView(Object obj, View convertView, int index) {
					// TODO Auto-generated method stub
					Survey info = (Survey) obj;
					int QuestionNo = model.SurveyQuestions.indexOf(info) + 1;
					if (model.IsAnswered(info.QID)) {
						convertView.findViewById(R.id.lyt_quiz_status1)
								.setVisibility(View.VISIBLE);
						TextView serial = (TextView) convertView
								.findViewById(R.id.txt_quiz_serial1);
						serial.setText(ToString(QuestionNo) + ".");
					} else {
						convertView.findViewById(R.id.lyt_quiz_status2)
								.setVisibility(View.VISIBLE);
						TextView serial = (TextView) convertView
								.findViewById(R.id.txt_quiz_serial2);
						serial.setText(ToString(QuestionNo) + ".");
					}
				}
			});

			lv.setAdapter(adaptor);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

					goToPendingQuestion(arg2);
				}
			});
		}
	}

	void setSurveyFinalScreenVisibility() {
		if (model.isSurveyCompleted()) {
			this.setVisibility(R.id.lyt_question, View.GONE);
			this.setVisibility(R.id.lyt_sumarry, View.VISIBLE);
		} else {
			this.setVisibility(R.id.lyt_question, View.VISIBLE);
			this.setVisibility(R.id.lyt_sumarry, View.GONE);
		}
	}

	private RadioGroup createRadioButton(ArrayList<Survey> _data, boolean isdone) {

		rg.clearCheck();
		rg.removeAllViews();
		rg.setVisibility(View.VISIBLE);
		RadioButton rbutton = new RadioButton(this);
		rbutton.setVisibility(View.GONE);
		rg.addView(rbutton);
		for (int i = 0; i < _data.Count(); i++) {
			rbutton = new RadioButton(this);
			rbutton.setId(ToInt(_data.get(i).AID));
			rbutton.setText(_data.get(i).DefaultAnswer);
			rg.addView(rbutton); // the RadioButtons are added to the radioGroup
			// instead of the layout
			if (isdone) {
				// rbutton.setEnabled(false);
				String IsAnswered = _data.get(i).IsAnswered.trim();
				if (IsAnswered.equalsIgnoreCase("true"))
					rbutton.setChecked(true);

			}

		}
		return rg;
	}

	private void createCheckBoxes(ArrayList<Survey> _data, boolean isdone) {
		rgm.removeAllViews();
		rgm.setVisibility(View.VISIBLE);
		for (int i = 0; i < _data.Count(); i++) {
			final CheckBox rbutton = new CheckBox(this);
			rbutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					makeAllOtherCheckboxUnchecked();
					rbutton.setChecked(isChecked);

				}
			});
			rbutton.setId(ToInt(_data.get(i).AID));
			rbutton.setText(_data.get(i).DefaultAnswer);
			rgm.addView(rbutton); // the RadioButtons are added to the
									// radioGroup
			// instead of the layout
			if (isdone) {
				// rbutton.setEnabled(false);
				if (_data.get(i).IsAnswered.equalsIgnoreCase("true"))
					rbutton.setChecked(true);
			}

		}
	}

	protected void makeAllOtherCheckboxUnchecked() {
		for (int i = 0; i < rgm.getChildCount(); i++) {
			CheckBox cbx = (CheckBox) rgm.getChildAt(i);
			cbx.setChecked(false);
		}

	}

	private void showMultipleChoiceScreen(boolean isdone) {
		// final EditText remarks = this.GetEditText(R.id.edt_remarks);
		createCheckBoxes(model.getAnswerOfCurrentQuestion(), isdone);
	}

	private String getAnswerID() {
		boolean IsChecked = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rgm.getChildCount(); i++) {
			CheckBox cbx = (CheckBox) rgm.getChildAt(i);
			if (cbx.isChecked()) {
				sb.append(cbx.getId() + ",");
				IsChecked = true;
			}
		}
		/*
		 * if (!IsChecked) ShowToastLong("Please select at least one option!",
		 * 0);
		 */

		return sb.toString();

	}

	private void showSingleChoiceScreen(boolean isdone) {
		final EditText remarks = this.GetEditText(R.id.edt_remarks);
		rg = createRadioButton(model.getAnswerOfCurrentQuestion(), isdone);
	}

	private void saveAnswer() {
		if (model.isMultipleChoice())
			model.saveAnswer(getAnswerID());
		else {
			int answeredid = rg.getCheckedRadioButtonId();
			if (answeredid == -1)
				return;
			model.saveAnswer(ToString(answeredid));
		}
	}

	private void goToPendingQuestion(int index) {
		model.setIndex(index);
		model.setCurrentSurvey();
		setSurveyStatusheader();
		setSurveyQuestionSerial();
		onClick(true);
	}

	private void setSurveyTitle() {
		if (model.IsSurveyValid()) {
			this.SetTextViewAsString(R.id.txt_title,
					model.currentSurvey.QuizTitle);

		} else
			this.SetTextViewAsString(R.id.txt_title, "N.A.");
	}

	private void setFirstQuestion() {
		if (model.isSurveyAttemptedBefore()) {
			model.setCurrentSurvey();
			this.goToSubmitScreen();
			return;
		}
		model.setCurrentSurvey();
		setSurveyStatusheader();
		setSurveyQuestionSerial();
		onClick(true);
	}

	private void goToSubmitScreen() {
		saveAnswer();
		model.setSurveyToFinished();
		model.setIndexToFinal();
		setSurveyStatusheader();
		setSurveyQuestionSerial();
		onClick(true);
	}

	private void goToNextQuestion() {
		model.setIndex(true);
		saveAnswer();
		model.setCurrentSurvey();
		setSurveyStatusheader();
		setSurveyQuestionSerial();
		onClick(true);
	}

	private void goToBackQuestion() {
		model.setIndex(false);
		saveAnswer();
		model.setCurrentSurvey();
		setSurveyStatusheader();
		setSurveyQuestionSerial();
		onClick(true);
	}

	private void submitSurvey() {
		showProgress("saving Survey ...");

		model.submitSurveyOnOnline(new IEvent() {

			@Override
			public void Completed(Object Sender, Object Args) {
				// TODO Auto-generated method stub
				hideProgress();
				if (model.IsSuccess) {
					model.currentSurvey.AnsweredOn = getMonthYear(getCurrentDate());
					model.currentSurvey.InsertOrUpdate();
					ShowToast("Update successfully!");
					Intent data = new Intent();
					setResult(Activity.RESULT_OK, data);
					SurveyActivity.this.finish();
//					showQuizStatus();

				} else
					new Dialog(SurveyActivity.this).show(model.Message);
			}
		});

	}

	private void confirmSubmitSurvey() {
		new AlertDialog.Builder(this)
				.setTitle("Want to submit Survey")
				.setNegativeButton("Cancel", null)
				.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								submitSurvey();
							}
						}).show();
	}

	private void showQuizStatus() {
		new AlertDialog.Builder(this).setTitle("Survey Result!")
				.setMessage(model.getSurveyResultMessage()).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
						SurveyActivity.this.finish();
					}
				}).create().show();

	}

	@Override
	public void onBackPressed() {
		  Intent data = new Intent(); setResult(Activity.RESULT_OK, data);
		  super.onBackPressed();
	}

	private void onClick(boolean isdone) {

		setQuestionText();
		if (!model.isSurveyCompleted()) {
			if (model.isMultipleChoice())
				showMultipleChoiceScreen(isdone);
			else
				showSingleChoiceScreen(isdone);
		} else {
			if (model.IsSurveyValid()) {
				model.currentSurvey.IsQuizCompleted = "true";
				model.currentSurvey.InsertOrUpdate();
			}
			model.setIndexToFinal();
		}

		setQuestionStatusVisibility();
		setAnswerScreenVisibility();
		setSubmitButtonVisibility();
		setWarningForSurvey();
		setSurveyFinalScreenVisibility();

	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
