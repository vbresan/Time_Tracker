package biz.binarysolutions.timetracker.data;

import android.widget.TextView;

import com.google.android.material.color.MaterialColors;

import biz.binarysolutions.timetracker.R;

/**
 * 
 *
 */
public class TotalActivity extends DisplayableActivity {

	private TextView textViewCounter;

	/**
	 *
	 * @param isTracking
	 * @return
	 */
	private int getColorResource(boolean isTracking) {

		if (isTracking) {
			return androidx.appcompat.R.attr.colorError;
		} else {
			return com.google.android.material.R.attr.colorOnSurface;
		}
	}

	/**
	 * 
	 * @param isTracking
	 */
	private void setTrackingColor(boolean isTracking) {

		if (textViewCounter == null) {
			return;
		}

		int resource = getColorResource(isTracking);
		int color    = MaterialColors.getColor(textViewCounter, resource);

		textViewCounter.setTextColor(color);
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
		textViewCounter = activity.findViewById(R.id.textViewTotalCounter);

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

		if (textViewCounter == null) {
			return;
		}
		
		String text = getRunningTime(time);
		textViewCounter.setText(text);
	}
}
