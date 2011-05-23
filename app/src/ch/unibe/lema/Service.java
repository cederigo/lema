package ch.unibe.lema;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import ch.unibe.lema.provider.Filter;
import ch.unibe.lema.provider.FilterCriterion;
import ch.unibe.lema.provider.ILectureDataProvider;
import ch.unibe.lema.provider.Lecture;

public class Service extends android.app.Service {

	private static final String TAG_NAME = "LeMaService";
	private List<ILectureDataProvider> ldProviders;
	/*
	 * simple cache for filter -> lecture-list lookup. valid as long asthe
	 * service object lives
	 */
	private HashMap<Filter, List<Lecture>> filter2Lectures;
	private Subscriptions sub;
	private int activeProvider;

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
		Log.d(TAG_NAME, "onCreate ");
		context = getApplicationContext();
		res = context.getResources();
		sub = new Subscriptions(context);
		filter2Lectures = new HashMap<Filter, List<Lecture>>();

		try {
			initDataProviders();
		} catch (LemaException e) {
			handleError(e);
		}
		super.onCreate();

	}

	public void onDestroy() {
		sub.cleanUp();
		Log.d(TAG_NAME, "onDestroy");
		super.onDestroy();
	}

	@SuppressWarnings("unchecked")
	private void initDataProviders() throws LemaException {
		activeProvider = -1;
		ldProviders = new LinkedList<ILectureDataProvider>();
		String[] providerClassNames = res.getStringArray(R.array.providers);

		for (String className : providerClassNames) {
			Log.i(TAG_NAME, "init provider: " + className);
			try {
				Class providerClass = Class.forName(className);
				ILectureDataProvider provider = (ILectureDataProvider) providerClass
						.newInstance();
				provider.init(res);
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
		for (ILectureDataProvider provider : ldProviders) {
			result[idx++] = provider.getName();
		}

		return result;
	}

	public void selectProvider(int idx) throws LemaException {
		if (idx < 0 || idx >= ldProviders.size())
			throw new LemaException("invalid provider selection: idx=" + idx);
		activeProvider = idx;
	}

	public List<Lecture> findLectures(Filter filter) throws LemaException {
		ILectureDataProvider provider = ldProviders.get(activeProvider);

		if (filter2Lectures.containsKey(filter)) {
			Log.d(TAG_NAME, "lectures from cache-entry: " + filter);
			return filter2Lectures.get(filter);
		} else {
			Log.d(TAG_NAME, "new cache-entry: " + filter);
			List<Lecture> newLectures = provider.getLectures(filter);
			filter2Lectures.put(filter, newLectures);
			return newLectures;
		}

	}

	public List<FilterCriterion> getFilterCriteria() {
		ILectureDataProvider provider = ldProviders.get(activeProvider);
		return provider.getCriteria();
	}

	public List<Lecture> getSubscriptions() {
		return sub.getAll();
	}

	public Lecture subscribe(Lecture l) {
		return sub.add(l);
	}

	public Lecture unsubscribe(Lecture l) {
		return sub.remove(l);
	}

	public void handleError(LemaException e) {
		Log.e(TAG_NAME, e.getMessage());
		// notify user..
		Toast toast = Toast.makeText(context,
				"an error occured: " + e.getMessage(), Toast.LENGTH_LONG);
		toast.show();
	}

	public void showInfo(String msg) {
		Log.i(TAG_NAME, msg);
		// notify user..
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void addToCalendar(final String title, final long dtstart,
			final long dtend) {
		final ContentResolver cr = context.getContentResolver();

		Cursor cursor;

		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			cursor = cr.query(
					Uri.parse("content://com.android.calendar/calendars"),
					new String[] { "_id", "displayname" }, null, null, null);
		} else {
			cursor = cr.query(Uri.parse("content://calendar/calendars"),
					new String[] { "_id", "displayname" }, null, null, null);
		}

		if (cursor.moveToFirst()) {
			final String[] calNames = new String[cursor.getCount()];
			final int[] calIds = new int[cursor.getCount()];

			for (int i = 0; i < calNames.length; i++) {
				calIds[i] = cursor.getInt(0);
				calNames[i] = cursor.getString(1);
				cursor.moveToNext();
			}

			ContentValues cv = new ContentValues();
			cv.put("calendar_id", calIds[0]);
			cv.put("title", title);
			cv.put("dtstart", dtstart);
			cv.put("hasAlarm", 0);
			cv.put("dtend", dtend);

			Uri newEvent;
			if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
				newEvent = cr.insert(
						Uri.parse("content://com.android.calendar/events"), cv);
			} else {
				newEvent = cr
						.insert(Uri.parse("content://calendar/events"), cv);
			}

			// if (newEvent != null) {
			// long id = Long.parseLong(newEvent.getLastPathSegment());
			// ContentValues values = new ContentValues();
			// values.put("event_id", id);
			// values.put("method", 1);
			// values.put("minutes", 15); // 15 minuti
			// if (Integer.parseInt(Build.VERSION.SDK) == 8)
			// cr.insert(Uri
			// .parse("content://com.android.calendar/reminders"),
			// values);
			// else
			// cr.insert(Uri.parse("content://calendar/reminders"), values);
			//
			// }

		}
		cursor.close();
	}

}
