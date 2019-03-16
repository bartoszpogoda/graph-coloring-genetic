package tsp.algorithm;

import tsp.algorithm.individual.Chromosome;

public interface BestInGenerationListener {
	public void notify(int afterGeneration, Chromosome chromosome);
}
