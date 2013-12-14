package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.util.PSSIFOption;


public interface Model {

  Node createNode(NodeType type);

  Edge createEdge(ConnectionMapping mapping, Node from, Node to);

  PSSIFOption<Node> findAll(NodeType type);
}
