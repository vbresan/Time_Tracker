package biz.binarysolutions.timetracker.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 
 *
 */
public abstract class DefaultOnItemSelectedListener 
	implements OnItemSelectedListener {

	/**
	 * 
	 * @param parent
	 * @param pos
	 */
	protected abstract void onItemSelected(int position);

	@Override
	public void onItemSelected
		(
				AdapterView<?> parent, 
				View           view, 
				int            pos, 
				long           id
		) {
		onItemSelected(pos);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// do nothing
	}
}
