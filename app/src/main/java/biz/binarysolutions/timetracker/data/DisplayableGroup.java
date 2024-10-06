package biz.binarysolutions.timetracker.data;

import java.util.Collection;
import java.util.LinkedHashMap;

import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 *
 */
@SuppressWarnings("serial")
public class DisplayableGroup extends LinkedHashMap<View, TrackableActivity> {

	/**
	 * 
	 * @param activityName
	 * @return
	 */
	public boolean containsActivity(String activityName) {
		
		Collection<TrackableActivity> activities = values();
		for (TrackableActivity activity : activities) {
			if (activityName.equals(activity.getName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param view
	 * @return
	 */
	public String getActivityName(LinearLayout view) {
		
		TrackableActivity activity = get(view);
		return activity.getName();
	}
}
