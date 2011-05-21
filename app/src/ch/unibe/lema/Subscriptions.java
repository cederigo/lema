package ch.unibe.lema;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;
import ch.unibe.lema.provider.Lecture;
import ch.unibe.lema.provider.Lecture.Event;

/**
 * Provide functionality for subscription management, wrap database stuff.
 */
public class Subscriptions {
	private SQLiteDatabase db;
	private static final String[] SUB_COLUMNS = { "id", "number", "title",
			"staff", "semester", "description", "ects", "start", "end" };
	private static final String[] EVENT_COLUMNS = { "id", "subscription_id",
			"start", "end" };
	private static final String KEY_COLUMN = "id";
	private static final String SUB_TABLE_NAME = "subscription";
	private static final String EVENT_TABLE_NAME = "event";
	private static final String LOG_TAG = "Subscriptions";

	public Subscriptions(Context context) {
		db = new Storage(context).getWritableDatabase();
	}

	public Lecture add(Lecture lecture) {
		ContentValues values = new ContentValues();

		values.put(SUB_COLUMNS[1], lecture.getNumber());
		values.put(SUB_COLUMNS[2], lecture.getTitle());
		values.put(SUB_COLUMNS[3], lecture.getStaff());
		values.put(SUB_COLUMNS[4], lecture.getSemester());
		values.put(SUB_COLUMNS[5], lecture.getDescription());
		values.put(SUB_COLUMNS[6], lecture.getEcts());
		values.put(SUB_COLUMNS[7], lecture.getTimeStart().toMillis(false));
		values.put(SUB_COLUMNS[8], lecture.getTimeEnd().toMillis(false));

		long id = db.insert(SUB_TABLE_NAME, null, values);

		// save lecture's events
		for (Event e : lecture.getEvents()) {
			ContentValues eventValues = new ContentValues();

			eventValues.put(EVENT_COLUMNS[1], id);
			eventValues.put(EVENT_COLUMNS[2], e.startTime.toMillis(false));
			eventValues.put(EVENT_COLUMNS[3], e.endTime.toMillis(false));

			db.insert(EVENT_TABLE_NAME, null, eventValues);
		}

		Log.d(LOG_TAG, "Subscribed to " + lecture.getTitle());
		return new Lecture(lecture, id);
	}

	public Lecture remove(Lecture l) {
		db.delete(SUB_TABLE_NAME, KEY_COLUMN + "=" + l.getId(), null);
		return new Lecture(l, -1);
	}

	public List<Lecture> getAll() {
		Cursor result = db.query(SUB_TABLE_NAME, SUB_COLUMNS, null, null, null,
				null, null);

		List<Lecture> subs = new ArrayList<Lecture>();

		while (result.moveToNext()) {
			long id = result.getLong(0);
			Lecture lecture = new Lecture(id);
			lecture.setNumber(result.getString(1));
			lecture.setTitle(result.getString(2));
			lecture.setStaff(result.getString(3));
			lecture.setSemester(result.getString(4));
			lecture.setDescription(result.getString(5));
			lecture.setEcts(result.getFloat(6));

			Time start = new Time();
			start.set(result.getLong(7));
			lecture.setTimeStart(start);

			Time end = new Time();
			end.set(result.getLong(8));
			lecture.setTimeEnd(end);

			// get lecture's events from db
			Cursor eventResult = db.query(EVENT_TABLE_NAME, EVENT_COLUMNS,
					"subscription_id=" + id, null, null, null, null);

			Log.d(LOG_TAG, eventResult.getCount() + " events for lecture " + id);

			while (eventResult.moveToNext()) {
				Time startTime = new Time();
				Time endTime = new Time();
				startTime.set(eventResult.getLong(2));
				endTime.set(eventResult.getLong(3));

				lecture.addEvent("test", startTime, endTime);
			}

			eventResult.close();
			subs.add(lecture);
		}

		result.close();
		return subs;
	}

	public void cleanUp() {
		db.close();
	}
}
