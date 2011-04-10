package ch.unibe.lema.ui;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ch.unibe.lema.R;
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
    }

    @Override
    public void onDestroy() {

        Log.d(TAG_NAME, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void serviceAvailable() {
        /* setup list */
        final ListView lectureList = (ListView) findViewById(R.id.home_lecturelist);
        listAdapter = new LectureListAdapter(this, new LinkedList<Lecture>(),
                mService);
        lectureList.setAdapter(listAdapter);
        lectureList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                Intent intent = new Intent(getBaseContext(),
                        LectureActivity.class);
                intent.putExtra("lecture", listAdapter.getItem(position));
                startActivityForResult(intent, 1);
            }
        });

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