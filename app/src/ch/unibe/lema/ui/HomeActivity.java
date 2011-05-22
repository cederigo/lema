package ch.unibe.lema.ui;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Lecture;
import ch.unibe.lema.ui.LectureListAdapter.OnIconClickListener;

public class HomeActivity extends BindingActivity implements OnIconClickListener {

    private static final String TAG_NAME = "home";
    private LectureListAdapter listAdapter;

    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void serviceAvailable() {
        /* setup list */
        final ListView lectureList = (ListView) findViewById(R.id.home_lecturelist);
        listAdapter = new LectureListAdapter(this, new LinkedList<Lecture>(),
                this);
        lectureList.setAdapter(listAdapter);
        lectureList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                Intent intent = new Intent(getBaseContext(),
                        LectureActivity.class);
                intent.putExtra("lecture", listAdapter.getItem(position));
                startActivity(intent);
            }
        });

        List<Lecture> subs = mService.getSubscriptions();

        listAdapter.clear();
        for (Lecture l : subs) {
            listAdapter.add(l);
        }

        listAdapter.notifyDataSetChanged();
        Log.i(TAG_NAME, subs.size() + "subscriptions");
        mService.showInfo(subs.size() + " subscriptions");
        stopWait();
    }

    public void onIconClick(int position, View v) {
        
        Lecture l = listAdapter.getItem(position);
        
        if (l.isSubscription()) {
            /*remove from list*/
            mService.unsubscribe(l);
            listAdapter.remove(position);
            
            mService.showInfo("Unsubscribed from " + l.getTitle());
            ImageView icon = (ImageView) v.findViewById(R.id.lecturelistitem_icon);
            icon.setImageResource(android.R.drawable.star_big_off);
        } else {
            /*should not be possible*/
        }
        
    }
}