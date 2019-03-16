package tsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import tsp.algorithm.Algorithm;
import tsp.algorithm.BestInGenerationListener;
import tsp.algorithm.crossover.SinglePointCrossoverOperator;
import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.individual.PhenotypeInterpreter;
import tsp.algorithm.mutation.RandomizeGeneMutationOperator;
import tsp.algorithm.util.FitnessCalculator;
import tsp.instance.AbstractInstance;
import tsp.instance.reader.InstanceFileReader;

public class VisualizedDemo {

	private static final int DISPLAY_EVERY_NTH_GENERATION = 10;
	private static final int DISPLAY_THROTTLING_MS = 1000;

	public static void main(String[] args) throws IOException {

		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		InstanceFileReader instanceFileReader = new InstanceFileReader();
		// AbstractInstance instance =
		// instanceFileReader.read(getPath("input/queen7_7.col"));
		AbstractInstance instance = instanceFileReader.read(getPath("input/my_very_simple_3.col"));

		PhenotypeInterpreter phenotypeInterpreter = new PhenotypeInterpreter();

		Graph graph = new SingleGraph("Visualization");
		List<Node> nodes = initalizeGraph(instance, graph);

		List<String> randomColors = generateRandomColors(instance);

		graph.display();

		FitnessCalculator fitnessCalculator = new FitnessCalculator(phenotypeInterpreter, instance);

		BestInGenerationListener listener = new BestInGenerationListener() {

			@Override
			public void notify(int generation, Chromosome chromosome) {

				if (generation % DISPLAY_EVERY_NTH_GENERATION == 0) {
					int invalidEdges = fitnessCalculator.countInvalidEdges(chromosome);
					int distinctColors = fitnessCalculator.countColors(chromosome);
					int fixedDistinctColors = distinctColors + invalidEdges;

					System.out.println(String.format(
							"%d generation: {Distinct colors: %d, Invalid edges: %d, Fixed worst case dist. colors: %d}",
							generation + 1, distinctColors, invalidEdges, fixedDistinctColors));

					for (int i = 0; i < chromosome.getLength(); i++) {
						int color = phenotypeInterpreter.getColor(chromosome, i);

						nodes.get(i).setAttribute("ui.style", randomColors.get(color));
					}

					try {
						Thread.sleep(DISPLAY_THROTTLING_MS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};

		Algorithm algorithm = new Algorithm.AlgorithmBuilder().numberOfGenerations(500).bestInGenerationListener(null)
				.phenotypeInterpreter(new PhenotypeInterpreter()).crossoverOperator(new SinglePointCrossoverOperator())
				.crossoverRate(0.7).mutationOperator(new RandomizeGeneMutationOperator()).mutationRate(0.1)
				.populationSize(100).tournamentSize(3).bestInGenerationListener(listener).build();

		algorithm.execute(instance);
	}

	private static String getPath(String classPath) {
		return Demo.class.getClassLoader().getResource(classPath).getPath();
	}

	private static List<Node> initalizeGraph(AbstractInstance instance, Graph graph) {
		List<Node> nodes = new ArrayList<>();

		for (int i = 0; i < instance.getSize(); i++) {
			nodes.add(graph.addNode(Integer.toString(i)));
		}

		for (int i = 0; i < instance.getSize() - 1; i++) {
			for (int j = i + 1; j < instance.getSize(); j++) {
				if (instance.areConnected(i, j)) {
					graph.addEdge(nodes.get(i).getId() + "_" + nodes.get(j).getId(), nodes.get(i), nodes.get(j));
				}
			}
		}
		return nodes;
	}

	private static List<String> generateRandomColors(AbstractInstance instance) {
		List<String> randomColors = new ArrayList<>();
		for (int i = 0; i < instance.getSize(); i++) {
			int a = (int) Math.floor(Math.random() * 256);
			int b = (int) Math.floor(Math.random() * 256);
			int c = (int) Math.floor(Math.random() * 256);

			randomColors.add("fill-color: rgb(" + a + "," + b + "," + c + ");");
		}
		Collections.shuffle(randomColors);
		return randomColors;
	}

}
