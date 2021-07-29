package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Car implements ClockTickListener{
	private static final int METERS_BETWEEN_LIGHTS = 1000;
	private static final int METERS_PER_PIXEL = 5;
	private ReentrantReadWriteLock rwl;
	//TODO:: private TrafficLight currentLight;
	private int xPosition;		//pixels
	private int yPosition;		//pixels
	private int width;			//pixels
	private int height;			//pixels
	private int speed;			//speed is in m/sec
	
	public Car(int x, int y){
		this.rwl = new ReentrantReadWriteLock();
		this.xPosition = x;
		this.yPosition = y;
		this.width = 40;
		this.height = 40;
		this.speed = 100;
		
	}
	
	
	
	public int getxPosition() {
		return xPosition;
	}
	
	public void setxPosition(int xPosition) {
		rwl.writeLock().lock();
		try {
			this.xPosition = xPosition;
		} finally{
			rwl.writeLock().unlock();
		}
	}
	public int getyPosition() {
		return yPosition;
	}
	
	public void setyPosition(int yPosition) {
		rwl.writeLock().lock();
		try {
			this.yPosition = yPosition;
		} finally{
			rwl.writeLock().unlock();
		}
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		rwl.writeLock().lock();
		try {
			this.speed = speed;
		} finally{
			rwl.writeLock().unlock();
		}
	}

	public void stopForRed(Boolean isRed) {
		if(isRed) {
			this.setSpeed(0);
		} else {
			this.setSpeed(100);
		}
	}
	
	public Boolean isStopped() {
		return(speed == 0);
	}
	
	public int getWidth() {
		return this.width;
	}
	public void paintMe(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(this.xPosition, this.yPosition, this.width, this.height);
	}
	
	public void clockTickNotify(int elapsedSeconds) {
		int distanceMeters = speed * elapsedSeconds;
		int carTraveled = distanceMeters / METERS_PER_PIXEL;
		int rVal = this.getxPosition() + carTraveled;
		this.setxPosition(rVal);
	}
}
