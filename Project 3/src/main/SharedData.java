package main;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SharedData {
	//ClockTick variables
	private ReentrantReadWriteLock clockTickLock;
	private int clockHours, clockMinutes, clockSeconds;
	private Boolean clockShutDown, clockPause;
	
	public SharedData() {
		clockTickLock = new ReentrantReadWriteLock();
	}
	public void clockTickUpdate(int hours, int minutes, int seconds){
		clockTickLock.writeLock().lock();
		try {
			this.clockHours = hours;
			this.clockMinutes = minutes;
			this.clockSeconds = seconds;
		} finally {
			clockTickLock.writeLock().unlock();
		}
	}
	
	public String clockTickToString() {
		String rVal;
		clockTickLock.readLock().lock();
		try {
			rVal = Integer.toString(clockHours) + " : " + Integer.toString(clockMinutes) + " : " + Integer.toString(clockSeconds);
		} finally {
			clockTickLock.readLock().unlock();
		}
		return rVal;
	}
}
