package tsp.algorithm.mutation;

import java.util.List;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.algorithm.util.RandomGenerator;
import tsp.instance.AbstractInstance;
import tsp.instance.Edge;

/**
 * Randomizes genes with color that is illegal (same to some of adjacents)
 * 
 * @author BPOGODA
 */
public class RandomizeIllegalGenesMutationOperator implements MutationOperator {

	private PhenotypeInterpreter interpreter;
	
	private AbstractInstance instance;
	
	private RandomGenerator randomGenerator;
	
	public RandomizeIllegalGenesMutationOperator(PhenotypeInterpreter interpreter, AbstractInstance instance) {
		super();
		this.interpreter = interpreter;
		this.instance = instance;
		
		this.randomGenerator = new RandomGenerator();
	}

	public RandomizeIllegalGenesMutationOperator(PhenotypeInterpreter interpreter, AbstractInstance instance,
			RandomGenerator randomGenerator) {
		this(interpreter, instance);
		this.randomGenerator = randomGenerator;
	}

	@Override
	public void mutate(Chromosome chromosome) {

		for(int i = 0 ; i < chromosome.getLength() ; i++) {
			int geneColor = interpreter.getColor(chromosome, i);
			
			List<Edge> adjacentVerts = instance.getAdjacentEdges(i);
			for(Edge adjacent : adjacentVerts) {
				if(interpreter.getColor(chromosome, adjacent.getTo()) == geneColor) {
					// same color (invalid situation)

					int randomGeneValue = randomGenerator.generateIntInRangeExclusive(0, chromosome.getLength());					
					interpreter.setColor(chromosome, i, randomGeneValue);
					
					break;
				}
			}
		}
		
	}

}
