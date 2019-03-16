package tsp.instance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EdgeListInstance extends AbstractInstance {

	private List<Set<Integer>> edges;

	public EdgeListInstance(int size) {
		super(size);

		edges = Stream.generate(HashSet<Integer>::new).limit(size).collect(Collectors.toList());
	}

	@Override
	public void setConnected(int vertexA, int vertexB, boolean connected) {
		edges.get(Math.min(vertexA, vertexB)).add(Math.max(vertexA, vertexB));
	}

	@Override
	public boolean areConnected(int vertexA, int vertexB) {
		return edges.get(Math.min(vertexA, vertexB)).contains(Math.max(vertexA, vertexB));
	}

	public List<Edge> getAllEdges() {
		List<Edge> allEdges = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {
			Integer j = i;
			allEdges.addAll(
					edges.get(i).stream().map(otherEdge -> new Edge(j, otherEdge)).collect(Collectors.toList()));
		}
		return allEdges;
	}

}
