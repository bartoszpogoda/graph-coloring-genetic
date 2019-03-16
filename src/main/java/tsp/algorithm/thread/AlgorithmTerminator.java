package tsp.algorithm.thread;

import tsp.algorithm.Algorithm;

/***
 * Terminates the algorithm after specified time has expired
 * 
 * @author Student225988
 *
 */
public class AlgorithmTerminator extends Thread{
	
	private Algorithm algorithm;
	private long timeLimitMs;
	
	public void setAlgorithm(Algorithm algorithm){
		this.algorithm = algorithm;
	}
	
	public void setTimeLimitMs(long timeLimitMs) {
		this.timeLimitMs = timeLimitMs;
	}
	
	public long getTimeLimitMs() {
		return timeLimitMs;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(timeLimitMs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		algorithm.terminate();
	}
}
