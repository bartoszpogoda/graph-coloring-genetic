package tsp.algorithm.inversion;


import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.util.RandomGenerator;

public class InversionOperator {
	private RandomGenerator randomGenerator = new RandomGenerator();
	
	public void inverse(Chromosome chromosome) {
		int chromosomeLength = chromosome.getLength();
		Chromosome mutated = new Chromosome(chromosome);

		int inversionPointA = randomGenerator.generateIntInRangeExclusive(0, chromosomeLength);
		int inversionPointB = randomGenerator.generateIntInRangeExclusive(0, chromosomeLength);

		int inversionStart = (int) Math.min(inversionPointA, inversionPointB);
		int inversionEnd = (int) Math.max(inversionPointA, inversionPointB);

		for(int i = 0 ; i < inversionEnd - inversionStart; i++) {
			mutated.setGeneAt(inversionStart + i, chromosome.getGeneAt(inversionEnd - i - 1));;
		}
		
		chromosome = mutated;
	}

	
	
}
