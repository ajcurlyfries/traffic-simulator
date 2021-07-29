import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 */


/**
 * @author ajzun
 *
 */
public class CarStats {
	private int speed;
	private int xPosition;
	private int yPosition;
	private int height;
	private int width;
	//private ReentrantReadWriteLock rwl;
	
	public CarStats(int speed) {
		this.speed = speed;
		this.yPosition = 0;
		this.xPosition = 0;
		//this.rwl = new ReentrantReadWriteLock();
	}
	
	//public void handleClockTick() {
	//	rwl.writeLock().lock();
	//	try {
			//based on speed increment xPosition
	//	} finally{
	//		rwl.writeLock().unlock();
	//	}
//	}
	
	public void stopOnRed() {
		//rwl.writeLock().lock();
		//try {
			//check light status
			//if red speed changes to 0
		//} finally{
		//	rwl.writeLock().unlock();
		//}
	}
}
