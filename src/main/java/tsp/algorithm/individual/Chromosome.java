package tsp.algorithm.individual;

import java.util.Arrays;

public class Chromosome {
	
	private int[] genes = null;

	public Chromosome(int length) {
		this.genes = new int[length];
	}
	
	public Chromosome(Chromosome otherChromosome) {
		this(otherChromosome.getLength());
		
		genes = Arrays.copyOf(otherChromosome.genes, genes.length);
	}

	public int getLength() {
		return genes.length;
	}
	
	public void setGeneAt(int i, int value) {
		this.genes[i] = value;
	}
	
	public int getGeneAt(int i) {
		return this.genes[i];
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < genes.length; i++) {
			builder.append(genes[i] + (i == genes.length - 1 ? "" : " -> "));
		}
		return builder.toString();
	}
}
