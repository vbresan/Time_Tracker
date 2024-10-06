package biz.binarysolutions.timetracker.tasks;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.AsyncTask;
import biz.binarysolutions.timetracker.MainActivity;
import biz.binarysolutions.timetracker.R;

/**
 * 
 *
 */
public class GetDateTask extends AsyncTask<Void, Void, String> {
	
	private MainActivity activity;

	/**
	 * 
	 * @param activity
	 */
	public GetDateTask(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected String doInBackground(Void... params) {

		String           format = activity.getString(R.string.app_dateFormat);
		SimpleDateFormat sdf    = new SimpleDateFormat(format);
		Date             date   = new Date();
		
		return sdf.format(date);
	}
	
	@Override
	protected void onPostExecute(String date) {
		activity.onDateAvailable(date);
    }
}
