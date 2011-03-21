package ch.unibe.lema;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import ch.unibe.lema.model.Lecture;
import ch.unibe.lema.provider.ILectureDataProvider;
import ch.unibe.lema.provider.filter.Filter;
import ch.unibe.lema.provider.filter.FilterCriterion;

public class Service extends android.app.Service {

    private static final String LOG_TAG = "ldService";
    private List<ILectureDataProvider> ldProviders;
    private int activeProvider;
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

    

    private void initDataProviders() throws LemaException {
        activeProvider=-1;
        ldProviders = new LinkedList<ILectureDataProvider>();
        String[] providerClassNames = res.getStringArray(R.array.providers);
        for (String className : providerClassNames) {
            Log.i("ldm", "init provider: " + className);
            try {
                Class providerClass = Class.forName(className);
                ILectureDataProvider provider = (ILectureDataProvider) providerClass
                        .newInstance();
                ldProviders.add(provider);
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
    
    public String[] getProviderNames() {
        String[] result = new String[ldProviders.size()];
        
        int idx = 0;
        for(ILectureDataProvider provider : ldProviders) {
            result[idx++] = provider.getName();
        }
        
        return result;
    }
    
    public void selectProvider(int idx) throws LemaException {
        if (idx < 0 || idx >= ldProviders.size())
            throw new LemaException("invalid provider selection: idx="+idx);
        activeProvider = idx;
    }
    
    public List<Lecture> findLectures(Filter filter) throws LemaException {
        ILectureDataProvider provider = ldProviders.get(activeProvider);
        return provider.getLectures(filter);
        
    }
    
    public List<FilterCriterion> getFilterCriteria() {
        ILectureDataProvider provider = ldProviders.get(activeProvider);
        return provider.getCriteria();
    }
    
    public void handleError(LemaException e) {
        Log.e(LOG_TAG, e.getMessage());
        // notify user..
        Toast toast = Toast.makeText(context, "an error occured: "+e.getMessage(), Toast.LENGTH_LONG);
        toast.show();
    }
    
    public void showInfo(String msg) {
        Log.i(LOG_TAG, msg);
        // notify user..
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    
}
