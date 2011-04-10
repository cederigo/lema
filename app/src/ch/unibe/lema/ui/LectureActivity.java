package ch.unibe.lema.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Lecture;

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
        TextView titleView = (TextView) findViewById(R.id.lecture_title);
        titleView.setText(mLecture.getTitle());
    }

    @Override
    protected void serviceAvailable() {
        /* nothing to do with the service */

    }

}
