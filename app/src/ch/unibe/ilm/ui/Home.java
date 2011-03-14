package ch.unibe.ilm.ui;

import android.os.Bundle;
import ch.unibe.ilm.LDManager;
import ch.unibe.ilm.R;

public class Home extends ILMActivity{
	
	private LDManager ldm;
	
	@Override
	/** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
    }
	
	@Override
	public void onDestroy(){
		ldm.shutdown();
	}
	
}