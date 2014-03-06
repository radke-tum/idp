package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public interface NodeType extends ElementType<NodeType> {
  Node create(Model model);

  PSSIFOption<Node> apply(Model model, boolean includeSubtypes);

  PSSIFOption<Node> apply(Model model, String id, boolean includeSubtypes);

  //  boolean isConnectionToTypeValid(Node sourceNode, EdgeType edgeType, NodeType toType);

  Collection<NodeType> leftClosure(EdgeType edgeType, Node node);

  //  boolean isConnectionFromTypeValid(Node targetNode, EdgeType edgeType, NodeType fromType);

  Collection<NodeType> rightClosure(EdgeType edgeType, Node node);

  void onOutgoingEdgeCreated(Node sourceNode, ConnectionMapping mapping, Edge edge);

  void onIncomingEdgeCreated(Node targetNode, ConnectionMapping mapping, Edge edge);
}
