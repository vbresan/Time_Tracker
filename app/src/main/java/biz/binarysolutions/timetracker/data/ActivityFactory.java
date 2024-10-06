package biz.binarysolutions.timetracker.data;


/**
 * 
 *
 */
public class ActivityFactory {

	/**
	 * 
	 * @param activity
	 * @param appActivity
	 */
	private static void setView
		(
				DisplayableActivity  activity, 
				android.app.Activity appActivity
		) {
		
		long time = System.currentTimeMillis() / 1000;
		
		activity.setView(appActivity);
		activity.refreshTimer(time);
	}

	/**
	 * 
	 * @param activity
	 * @param mainActivity
	 * @return
	 */
	public static TrackableActivity getTrackableActivity
		(
				Activity             activity,
				android.app.Activity mainActivity
				
		) {
		
		TrackableActivity trackable = new TrackableActivity(activity);
		setView(trackable, mainActivity);

		return trackable;
	}

	/**
	 * 
	 * @param activity
	 * @param mainActivity
	 * @return
	 */
	public static TotalActivity getTotalActivity
		(
				Activity             activity, 
				android.app.Activity mainActivity
		) {
		
		TotalActivity total = new TotalActivity(activity); 
		setView(total, mainActivity);
		
		return total; 
	}
}
