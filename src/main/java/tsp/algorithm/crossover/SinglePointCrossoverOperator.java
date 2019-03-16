package tsp.algorithm.crossover;

import java.util.stream.IntStream;

import org.javatuples.Pair;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.util.RandomGenerator;

public class SinglePointCrossoverOperator implements CrossoverOperator {

	private RandomGenerator randomGenerator;

	public SinglePointCrossoverOperator() {
		this.randomGenerator = new RandomGenerator();
	}

	public SinglePointCrossoverOperator(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	@Override 
	public Pair<Chromosome, Chromosome> crossover(Chromosome firstParent, Chromosome secondParent) {
		int chromosomeLength = firstParent.getLength();

		Chromosome childOne = new Chromosome(firstParent);
		Chromosome childTwo = new Chromosome(secondParent);

		int crossoverPoint = randomGenerator.generateIntInRangeExclusive(0, chromosomeLength - 1);

		IntStream.range(crossoverPoint, chromosomeLength).forEach((i) -> {
			childOne.setGeneAt(i, secondParent.getGeneAt(i));
			childTwo.setGeneAt(i, firstParent.getGeneAt(i));
		});

		return new Pair<>(childOne, childTwo);
	}

	@Override
	public String toString() {
		return "Single Point Crossover Operator";
	}
}
