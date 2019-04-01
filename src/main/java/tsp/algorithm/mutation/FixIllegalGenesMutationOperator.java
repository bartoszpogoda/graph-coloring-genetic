package tsp.algorithm.mutation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.instance.AbstractInstance;
import tsp.instance.Edge;

/**
 * Fixes genes with color that is illegal (same to some of adjacents)
 * 
 * @author BPOGODA
 */
public class FixIllegalGenesMutationOperator implements MutationOperator {

	private PhenotypeInterpreter interpreter;

	private AbstractInstance instance;

	public FixIllegalGenesMutationOperator(PhenotypeInterpreter interpreter, AbstractInstance instance) {
		super();
		this.interpreter = interpreter;
		this.instance = instance;
	}

	@Override
	public void mutate(Chromosome chromosome) {

		for (int i = 0; i < chromosome.getLength(); i++) {
			int geneColor = interpreter.getColor(chromosome, i);

			List<Edge> adjacentEdges = instance.getAdjacentEdges(i);

			boolean isIllegal = false;
			for (Edge adjacent : adjacentEdges) {
				if (interpreter.getColor(chromosome, adjacent.getTo()) == geneColor) {
					isIllegal = true;
					break;
				}
			}

			if (isIllegal) {
				Set<Integer> adjacentColors = adjacentEdges.stream()
						.map(edge -> interpreter.getColor(chromosome, edge.getTo())).collect(Collectors.toSet());

				for (int j = 0; j < chromosome.getLength(); j++) {
					if (!adjacentColors.contains(j)) {
						interpreter.setColor(chromosome, i, j);
						break;
					}
				}
			}
		}

	}

}
