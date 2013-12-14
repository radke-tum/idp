package de.tum.pssif.core.model.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class NodeImpl extends ElementImpl implements Node {
  private final Multimap<EdgeEnd, Edge> edges = HashMultimap.create();

  @Override
  public void connect(EdgeEnd end, Edge edge) {
    edges.put(end, edge);
  }

  @Override
  public void disconnect(EdgeEnd end, Edge edge) {
    edges.remove(end, edge);
  }

  @Override
  public PSSIFOption<Edge> get(EdgeEnd end) {
    return PSSIFOption.many(edges.get(end));
  }
}
