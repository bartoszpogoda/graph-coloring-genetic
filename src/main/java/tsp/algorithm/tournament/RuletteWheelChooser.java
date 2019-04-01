package tsp.algorithm.tournament;

import java.util.HashMap;
import java.util.Map;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.population.Population;
import tsp.algorithm.util.FitnessCalculator;
import tsp.algorithm.util.RandomGenerator;

public class RuletteWheelChooser implements Chooser {

	private FitnessCalculator fitnessCalculator;
	private RandomGenerator randomGenerator;
	
	private double[] wheel;
	private Chromosome[] wheelChromosomes;
	
	private double wheelRange;
	
	public RuletteWheelChooser(FitnessCalculator fitnessCalculator) {
		super();
		this.fitnessCalculator = fitnessCalculator;
		randomGenerator = new RandomGenerator();
	}

	@Override
	public Chromosome choose(Population population) {
		if(wheel == null) {
			buildWheel(population);
		}
		
		double random = randomGenerator.nextDouble() * wheelRange;
		for(int i = 0 ; i < wheel.length ; i++) {
			if(wheel[i] > random) {
				return wheelChromosomes[i];
			}
		}
		
		return null; // shouldn't happen
	}

	public void buildWheel(Population population) {
		
		wheel = new double[population.getSize()];
		wheelChromosomes = new Chromosome[population.getSize()];
		
		double totalFitness = 0;
		for(int i = 0 ; i < population.getSize() ; i++) {
			Chromosome chromosome = population.getChromosome(i);
		
			double fitness = fitnessCalculator.calculateFitness(chromosome);
			totalFitness += fitness;
			
			wheel[i] = totalFitness;
			wheelChromosomes[i] = chromosome;
			
			if(i == population.getSize() - 1) {
				wheelRange = totalFitness;
			}
		}
		
	}
	
	@Override
	public void resetForNewPopulation() {
		wheel = null;
		
	}
	
	
	
}
