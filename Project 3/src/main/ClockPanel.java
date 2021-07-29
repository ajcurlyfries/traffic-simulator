package main;

import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClockPanel extends JPanel 
implements ClockTickListener {
	private ReentrantReadWriteLock rwl;
	private String timeString;
	private SharedData dataPool;
	private JLabel timeLabel;
	
	public ClockPanel(){
		super();
		this.rwl = new ReentrantReadWriteLock();
		timeString = "0: ";
		timeLabel = new JLabel(timeString);
		this.add(timeLabel);
	}
	
	public void setSharedData(SharedData data) {
		this.dataPool = data;
	}
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
	}
	
	public String getTimeString() {
		return timeString;
	}
	
	public void setTimeString(String aString) {
		rwl.writeLock().lock();
		try{
			timeString = aString;
		} finally {
			rwl.writeLock().unlock();
		}
	}

	
	
	public void clockTickNotify(int elapsedSeconds) {
		String tempString = this.dataPool.clockTickToString();
		this.setTimeString(tempString);
		timeLabel.setText(timeString);
		timeLabel.paintImmediately(getVisibleRect());
		return;
	}
}
