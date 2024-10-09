package biz.binarysolutions.timetracker.preferences;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import biz.binarysolutions.timetracker.R;

/**
 *
 */
public class PreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences
        (
            @Nullable Bundle savedInstanceState,
            @Nullable String rootKey
        ) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
