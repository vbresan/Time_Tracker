package biz.binarysolutions.timetracker.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import biz.binarysolutions.timetracker.data.Activity;
import biz.binarysolutions.timetracker.data.TotalActivity;
import biz.binarysolutions.timetracker.data.TrackableActivity;
import biz.binarysolutions.timetracker.util.Pair;

/**
 * 
 *
 */
public class DatabaseAdapter {
	
	private Context context;
	
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase database;
	
	/**
	 * 
	 * @param tableName
	 * @param columns
	 * @param where
	 * @return
	 */
	private Cursor getCursor(String tableName, String[] columns, String where) {
		return database.query(tableName, columns, where, null, null, null, null);
	}	
	
	/**
	 * @param tableName
	 * @param columns
	 * @return
	 */
	private Cursor getCursor(String tableName, String[] columns) {
		return getCursor(tableName, columns, null);
	}
	
	/**
	 * 
	 * @param tableName
	 * @param where
	 */
	private void delete(String tableName, String where) {
		database.delete(tableName, where, null);
	}	
	
	/**
	 * 
	 * @param activity
	 */
	private void saveActivity(Activity activity) {

		ContentValues values = new ContentValues();
		values.put("name",        activity.getName());
		values.put("accumulated", Long.valueOf(activity.getAccumulated()));
		values.put("startedAt",   Long.valueOf(activity.getStartedAt()));
		
		database.insert("Activities", null, values);
	}
	
	/**
	 * 
	 * @param activity
	 */
	private void updateActivity(TrackableActivity activity) {

		ContentValues values = new ContentValues();
		values.put("accumulated", Long.valueOf(activity.getAccumulated()));
		values.put("startedAt",   Long.valueOf(activity.getStartedAt()));
		
		String where = "name = \"" + activity.getName() + "\""; 
		
		database.update("Activities", values, where, null);
	}
	
	/**
	 * 
	 * @param activity
	 */
	private void saveTotalActivity(Activity activity) {

		ContentValues values = new ContentValues();
		values.put("name",        "Total");
		values.put("accumulated", Long.valueOf(activity.getAccumulated()));
		values.put("startedAt",   Long.valueOf(activity.getStartedAt()));
		
		database.insert("TotalActivity", null, values);		
	}
	
	/**
	 * 
	 * @param activity
	 */
	private void updateTotalActivity(TotalActivity activity) {

		ContentValues values = new ContentValues();
		values.put("accumulated", Long.valueOf(activity.getAccumulated()));
		values.put("startedAt",   Long.valueOf(activity.getStartedAt()));
		
		String where = "name = \"Total\""; 
		
		database.update("TotalActivity", values, where, null);
	}	
	
	/**
	 * 
	 * @param groupId
	 * @param activityName
	 */
	private void saveCurrentActivity(Integer groupId, String activityName) {

		ContentValues values = new ContentValues();
		values.put("groupId",      groupId);
		values.put("activityName", activityName);
		
		database.insert("CurrentActivities", null, values);		
	}
	
	/**
	 * 
	 * @param id 
	 */
	private void updateCurrentGroup(Integer id) {

		ContentValues values = new ContentValues();
		values.put("id", id);
		
		database.update("CurrentGroup", values, null, null);
	}	

	/**
	 * 
	 * @param name
	 */
	private void deleteActivity(String name) {
		
		String tableName = "Activities";
		String where     = "name = \"" + name + "\"";
		
		delete(tableName, where);
	}
	
	/**
	 * 
	 * @param name
	 */
	private void deleteCurrentActivity(String name) {
		
		String tableName = "CurrentActivities";
		String where     = "activityName = \"" + name + "\"";
		
		delete(tableName, where);
	}
	
	/**
	 * 
	 */
	private void resetAllActivities() {
		
		ContentValues values = new ContentValues();
		values.put("accumulated", Long.valueOf(0));
		values.put("startedAt",   Long.valueOf(0));
		
		database.update("Activities", values, null, null);
	}

	/**
	 * 
	 */
	private void resetTotalActivity() {
		
		ContentValues values = new ContentValues();
		values.put("accumulated", Long.valueOf(0));
		values.put("startedAt",   Long.valueOf(0));
		
		database.update("TotalActivity", values, null, null);	
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private Activity getActivity(String name) {
		
		Activity activity = null;
		
		String   tableName = "Activities";
		String[] columns   = new String[] { "accumulated", "startedAt" };
		String   where     = "name = \"" + name + "\"";
		Cursor   cursor    = getCursor(tableName, columns, where);
		
		if (cursor.moveToFirst()) {
			
			do {
				long accumulated = cursor.getLong(0);
				long startedAt   = cursor.getLong(1);
				
				activity = new Activity(name, accumulated, startedAt);
				
			} while (cursor.moveToNext());
		}
		
		if (!cursor.isClosed()) {
			cursor.close();
		}		
		
		return activity;
	}	

	/**
	 * 
	 * @return
	 */
	private Activity getTotalActivity() {
		
		Activity activity = null;
		
		String   tableName = "TotalActivity";
		String[] columns   = new String[] { "accumulated", "startedAt" };
		String   where     = "name = \"Total\"";
		Cursor   cursor    = getCursor(tableName, columns, where);
		
		if (cursor.moveToFirst()) {
			
			do {
				long accumulated = cursor.getLong(0);
				long startedAt   = cursor.getLong(1);
				
				activity = new Activity("Total", accumulated, startedAt);
				
			} while (cursor.moveToNext());
		}
		
		if (!cursor.isClosed()) {
			cursor.close();
		}		
		
		return activity;
	}

	/**
	 * 
	 * @param tableName
	 * @return
	 */
	private ArrayList<String> getNames(String tableName) {
		
		ArrayList<String> names = new ArrayList<String>();
		
		String[] columns = new String[] { "name" };
		Cursor   cursor  = getCursor(tableName, columns);
		
		if (cursor.moveToFirst()) {
			
			do {
				String name = cursor.getString(0);
				names.add(name);
				
			} while (cursor.moveToNext());
		}
		
		if (!cursor.isClosed()) {
			cursor.close();
		}		
		
		return names;
	}

	/**
	 * 
	 * @return
	 */
	private ArrayList<String> getActivityNames() {
		return getNames("Activities");
	}
	
	/**
	 * 
	 * @return
	 */
	private ArrayList<String> getGroupNames() {
		return getNames("Groups");
	}

	/**
	 * 
	 * @return
	 */
	private ArrayList<Pair<Integer, String>> getCurrentActivityNames() {
		
		ArrayList<Pair<Integer, String>> activities = 
			new ArrayList<Pair<Integer, String>>();
		
		String   tableName = "CurrentActivities";
		String[] columns   = new String[] { "groupId", "activityName" };
		Cursor   cursor    = getCursor(tableName, columns);
		
		if (cursor.moveToFirst()) {
			
			do {
				Integer groupId      = Integer.valueOf(cursor.getInt(0));
				String  activityName = cursor.getString(1);
				
				Pair<Integer, String> pair = 
					new Pair<Integer, String>(groupId, activityName);
				
				activities.add(pair);
				
			} while (cursor.moveToNext());
		}
		
		if (!cursor.isClosed()) {
			cursor.close();
		}		
		
		return activities;
	}
	
	/**
	 * 
	 * @return
	 */
	private int getCurrentGroupId() {
		
		int id = 0;
		
		String   tableName = "CurrentGroup";
		String[] columns   = new String[] { "id" };
		Cursor   cursor    = getCursor(tableName, columns);
		
		if (cursor.moveToFirst()) {
			
			do {
				id = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		
		if (!cursor.isClosed()) {
			cursor.close();
		}		
		
		return id;
	}	

	/**
	 * 
	 * @param context
	 */
	public DatabaseAdapter(Context context) {
		this.context = context;
	}
	
	/**
	 * 
	 */
	public DatabaseAdapter open() {

		databaseHelper = DatabaseHelper.getInstance(context);
        database = databaseHelper.getWritableDatabase();
        
        return this;
	}
	
	/**
	 * 
	 * @param context
	 * @param activity
	 */
	public static void saveActivity
		(
				final Context  context, 
				final Activity activity
		) {

		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.saveActivity(activity);
			}
		}.start();		
	}

	/**
	 * 
	 * @param context
	 * @param activity
	 */
	public static void updateActivity
		(
				final Context           context,
				final TrackableActivity activity
		) {

		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.updateActivity(activity);
			}
		}.start();	
	}

	/**
	 * 
	 * @param context
	 * @param activity
	 */
	public static void saveTotalActivity
		(
				final Context  context,
				final Activity activity
		) {

		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.saveTotalActivity(activity);
			}
		}.start();
	}

	/**
	 * 
	 * @param context
	 * @param activity
	 */
	public static void updateTotalActivity
		(
				final Context       context,
				final TotalActivity activity
		) {

		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.updateTotalActivity(activity);
			}
		}.start();
	}

	/**
	 * 
	 * @param context
	 * @param activityName
	 */
	public static void saveCurrentActivityName
		(
				final Context context,
				final Integer groupId,
				final String  activityName
		) {

		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.saveCurrentActivity(groupId, activityName);
			}
		}.start();	
	}

	/**
	 * Started in AsyncTask, doesn't need separate thread
	 * 
	 * @param context
	 * @param id 
	 */
	public static void updateCurrentGroup
		(
				Context context, 
				Integer id
		) {

		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.open();
		databaseAdapter.updateCurrentGroup(id);
	}

	/**
	 * 
	 * @param name
	 */
	public static void deleteActivity
		(
				final Context context, 
				final String name
		) {

		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.deleteActivity(name);
			}
		}.start();	
	}

	/**
	 * 
	 * @param mainActivity
	 * @param name
	 */
	public static void deleteCurrentActivity
		(
				final Context context,
				final String  name
		) {

		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.deleteCurrentActivity(name);
			}
		}.start();	
	}

	/**
	 * 
	 * @param context
	 */
	public static void resetAllActivities(final Context context) {
		
		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.resetAllActivities();
			}
		}.start();
	}

	/**
	 * 
	 * @param context
	 */
	public static void resetTotalActivity(final Context context) {
		
		new Thread() {

			@Override
			public void run() {
				
				DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
				databaseAdapter.open();
				databaseAdapter.resetTotalActivity();
			}
		}.start();
	}

	/**
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static Activity getActivity(Context context, String name) {
		
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.open();
		
		Activity activity = databaseAdapter.getActivity(name);
		
		return activity;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static Activity getTotalActivity(Context context) {
		
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.open();
		
		Activity total = databaseAdapter.getTotalActivity();
		
		return total;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<String> getActivityNames(Context context) {
		
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.open();
		
		ArrayList<String> activityNames = databaseAdapter.getActivityNames();
		
		return activityNames;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<String> getGroupNames(Context context) {
		
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.open();
		
		ArrayList<String> names = databaseAdapter.getGroupNames();
		
		return names;
	}	

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<Pair<Integer, String>> getCurrentActivityNames
		(
				Context context
		) {
		
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.open();
		
		ArrayList<Pair<Integer, String>> activities = 
			databaseAdapter.getCurrentActivityNames();
		
		return activities;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static int getCurrentGroupId(Context context) {
		
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.open();
		
		int position = databaseAdapter.getCurrentGroupId();
		
		return position;
	}
}
