package biz.binarysolutions.timetracker.tasks;

import android.os.AsyncTask;
import biz.binarysolutions.timetracker.MainActivity;
import biz.binarysolutions.timetracker.data.Activity;
import biz.binarysolutions.timetracker.db.DatabaseAdapter;

/**
 * 
 *
 */
public class AddCurrentActivityTask extends AsyncTask<Object, Void, Activity> {

	private MainActivity mainActivity;
	
	/**
	 * 
	 * @param mainActivity
	 */
	public AddCurrentActivityTask(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	protected Activity doInBackground(Object... params) {
		
		Integer groupId = (Integer) params[0];
		String  name    = (String)  params[1];
		
		Activity activity = DatabaseAdapter.getActivity(mainActivity, name);
		if (activity == null) {
			
			activity = new Activity(name, 0, 0);
			DatabaseAdapter.saveActivity(mainActivity, activity);
		}
		
		DatabaseAdapter.saveCurrentActivityName(mainActivity, groupId, name);
		
		return activity;
	}
	
	@Override
	protected void onPostExecute(Activity activity) {
		mainActivity.onCurrentActivityAvailable(activity);
    }

}

