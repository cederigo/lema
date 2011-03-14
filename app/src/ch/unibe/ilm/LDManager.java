package ch.unibe.ilm;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ch.unibe.ilm.model.Lecture;
import ch.unibe.ilm.provider.ILectureDataProvider;
import ch.unibe.ilm.R;

public class LDManager {

	private HashMap<String, ILectureDataProvider> ldProviders;
	private LDPersistence persistence;
	private Prefs prefs;
	private Context context;
	private Resources res;
	
	
	public LDManager(Context inCtx) throws ILMException {
		context = inCtx;
		res = context.getResources();
		SharedPreferences sprefs = context.getSharedPreferences("ilmprefs", Context.MODE_PRIVATE);
		prefs = new Prefs(sprefs);
		SQLiteDatabase sqdb = context.openOrCreateDatabase("ilmdb", Context.MODE_PRIVATE,null);
		persistence = new LDPersistence(sqdb);
		initDataProviders();
	}
	
	public Prefs getPrefs(){
		return prefs;
	}
	
	public boolean update() {
		
		/**
		 * get data from provider specified in prefs.
		 * and insert/update 
		 */
		persistence.update(ldProviders.get(prefs.myUniversity()));
		
		return true;
	}
	
	public List<Lecture> getMyLectures(){
		return null;
	}
	
	public void subscribe(Lecture l){
		
	}
	public void unsubscribe(Lecture l){
		
	}
	public void listLectures(){
		
	}
	
	public void shutdown(){
		
	}
	
	private void initDataProviders() throws ILMException{
		ldProviders = new HashMap<String, ILectureDataProvider>();
		String [] providerClassNames = res.getStringArray(R.array.providers);
		for (String className : providerClassNames) {
			Log.i("ldm", "init provider: " + className);
			try {
				Class providerClass = Class.forName(className);
				ILectureDataProvider provider = (ILectureDataProvider) providerClass.newInstance();
				ldProviders.put(provider.getName(), provider);
			} catch (ClassNotFoundException e) {
				throw new ILMException(e);
			} catch (InstantiationException e) {
				throw new ILMException(e);
			} catch (IllegalAccessException e) {
				throw new ILMException(e);
			}
		}
		
	}
	

}
