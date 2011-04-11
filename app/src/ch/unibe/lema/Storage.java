package ch.unibe.lema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for facilitating database management.
 */
public class Storage extends SQLiteOpenHelper {

    static final String DB_NAME = "lema";
    static final String TABLE_NAME = "subscription";
    static final int DB_VER = 2;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "number CHAR(10) NOT NULL, title VARCHAR(100) NOT NULL, "
            + "staff VARCHAR(50), ects FLOAT, start TIMESTAMP, end TIMESTAMP,"
            + "description TEXT, semester CHAR(10));";

    public Storage(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        db.execSQL(TABLE_CREATE);
    }
}
