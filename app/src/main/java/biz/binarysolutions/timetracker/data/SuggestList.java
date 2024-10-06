package biz.binarysolutions.timetracker.data;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 *
 */
public class SuggestList {

	private ArrayList<String> suggestions = new ArrayList<String>();

	/**
	 * 
	 * @param suggestions
	 */
	public void set(ArrayList<String> suggestions) {
		this.suggestions = suggestions;
		
		Collections.sort(this.suggestions);
	}

	/**
	 * 
	 * @return
	 */
	public String[] get() {
		return suggestions.toArray(new String[0]);
	}

	/**
	 * 
	 * @param string
	 */
	public void remove(String string) {
		suggestions.remove(string);
	}

	/**
	 * 
	 * @param string
	 */
	public void add(String string) {
		suggestions.add(string);
		Collections.sort(this.suggestions);
	}
}
