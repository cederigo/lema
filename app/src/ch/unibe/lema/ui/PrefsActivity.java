package ch.unibe.lema.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PrefsActivity extends Activity {
  
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TextView textview = new TextView(this);
    textview.setText("This is the Artists tab");
    setContentView(textview);
}
}