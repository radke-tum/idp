package de.tum.pssif.core.model;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.operation.CreateEdgeOperation;
import de.tum.pssif.core.model.operation.CreateNodeOperation;
import de.tum.pssif.core.util.PSSIFOption;


public interface Model {

  Node createNode(CreateNodeOperation createOperation);

  Edge createEdge(CreateEdgeOperation createOperation);

  PSSIFOption<Node> findAll(NodeType type);
}
