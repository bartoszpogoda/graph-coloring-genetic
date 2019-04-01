package tsp.algorithm.crossover;

import java.util.stream.IntStream;

import org.javatuples.Pair;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.util.RandomGenerator;

public class TwoPointCrossoverOperator implements CrossoverOperator {

	private RandomGenerator randomGenerator;

	public TwoPointCrossoverOperator() {
		this.randomGenerator = new RandomGenerator();
	}

	public TwoPointCrossoverOperator(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	@Override 
	public Pair<Chromosome, Chromosome> crossover(Chromosome firstParent, Chromosome secondParent) {
		int chromosomeLength = firstParent.getLength();

		Chromosome childOne = new Chromosome(firstParent);
		Chromosome childTwo = new Chromosome(secondParent);

		int crossoverPointA = randomGenerator.generateIntInRangeExclusive(0, chromosomeLength - 1);
		int crossoverPointB = randomGenerator.generateIntInRangeExclusive(0, chromosomeLength - 1);

		int crossoverStart = (int) Math.min(crossoverPointA, crossoverPointB);
		int crossoverEnd = (int) Math.max(crossoverPointA, crossoverPointB);

		
		IntStream.range(crossoverStart, crossoverEnd).forEach((i) -> {
			childOne.setGeneAt(i, secondParent.getGeneAt(i));
			childTwo.setGeneAt(i, firstParent.getGeneAt(i));
		});

		return new Pair<>(childOne, childTwo);
	}

	@Override
	public String toString() {
		return "Two Point Crossover Operator";
	}

}
