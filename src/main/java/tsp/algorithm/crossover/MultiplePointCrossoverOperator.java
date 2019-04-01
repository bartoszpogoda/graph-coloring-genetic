package tsp.algorithm.crossover;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.javatuples.Pair;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.util.RandomGenerator;

public class MultiplePointCrossoverOperator implements CrossoverOperator {

	private RandomGenerator randomGenerator;
	private int points;
	
	public MultiplePointCrossoverOperator(int points, RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
		this.points = points;
	}
	
	public MultiplePointCrossoverOperator(int points) {
		this(points, new RandomGenerator());
	}


	@Override
	public Pair<Chromosome, Chromosome> crossover(Chromosome firstParent, Chromosome secondParent) {
		int chromosomeLength = firstParent.getLength();

		Chromosome childOne = new Chromosome(firstParent);
		Chromosome childTwo = new Chromosome(secondParent);

		Set<Integer> randomCrossoverPoints = IntStream.range(0, points)
				.map(i -> randomGenerator.generateIntInRangeExclusive(0, chromosomeLength - 1)).mapToObj(Integer::new)
				.collect(Collectors.toSet());

		Set<Integer> sortedCrossoverPoints = new TreeSet<>(randomCrossoverPoints);

		int geneIterator = 0;
		boolean parentSwap = true;
		for (Integer crossoverPoint : sortedCrossoverPoints) {

			for (; geneIterator <= crossoverPoint; geneIterator++) {
				if (parentSwap) {
					childTwo.setGeneAt(geneIterator, secondParent.getGeneAt(geneIterator));
					childOne.setGeneAt(geneIterator, firstParent.getGeneAt(geneIterator));
				} else {
					childOne.setGeneAt(geneIterator, secondParent.getGeneAt(geneIterator));
					childTwo.setGeneAt(geneIterator, firstParent.getGeneAt(geneIterator));
				}
			}

			parentSwap = !parentSwap;
		}
		
		for (; geneIterator < chromosomeLength; geneIterator++) {
			if (parentSwap) {
				childTwo.setGeneAt(geneIterator, secondParent.getGeneAt(geneIterator));
				childOne.setGeneAt(geneIterator, firstParent.getGeneAt(geneIterator));
			} else {
				childOne.setGeneAt(geneIterator, secondParent.getGeneAt(geneIterator));
				childTwo.setGeneAt(geneIterator, firstParent.getGeneAt(geneIterator));
			}
		}

		return new Pair<>(childOne, childTwo);
	}

	@Override
	public String toString() {
		return "Multiple Point Crossover Operator";
	}

}
