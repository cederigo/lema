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
    private Subscriptions sub;

    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG_NAME, "onCreate");
        setContentView(R.layout.main);

        /* setup list */
        final ListView lectureList = (ListView) findViewById(R.id.lecturelist);
        listAdapter = new LectureListAdapter(this, new LinkedList<Lecture>());
        lectureList.setAdapter(listAdapter);

        Context context = getApplicationContext();
        sub = new Subscriptions(context);

        // TODO remove, demonstration only
        Lecture l = new Lecture();
        l.setNumber("W1234");
        l.setTitle("Android GUI for Dummies");
        l.setStaff("Eric Schmidt");

        sub.add(l);
    }

    @Override
    public void onDestroy() {
        sub.cleanUp();
        Log.d(TAG_NAME, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void serviceAvailable() {
        List<Lecture> subs = sub.getAll();

        listAdapter.clear();
        for (Lecture l : subs) {
            listAdapter.add(l);
        }

        listAdapter.notifyDataSetChanged();
        mService.showInfo(subs.size() + " subscriptions");
        stopWait();
    }
}