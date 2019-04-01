package tsp.algorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;

import tsp.algorithm.crossover.CrossoverOperator;
import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.algorithm.inversion.InversionOperator;
import tsp.algorithm.mutation.MutationOperator;
import tsp.algorithm.population.Population;
import tsp.algorithm.thread.AlgorithmTerminator;
import tsp.algorithm.tournament.Chooser;
import tsp.algorithm.tournament.TournamentChooser;
import tsp.algorithm.util.FitnessCalculator;
import tsp.algorithm.util.RandomGenerator;
import tsp.instance.AbstractInstance;
import tsp.instance.Edge;

public class Algorithm {

	private volatile boolean running = true;

	// dependencies
	private AlgorithmTerminator algorithmTerminator;

	private FitnessCalculator fitnessCalculator;
	private CrossoverOperator crossoverOperator;
	private MutationOperator mutationOperator;
	private RandomGenerator randomGenerator;
	private InversionOperator inversionOperator;
	private Chooser chooser;
	
	private PhenotypeInterpreter phenotypeInterpreter;
	private BestInGenerationListener bestInGenerationListener;

	// parameters
	private int populationSize = 100;
	private double crossoverRate = 0.8;
	private double mutationRate = 0.01;
	private int numberOfGenerations = 0;
	private boolean eliteSelection = false;
	private double inversionRate = 0;

	// best fittest tracking
	private Chromosome currentBest = null;
	private double currentBestDistance = Double.MAX_VALUE;

	public Algorithm() {
		randomGenerator = new RandomGenerator();
	}

	public double getCurrentBestDistance() {
		return currentBestDistance;
	}

	public synchronized Chromosome execute(AbstractInstance instance) {
		currentBest = null;
		currentBestDistance = Double.MAX_VALUE;
		running = true;

		fitnessCalculator = new FitnessCalculator(phenotypeInterpreter, instance);

		Population initialPopulation = Population.generateInitialPopulation(phenotypeInterpreter, populationSize,
				instance);

		if (algorithmTerminator != null) {
			algorithmTerminator.start();
		}

		startEvolution(instance, initialPopulation);
		applyResultFix(instance, currentBest);

		return currentBest;
	}

	public void startEvolution(AbstractInstance instance, Population initialPopulation) {
		Population population = initialPopulation;

		if (numberOfGenerations > 0) {
			for (int i = 0; i < numberOfGenerations; i++) {
				population = evolve(instance, population, i);
			}
		} else {
			int i = 0;
			while (running) {
				population = evolve(instance, population, i);
				i += 1;
			}
		}

	}

	private Population evolve(AbstractInstance instance, Population population, int generation) {

		Chromosome theFittest = fitnessCalculator.findTheFittest(population);

		if (currentBest == null
				|| fitnessCalculator.calculateFitness(theFittest) > fitnessCalculator.calculateFitness(currentBest)) {
			currentBest = theFittest;
		}

		if (bestInGenerationListener != null) {
			bestInGenerationListener.notify(generation, theFittest);
		}

		Population evolvedPopulation = new Population(populationSize);
		
		// Elite selection - incubate best chromosome
		int incubatedChromosomes = 0;
		if(eliteSelection) {			
			evolvedPopulation.saveChromosome(incubatedChromosomes++, theFittest);
			evolvedPopulation.saveChromosome(incubatedChromosomes++, theFittest);
		}
		
		chooser.resetForNewPopulation();
		for (int i = incubatedChromosomes; i < evolvedPopulation.getSize() - 1; i += 2) {
			Chromosome firstParent = chooser.choose(population);
			Chromosome secondParent = chooser.choose(population);

			if (randomGenerator.nextDouble() < crossoverRate) {
				Pair<Chromosome, Chromosome> children = crossoverOperator.crossover(firstParent, secondParent);
				evolvedPopulation.saveChromosome(i, children.getValue0());
				evolvedPopulation.saveChromosome(i + 1, children.getValue1());
			} else {
				evolvedPopulation.saveChromosome(i, firstParent);
				evolvedPopulation.saveChromosome(i + 1, secondParent);
			}
		}

		for (int i = incubatedChromosomes; i < evolvedPopulation.getSize(); i++) {
			if (randomGenerator.nextDouble() < mutationRate) {
				mutationOperator.mutate(evolvedPopulation.getChromosome(i));
			}
		}
		
		if(inversionRate > 0) {
			for (int i = incubatedChromosomes; i < evolvedPopulation.getSize(); i++) {
				if (randomGenerator.nextDouble() < inversionRate) {
					inversionOperator.inverse(evolvedPopulation.getChromosome(i));
				}
			}
		}
		

		return evolvedPopulation;
	}

	// TODO for 2nd stage make it smarter (apply not new colors but if possible reaply already used)
	private void applyResultFix(AbstractInstance instance, Chromosome result) {
		List<Edge> edges = instance.getAllEdges();

		for (Edge edge : edges) {
			if (phenotypeInterpreter.getColor(result, edge.getFrom()) == phenotypeInterpreter.getColor(result,
					edge.getTo())) {

				phenotypeInterpreter.setColor(result, edge.getFrom(), findFirstUnusedColor(instance, result));
			}
		}
	}
	
	private int findFirstUnusedColor(AbstractInstance instance, Chromosome chromosome) {
		Set<Integer> usedColors = new HashSet<>();

		for (int i = 0; i < chromosome.getLength(); i++) {
			int color = phenotypeInterpreter.getColor(chromosome, i);

			usedColors.add(color);
		}
		
		for(int i = 0 ; ; i++) {
			if(!usedColors.contains(i)) {
				return i;
			}
		}
	}

	public void terminate() {
		running = false;
	}

	public static class AlgorithmBuilder {
		private Algorithm builtAlgorithm;

		public AlgorithmBuilder() {
			this.builtAlgorithm = new Algorithm();
		}

		public AlgorithmBuilder numberOfGenerations(int numberOfGenerations) {
			builtAlgorithm.numberOfGenerations = numberOfGenerations;
			return this;
		}

		public AlgorithmBuilder populationSize(int populationSize) {
			builtAlgorithm.populationSize = populationSize;
			return this;
		}

		public AlgorithmBuilder chooser(Chooser chooser) {
			builtAlgorithm.chooser = chooser;
			return this;
		}
		
		public AlgorithmBuilder eliteSelection(boolean eliteSelection) {
			builtAlgorithm.eliteSelection = eliteSelection;
			return this;
		}

		public AlgorithmBuilder crossoverRate(double crossoverRate) {
			builtAlgorithm.crossoverRate = crossoverRate;
			return this;
		}

		public AlgorithmBuilder mutationRate(double mutationRate) {
			builtAlgorithm.mutationRate = mutationRate;
			return this;
		}

		public AlgorithmBuilder crossoverOperator(CrossoverOperator crossoverOperator) {
			builtAlgorithm.crossoverOperator = crossoverOperator;
			return this;
		}

		public AlgorithmBuilder mutationOperator(MutationOperator mutationOperator) {
			builtAlgorithm.mutationOperator = mutationOperator;
			return this;
		}
		

		public AlgorithmBuilder inversionOperator(InversionOperator inversionOperator) {
			builtAlgorithm.inversionOperator = inversionOperator;
			return this;
		}
		

		public AlgorithmBuilder inversionRate(double inversionRate) {
			builtAlgorithm.inversionRate = inversionRate;
			return this;
		}

		public AlgorithmBuilder algorithmTerminator(AlgorithmTerminator algorithmTerminator) {
			builtAlgorithm.algorithmTerminator = algorithmTerminator;
			algorithmTerminator.setAlgorithm(builtAlgorithm);
			return this;
		}

		public AlgorithmBuilder phenotypeInterpreter(PhenotypeInterpreter phenotypeInterpreter) {
			builtAlgorithm.phenotypeInterpreter = phenotypeInterpreter;
			return this;
		}

		public AlgorithmBuilder bestInGenerationListener(BestInGenerationListener bestInGenerationListener) {
			builtAlgorithm.bestInGenerationListener = bestInGenerationListener;
			return this;
		}

		public Algorithm build() {
			return builtAlgorithm;
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();

			stringBuilder.append("Wielkoœæ populacji: " + builtAlgorithm.populationSize + "\n");
			stringBuilder.append("Operator mutacji: " + builtAlgorithm.mutationOperator + "\n");
			stringBuilder.append("Wspó³czynnik mutacji: " + builtAlgorithm.mutationRate + "\n");
			stringBuilder.append("Operator krzy¿owania: " + builtAlgorithm.crossoverOperator + "\n");
			stringBuilder.append("Wspó³czynnik krzy¿owania: " + builtAlgorithm.crossoverRate + "\n");

			return stringBuilder.toString();
		}

	}
}
