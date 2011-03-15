package ch.unibe.ilm.ui;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import ch.unibe.ilm.Service;
import ch.unibe.ilm.R;
import ch.unibe.ilm.model.Lecture;
import ch.unibe.ilm.provider.evub.EvubDataProvider;
import ch.unibe.ilm.provider.filter.Filter;
import ch.unibe.ilm.provider.filter.FilterCriterion;

public class HomeActivity extends BindingActivity {

  private static final String TAG_NAME = "home";
  
  @Override
  /** Called when the activity is first created. */
  public void onCreate(Bundle savedInstanceState) {
    
    Log.d(TAG_NAME, "onCreate");
    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    // TODO remove, only proof of concept
    Filter filter = new Filter();
    FilterCriterion crit = new FilterCriterion("institution", "informatik", "");
    FilterCriterion crit2 = new FilterCriterion("person", "strahm", "");
    FilterCriterion crit3 = new FilterCriterion("semester", "S2011", "");
    filter.addCriteria(crit);
    filter.addCriteria(crit2);
    filter.addCriteria(crit3);
    
    EvubDataProvider edp = new EvubDataProvider();
    List<Lecture> lectures = edp.getLectures(filter);
    
    Log.d(TAG_NAME, lectures.size() + " lectures found:");
    
    for ( Lecture l : lectures ) {
      Log.d(TAG_NAME, l.getTitle());
    }
  }

  @Override
  public void onDestroy() {
    Log.d(TAG_NAME, "onDestroy");
    
    mService.shutdown();
  }

}