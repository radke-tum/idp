package de.tum.pssif.core.model;

import java.util.Collection;

import de.tum.pssif.core.metamodel.NodeType;

public interface Model {
	Node createNode(NodeType type);

	Collection<Node> findAll(NodeType type);
}
