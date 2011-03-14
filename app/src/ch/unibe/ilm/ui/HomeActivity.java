package ch.unibe.ilm.ui;

import android.os.Bundle;
import android.util.Log;
import ch.unibe.ilm.Service;
import ch.unibe.ilm.R;

public class HomeActivity extends BindingActivity {

  private static final String TAG_NAME = "home";
  
  @Override
  /** Called when the activity is first created. */
  public void onCreate(Bundle savedInstanceState) {
    
    Log.d(TAG_NAME, "onCreate");
    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

  }

  @Override
  public void onDestroy() {
    Log.d(TAG_NAME, "onCreate");
    
    mService.shutdown();
  }

}