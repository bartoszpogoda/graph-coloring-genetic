package tsp.algorithm.util;

import java.util.HashMap;
import java.util.Map;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.algorithm.population.Population;
import tsp.instance.AbstractInstance;

public class FitnessCalculator {
	private AbstractInstance instance;

	private PhenotypeInterpreter phenotypeInterpreter;

	public FitnessCalculator() {
	}

	public FitnessCalculator(PhenotypeInterpreter phenotypeInterpreter, AbstractInstance instance) {
		this.instance = instance;
		this.phenotypeInterpreter = phenotypeInterpreter;
	}

	public void setInstance(AbstractInstance instance) {
		this.instance = instance;
	}
	
    public Chromosome findTheFittest(Population population) {
    	Chromosome currentBestPath = null;
    	double currentBestPathFitness = 0;
    	
    	for (int i = 0; i < population.getSize(); i++) {
    		double currentPathFitness = calculateFitness(population.getChromosome(i));
    		
			if(currentPathFitness > currentBestPathFitness) {
				currentBestPath = population.getChromosome(i);
				currentBestPathFitness = currentPathFitness;
			}
		}
    	
    	return currentBestPath;
    }
	
	public double calculateFitness(Chromosome chromosome) {

		int colorCount = countColors(chromosome);
		int invalidEdgesCount = countInvalidEdges(chromosome);
		
		double score = (double) 1 / (colorCount + invalidEdgesCount);
		
		return score;
		
//		double colorCountsScore = (double) 1 / (colorCount);
//		double validityScore = invalidEdgesCount == 0 ? 1 : (double) 1 / invalidEdgesCount;
//
//		// TODO weights as parameter
//		return 0.3 * colorCountsScore + 0.7 * validityScore;
	}
	
	public int countColors(Chromosome chromosome) {
		Map<Integer, Integer> colorCounts = new HashMap<>();

		for (int i = 0; i < chromosome.getLength(); i++) {
			int color = phenotypeInterpreter.getColor(chromosome, i);

			colorCounts.putIfAbsent(color, 0);
			colorCounts.computeIfPresent(color, (key, value) -> value + 1);
		}
		
		return colorCounts.keySet().size();
	}

	// TODO maybe store graph as list of edges for performance of this method?
	// TODO maybe reimplement to use edge list instance better
	public int countInvalidEdges(Chromosome chromosome) {
		int count = 0;
		int size = instance.getSize();

		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (instance.areConnected(i, j) && phenotypeInterpreter.getColor(chromosome, i) == phenotypeInterpreter
						.getColor(chromosome, j)) {
					count++;
				}
			}
		}

		return count;
	}
}
