package de.tum.pssif.core.model.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.CreateEdgeOperation;
import de.tum.pssif.core.metamodel.impl.CreateNodeOperation;
import de.tum.pssif.core.metamodel.impl.ReadNodesOperation;
import de.tum.pssif.core.metamodel.impl.base.ReadEdgesOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class ModelImpl implements Model {
  private final Multimap<NodeType, Node>          nodes = ArrayListMultimap.create();
  private final Multimap<ConnectionMapping, Edge> edges = ArrayListMultimap.create();

  @Override
  public Node apply(CreateNodeOperation op) {
    Node result = new NodeImpl();
    nodes.put(op.getType(), result);
    return result;
  }

  @Override
  public Edge apply(CreateEdgeOperation op) {
    Edge result = new EdgeImpl();
    op.getMapping().connectFrom(result, op.getFrom());
    op.getMapping().connectTo(result, op.getTo());
    edges.put(op.getMapping(), result);
    return result;
  }

  @Override
  public PSSIFOption<Node> apply(ReadNodesOperation op) {
    return PSSIFOption.many(Sets.<Node> newHashSet(nodes.get(op.getType())));
  }

  @Override
  public PSSIFOption<Edge> apply(ReadEdgesOperation op) {
    return PSSIFOption.many(edges.get(op.getMapping()));
  }
}
