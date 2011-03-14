package ch.unibe.ilm.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import ch.unibe.ilm.ILMException;
import ch.unibe.ilm.LDManager;

public abstract class ILMActivity extends Activity {

	protected String logTag = getClass().getSimpleName();
	protected static LDManager ldm;
	
	@Override
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        
		Log.i(logTag, "starting up.");
		
		super.onCreate(savedInstanceState);
        
        try {
			ldm = new LDManager(getApplicationContext());
		} catch (ILMException e) {
			handleException(e);
		}
    }
	
	
	protected void handleException(ILMException e){
		//do something cool withit
	}
	
}
