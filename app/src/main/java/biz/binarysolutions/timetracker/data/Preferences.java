package biz.binarysolutions.timetracker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import biz.binarysolutions.timetracker.R;

/**
 * 
 *
 */
public class Preferences {

	private static boolean showSeconds;

	/**
	 * 
	 */
	public static void load(Context context) {

		SharedPreferences preferences = 
			PreferenceManager.getDefaultSharedPreferences(context);	
		
		String key = context.getString(
			R.string.preferences_showSeconds_key
		);
		
		String defaultValue = context.getString(
			R.string.preferences_showSeconds_default_value
		);
		
		showSeconds = preferences.getBoolean(
			key, 
			Boolean.getBoolean(defaultValue)
		);
	}

	/**
	 * 
	 * @return
	 */
	public static boolean showSeconds() {
		return showSeconds;
	}
}
