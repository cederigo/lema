package ch.unibe.lema.ui;

import ch.unibe.lema.Service;
import ch.unibe.lema.Service.LocalBinder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public abstract class BindingActivity extends Activity {

    private static final String TAG_NAME = "binding";
    private ProgressDialog pDialog;
    protected Service mService;
    protected boolean mBound = false;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get
            // LocalService
            // instance
            Log.d(TAG_NAME, "service connected");
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            serviceAvailable();
        }

        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, Service.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
            stopWait();
        }
    }
    
    protected abstract void serviceAvailable();
    
    protected void startWait() {
        runOnUiThread(new Runnable() {
            public void run() {
                pDialog = ProgressDialog.show(BindingActivity.this, "", 
                        "Loading. Please wait...", true);
                
            }
        });
        
        
       
    }
    
    protected void stopWait() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
        
    }

}