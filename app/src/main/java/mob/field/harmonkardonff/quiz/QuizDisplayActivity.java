package mob.field.harmonkardonff.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import app.core.adapter.GenricAdapter;
import app.core.adapter.IAdapter;
import app.core.base.InnosolsActivity;
import app.core.events.IEvent;
import app.core.utils.Dialog;
import app.core.utils.IntentFactory;

import com.fieldforce.harmonkardonff.R;

//import com.smartinfield.amso.GcmBroadcastReceiver.AppTracker;
//import app.entitymodels.UserActivity;
public class QuizDisplayActivity extends InnosolsActivity {

	VM_Quiz model = new VM_Quiz();
	boolean isactivityCreated = true;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			isactivityCreated = false;

			// setTracker();
			/*
			 * new UserActivity() .setActivity(User.GetUserName(), "My Quiz",
			 * "Training", "Open", "My Quiz")
			 * .setAppVersion(getCurrentVersion())
			 * .setAndroidVersion(ToString(getAndroidVersion())).build();
			 */

			setContentView(R.layout.activity_display_quiz);
			loadQuiz();
			AttachRefreshQuiz();
			refreshQuiz();
		} catch (Exception ex) {
			ShowToastLong(ex.toString() + "exception", 0);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				loadQuiz();
			}
		}
	}

	private void loadQuiz() {
		// this.showProgress();
		model.loadQuizFromLocal(new IEvent() {
			@Override
			public void Completed(Object Sender, Object Args) {
				// TODO Auto-generated method stub
				hideProgress();
				setList();
				if (!model.IsSuccess)
					ShowToastLong(model.Message, 0);
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setList() {
		if (!model.IsQuizFound()) {
			this.setVisibility(R.id.txt_msg, View.VISIBLE);
			return;
		} else
			this.setVisibility(R.id.txt_msg, View.GONE);

		
		ListView lv = this.getListView(R.id.lv_quizs);
		if (lv == null)
			return;
		final GenricAdapter adaptor = new GenricAdapter(this,
				R.layout.listitem_quiz);

		adaptor.setGenricAdapter(new IAdapter() {
			@Override
			public void setItemView(Object obj, View convertView, int index) {
				// TODO Auto-generated method stub
				Quiz info = (Quiz) obj;
				setScore(info, convertView);
			}
		});

		adaptor.setData(model.QuizQuestions);

		lv.setAdapter(adaptor);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				goToQuizRendring(model.QuizQuestions.get(arg2));
			}
		});
	}

	private void setQuizTitle(Quiz info, View convertView) {
		if (info == null)
			return;
		TextView title = (TextView) convertView.findViewById(R.id.txt_title);
		title.setText(info.QuizTitle);
	}

	int daysLeft = 0;

	private int getdaysMargin(Quiz info) {
		try {
			daysLeft = (int) this.GetDaysBetween(this.getCurrentDate(),
					this.ConvertStringToDate(info.EffTill));
			return daysLeft;
		} catch (Exception ex) {
			ex.printStackTrace();
			daysLeft = -1;
			return 1;
		}
	}

	private boolean isQuizInCriticalZone(Quiz info) {
		getdaysMargin(info);
		if (daysLeft > 0 && daysLeft < 5)
			return true;
		else
			return false;
	}

	private void setScore(Quiz info, View convertView) {
		setQuizTitle(info, convertView);

		TextView publishedOn = (TextView) convertView
				.findViewById(R.id.txt_publishedon);
		publishedOn.setText(info.PublishedOn);

		if (model.isQuizUpdateOnline(info.QuizID)) {

			convertView.findViewById(R.id.lyt_score)
					.setVisibility(View.VISIBLE);
			TextView score = (TextView) convertView
					.findViewById(R.id.txt_score);
			score.setText(model.getQuizScore(info.QuizID));

			convertView.findViewById(R.id.lyt_quiz_two).setVisibility(
					View.VISIBLE);
			TextView submitedon = (TextView) convertView
					.findViewById(R.id.txt_submittedon);
			submitedon.setText(model.getQuizSubmitDate(info.QuizID));

			TextView status = (TextView) convertView
					.findViewById(R.id.txt_score_status);
			if (!model.isPassed(info.QuizID)) {
				status.setText("Result");
			//	status.setText("Fail");
			//	status.setTextColor(getResources().getColor(R.color.red));
			//	ImageView img = (ImageView) convertView
			//			.findViewById(R.id.img_score_status);
			//	img.setImageResource(R.drawable.fail);
			}
		}

		else {

			convertView.findViewById(R.id.lyt_quiz_two)
					.setVisibility(View.GONE);
			convertView.findViewById(R.id.lyt_pending).setVisibility(
					View.VISIBLE);
			convertView.findViewById(R.id.lyt_closed).setVisibility(View.GONE);

			TextView pending = (TextView) convertView
					.findViewById(R.id.txt_pending);

			if (isQuizInCriticalZone(info))
				pending.setText(daysLeft + " days pending..");
			else if (daysLeft > 4)
				pending.setText(model.getPendingQuestion(info.QuizID)
						+ " Pending..");
			else {
				convertView.findViewById(R.id.lyt_pending).setVisibility(
						View.GONE);
				convertView.findViewById(R.id.lyt_quiz_two).setVisibility(
						View.GONE);
				convertView.findViewById(R.id.lyt_closed).setVisibility(
						View.VISIBLE);
			}
		}

	}

	private void goToQuizRendring(Quiz quiz) {
		IntentFactory.putData(Keys.QUIZ, quiz);
		if (!model.isQuizUpdateOnline(quiz.QuizID)) {
			Intent intent = new Intent(this, QuizActivity.class);
			startActivityForResult(intent, 1);
		} else {
			// Remove
			// Intent intent = new Intent(this, QuizActivity.class);
			// startActivityForResult(intent, 1);
			ShowToastLong("Quiz already submitted", 0);
		}
	}

	private void AttachRefreshQuiz() {
		this.getImageView(R.id.img_action_refresh).setClickable(true);
		this.getImageView(R.id.img_action_refresh).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						refreshQuiz();
					}
				});
	}

	private void refreshQuiz() {
		showProgress(false);
		model.loadQuizFromOnline(new IEvent() {

			@Override
			public void Completed(Object Sender, Object Args) {
				hideProgress();
				if (model.IsSuccess) {
					setList();
//					loadQuiz();
					ShowToast("Updated successfully!");
				} else
					new Dialog(QuizDisplayActivity.this).show(model.Message);
			}
		});
	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
