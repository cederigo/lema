package ch.unibe.lema.ui;

import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.unibe.lema.LemaException;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Filter;
import ch.unibe.lema.provider.FilterCriterion;
import ch.unibe.lema.provider.Lecture;

public class HomeActivity extends BindingActivity {

    private static final String TAG_NAME = "home";
    private ArrayAdapter<String> listAdapter;

    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_NAME, "onCreate");
        setContentView(R.layout.main);

        /* setup list */
        final ListView lectureList = (ListView) findViewById(R.id.lectureList);
        listAdapter = new ArrayAdapter<String>(this, R.layout.lecturelist_item);
        lectureList.setAdapter(listAdapter);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG_NAME, "onDestroy");
        super.onDestroy();

    }

    private Filter sampleFilter() {
        /* TODO interactive filter creation */
        Filter filter = new Filter();
        FilterCriterion crit = new FilterCriterion("institution", "informatik",
                "");
        // FilterCriterion crit2 = new FilterCriterion("person", "strahm", "");
        FilterCriterion crit3 = new FilterCriterion("semester", "S2011", "");
        filter.addCriteria(crit);
        // filter.addCriteria(crit2);
        filter.addCriteria(crit3);

        return filter;
    }

    @Override
    protected void serviceAvailable() {
        // TODO Auto-generated method stub
        AsyncTask<Filter, Integer, List<Lecture>> loader = new AsyncTask<Filter, Integer, List<Lecture>>() {

            @Override
            protected List<Lecture> doInBackground(Filter... filters) {

                List<Lecture> result = null;

                if (filters.length == 1) {
                    startWait();
                    // TODO interactive provider selection
                    try {
                        mService.selectProvider(0);
                        result = mService.findLectures(filters[0]);
                    } catch (LemaException e) {
                        mService.handleError(e);
                    }

                    return result;
                }
                // return empty list
                return new LinkedList<Lecture>();
            }

            protected void onPostExecute(final List<Lecture> result) {

                listAdapter.clear();
                for (Lecture l : result) {
                    listAdapter.add(l.toString());
                }

                listAdapter.notifyDataSetChanged();
                mService.showInfo("found " + result.size() + " lectures");
                stopWait();

            }

        };

        loader.execute(sampleFilter());
    }

}