package ch.unibe.lema.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Lecture;
import ch.unibe.lema.provider.Lecture.Event;

/**
 * Display details about a Lecture
 * 
 * @author cede
 * 
 */

public class LectureActivity extends BindingActivity {

	private static final String TAG_NAME = "Lecture";
	private Lecture mLecture;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG_NAME, "onCreate");
		setContentView(R.layout.lecture);

		Intent i = getIntent();
		mLecture = i.getParcelableExtra("lecture");
		if (mLecture == null) {
			setResult(RESULT_CANCELED);
			finish();
			return;
		}

		Log.i(TAG_NAME, "show " + mLecture.getNumber());

		TextView numberView = (TextView) findViewById(R.id.lecture_number);
		numberView.setText(mLecture.getNumber());
		TextView titleView = (TextView) findViewById(R.id.lecture_title);
		titleView.setText(mLecture.getTitle());
		TextView staffView = (TextView) findViewById(R.id.lecture_staff);
		staffView.setText(mLecture.getStaff());
		TextView semesterView = (TextView) findViewById(R.id.lecture_semester);
		semesterView.setText(mLecture.getSemester());
		TextView ectsView = (TextView) findViewById(R.id.lecture_ects);
		ectsView.setText(Float.toString(mLecture.getEcts()));
		TextView whenView = (TextView) findViewById(R.id.lecture_when);

		/* events */
		whenView.setText("");
		for (Lecture.Event e : mLecture.getEvents()) {
			Log.d(TAG_NAME, "week-day: " + e.startTime.weekDay);
			Log.d(TAG_NAME, "start-time: " + e.startTime + "end-time: "
					+ e.endTime);
			Log.d(TAG_NAME, "location: " + e.location);

			whenView.setText(whenView.getText()
					+ e.startTime.format("%A %H - ") + e.endTime.format("%H\n"));

		}

		TextView descView = (TextView) findViewById(R.id.lecture_desc);
		descView.setText(mLecture.getDescription());

		Button buttonCalendar = (Button) findViewById(R.id.button_event_calendar);
		buttonCalendar.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.d(TAG_NAME, "adding events to calendar.");

				for (Event e : mLecture.getEvents()) {
					Log.d(TAG_NAME,
							"Adding " + mLecture.getTitle() + ", "
									+ e.startTime.format3339(false) + ", "
									+ e.endTime.format3339(false));
					mService.addToCalendar(mLecture.getTitle(),
							e.startTime.toMillis(true),
							e.endTime.toMillis(true));
				}

			}
		});
	}

	/**
	 * gets called when "map-button" is pressed
	 * 
	 * @param v
	 */
	public void showMap(View v) {

		Intent mapIntent = new Intent(getBaseContext(), LocationActivity.class);
		mapIntent.putExtra("lecture", mLecture);

		startActivity(mapIntent);

	}

	@Override
	protected void serviceAvailable() {
		/* nothing to do with the service */

	}

	protected void onStop() {
		super.onStop();
		setResult(RESULT_OK);
		finish();
	}
}
