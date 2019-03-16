package tsp.algorithm.mutation;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.util.RandomGenerator;

public class RandomizeGeneMutationOperator implements MutationOperator {

	RandomGenerator randomGenerator;

	public RandomizeGeneMutationOperator() {
		randomGenerator = new RandomGenerator();
	}

	public RandomizeGeneMutationOperator(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	@Override
	public void mutate(Chromosome chromosome) {
		int randomGenePosition = randomGenerator.generateIntInRangeExclusive(0, chromosome.getLength());
		int randomGeneValue = randomGenerator.generateIntInRangeExclusive(0, chromosome.getLength());
		
		chromosome.setGeneAt(randomGenePosition, randomGeneValue);
	}

	@Override
	public String toString() {
		return "Randomize Gene Mutation Operator";
	}
}
