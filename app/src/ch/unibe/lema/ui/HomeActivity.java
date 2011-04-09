package ch.unibe.lema.ui;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import ch.unibe.lema.R;
import ch.unibe.lema.Subscriptions;
import ch.unibe.lema.provider.Lecture;

public class HomeActivity extends BindingActivity {

    private static final String TAG_NAME = "home";
    private LectureListAdapter listAdapter;
    

    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG_NAME, "onCreate");
        setContentView(R.layout.main);

        /* setup list */
        final ListView lectureList = (ListView) findViewById(R.id.home_lecturelist);
        listAdapter = new LectureListAdapter(this, new LinkedList<Lecture>());
        lectureList.setAdapter(listAdapter);

    }

    @Override
    public void onDestroy() {
        
        Log.d(TAG_NAME, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void serviceAvailable() {
        List<Lecture> subs = mService.getSubscriptions();

        listAdapter.clear();
        for (Lecture l : subs) {
            listAdapter.add(l);
        }

        listAdapter.notifyDataSetChanged();
        mService.showInfo(subs.size() + " subscriptions");
        stopWait();
    }
}