package ch.unibe.lema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for facilitating database management.
 */
public class Storage extends SQLiteOpenHelper {

	static final String DB_NAME = "lema";
	static final String TABLE_SUBSCRIPTION_NAME = "subscription";
	static final String TABLE_EVENT_NAME = "event";
	static final int DB_VER = 2;
	private static final String CREATE_TABLE_SUBSCRIPTION = "CREATE TABLE "
			+ TABLE_SUBSCRIPTION_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
			+ "number CHAR(10) NOT NULL, title VARCHAR(100) NOT NULL, "
			+ "staff VARCHAR(50), ects FLOAT, start TIMESTAMP, end TIMESTAMP,"
			+ "description TEXT, semester CHAR(10));";

	private static final String CREATE_TABLE_EVENT = "CREATE TABLE "
			+ TABLE_EVENT_NAME
			+ " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
			+ "subscription_id NOT NULL, description TEXT, start TIMESTAMP, end TIMESTAMP);";

	public Storage(Context context) {
		super(context, DB_NAME, null, DB_VER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SUBSCRIPTION);
		db.execSQL(CREATE_TABLE_EVENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_SUBSCRIPTION_NAME);
		db.execSQL(CREATE_TABLE_SUBSCRIPTION);
		db.execSQL("DROP TABLE " + TABLE_EVENT_NAME);
		db.execSQL(CREATE_TABLE_EVENT);
	}
}
