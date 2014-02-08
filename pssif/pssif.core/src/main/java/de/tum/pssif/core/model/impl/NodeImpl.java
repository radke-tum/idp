package de.tum.pssif.core.model.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.ConnectOperation;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class NodeImpl extends ElementImpl implements Node {
  private final Multimap<EdgeEnd, Edge> edges = HashMultimap.create();

  public NodeImpl(Model model) {
    super(model);
  }

  @Override
  public void apply(ConnectOperation op) {
    edges.put(op.getEnd(), op.getEdge());
  }

  @Override
  public void apply(DisconnectOperation op) {
    edges.remove(op.getEnd(), op.getEdge());
  }

  @Override
  public PSSIFOption<Edge> apply(ReadConnectedOperation op) {
    return PSSIFOption.many(edges.get(op.getEnd()));
  }

  @Override
  public String toString() {
    return "Node (" + getId() + ")";
  }
}
