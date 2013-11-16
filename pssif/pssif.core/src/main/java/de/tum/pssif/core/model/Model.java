package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.NodeType;

public interface Model {
	Node createNode(NodeType type);
}
