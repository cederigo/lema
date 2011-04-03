package ch.unibe.lema;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.unibe.lema.provider.Lecture;

/**
 * Provide functionality for subscription management, wrap database stuff.
 */
public class Subscriptions {
    private SQLiteDatabase db;
    private static final String[] COLUMNS = { "number", "title", "staff" };
    private static final String TABLE_NAME = "subscription";

    public Subscriptions(Context context) {
        db = new Storage(context).getWritableDatabase();
    }

    public void add(Lecture lecture) {
        ContentValues values = new ContentValues();

        values.put(COLUMNS[0], lecture.getNumber());
        values.put(COLUMNS[1], lecture.getTitle());
        values.put(COLUMNS[2], lecture.getStaff());

        db.insert(TABLE_NAME, null, values);
    }

    public void unsubscribe(long lectureId) {

    }

    public List<Lecture> getAll() {
        Cursor result = db.query(TABLE_NAME, COLUMNS, null, null, null, null,
                null);

        List<Lecture> subs = new ArrayList<Lecture>();

        while (result.moveToNext()) {
            Lecture lecture = new Lecture();
            lecture.setNumber(result.getString(0));
            lecture.setTitle(result.getString(1));
            lecture.setStaff(result.getString(2));

            subs.add(lecture);
        }

        return subs;
    }
}
