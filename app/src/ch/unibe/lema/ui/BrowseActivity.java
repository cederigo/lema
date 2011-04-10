package ch.unibe.lema.ui;

import java.util.LinkedList;
import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import ch.unibe.lema.LemaException;
import ch.unibe.lema.R;
import ch.unibe.lema.provider.Filter;
import ch.unibe.lema.provider.FilterCriterion;
import ch.unibe.lema.provider.Lecture;

public class BrowseActivity extends BindingActivity {
    private static final String TAG_NAME = "Browse";
    private LectureListAdapter listAdapter;

    private final class LectureLoadAsyncTask extends
            AsyncTask<Filter, Integer, List<Lecture>> {
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
                listAdapter.add(l);
            }

            listAdapter.notifyDataSetChanged();
            mService.showInfo("found " + result.size() + " lectures");
            stopWait();
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG_NAME, "onCreate");
        setContentView(R.layout.browse);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private Filter buildFilter(String query) {
        // simple search based on lecture title
        // in the informatik institution at current semester
        Filter filter = new Filter();

        filter.addCriteria(new FilterCriterion("institution", "informatik", ""));
        filter.addCriteria(new FilterCriterion("semester", "S2011", ""));
        filter.addCriteria(new FilterCriterion("title", query, ""));

        return filter;

    }

    private Filter defaultFilter() {
        /* TODO interactive filter creation */

        Filter filter = new Filter();

        filter.addCriteria(new FilterCriterion("institution", "informatik", ""));
        filter.addCriteria(new FilterCriterion("semester", "S2011", ""));

        return filter;
    }

    protected void serviceAvailable() {
        /* setup list */
        final ListView lectureList = (ListView) findViewById(R.id.browse_lecturelist);
        listAdapter = new LectureListAdapter(this, new LinkedList<Lecture>(),
                mService);
        lectureList.setAdapter(listAdapter);
        lectureList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                Intent intent = new Intent(getBaseContext(), LectureActivity.class);
                intent.putExtra("lecture", listAdapter.getItem(position));
                startActivity(intent);
            }
        });
        
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG_NAME, "search: " + query);
            new LectureLoadAsyncTask().execute(buildFilter(query));

        } else {
            new LectureLoadAsyncTask().execute(defaultFilter());
        }

    }
}
