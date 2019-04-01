package tsp.algorithm.mutation;

import java.util.Random;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.instance.AbstractInstance;

public class HybridMutationOperator implements MutationOperator {

	private double RANDOMIZE_GENE_FREQ = 0.90;
	private double RANDOMIZE_ILLEGAL_GENE_FREQ = 0.07;
//	private double FIX_ILLEGAL_GENE_FREQ = 0.05;
	
	private Random randomGenerator = new Random();

	private RandomizeGeneMutationOperator randomizeGeneOperator;	
	private FixIllegalGenesMutationOperator fixIllegalGenesOperator;
	private RandomizeIllegalGenesMutationOperator randomizeIllegalGenesOperator;
	
	public HybridMutationOperator(PhenotypeInterpreter interpreter, AbstractInstance instance) {
		super();
		
		this.randomizeGeneOperator = new RandomizeGeneMutationOperator();
		this.fixIllegalGenesOperator = new FixIllegalGenesMutationOperator(interpreter, instance);
		this.randomizeIllegalGenesOperator = new RandomizeIllegalGenesMutationOperator(interpreter, instance);
	}

	@Override
	public void mutate(Chromosome chromosome) {
		double random = randomGenerator.nextDouble();
		
		if(random < RANDOMIZE_GENE_FREQ) {
			randomizeGeneOperator.mutate(chromosome);
		} else if( random < RANDOMIZE_GENE_FREQ + RANDOMIZE_ILLEGAL_GENE_FREQ) {
			randomizeIllegalGenesOperator.mutate(chromosome);
		} else {
			// force fit freq values sum to 1.0
			fixIllegalGenesOperator.mutate(chromosome);
		}
		
	}
	
}
