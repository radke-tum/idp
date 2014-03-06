package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableJunctionNodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public class JunctionNodeTypeImpl extends NodeTypeImpl implements MutableJunctionNodeType {
  public JunctionNodeTypeImpl(String name) {
    super(name);
  }

  @Override
  public JunctionNode create(Model model) {
    return new CreateJunctionNodeOperation(this).apply(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model, boolean includeSubtypes) {
    PSSIFOption<Node> result = PSSIFOption.none();
    if (includeSubtypes) {
      for (NodeType special : getSpecials()) {
        result = PSSIFOption.merge(result, special.apply(model, includeSubtypes));
      }
    }
    return PSSIFOption.merge(result, new ReadJunctionNodesOperation(this).apply(model));
  }

  @Override
  public PSSIFOption<Node> apply(Model model, String id, boolean includeSubtypes) {
    PSSIFOption<Node> result = PSSIFOption.none();
    if (includeSubtypes) {
      for (NodeType special : getSpecials()) {
        result = PSSIFOption.merge(result, special.apply(model, id, includeSubtypes));
      }
    }
    return PSSIFOption.merge(result, new ReadJunctionNodeOperation(this, id).apply(model));
  }

  //  @Override
  //  public boolean isConnectionFromTypeValid(Node node, EdgeType edgeType, NodeType fromType) {
  //    //TODO check cardinality constraints (either many incoming or outgoing edges, exclusive or!)
  //    PSSIFOption<ConnectionMapping> mappings = edgeType.getMapping(fromType, this);
  //    if (!mappings.isOne()) {
  //      return false;
  //    }
  //
  //    ConnectionMapping mapping = mappings.getOne();
  //    for (ConnectionMapping outgoingMapping : mapping.getType().getOutgoingMappings(mapping.getTo()).getMany()) {
  //      PSSIFOption<Edge> outgoingEdges = outgoingMapping.applyOutgoing(node);
  //      if (outgoingEdges.size() > 0) {
  //        for (Edge outgoingEdge : outgoingEdges.getMany()) {
  //          Node outgoingConnected = outgoingMapping.applyTo(outgoingEdge); //(2)
  //          if (!outgoingMapping.getTo().isConnectionFromTypeValid(outgoingConnected, edgeType, fromType)) {
  //            return false;
  //          }
  //        }
  //      }
  //    }
  //
  //    return true;
  //  }

  @Override
  public Collection<NodeType> leftClosure(EdgeType edgeType, Node node) {
    Collection<NodeType> result = Sets.<NodeType> newHashSet(this);
    for (ConnectionMapping incomingMapping : edgeType.getIncomingMappings(this).getMany()) {
      for (Edge incomingEdge : incomingMapping.applyIncoming(node).getMany()) {
        Node fromConnected = incomingMapping.applyFrom(incomingEdge);
        result.addAll(incomingMapping.getFrom().leftClosure(edgeType, fromConnected));
      }
    }
    return result;
  }

  //
  //  //TODO avoid loops within junction chains!!!
  //  @Override
  //  public boolean isConnectionToTypeValid(Node node /* (1) */, EdgeType edgeType, NodeType toType) {
  //    //TODO check cardinality constraints (either many incoming or outgoing edges, exclusive or!)
  //
  //    PSSIFOption<ConnectionMapping> mappings = edgeType.getMapping(this, toType);
  //    if (!mappings.isOne()) {
  //      return false;
  //    }
  //
  //    ConnectionMapping mapping = mappings.getOne();
  //    for (ConnectionMapping incomingMapping : mapping.getType().getIncomingMappings(mapping.getFrom()).getMany()) {
  //      PSSIFOption<Edge> incomingEdges = incomingMapping.applyIncoming(node);
  //      if (incomingEdges.size() > 0) {
  //        for (Edge incomingEdge : incomingEdges.getMany()) {
  //          Node incomingConnected = incomingMapping.applyFrom(incomingEdge); //(2)
  //          if (!incomingMapping.getFrom().isConnectionToTypeValid(incomingConnected, edgeType, toType)) {
  //            return false;
  //          }
  //        }
  //      }
  //    }
  //
  //    return true;
  //  }

  @Override
  public Collection<NodeType> rightClosure(EdgeType edgeType, Node node) {
    Collection<NodeType> result = Sets.<NodeType> newHashSet(this);
    for (ConnectionMapping outgoingMapping : edgeType.getOutgoingMappings(this).getMany()) {
      for (Edge outgoingEdge : outgoingMapping.applyOutgoing(node).getMany()) {
        Node toConnected = outgoingMapping.applyTo(outgoingEdge);
        result.addAll(outgoingMapping.getTo().rightClosure(edgeType, toConnected));
      }
    }
    return result;
  }

  @Override
  public void onIncomingEdgeCreated(Node targetNode, ConnectionMapping mapping, Edge edge) {
    // TODO Auto-generated method stub
    super.onIncomingEdgeCreated(targetNode, mapping, edge);
  }

  @Override
  public void onOutgoingEdgeCreated(Node sourceNode, ConnectionMapping mapping, Edge edge) {
    // TODO Auto-generated method stub
    super.onOutgoingEdgeCreated(sourceNode, mapping, edge);
  }
}
