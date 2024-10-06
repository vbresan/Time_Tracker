package biz.binarysolutions.timetracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 *
 */
class DatabaseHelper extends SQLiteOpenHelper {
	
	private static Context context;
	
	/**
	 * 
	 *
	 */
	private static class SingletonHolder { 
        public static final DatabaseHelper INSTANCE = new DatabaseHelper(context);
	}
	
	
	private static final String DATABASE_NAME    = "data.db";
	private static final int    DATABASE_VERSION = 10;
	
	private static final String CREATE_TABLE_TOTAL_ACTIVITY =
    	"CREATE TABLE TotalActivity (name TEXT PRIMARY KEY, accumulated INTEGER NOT NULL, startedAt INTEGER NOT NULL);";
	
    private static final String CREATE_TABLE_ACTIVITIES =
    	"CREATE TABLE Activities (name TEXT PRIMARY KEY, accumulated INTEGER NOT NULL, startedAt INTEGER NOT NULL);";
    
    private static final String CREATE_TABLE_CURRENT_ACTIVITIES =
    	"CREATE TABLE CurrentActivities (groupId INTEGER, activityName TEXT, PRIMARY KEY(groupId, activityName));";

    
    private static final String CREATE_TABLE_CURRENT_GROUP =
    	"CREATE TABLE CurrentGroup (id INTEGER NOT NULL);";
    
    private static final String INSERT_CURRENT_GROUP =
    	"INSERT INTO CurrentGroup VALUES (0);";    
    
    
    private static final String CREATE_TABLE_GROUPS =
    	"CREATE TABLE Groups (id INTEGER NOT NULL, name TEXT PRIMARY KEY, accumulated INTEGER NOT NULL, startedAt INTEGER NOT NULL);";
    
    private static final String INSERT_GROUP_WORK =
    	"INSERT INTO Groups VALUES (0, 'Work', 0, 0);";
    
    private static final String INSERT_GROUP_HOME =
    	"INSERT INTO Groups VALUES (1, 'Home', 0, 0);";
    
    private static final String INSERT_GROUP_OTHER =
    	"INSERT INTO Groups VALUES (2, 'Other', 0, 0);";
	
    
    private static final String DROP_TABLE_TOTAL_ACTIVITY = 
    	"DROP TABLE IF EXISTS TotalActivity;";	
    
    private static final String DROP_TABLE_ACTIVITIES = 
    	"DROP TABLE IF EXISTS Activities;";
    
    private static final String DROP_TABLE_CURRENT_GROUP = 
    	"DROP TABLE IF EXISTS CurrentGroup;";
    
    private static final String DROP_TABLE_CURRENT_ACTIVITIES = 
    	"DROP TABLE IF EXISTS CurrentActivities;";
    
    private static final String DROP_TABLE_GROUPS =
    	"DROP TABLE IF EXISTS Groups;";   

    /**
	 * 
	 * @param db
	 */
	private void createCurrentGroup(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CURRENT_GROUP);
		db.execSQL(INSERT_CURRENT_GROUP);
	}

	/**
     * @param db 
     * 
     */
    private void createActivityGroups(SQLiteDatabase db) {
    	db.execSQL(CREATE_TABLE_GROUPS);
    	db.execSQL(INSERT_GROUP_WORK);
    	db.execSQL(INSERT_GROUP_HOME);
    	db.execSQL(INSERT_GROUP_OTHER);
    }
    
	/**
	 * 
	 * @param context
	 */
	private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TOTAL_ACTIVITY);
		db.execSQL(CREATE_TABLE_ACTIVITIES);
        db.execSQL(CREATE_TABLE_CURRENT_ACTIVITIES);
        
        createCurrentGroup(db);
        createActivityGroups(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL(DROP_TABLE_TOTAL_ACTIVITY);
        db.execSQL(DROP_TABLE_ACTIVITIES);
        db.execSQL(DROP_TABLE_CURRENT_ACTIVITIES);
        db.execSQL(DROP_TABLE_CURRENT_GROUP);
        db.execSQL(DROP_TABLE_GROUPS);
        onCreate(db);
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static DatabaseHelper getInstance(Context context) {
		DatabaseHelper.context = context;
        return SingletonHolder.INSTANCE;
	}	
}
