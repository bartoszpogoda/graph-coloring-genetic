package tsp.algorithm.individual;

import java.util.stream.IntStream;

import tsp.algorithm.util.RandomGenerator;

public class PhenotypeInterpreter {

	private RandomGenerator randomGenerator;

	public PhenotypeInterpreter() {
		this.randomGenerator = new RandomGenerator();
	}
	
	public PhenotypeInterpreter(RandomGenerator randomGenerator) {
		this.randomGenerator = randomGenerator;
	}
	
	public Chromosome generateRandomChromosome(int graphSize) {
		Chromosome chromosome = new Chromosome(graphSize);

		IntStream.range(0, graphSize).forEach((i) -> {
			chromosome.setGeneAt(i, randomGenerator.generateIntInRangeExclusive(0, graphSize));
		});

		return chromosome;
	}
	
//	public Chromosome generateGreedyChromosome(AbstractInstance instance, int graphSize) {
//		Chromosome chromosome = new Chromosome(graphSize);
//
//		IntStream.range(0, graphSize).forEach((i) -> {
//			// set first possible color
//		});
//
//		return chromosome;
//	}
	
	public int getColor(Chromosome chromosome, int vertexId) {
		return chromosome.getGeneAt(vertexId);
	}

	public void setColor(Chromosome chromosome, int vertexId, int color) {
		chromosome.setGeneAt(vertexId, color);
	}

}
