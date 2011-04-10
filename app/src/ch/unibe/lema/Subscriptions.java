package ch.unibe.lema;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.unibe.lema.provider.Lecture;

/**
 * Provide functionality for subscription management, wrap database stuff.
 */
public class Subscriptions {
    private SQLiteDatabase db;
    private static final String[] INSERT_COLUMNS = { "number", "title", "staff" };
    private static final String[] SELECT_COLUMNS = { "id", "number", "title",
            "staff" };
    private static final String KEY_COLUMN = "id";
    private static final String TABLE_NAME = "subscription";
    private static final String LOG_TAG = "Subscriptions";

    public Subscriptions(Context context) {
        db = new Storage(context).getWritableDatabase();
    }

    public Lecture add(Lecture lecture) {
        ContentValues values = new ContentValues();

        values.put(INSERT_COLUMNS[0], lecture.getNumber());
        values.put(INSERT_COLUMNS[1], lecture.getTitle());
        values.put(INSERT_COLUMNS[2], lecture.getStaff());

        long id = db.insert(TABLE_NAME, null, values);

        Log.d(LOG_TAG, "Subscribed to " + lecture.getTitle());
        return new Lecture(lecture, id);
    }

    public void remove(Lecture l) {
        db.delete(TABLE_NAME, KEY_COLUMN + "=" + l.getId(), null);
    }

    public List<Lecture> getAll() {
        Cursor result = db.query(TABLE_NAME, SELECT_COLUMNS, null, null, null,
                null, null);

        List<Lecture> subs = new ArrayList<Lecture>();

        while (result.moveToNext()) {
            Lecture lecture = new Lecture(result.getLong(0));
            lecture.setNumber(result.getString(1));
            lecture.setTitle(result.getString(2));
            lecture.setStaff(result.getString(3));

            subs.add(lecture);
        }

        result.close();
        return subs;
    }

    public void cleanUp() {
        db.close();
    }
}
