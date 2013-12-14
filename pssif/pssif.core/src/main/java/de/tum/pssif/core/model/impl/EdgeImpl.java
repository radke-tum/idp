package de.tum.pssif.core.model.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class EdgeImpl extends ElementImpl implements Edge {
  private final Multimap<EdgeEnd, Node> nodes = HashMultimap.create();

  @Override
  public void connect(EdgeEnd end, Node node) {
    nodes.put(end, node);
    node.connect(end, this);
  }

  @Override
  public void disconnect(EdgeEnd end, Node node) {
    nodes.remove(end, node);
    node.disconnect(end, this);
  }

  @Override
  public PSSIFOption<Node> get(EdgeEnd end) {
    return PSSIFOption.many(nodes.get(end));
  }

}
