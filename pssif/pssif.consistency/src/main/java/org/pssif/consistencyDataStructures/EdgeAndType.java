package org.pssif.consistencyDataStructures;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.model.Edge;

public class EdgeAndType {

	/**
	 * @param edge
	 * @param type
	 */
	public EdgeAndType(Edge edge, EdgeType type) {
		super();
		this.edge = edge;
		this.type = type;
	}
	private Edge edge;
	private EdgeType type;
	/**
	 * @return the edge
	 */
	public Edge getEdge() {
		return edge;
	}
	/**
	 * @param edge the edge to set
	 */
	public void setEdge(Edge edge) {
		this.edge = edge;
	}
	/**
	 * @return the type
	 */
	public EdgeType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(EdgeType type) {
		this.type = type;
	}
	
	 
	
}
