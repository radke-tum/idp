package org.pssif.consistencyDataStructures;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Node;

public class NodeAndType {

	/**
	 * @param node
	 * @param type
	 */
	public NodeAndType(Node node, NodeType type) {
		super();
		this.node = node;
		this.type = type;
	}
	private Node node;
	private NodeType type;
	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}
	/**
	 * @return the type
	 */
	public NodeType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(NodeType type) {
		this.type = type;
	}
	
}
