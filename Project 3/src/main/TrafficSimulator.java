//File: .java
//Name: Andorra Zuniga
//Date: May 5th, 2020
//Purpose:

package main;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class TrafficSimulator implements ActionListener, Runnable{
	static Boolean running = false;
	SharedData dataPool;
	Thread roadThread;
	ClockTick clockTickObject;
	TrafficLight light1, light2, light3;
	int lastLightX = 635; 
	JFrame app = new JFrame("Traffic Simulator App");
	Road road = new Road();
	//south
	ClockPanel myClock;
	JButton start = new JButton("Start");
	JButton pause = new JButton("Pause");
	JButton resume = new JButton("Resume");
	JButton stop = new JButton("Stop");
	Container south = new Container();
	//west
	JButton car = new JButton("Add Car");
	JButton light = new JButton("Add Intersection");
	Container west = new Container();
	
	public TrafficSimulator() {
		dataPool = new SharedData();
		//clock set up
		clockTickObject = new ClockTick(dataPool);
		myClock = new ClockPanel();
		myClock.setSharedData(dataPool);
		clockTickObject.register(myClock);
		app.repaint();
		
		app.setSize( 1000, 550 );
		app.setLayout(new BorderLayout());
		app.add(road, BorderLayout.CENTER);
		
		south.setLayout(new GridLayout(1,2));
		south.add(myClock);
		south.add(start);
		start.addActionListener(this);
		south.add(pause);
		pause.addActionListener(this);
		south.add(resume);
		resume.addActionListener(this);
		south.add(stop);
		stop.addActionListener(this);
		app.add(south, BorderLayout.SOUTH);
		
		west.setLayout(new GridLayout(2,1));
		west.add(car);
		west.add(light);
		car.addActionListener(this);
		light.addActionListener(this);
		app.add(west, BorderLayout.WEST);
		
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
		
		//clock set up
		myClock = new ClockPanel();
		app.repaint();
		
		//adding 3 lights
		light1 = new TrafficLight(165, 225);
		light2 = new TrafficLight(400, 225);
		light3 = new TrafficLight(635, 225);
		road.addLight(light1);
		road.addLight(light2);
		road.addLight(light3);
		clockTickObject.register(light1);
		clockTickObject.register(light2);
		clockTickObject.register(light3);
		
		clockTickObject.register(road);
		app.repaint();
		
	}
	
	public static void main(String[] args) {
		TrafficSimulator simulator = new TrafficSimulator();
		do {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				System.out.println("Main thread exception");
			}
		}while(running);
	}

	//********************Button Actions****************************
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(start)) {
			//start clockTick
			clockTickObject.startUp();
			//start road thread if not already running
			if(!running) {
				running = true;
				roadThread = new Thread(this);
				roadThread.start();
				System.out.println("Main thread started");
			}
		
		}
		if(arg0.getSource().equals(pause)) {
			clockTickObject.pause();
		}
		
		if(arg0.getSource().equals(resume)) {
			clockTickObject.resumeClock();
		}
		
		if(arg0.getSource().equals(stop)) {
			running = false;
		}
		
		if(arg0.getSource().equals(car)) {
			Car tempCar = new Car(0, 20);
			road.addCar(tempCar);
			clockTickObject.register(tempCar);
			for(int x = 0; x > 800; x += 20) {
				for(int y = 0; y < 600; y += 150 ) {
					tempCar.setxPosition(x);
					tempCar.setyPosition(y);
					if(!road.collision(x, y, tempCar)) {
						app.repaint();
						return;
					}
				}
				
			}
			
		}//end if car
		
		if(arg0.getSource().equals(light)) {
			lastLightX += 235;
			TrafficLight tmpLight = new TrafficLight(lastLightX, 225);
			road.addLight(tmpLight);
			clockTickObject.register(tmpLight);
		}
		
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Road Thread");
		while(running) {
			//TODO:: road.tick();
			app.repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
