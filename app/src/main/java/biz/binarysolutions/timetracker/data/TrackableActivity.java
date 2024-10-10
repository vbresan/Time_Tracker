package biz.binarysolutions.timetracker.data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import biz.binarysolutions.timetracker.R;

/**
 * 
 *
 */
public class TrackableActivity extends DisplayableActivity {
	
	/**
	 * 
	 * @param visible
	 */
	private void setHourglassVisible(boolean visible) {
		
		ImageView imageView = (ImageView) view.findViewById(R.id.imageViewHourGlass);
		if (imageView != null) {
			
			int visibility = visible ? View.VISIBLE : View.INVISIBLE;
			imageView.setVisibility(visibility);
		}
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

		TextView textView = view.findViewById(R.id.textViewName);
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
		
		TextView textView = (TextView) view.findViewById(R.id.textViewCounter);
		textView.setText(text);
	}

}
