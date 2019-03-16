package tsp.algorithm.population;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.instance.AbstractInstance;

public class Population {

	private Chromosome[] population;

	public Population(int populationSize) {
		this.population = new Chromosome[populationSize];
	}
	
    public void saveChromosome(int index, Chromosome chromsome) {
    	population[index] = chromsome;
    }
    
    public Chromosome getChromosome(int index) {
        return population[index];
    }
    
    public int getSize() {
    	return population.length;
    }
    
	public static Population generateInitialPopulation(PhenotypeInterpreter phenotypeInterpreter, int populationSize, AbstractInstance instance) {
		Population population = new Population(populationSize);

		population.population[0] = phenotypeInterpreter.generateRandomChromosome(instance.getSize());
		
		for (int i = 1; i < population.population.length; i++) {
			population.population[i] = phenotypeInterpreter.generateRandomChromosome(instance.getSize());
		}

		return population;
	}
}
