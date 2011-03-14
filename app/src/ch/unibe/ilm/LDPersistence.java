package ch.unibe.ilm;

import ch.unibe.ilm.provider.ILectureDataProvider;
import android.database.sqlite.SQLiteDatabase;


/**
 * tables.
 *  university
 *  faculty
 *  department
 *  lecture
 *  subscription
 *  
 * @author cede
 *
 */

public class LDPersistence {

	private SQLiteDatabase db;
	
	public LDPersistence(SQLiteDatabase inDB){
		db = inDB;
		init();
	}
	
	private void init() {
		//check for existing tables and create them if necessary
	}
	
	
	public void update(ILectureDataProvider provider) {
		//sync db
	}
	
	
	
}
