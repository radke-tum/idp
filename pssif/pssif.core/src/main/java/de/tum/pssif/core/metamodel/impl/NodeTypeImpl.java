package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableNodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public class NodeTypeImpl extends ElementTypeImpl<NodeType> implements MutableNodeType {
  public NodeTypeImpl(String name) {
    super(name);
  }

  @Override
  public Node create(Model model) {
    return new CreateNodeOperation(this).apply(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model, boolean includeSubtypes) {
    PSSIFOption<Node> result = PSSIFOption.none();
    if (includeSubtypes) {
      for (NodeType special : getSpecials()) {
        result = PSSIFOption.merge(result, special.apply(model, includeSubtypes));
      }
    }
    return PSSIFOption.merge(result, new ReadNodesOperation(this).apply(model));
  }

  @Override
  public PSSIFOption<Node> apply(Model model, String id, boolean includeSubtypes) {
    PSSIFOption<Node> result = PSSIFOption.none();
    if (includeSubtypes) {
      for (NodeType special : getSpecials()) {
        result = PSSIFOption.merge(result, special.apply(model, id, includeSubtypes));
      }
    }
    return PSSIFOption.merge(result, new ReadNodeOperation(this, id).apply(model));
  }

  @Override
  public Collection<NodeType> leftClosure(EdgeType edgeType, Node node) {
    return ImmutableSet.<NodeType> of(this);
  }

  @Override
  public Collection<NodeType> rightClosure(EdgeType edgeType, Node node) {
    return ImmutableSet.<NodeType> of(this);
  }

  @Override
  public int junctionIncomingEdgeCount(EdgeType edgeType, Node node) {
    return 0;
  }

  @Override
  public int junctionOutgoingEdgeCount(EdgeType edgeType, Node node) {
    return 0;
  }

  @Override
  public void onOutgoingEdgeCreated(Node sourceNode, ConnectionMapping mapping, Edge edge) {
    sourceNode.registerOutgoingEdge(mapping, edge);
  }

  @Override
  public void onIncomingEdgeCreated(Node targetNode, ConnectionMapping mapping, Edge edge) {
    targetNode.registerIncomingEdge(mapping, edge);
  }
}
