package biz.binarysolutions.timetracker.tasks.sequencedloads;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import biz.binarysolutions.timetracker.MainActivity;
import biz.binarysolutions.timetracker.R;
import biz.binarysolutions.timetracker.data.Activity;
import biz.binarysolutions.timetracker.util.Pair;

/**
 * 
 *
 */
public class SequencedLoadsHandler extends Handler {
	
	private MainActivity mainActivity;
	private String       packageName;
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private int getInt(Bundle data) {
		
		String key = packageName + mainActivity.getString(R.string.app_bundle_serializable);
		return ((Integer) data.getSerializable(key)).intValue();
	}	
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private ArrayList<String> getStrings(Bundle data) {
		
		String key = packageName + mainActivity.getString(R.string.app_bundle_serializable);
		return (ArrayList<String>) data.getSerializable(key);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private Activity getActivity(Bundle data) {
		
		String key = packageName + mainActivity.getString(R.string.app_bundle_serializable);
		return (Activity) data.getSerializable(key);
	}	

	/**
	 * 
	 * @param data
	 * @return
	 */
	private ArrayList<Pair<Integer, Activity>> getActivities(Bundle data) {

		String key = packageName + mainActivity.getString(R.string.app_bundle_serializable);
		return (ArrayList<Pair<Integer, Activity>>) data.getSerializable(key);
	}

	/**
	 * 
	 * @param data
	 */
	private void onSuggestionsAvailable(Bundle data) {

		ArrayList<String> strings = getStrings(data); 
		mainActivity.onSuggestionsAvailable(strings);
	}
	
	/**
	 * 
	 * @param data
	 */
	private void onActivityGroupNamesAvailable(Bundle data) {

		ArrayList<String> strings = getStrings(data); 
		mainActivity.onGroupNamesAvailable(strings);
	}
	
	/**
	 * 
	 * @param data
	 */
	private void onCurrentGroupPositionAvailable(Bundle data) {

		int position = getInt(data); 
		mainActivity.onCurrentGroupPositionAvailable(position);
	}	
	
	/**
	 * 
	 * @param data
	 */
	private void onCurrentActivitiesAvailable(Bundle data) {
		
		ArrayList<Pair<Integer, Activity>> activities = getActivities(data); 
		mainActivity.onCurrentActivitiesAvailable(activities);
	}
	
	/**
	 * 
	 * @param data
	 */
	private void onTotalActivityAvailable(Bundle data) {
		
		Activity activity = getActivity(data); 
		mainActivity.onTotalActivityAvailable(activity);
	}	

	/**
	 * 
	 * @param activity
	 */
	public SequencedLoadsHandler(MainActivity mainActivity) {
		super();
		
		this.mainActivity = mainActivity;
		this.packageName  = mainActivity.getPackageName();
	}

	@Override
	public void handleMessage(Message message) {
		
		switch (message.what) {
		
		case MessageCode.SUGGEST_LIST:
			onSuggestionsAvailable(message.getData());
			break;
			
		case MessageCode.GROUP_NAMES:
			onActivityGroupNamesAvailable(message.getData());
			break;
			
		case MessageCode.CURRENT_GROUP_POSITION:
			onCurrentGroupPositionAvailable(message.getData());
			break;
			
		case MessageCode.CURRENT_ACTIVITIES:
			onCurrentActivitiesAvailable(message.getData());
			break;
			
		case MessageCode.TOTAL_ACTIVITY:
			onTotalActivityAvailable(message.getData());
			break;

		default:
			break;
		}
	}
}
