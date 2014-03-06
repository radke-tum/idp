package de.tum.pssif.core.model.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.impl.CreateEdgeOperation;
import de.tum.pssif.core.metamodel.impl.CreateJunctionNodeOperation;
import de.tum.pssif.core.metamodel.impl.CreateNodeOperation;
import de.tum.pssif.core.metamodel.impl.ReadEdgesOperation;
import de.tum.pssif.core.metamodel.impl.ReadJunctionNodeOperation;
import de.tum.pssif.core.metamodel.impl.ReadJunctionNodesOperation;
import de.tum.pssif.core.metamodel.impl.ReadNodeOperation;
import de.tum.pssif.core.metamodel.impl.ReadNodesOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public class ModelImpl implements Model {
  private Multimap<String, Node>                         nodes         = HashMultimap.create();
  private Multimap<String, JunctionNode>                 junctionNodes = HashMultimap.create();
  private Multimap<ConnectionMappingSignature, Edge> edges         = HashMultimap.create();

  @Override
  public Node apply(CreateNodeOperation op) {
    Node result = new NodeImpl();
    nodes.put(op.getType().getName(), result);
    return result;
  }

  @Override
  public JunctionNode apply(CreateJunctionNodeOperation op) {
    JunctionNode result = new JunctionNodeImpl();
    junctionNodes.put(op.getType().getName(), result);
    return result;
  }

  @Override
  public Edge apply(CreateEdgeOperation op) {
    Edge result = new EdgeImpl(op.getFrom(), op.getTo());
    edges.put(new ConnectionMappingSignature(op.getMapping()), result);
    return result;
  }

  @Override
  public PSSIFOption<Node> apply(ReadNodesOperation op) {
    return PSSIFOption.many(nodes.get(op.getType().getName()));
  }

  @Override
  public PSSIFOption<Node> apply(ReadNodeOperation op) {
    for (Node n : nodes.get(op.getType().getName())) {
      if (n.getId().equals(op.getId())) {
        return PSSIFOption.one(n);
      }
    }
    return PSSIFOption.none();
  }

  @Override
  public PSSIFOption<JunctionNode> apply(ReadJunctionNodesOperation op) {
    return PSSIFOption.many(junctionNodes.get(op.getType().getName()));
  }

  @Override
  public PSSIFOption<JunctionNode> apply(ReadJunctionNodeOperation op) {
    for (JunctionNode n : junctionNodes.get(op.getType().getName())) {
      if (n.getId().equals(op.getId())) {
        return PSSIFOption.one(n);
      }
    }
    return PSSIFOption.none();
  }

  @Override
  public PSSIFOption<Edge> apply(ReadEdgesOperation op) {
    for (ConnectionMappingSignature candidate : edges.keySet()) {
      if (candidate.isCompatibleWith(op.getMapping())) {
        return PSSIFOption.many(edges.get(candidate));
      }
    }
    return PSSIFOption.none();
  }
}
