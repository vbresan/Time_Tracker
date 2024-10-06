package biz.binarysolutions.timetracker.data;

import android.view.View;

/**
 * 
 *
 */
@SuppressWarnings("serial")
abstract class DisplayableActivity extends Activity {
	
	//TODO: make private
	protected View view;
	
	/**
	 * 
	 * @param name 
	 * @param accumulated
	 * @param startedAt
	 */
	protected DisplayableActivity(String name, long accumulated, long startedAt) {
		super(name, accumulated, startedAt);
	}

	/**
	 * 
	 * @param activity
	 */
	public DisplayableActivity(Activity activity) {
		super(activity);
	}

	/**
	 * 
	 */
	abstract void setView(android.app.Activity activity);
	
	/**
	 * 
	 * @param time
	 */
	abstract void refreshTimer(long time);
	
	/**
	 * 
	 */
	@Override
	public void resetTimer() {
		super.resetTimer();
		refreshTimer(0);
	}
}
