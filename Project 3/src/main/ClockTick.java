package main;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClockTick extends Thread{
	private static final long CLOCK_TICK_MSECS = 1000;
	private static final int CLOCK_TICK_UPDATE_SECS = 1;
	
	private ReentrantReadWriteLock rwl;
	private SharedData sharedData;
	private Boolean shutDownFlag, pauseFlag;
	private int currentHours, currentMinutes, currentSeconds;
	private ArrayList<ClockTickListener> listenerList;
	
	public ClockTick(SharedData data) {
		this.sharedData = data;
		this.rwl = new ReentrantReadWriteLock();
		this.shutDownFlag = false;
		this.pauseFlag = false;
		this.listenerList = new ArrayList<ClockTickListener>();
		this.currentHours = 0;
		this.currentMinutes = 0;
		this.currentSeconds = 0;
		
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("Clock Tick");
		do {
			//wait until tick period has elapsed
			try {
				Thread.sleep(CLOCK_TICK_MSECS);
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
			if(pauseFlag) {
				continue;
			}
			//update local time
			rwl.writeLock().lock();
			try {
				currentSeconds += CLOCK_TICK_UPDATE_SECS;
				if( currentSeconds > 59 ) {
					currentSeconds -= 60;
					currentMinutes++;
					if( currentMinutes > 59 ) {
						currentMinutes -= 60;
						currentHours++;
					}
				}
			} finally {
				rwl.writeLock().unlock();
			}
			
			//update shared time
			sharedData.clockTickUpdate(currentHours, currentMinutes, currentSeconds);
			
			//notify listerList
			rwl.readLock().lock();
			try {
				for(ClockTickListener lTemp: listenerList) {
					lTemp.clockTickNotify(CLOCK_TICK_UPDATE_SECS);
				}
			} finally {
				rwl.readLock().unlock();
			}
			
		} while(!shutDownFlag);
	}
	
	public void register(ClockTickListener listener) {
		this.listenerList.add(listener);
	}
	
	public void startUp() {
		this.shutDownFlag = false;
		this.start();
	}
	
	public void shutDown() {
		this.shutDownFlag = true;
	}
	
	public void pause() {
		pauseFlag = true;
	}
	
	public void resumeClock() {
		pauseFlag = false;
	}
}
