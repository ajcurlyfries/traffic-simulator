package main;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Road extends JPanel implements ClockTickListener{
	
	private final int LANE_HEIGHT = 240;
	private final int INTERSECTION_DISTANCE = 235;		//pixels
	ArrayList<Car> cars = new ArrayList<Car>();
	ArrayList<TrafficLight> lights = new ArrayList<TrafficLight>();
	public Road() {
		super();
	}
	
	public void addCar(Car myCar) {
		cars.add(myCar);
	}
	
	public void addLight(TrafficLight aLight) {
		lights.add(aLight);
	}
	
	//draws a road
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		//drawing lane lines
		g.setColor(Color.WHITE);
		for(int a = LANE_HEIGHT; a < LANE_HEIGHT*2; a += LANE_HEIGHT) {
			for(int b = 0; b < getWidth(); b += 40) {
				g.fillRect(b, a, 30, 5);
			}
		}
		
		//paint lights
		for(int a = 0; a < lights.size(); a++) {
			lights.get(a).paintMe(g);
		}
		
		//drawing cars on the road
		for(int a = 0; a < cars.size(); a++) {
			cars.get(a).paintMe(g);
		}
		
		
	}//end of draw
	
	public Boolean collision(int x, int y, Car aCar) {
		Boolean rVal = false;
		for(int a = 0; a < cars.size(); a++) {
			Car tempCar = cars.get(a);
			
			if(y == tempCar.getyPosition()) { //same "lane"
				if(!aCar.equals(tempCar)) { //not checking itself
					if(x < (tempCar.getxPosition() + tempCar.getWidth() ) && //check left side is left of other cars right side
							(x + aCar.getWidth()) > tempCar.getxPosition()) { //right is to the right of the left side
						rVal = true;
					}
				}
			}
		}
		return rVal;
	}
	
	public void clockTickNotify(int elapsedSeconds) {
		//check for red lights
		for(TrafficLight light: lights) {
				//check cars
				for(Car car: cars) {
					if(car.isStopped()) {
						if( ((light.getxPosition() - 175) <= car.getxPosition()) && (car.getxPosition() <= (light.getxPosition() - 100) ) && !light.isRed()) {
							car.stopForRed(light.isRed());
						}
					} else {
						if(((light.getxPosition() - 175) <= car.getxPosition()) && (car.getxPosition() <= (light.getxPosition() - 100) ) && light.isRed()) {
							car.stopForRed(light.isRed());
						}
					}
				}
		}
		this.repaint();
	}
}
