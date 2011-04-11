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

/**
 * Provide functionality for subscription management, wrap database stuff.
 */
public class Subscriptions {
    private SQLiteDatabase db;
    private static final String[] COLUMNS = { "id", "number", "title", "staff",
            "semester", "description", "ects", "start", "end" };
    private static final String KEY_COLUMN = "id";
    private static final String TABLE_NAME = "subscription";
    private static final String LOG_TAG = "Subscriptions";

    public Subscriptions(Context context) {
        db = new Storage(context).getWritableDatabase();
    }

    public Lecture add(Lecture lecture) {
        ContentValues values = new ContentValues();

        values.put(COLUMNS[1], lecture.getNumber());
        values.put(COLUMNS[2], lecture.getTitle());
        values.put(COLUMNS[3], lecture.getStaff());
        values.put(COLUMNS[4], lecture.getSemester());
        values.put(COLUMNS[5], lecture.getDescription());
        values.put(COLUMNS[6], lecture.getEcts());
        values.put(COLUMNS[7], lecture.getTimeStart().toMillis(false));
        values.put(COLUMNS[8], lecture.getTimeEnd().toMillis(false));

        long id = db.insert(TABLE_NAME, null, values);

        Log.d(LOG_TAG, "Subscribed to " + lecture.getTitle());
        return new Lecture(lecture, id);
    }

    public Lecture remove(Lecture l) {
        db.delete(TABLE_NAME, KEY_COLUMN + "=" + l.getId(), null);
        return new Lecture(l, -1);
    }

    public List<Lecture> getAll() {
        Cursor result = db.query(TABLE_NAME, COLUMNS, null, null, null, null,
                null);

        List<Lecture> subs = new ArrayList<Lecture>();

        while (result.moveToNext()) {
            Lecture lecture = new Lecture(result.getLong(0));
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

            subs.add(lecture);
        }

        result.close();
        return subs;
    }

    public void cleanUp() {
        db.close();
    }
}
