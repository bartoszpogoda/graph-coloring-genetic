package tsp.instance;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixInstance extends AbstractInstance {

		private double adjacencyMatrix[][];

		public AdjacencyMatrixInstance(int size) {
			super(size);

			adjacencyMatrix = new double[size][size];
		}

		public void setConnected(int vertexA, int vertexB, boolean connected) {
			adjacencyMatrix[Math.min(vertexA, vertexB)][Math.max(vertexA, vertexB)] = connected ? 1 : 0;
		}

		public boolean areConnected(int vertexA, int vertexB) {
			return adjacencyMatrix[Math.min(vertexA, vertexB)][Math.max(vertexA, vertexB)] == 1;
		}
		
		@Override
		public List<Edge> getAllEdges() {
			
			List<Edge> allEdges = new ArrayList<>();
			
			for(int i = 0 ; i < size ; i++) {
				for(int j = i + 1 ; j < size ; j++) {
					if(areConnected(i, j)) {
						allEdges.add(new Edge(i, j));
					}
				}
			}
			
			return allEdges;
		}
		
		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			
//			for(int i = 0 ; i < numberOfCities ; i++) {
//				for(int j = 0 ; j < numberOfCities ; j++) {
//					stringBuilder.append(String.format("%8.2f", getDistance(i, j)));
//				}
//				stringBuilder.append("\n");
//			}
			
			return stringBuilder.toString();
		}

	
}
