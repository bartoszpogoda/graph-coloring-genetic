package tsp.instance;

import java.util.List;

/***
 * Class responsible for holding TS Problem instance data
 * 
 * @author Student225988
 */
public abstract class AbstractInstance {
	
	protected int size;
	
	protected String name;
	
	public AbstractInstance(int size) {
		this.size = size;
	}

	public final int getSize() {
		return size;
	}

	public abstract void setConnected(int vertexA, int vertexB, boolean connected);

	public abstract boolean areConnected(int vertexA, int vertexB);

	
	public final void setName(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}

	public abstract List<Edge> getAllEdges();
	
	public abstract List<Edge> getAdjacentEdges(int vertex);
	
	// TODO implement toString
}
