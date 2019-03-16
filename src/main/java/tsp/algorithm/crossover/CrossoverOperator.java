package tsp.algorithm.crossover;

import org.javatuples.Pair;

import tsp.algorithm.individual.Chromosome;

public interface CrossoverOperator {
	Pair<Chromosome, Chromosome> crossover(Chromosome firstParent, Chromosome secondParent);
}
