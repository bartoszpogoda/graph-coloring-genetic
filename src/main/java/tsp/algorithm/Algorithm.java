package tsp.algorithm;


import org.javatuples.Pair;

import tsp.algorithm.crossover.CrossoverOperator;
import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.algorithm.mutation.MutationOperator;
import tsp.algorithm.population.Population;
import tsp.algorithm.thread.AlgorithmTerminator;
import tsp.algorithm.tournament.TournamentChooser;
import tsp.algorithm.util.FitnessCalculator;
import tsp.algorithm.util.RandomGenerator;
import tsp.instance.AbstractInstance;

public class Algorithm {

	private volatile boolean running = true;

	// dependencies
	private AlgorithmTerminator algorithmTerminator;

	private FitnessCalculator fitnessCalculator;
	private TournamentChooser tournamentChooser;
	private CrossoverOperator crossoverOperator;
	private MutationOperator mutationOperator;
	private RandomGenerator randomGenerator;
	private PhenotypeInterpreter phenotypeInterpreter;
	private BestInGenerationListener bestInGenerationListener;

	// parameters
	private int populationSize = 100;
	private int tournamentSize = 2;
	private double crossoverRate = 0.8;
	private double mutationRate = 0.01;
	private int numberOfGenerations = 0;

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
		tournamentChooser = new TournamentChooser(fitnessCalculator, tournamentSize);

		Population initialPopulation = Population.generateInitialPopulation(phenotypeInterpreter, populationSize,
				instance);

		
		if (algorithmTerminator != null) {
			algorithmTerminator.start();
		}

		startEvolution(instance, initialPopulation);

		// TODO in the end fix result by applying possible or new color in bad verticles (greedy way)

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

		for (int i = 0; i < evolvedPopulation.getSize() - 1; i += 2) {
			Chromosome firstParent = tournamentChooser.choose(population);
			Chromosome secondParent = tournamentChooser.choose(population);

			if (randomGenerator.nextDouble() < crossoverRate) {
				Pair<Chromosome, Chromosome> children = crossoverOperator.crossover(firstParent, secondParent);
				evolvedPopulation.saveChromosome(i, children.getValue0());
				evolvedPopulation.saveChromosome(i + 1, children.getValue1());
			} else {
				evolvedPopulation.saveChromosome(i, firstParent);
				evolvedPopulation.saveChromosome(i + 1, secondParent);
			}
		}

		for (int i = 0; i < evolvedPopulation.getSize(); i++) {
			if (randomGenerator.nextDouble() < mutationRate) {
				mutationOperator.mutate(evolvedPopulation.getChromosome(i));
			}
		}

		return evolvedPopulation;
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

		public AlgorithmBuilder tournamentSize(int tournamentSize) {
			builtAlgorithm.tournamentSize = tournamentSize;
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
			stringBuilder.append("Wielkosæ turnieju: " + builtAlgorithm.tournamentSize + "\n");
			stringBuilder.append("Operator mutacji: " + builtAlgorithm.mutationOperator + "\n");
			stringBuilder.append("Wspó³czynnik mutacji: " + builtAlgorithm.mutationRate + "\n");
			stringBuilder.append("Operator krzy¿owania: " + builtAlgorithm.crossoverOperator + "\n");
			stringBuilder.append("Wspó³czynnik krzy¿owania: " + builtAlgorithm.crossoverRate + "\n");

			return stringBuilder.toString();
		}

	}
}
