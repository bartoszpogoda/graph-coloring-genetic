package tsp;

import java.io.IOException;

import tsp.algorithm.Algorithm;
import tsp.algorithm.BestInGenerationListener;
import tsp.algorithm.crossover.SinglePointCrossoverOperator;
import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.algorithm.mutation.RandomizeGeneMutationOperator;
import tsp.algorithm.util.FitnessCalculator;
import tsp.instance.AbstractInstance;
import tsp.instance.reader.InstanceFileReader;

public class Demo {

	private static final int NUMBER_OF_SAMPLES = 10;

	public static void main(String[] args) throws IOException {

		InstanceFileReader instanceFileReader = new InstanceFileReader();
		// AbstractInstance instance =
		// instanceFileReader.read(getPath("input/queen7_7.col"));
		// AbstractInstance instance =
		// instanceFileReader.read(getPath("input/le450_15b.col)");
		AbstractInstance instance = instanceFileReader.read(getPath("input/my_very_simple_3.col"));

		PhenotypeInterpreter phenotypeInterpreter = new PhenotypeInterpreter();
		FitnessCalculator fitnessCalculator = new FitnessCalculator(phenotypeInterpreter, instance);
		int numberOfGenerations = 50;

		BestInGenerationListener listener = new BestInGenerationListener() {

			@Override
			public void notify(int afterGeneration, Chromosome theFittest) {

				if (afterGeneration == 0 || (afterGeneration + 1) % (numberOfGenerations / 10) == 0) {
					System.out.print(fitnessCalculator.countColors(theFittest)
							+ fitnessCalculator.countInvalidEdges(theFittest) + "\t");
				}

			}
		};

		Algorithm algorithm = new Algorithm.AlgorithmBuilder().numberOfGenerations(numberOfGenerations)
				.bestInGenerationListener(null).phenotypeInterpreter(phenotypeInterpreter)
				.crossoverOperator(new SinglePointCrossoverOperator()).crossoverRate(1)
				.mutationOperator(new RandomizeGeneMutationOperator()).mutationRate(1).populationSize(50)
				.tournamentSize(3).bestInGenerationListener(listener).build();

		for (int i = 0; i < NUMBER_OF_SAMPLES; i++) {
			algorithm.execute(instance);
			System.out.println();
		}

	}

	private static String getPath(String classPath) {
		return Demo.class.getClassLoader().getResource(classPath).getPath();
	}

}
