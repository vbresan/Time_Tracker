package biz.binarysolutions.timetracker.tasks.sequencedloads;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import biz.binarysolutions.timetracker.MainActivity;
import biz.binarysolutions.timetracker.R;
import biz.binarysolutions.timetracker.data.Activity;
import biz.binarysolutions.timetracker.db.DatabaseAdapter;
import biz.binarysolutions.timetracker.util.Pair;

/**
 * 
 *
 */
public class SequencedLoadsThread extends Thread {
	
	private MainActivity mainActivity;
	private Handler      handler;
	
	private String packageName;
	
	/**
	 * 
	 * @param serializable
	 * @return
	 */
	private Bundle getBundle(Serializable serializable) {
		
		Bundle bundle = new Bundle();
		String key    = packageName + mainActivity.getString(R.string.app_bundle_serializable); 
		
		bundle.putSerializable(key, serializable);
		
		return bundle;
	}
	
	/**
	 * 
	 * @param serializable
	 * @param messageCode
	 */
	private void send(Serializable serializable, int messageCode) {
		
		Bundle  bundle  = getBundle(serializable);
		Message message = Message.obtain();
		
		message.what = messageCode;
		message.setData(bundle);
		
		handler.sendMessage(message);	
	}

	/**
	 * 
	 */
	private void loadSuggestList() {
		
		ArrayList<String> strings = DatabaseAdapter.getActivityNames(mainActivity);
		int messageCode = MessageCode.SUGGEST_LIST;
		
		send(strings, messageCode);
	}
	
	/**
	 * 
	 */
	private void loadGroupNames() {
		
		ArrayList<String> strings = DatabaseAdapter.getGroupNames(mainActivity);
		int messageCode = MessageCode.GROUP_NAMES;
		
		send(strings, messageCode);
	}
	
	/**
	 * 
	 */
	private void loadCurrentGroupPosition() {
		
		int position = DatabaseAdapter.getCurrentGroupId(mainActivity);
		int messageCode = MessageCode.CURRENT_GROUP_POSITION;
		
		send(new Integer(position), messageCode);
	}
	
	/**
	 * 
	 */
	private void loadCurrentActivities() {
		
		ArrayList<Pair<Integer, String>> names = 
			DatabaseAdapter.getCurrentActivityNames(mainActivity);
		
		ArrayList<Pair<Integer, Activity>> activities = 
			new ArrayList<Pair<Integer, Activity>>();
		
		for (Pair<Integer, String> name : names) {
			
			Activity activity = 
				DatabaseAdapter.getActivity(mainActivity, name.second);
			
			activities.add(new Pair<Integer, Activity>(name.first, activity));
		}
		
		send(activities, MessageCode.CURRENT_ACTIVITIES);
	}
	
	/**
	 * 
	 */
	private void loadTotalActivity() {
		
		Activity activity = DatabaseAdapter.getTotalActivity(mainActivity);
		if (activity == null) {
			
			activity = new Activity("Total", 0, 0);
			DatabaseAdapter.saveTotalActivity(mainActivity, activity);
		}
		
		int messageCode = MessageCode.TOTAL_ACTIVITY;
		
		send(activity, messageCode);
	}	

	/**
	 * 
	 * @param mainActivity 
	 * @param handler
	 */
	public SequencedLoadsThread(MainActivity mainActivity, Handler handler) {
		
		this.mainActivity = mainActivity;
		this.handler      = handler;
		
		this.packageName  = mainActivity.getPackageName();
	}

	@Override
	public void run() {

		loadSuggestList();
			
		loadGroupNames();
		loadCurrentGroupPosition();
		loadCurrentActivities();
		loadTotalActivity();
	}
}
