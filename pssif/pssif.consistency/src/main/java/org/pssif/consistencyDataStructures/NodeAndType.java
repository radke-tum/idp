package org.pssif.consistencyDataStructures;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.model.Node;

public class NodeAndType {

	/**
	 * @param node
	 * @param actNodeType
	 */
	public NodeAndType(Node node, NodeTypeBase actNodeType) {
		super();
		this.node = node;
		this.type = actNodeType;
	}
	private Node node;
	private NodeTypeBase type;
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
	public NodeTypeBase getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(NodeType type) {
		this.type = type;
	}
	
}
