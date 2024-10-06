package biz.binarysolutions.timetracker.data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import biz.binarysolutions.timetracker.R;

/**
 * 
 *
 */
@SuppressWarnings("serial")
public class TrackableActivity extends DisplayableActivity {
	
	/**
	 * 
	 * @param visible
	 */
	private void setHourglassVisible(boolean visible) {
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ImageViewHourGlass);
		if (imageView != null) {
			
			int visibility = visible ? View.VISIBLE : View.INVISIBLE;
			imageView.setVisibility(visibility);
		}
	}

	/**
	 * 
	 * @param name
	 * @param accumulated
	 * @param startedAt
	 */
	protected TrackableActivity(String name, long accumulated, long startedAt) {
		super(name, accumulated, startedAt);
	}

	/**
	 * 
	 * @param activity
	 */
	public TrackableActivity(Activity activity) {
		super(activity);
	}

	/**
	 * 
	 * @param appActivity
	 */
	@Override
	void setView(android.app.Activity appActivity) {
		
		view = View.inflate(appActivity, R.layout.activity, null);
		
		TextView textView = (TextView) view.findViewById(R.id.TextViewName);
		textView.setText(super.getName());
		
		if (getStartedAt() > 0) {
			setHourglassVisible(true);
		}
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * 
	 * @return
	 */
	public View getView() {
		return view;
	}

	/**
	 * @param time 
	 * 
	 */
	@Override
	public void startTracking(long time) {
		super.startTracking(time);
		setHourglassVisible(true);
	}

	/**
	 * @param time 
	 * 
	 */
	@Override
	public void stopTracking(long time) {
		super.stopTracking(time);
		setHourglassVisible(false);
		
		refreshTimer(time);
	}

	/**
	 * 
	 * @param time
	 */
	@Override
	public void refreshTimer(long time) {
		
		String text = getRunningTime(time); 
		
		TextView textView = (TextView) view.findViewById(R.id.TextViewCounter);
		textView.setText(text);
	}

}
