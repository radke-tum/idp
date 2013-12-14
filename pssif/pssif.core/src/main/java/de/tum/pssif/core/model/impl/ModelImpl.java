package de.tum.pssif.core.model.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class ModelImpl implements Model {
  private final Multimap<NodeType, Node>          nodes = ArrayListMultimap.create();
  private final Multimap<ConnectionMapping, Edge> edges = ArrayListMultimap.create();

  @Override
  public Node createNode(NodeType type) {
    Node result = new NodeImpl();
    nodes.put(type, result);
    return result;
  }

  @Override
  public Edge createEdge(ConnectionMapping type) {
    Edge result = new EdgeImpl();
    edges.put(type, result);
    return result;
  }

  @Override
  public PSSIFOption<Node> findAll(NodeType type) {
    return PSSIFOption.many(Sets.<Node> newHashSet(nodes.get(type)));
  }
}
