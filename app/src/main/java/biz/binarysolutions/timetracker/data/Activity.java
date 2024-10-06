package biz.binarysolutions.timetracker.data;

import java.io.Serializable;


/**
 * 
 *
 */
public class Activity implements Serializable {

	private static final long serialVersionUID = -1734560759729264782L;
	
	private String name;
	private long   accumulated = 0;
	private long   startedAt   = 0;

	/**
	 * 
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 */
	private static String getFormattedTime
		(
			long hours, 
			long minutes,
			long seconds
		) {

		return String.format(
			"%02d:%02d:%02d", 
			Long.valueOf(hours), 
			Long.valueOf(minutes), 
			Long.valueOf(seconds)
		);
	}

	/**
	 * 
	 * @param hours
	 * @param minutes
	 * @return
	 */
	private static String getFormattedTime(long hours, long minutes) {
		
		return String.format(
			"%02d:%02d", 
			Long.valueOf(hours), 
			Long.valueOf(minutes)
		);
	}

	/**
	 * 
	 * @param totalSeconds
	 * @return
	 */
	private static String getFormattedTime(long totalSeconds) {
	
		long seconds = totalSeconds % 60;
		long minutes = totalSeconds / 60;
		long hours   = minutes / 60;
		minutes = minutes % 60;
		
		String formatted; 
		if (Preferences.showSeconds()) {
			formatted = getFormattedTime(hours, minutes, seconds); 
		} else {
			formatted = getFormattedTime(hours, minutes);
		}
			
		return formatted;
	}

	/**
	 * 
	 * @param name
	 * @param accumulated
	 * @param startedAt
	 */
	public Activity(String name, long accumulated, long startedAt) {
		
		this.name        = name;
		this.accumulated = accumulated;
		this.startedAt   = startedAt;
	}
	
	/**
	 * 
	 * @param other
	 */
	Activity(Activity other) {
		this("", 0, 0);
		
		if (other != null) {
			
			this.name        = other.name;
			this.accumulated = other.accumulated;
			this.startedAt   = other.startedAt;
		}
	}
	
	/**
	 * 
	 * @param time
	 */
	protected void startTracking(long time) {
		startedAt = time;
	}

	/**
	 * 
	 * @param time
	 */
	protected void stopTracking(long time) {
		accumulated += time - startedAt;
		startedAt = 0;
	}
	
	/**
	 * 
	 */
	protected void resetTimer() {
		accumulated = 0;
		startedAt   = 0;
	}
	
	/**
	 * 
	 * @param name
	 */
	protected void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param startedAt
	 */
	protected void setStartedAt(long startedAt) {
		this.startedAt = startedAt;
	}

	/**
	 * 
	 * @param accumulated
	 */
	public void setAccumulated(long accumulated) {
		this.accumulated = accumulated;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public long getAccumulated() {
		return accumulated;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getStartedAt() {
		return startedAt;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public String getRunningTime(long time) {
		
		long seconds = accumulated;
		
		if (startedAt > 0) {
			seconds += time - startedAt;
		}
		
		return getFormattedTime(seconds);
	}	
}
