package tsp.algorithm.mutation;

import tsp.algorithm.individual.Chromosome;

public interface MutationOperator {
	void mutate(Chromosome chromosome);
}
