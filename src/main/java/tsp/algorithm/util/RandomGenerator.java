package tsp.algorithm.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator {
	
	public int generateIntInRangeInclusive(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public int generateIntInRangeExclusive(int min, int maxExclusive) {
		return ThreadLocalRandom.current().nextInt(min, maxExclusive);
	}
	
	/**
	 * Returns a pseudorandom double value between zero (inclusive) and one (exclusive).
	 * 
	 * @return a pseudorandom double value between zero (inclusive) and one (exclusive).
	 */
	public double nextDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}
}
