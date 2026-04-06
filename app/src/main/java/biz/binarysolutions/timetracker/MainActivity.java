package biz.binarysolutions.timetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.color.DynamicColors;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import biz.binarysolutions.timetracker.data.Activity;
import biz.binarysolutions.timetracker.data.ActivityFactory;
import biz.binarysolutions.timetracker.data.DisplayableGroup;
import biz.binarysolutions.timetracker.data.Preferences;
import biz.binarysolutions.timetracker.data.SuggestList;
import biz.binarysolutions.timetracker.data.TotalActivity;
import biz.binarysolutions.timetracker.data.TrackableActivity;
import biz.binarysolutions.timetracker.db.DatabaseAdapter;
import biz.binarysolutions.timetracker.tasks.AddCurrentActivityTask;
import biz.binarysolutions.timetracker.tasks.GetDateTask;
import biz.binarysolutions.timetracker.tasks.UpdateCurrentGroupNameTask;
import biz.binarysolutions.timetracker.tasks.sequencedloads.SequencedLoadsHandler;
import biz.binarysolutions.timetracker.tasks.sequencedloads.SequencedLoadsThread;
import biz.binarysolutions.timetracker.util.DefaultOnItemSelectedListener;
import biz.binarysolutions.timetracker.util.Pair;

/**
 * 
 *
 */
public class MainActivity extends AppCompatActivity {
	
	//private static final int REFRESH_DELAY_MINUTES = 60 * 1000;
	private static final int REFRESH_DELAY_SECONDS = 200;
	
	private final int refreshDelay = REFRESH_DELAY_SECONDS;
	
	
	private Integer currentGroupId = 0;
	private final LinkedHashMap<Integer, View> groups = new LinkedHashMap<>();
	
	private final SuggestList      suggestList       = new SuggestList();
	private final DisplayableGroup currentActivities = new DisplayableGroup();
	
	private TotalActivity     totalActivity   = null;
	private TrackableActivity trackedActivity = null;
	
	private LinearLayout longClickedView = null;
	
	private final Handler updateHandler = new Handler();
	
	/**
	 * 
	 */
	private final Runnable refreshCountersTask = new Runnable() {
		
		@Override
		public void run() {
			
			refreshCounters();
			updateHandler.postDelayed(this, refreshDelay);
		}
	};
	
	/**
	 * 
	 */
	private void refreshAllCounters() {
		
		long time = System.currentTimeMillis() / 1000;
		
		for (TrackableActivity activity : currentActivities.values()) {
			activity.refreshTimer(time);
		}
		
		if (totalActivity != null) {
			totalActivity.refreshTimer(time);
		}
	}
	
	/**
	 * 
	 */
	private void refreshCounters() {
		
		long time = System.currentTimeMillis() / 1000;
		
		trackedActivity.refreshTimer(time);
		totalActivity.refreshTimer(time);
	}

	/**
	 * 
	 * @param activity
	 */
	private void startTracking(TrackableActivity activity) {

		long time = System.currentTimeMillis() / 1000;

		activity.startTracking(time);
		totalActivity.startTracking(time);

		updateHandler.removeCallbacks(refreshCountersTask);
		updateHandler.postDelayed(refreshCountersTask, refreshDelay);
		
		DatabaseAdapter.updateActivity(this, activity);
		DatabaseAdapter.updateTotalActivity(this, totalActivity);
	}
	
	/**
	 * 
	 */
	private void continueTracking() {
		
		updateHandler.removeCallbacks(refreshCountersTask);
		updateHandler.postDelayed(refreshCountersTask, refreshDelay);
	}

	/**
	 * TODO: called always with tracked activity
	 * 
	 * @param activity
	 */
	private void stopTracking(TrackableActivity activity) {

		long time = System.currentTimeMillis() / 1000;

		activity.stopTracking(time);
		totalActivity.stopTracking(time);

		updateHandler.removeCallbacks(refreshCountersTask);
		
		DatabaseAdapter.updateActivity(this, activity);
		DatabaseAdapter.updateTotalActivity(this, totalActivity);
	}

	/**
	 * 
	 */
	private void resetAllTimers() {
		
		if (trackedActivity != null) {
			stopTracking(trackedActivity);
			trackedActivity = null;
		}
		
		for (TrackableActivity activity : currentActivities.values()) {
			activity.resetTimer();
		}
		
		totalActivity.resetTimer();
		
		DatabaseAdapter.resetAllActivities(this);
		DatabaseAdapter.resetTotalActivity(this);
	}

	/**
	 * @param groupId 
	 * @param activity 
	 * 
	 */
	private void addCurrentActivity(Integer groupId, Activity activity) {
		
		final TrackableActivity trackable =
			ActivityFactory.getTrackableActivity(activity, this);
		
		View activityView = trackable.getView();
		registerForContextMenu(activityView);
		activityView.setOnClickListener(view -> {

			if (trackedActivity == null) {

				startTracking(trackable);
				trackedActivity = trackable;

			} else {

				stopTracking(trackedActivity);

				if (view != trackedActivity.getView()) {
					startTracking(trackable);
					trackedActivity = trackable;
				} else {
					trackedActivity = null;
				}
			}
		});
		
		View group = groups.get(groupId);
		if (group != null) {
			LinearLayout parent = group.findViewById(R.id.linearLayoutActivities);
			parent.addView(activityView);
		}

		currentActivities.put(activityView, trackable);
		suggestList.remove(trackable.getName());
		refreshEditTextAdapter();
		
		if (trackable.getStartedAt() > 0) {
			trackedActivity = trackable;
			continueTracking();
		}
	}

	/**
	 * 
	 */
	private void addNewCurrentActivity() {
		
		String activityName = getEditTextString();
		if (activityName.length() > 0) {
			
			if (! currentActivities.containsActivity(activityName)) {
				
				new AddCurrentActivityTask(this)
					.execute(currentGroupId, activityName);
				
				clearEditText();
			}
		}		
	}

	/**
	 * 
	 * @return
	 */
	private String getEditTextString() {

		EditText editText = findViewById(R.id.autoCompleteTextViewActivity);
		String   string   = editText.getText().toString();

		return string.trim();
	}
	
	/**
	 * @param editText
	 */
	private void hideKeyboard(EditText editText) {
		
		InputMethodManager imm = 
			(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * 
	 */
	private void clearEditText() {
		
		EditText editText = findViewById(R.id.autoCompleteTextViewActivity);
		
		editText.setText("");
		editText.clearFocus();
		
		hideKeyboard(editText);
	}

	/**
	 * 
	 */
	private void setEditTextOnKeyListener() {

		AutoCompleteTextView editText = findViewById(R.id.autoCompleteTextViewActivity);
		
		editText.setOnKeyListener((view, keyCode, event) -> {

			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				addNewCurrentActivity();
				return true;
			} else {
				return false;
			}
		});
	}
	
	/**
	 * 
	 */
	private void setButtonListener() {
		
		Button button = findViewById(R.id.ButtonAdd);
		button.setOnClickListener(view -> addNewCurrentActivity());
	}
	
	/**
	 * 
	 */
	private void refreshEditTextAdapter() {
		
		AutoCompleteTextView editText = findViewById(R.id.autoCompleteTextViewActivity);
		
		String[] strings = suggestList.get();
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				this, android.R.layout.simple_dropdown_item_1line, strings);
		
		editText.setAdapter(adapter);
	}

	/**
	 * 
	 * @return
	 */
	private boolean isTrackedActivityLongClicked() {
		
		return 
			trackedActivity != null && 
			longClickedView == trackedActivity.getView();
	}
	
	/**
	 * 
	 */
	private void showToastErrorEditing() {
		
		Toast.makeText(
			this, 
			R.string.InvalidTimeFormat, 
			Toast.LENGTH_LONG
		).show();
	}	
	
	/**
	 * 
	 */
	private void showToastRunningActivity() {
		
		Toast.makeText(
			this, 
			R.string.RunningActivity, 
			Toast.LENGTH_LONG
		).show();
	}
	
	/**
	 * 
	 * @param view 
	 * @param activity
	 */
	private void fillEditTimeDialog(View view, TrackableActivity activity) {
		
		String time = activity.getRunningTime(0);
		
		EditText editText = view.findViewById(R.id.EditTextTime);
		editText.setText(time);
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	private long convertToSeconds(String time) throws Exception {
		
		String[] fragments = time.split(":");
		
		long hours   = Long.parseLong(fragments[0]);
		long minutes = Long.parseLong(fragments[1]);
		long seconds = 0;
		
		if (hours > 1000000) {
			throw new Exception();
		}
		
		if (fragments.length > 2) {
			seconds = Long.parseLong(fragments[2]);
		}
		
		minutes += hours * 60;
		seconds += minutes * 60;
		
		return seconds;
	}
	
	/**
	 * 
	 */
	private void showEditTimeDialog(final TrackableActivity activity) {
		
		if (activity == null) {
			return;
		}
		
		LayoutInflater inflater = getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_edit_time, null);
		
		fillEditTimeDialog(view, activity);
		
		if (isFinishing()) {
			return;
		}
		
		DialogInterface.OnClickListener listener = (dialog, which) -> {

			EditText editText = view.findViewById(R.id.EditTextTime);
			String time = editText.getText().toString();

			try {
				long seconds    = convertToSeconds(time);
				long difference = seconds - activity.getAccumulated();
				long newTotal   = totalActivity.getAccumulated() + difference;

				activity.setAccumulated(seconds);
				totalActivity.setAccumulated(newTotal);

				DatabaseAdapter.updateActivity(MainActivity.this, activity);
				DatabaseAdapter.updateTotalActivity(MainActivity.this, totalActivity);

				refreshAllCounters();

			} catch (Exception e) {
				showToastErrorEditing();
			}
		};
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.EditTime)
			.setView(view)
			.setPositiveButton(android.R.string.ok, listener)
			.setNegativeButton(android.R.string.cancel, null)
			.show();		
	}
	
	/**
	 * 
	 */
	private void editTime() {
		
		if (longClickedView == null) {
			return;
		}
		
		if (isTrackedActivityLongClicked()) {
			showToastRunningActivity();
			return;
		}
		
		TrackableActivity activity = currentActivities.get(longClickedView);
		showEditTimeDialog(activity);
		
		longClickedView = null;
	}
	
	/**
	 * 
	 */
	private void removeActivityFromList() {
		
		if (longClickedView != null) {
			
			if (isTrackedActivityLongClicked()) {
				showToastRunningActivity();
				return;
			}
		
			ViewParent parent = longClickedView.getParent();
			if (parent instanceof LinearLayout linearLayout) {
				linearLayout.removeView(longClickedView);
			}

			String name = currentActivities.getActivityName(longClickedView);
			suggestList.add(name);
			refreshEditTextAdapter();
			
			currentActivities.remove(longClickedView);
			DatabaseAdapter.deleteCurrentActivity(this, name);

			longClickedView = null;
		}
	}

	/**
	 * 
	 */
	private void deleteActivityPermanently() {
		
		if (longClickedView != null) {
			
			if (isTrackedActivityLongClicked()) {
				showToastRunningActivity();
				return;
			}			
			
			String name = currentActivities.getActivityName(longClickedView);
			DatabaseAdapter.deleteActivity(this, name);
			
			removeActivityFromList();
			
			suggestList.remove(name); //TODO: redundant?
			refreshEditTextAdapter();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DynamicColors.applyToActivityIfAvailable(this);
	    setContentView(R.layout.main);
	    
	    new GetDateTask(this).execute();
	    
	    Handler handler = new SequencedLoadsHandler(this);
	    new SequencedLoadsThread(this, handler).start();
	    
	    setEditTextOnKeyListener();
	    setButtonListener();

		new FlavorSpecific(this).initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Preferences.load(this);
		refreshAllCounters();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    return true;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.optionsMenuItemResetAll) {
			resetAllTimers();
			return true;
		} else if (itemId == R.id.optionsMenuItemSettings) {
			startActivity(new Intent(this, PreferencesActivity.class));
			return true;
		}

		/*
	    case R.id.optionsMenuItemAbout:
	    	// implement this
	    	return true;
		*/

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu
		(
				ContextMenu     menu, 
				View            view,
				ContextMenuInfo menuInfo
		) {
		super.onCreateContextMenu(menu, view, menuInfo);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		
		if (view instanceof LinearLayout) {
			longClickedView = (LinearLayout) view;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.contextMenuItemEditTime) {
			editTime();
			return true;
		} else if (itemId == R.id.contextMenuItemRemoveFromList) {
			removeActivityFromList();
			return true;
		} else if (itemId == R.id.contextMenuItemDeletePermanently) {
			deleteActivityPermanently();
			return true;
		}

		return super.onContextItemSelected(item);
	}

	/**
	 * 
	 */
	public void onDateAvailable(String date) {
		TextView view = findViewById(R.id.textViewDate);
		view.setText(date);
	}

	/**
	 * 
	 */
	public void onSuggestionsAvailable(ArrayList<String> suggestions) {
		suggestList.set(suggestions);
		refreshEditTextAdapter();
	}

	/**
	 * 
	 * @param names
	 */
	public void onGroupNamesAvailable(ArrayList<String> names) {
		
		Spinner spinner = findViewById(R.id.spinnerActivityGroups);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
	    
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    spinner.setOnItemSelectedListener(new DefaultOnItemSelectedListener() {

			@Override
			protected void onItemSelected(int position) {
				
				//TODO: duplicate code
				currentGroupId = position;
				View view = groups.get(currentGroupId);
				FrameLayout layout = findViewById(R.id.frameLayoutGroups);
				layout.removeAllViews();
				layout.addView(view);
				
				new UpdateCurrentGroupNameTask(MainActivity.this)
						.execute(currentGroupId);
			}
	    });

	    for (int i = 0, length = names.size(); i < length; i++) {
	    	
	    	View view = View.inflate(this, R.layout.activities_layout, null);
			groups.put(i, view);
		}
	}

	/**
	 * 
	 * @param position
	 */
	public void onCurrentGroupPositionAvailable(int position) {
	
		Spinner spinner = findViewById(R.id.spinnerActivityGroups);
		spinner.setSelection(position);
		
		//TODO: duplicate code
		currentGroupId = position;
		View view = groups.get(currentGroupId);
		FrameLayout layout = findViewById(R.id.frameLayoutGroups);
		layout.removeAllViews();
		layout.addView(view);
	}

	/**
	 * @param activities 
	 * 
	 */
	public void onCurrentActivitiesAvailable
		(
				ArrayList<Pair<Integer, Activity>> activities
		) {
		
		for (Pair<Integer, Activity> activity : activities) {
			addCurrentActivity(activity.first, activity.second);
		}
	}

	/**
	 * 
	 * @param activity
	 */
	public void onTotalActivityAvailable(Activity activity) {
		totalActivity = ActivityFactory.getTotalActivity(activity, this);
	}

	/**
	 * 
	 * @param activity
	 */
	public void onCurrentActivityAvailable(Activity activity) {
		addCurrentActivity(currentGroupId, activity);
	}	
}