package biz.binarysolutions.timetracker.data;

import android.content.res.Resources;
import android.widget.TextView;
import biz.binarysolutions.timetracker.R;

/**
 * 
 *
 */
@SuppressWarnings("serial")
public class TotalActivity extends DisplayableActivity {
	
	/**
	 * 
	 * @param isTracking
	 */
	private void setTrackingColor(boolean isTracking) {
		
		Resources resources = view.getContext().getResources();
		int color;
		if (isTracking) {
			color = resources.getColor(R.color.Red);
		} else {
			color = resources.getColor(R.color.BlackColor);
		}

		TextView textView = (TextView) view.findViewById(R.id.TextViewCounter);
		textView.setTextColor(color);
	}
	
	/**
	 * 
	 * @param name
	 * @param accumulated
	 * @param startedAt
	 */
	protected TotalActivity(String name, long accumulated, long startedAt) {
		super(name, accumulated, startedAt);
	}

	/**
	 * 
	 * @param activity
	 */
	public TotalActivity(Activity activity) {
		super(activity);
	}

	@Override
	void setView(android.app.Activity activity) {
		view = activity.findViewById(R.id.LinearLayoutTotalActivity);
		
		if (getStartedAt() > 0) {
			setTrackingColor(true);
		}
	}

	@Override
	public void startTracking(long time) {
		super.startTracking(time);
		setTrackingColor(true);
	}
	
	@Override
	public void stopTracking(long time) {
		super.stopTracking(time);
		setTrackingColor(false);
		refreshTimer(time);
	}	

	@Override
	public void refreshTimer(long time) {
		
		String text = getRunningTime(time); 
		
		TextView textView = (TextView) view.findViewById(R.id.TextViewCounter);
		textView.setText(text);		
	}
}
