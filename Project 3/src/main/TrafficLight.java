package main;
/**
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ajzun
 *
 */
public class TrafficLight 
implements ClockTickListener {
	private final int GREEN_SECS = 10;
	private final int YELLOW_SECS = 15;
	private final int RED_SECS = 25;
	private ReentrantReadWriteLock rwl;
	private Boolean isRed;
	private int xPosition, yPosition, timer;
	private int radius = 35;
	private String yellow = "yellow";
	private String red = "red";
	private String green = "green";
	private String lightColor;
	
	public TrafficLight(int x, int y) {
		this.xPosition = x;
		this.yPosition = y;
		this.timer = 0;
		this.isRed = false;
		this.lightColor = green;
		this.rwl = new ReentrantReadWriteLock();
	}
	
	
	public String getLightColor() {
		return this.lightColor;
	}
	
	public Boolean isRed() {
		return this.isRed;
	}

	public void setLightColor(String lightColor) {
		rwl.writeLock().lock();
		try {
			this.lightColor = lightColor;
		} finally {
			rwl.writeLock().unlock();
		}
	}
	
	public void paintMe(Graphics g) {
		if(lightColor.contentEquals(green)) {
			g.setColor(Color.GREEN);
		}
		if(lightColor.contentEquals(yellow)) {
			g.setColor(Color.YELLOW);
		}
		if(lightColor.contentEquals(red)) {
			g.setColor(Color.RED);
		}
		g.fillOval(this.xPosition, this.yPosition, this.radius, this.radius);
	}
	
	public int getxPosition() {
		return xPosition;
	}
	
	public void clockTickNotify(int elapsedSeconds) {
		timer += elapsedSeconds;
		String currentColor = this.lightColor;
		String nextColor;
		if (currentColor.equals(green)) {
			nextColor = yellow;
			if( timer == GREEN_SECS ) {
				this.setLightColor(nextColor);
			}
		}
		if (currentColor.equals(yellow)) {
			nextColor = red;
			if( timer == YELLOW_SECS ) {
				this.setLightColor(nextColor);
				this.isRed = true;
			}
		}
		if (currentColor.equals(red)) {
			nextColor = green;
			if( timer == RED_SECS ) {
				this.setLightColor(nextColor);
				isRed = false;
				timer = 0;
				return;
			}
		}
		
	}

	
}
