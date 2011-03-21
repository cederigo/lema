package ch.unibe.lema;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import ch.unibe.lema.model.Lecture;
import ch.unibe.lema.provider.ILectureDataProvider;
import ch.unibe.lema.R;

public class Service extends android.app.Service {

    private static final String LOG_TAG = "ldService";
    private HashMap<String, ILectureDataProvider> ldProviders;
    private LDPersistence persistence;
    private Prefs prefs;
    private Context context;
    private Resources res;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public Service getService() {
            // this
            return Service.this;
        }
    }

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");
        context = getApplicationContext();
        res = context.getResources();
        SharedPreferences sprefs = context.getSharedPreferences("ilmprefs",
                Context.MODE_PRIVATE);
        prefs = new Prefs(sprefs);
        SQLiteDatabase sqdb = context.openOrCreateDatabase("ilmdb",
                Context.MODE_PRIVATE, null);
        persistence = new LDPersistence(sqdb);
        try {
            initDataProviders();
        } catch (LemaException e) {
            handleError(e);
        }

    }

    private void handleError(LemaException e) {
        // notify user..
    }

    private void initDataProviders() throws LemaException {
        ldProviders = new HashMap<String, ILectureDataProvider>();
        String[] providerClassNames = res.getStringArray(R.array.providers);
        for (String className : providerClassNames) {
            Log.i("ldm", "init provider: " + className);
            try {
                Class providerClass = Class.forName(className);
                ILectureDataProvider provider = (ILectureDataProvider) providerClass
                        .newInstance();
                ldProviders.put(provider.getName(), provider);
            } catch (ClassNotFoundException e) {
                throw new LemaException(e);
            } catch (InstantiationException e) {
                throw new LemaException(e);
            } catch (IllegalAccessException e) {
                throw new LemaException(e);
            }
        }

    }

    @Override
    public IBinder onBind(Intent i) {
        return binder;
    }

    /*
     * Public API
     */

    public Prefs getPrefs() {
        return prefs;
    }

    public boolean update() {

        /**
         * get data from provider specified in prefs. and insert/update
         */
        // persistence.update(ldProviders.get(prefs.myUniversity()));

        return true;
    }

    public List<Lecture> getMyLectures() {
        return null;
    }

    public void subscribe(Lecture l) {

    }

    public void unsubscribe(Lecture l) {

    }

    public void listLectures() {

    }

    public void shutdown() {

    }

}
