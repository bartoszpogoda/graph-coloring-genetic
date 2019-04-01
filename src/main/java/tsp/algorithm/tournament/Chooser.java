package tsp.algorithm.tournament;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.population.Population;

public interface Chooser {

	public Chromosome choose(Population population);
	
	public void resetForNewPopulation();
}
