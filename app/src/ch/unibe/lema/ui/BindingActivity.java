package ch.unibe.lema.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import ch.unibe.lema.R;
import ch.unibe.lema.Service;
import ch.unibe.lema.Service.LocalBinder;

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

    /**
     * callback when service is available
     */
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

    /**
     * Options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.item_browse:
            Intent browse = new Intent(getBaseContext(), BrowseActivity.class);
            startActivity(browse);
            return true;
        case R.id.item_subscriptions:
            Intent home = new Intent(getBaseContext(),HomeActivity.class);
            startActivity(home);
            return true;
        case R.id.item_search:
            onSearchRequested();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}