package biz.binarysolutions.timetracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.color.DynamicColors;

import biz.binarysolutions.timetracker.preferences.PreferenceFragment;

/**
 * 
 *
 */
public class PreferencesActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DynamicColors.applyToActivityIfAvailable(this);

		getSupportFragmentManager()
			.beginTransaction()
			.replace(android.R.id.content, new PreferenceFragment())
			.commit();
	}
}
