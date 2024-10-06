package biz.binarysolutions.timetracker.tasks;

import biz.binarysolutions.timetracker.db.DatabaseAdapter;
import android.content.Context;
import android.os.AsyncTask;

/**
 * 
 *
 */
public class UpdateCurrentGroupNameTask extends AsyncTask<Integer, Void, Void> {
	
	private Context context;

	/**
	 * 
	 * @param context
	 */
	public UpdateCurrentGroupNameTask(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		
		DatabaseAdapter.updateCurrentGroup(context, params[0]);
		return null;
	}
}
