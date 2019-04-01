package tsp;

import java.io.IOException;

import tsp.algorithm.Algorithm;
import tsp.algorithm.BestInGenerationListener;
import tsp.algorithm.crossover.CrossoverOperator;
import tsp.algorithm.crossover.MultiplePointCrossoverOperator;
import tsp.algorithm.crossover.SinglePointCrossoverOperator;
import tsp.algorithm.crossover.TwoPointCrossoverOperator;
import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.algorithm.inversion.InversionOperator;
import tsp.algorithm.mutation.FixIllegalGenesMutationOperator;
import tsp.algorithm.mutation.HybridMutationOperator;
import tsp.algorithm.mutation.MutationOperator;
import tsp.algorithm.mutation.RandomizeGeneMutationOperator;
import tsp.algorithm.mutation.RandomizeIllegalGenesMutationOperator;
import tsp.algorithm.tournament.Chooser;
import tsp.algorithm.tournament.RuletteWheelChooser;
import tsp.algorithm.tournament.TournamentChooser;
import tsp.algorithm.util.FitnessCalculator;
import tsp.instance.AbstractInstance;
import tsp.instance.reader.InstanceFileReader;

public class Demo {

	private static final int NUMBER_OF_SAMPLES = 10;

	public static void main(String[] args) throws IOException {

		InstanceFileReader instanceFileReader = new InstanceFileReader();
		AbstractInstance instance = instanceFileReader.read(getPath("input/queen7_7.col"));
		// AbstractInstance instance =
		// instanceFileReader.read(getPath("input/le450_15b.col)");
		// AbstractInstance instance =
		// instanceFileReader.read(getPath("input/my_very_simple_3.col"));

		PhenotypeInterpreter phenotypeInterpreter = new PhenotypeInterpreter();
		FitnessCalculator fitnessCalculator = new FitnessCalculator(phenotypeInterpreter, instance);
		int numberOfGenerations = 500;

		BestInGenerationListener listener = new BestInGenerationListener() {

			@Override
			public void notify(int afterGeneration, Chromosome theFittest) {

				if (afterGeneration == 0 || (afterGeneration + 1) % (numberOfGenerations / 10) == 0) {
					System.out.print(fitnessCalculator.countColors(theFittest)
							+ fitnessCalculator.countInvalidEdges(theFittest) + "\t");
				}

			}
		};
		
		Chooser chooser = new TournamentChooser(fitnessCalculator, 3);
//		Chooser chooser = new RuletteWheelChooser(fitnessCalculator);

//		boolean eliteSelection = false;
		boolean eliteSelection = true;
		
//		MutationOperator mutationOperator = new RandomizeIllegalGenesMutationOperator(phenotypeInterpreter, instance);
//		MutationOperator mutationOperator = new FixIllegalGenesMutationOperator(phenotypeInterpreter, instance);
//		MutationOperator mutationOperator = new RandomizeGeneMutationOperator();
		MutationOperator mutationOperator = new HybridMutationOperator(phenotypeInterpreter, instance);
		
		InversionOperator inversionOperator = new InversionOperator();
		
		
//		CrossoverOperator crossoverOperator = new SinglePointCrossoverOperator();
//		CrossoverOperator crossoverOperator = new TwoPointCrossoverOperator();
		CrossoverOperator crossoverOperator = new MultiplePointCrossoverOperator(4);
		
		
		Algorithm algorithm = new Algorithm.AlgorithmBuilder().numberOfGenerations(numberOfGenerations)
				.bestInGenerationListener(null).phenotypeInterpreter(phenotypeInterpreter)
				.chooser(chooser).eliteSelection(eliteSelection)
				.crossoverOperator(crossoverOperator).crossoverRate(0.7)
				.mutationOperator(mutationOperator).mutationRate(0.5).populationSize(100)
				.inversionOperator(inversionOperator).inversionRate(0.7)
				.bestInGenerationListener(listener).build();

		for (int i = 0; i < NUMBER_OF_SAMPLES; i++) {
			algorithm.execute(instance);
			System.out.println();
		}

	}

	private static String getPath(String classPath) {
		return Demo.class.getClassLoader().getResource(classPath).getPath();
	}

}
