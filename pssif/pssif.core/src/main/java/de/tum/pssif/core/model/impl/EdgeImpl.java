package de.tum.pssif.core.model.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.ConnectOperation;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class EdgeImpl extends ElementImpl implements Edge {
  private final Multimap<EdgeEnd, Node> nodes = HashMultimap.create();

  @Override
  public void apply(ConnectOperation op) {
    nodes.put(op.getEnd(), op.getNode());
  }

  @Override
  public void apply(DisconnectOperation op) {
    nodes.remove(op.getEnd(), op.getNode());
  }

  @Override
  public PSSIFOption<Node> apply(ReadConnectedOperation op) {
    return PSSIFOption.many(nodes.get(op.getEnd()));
  }

  @Override
  public String toString() {
    return "Edge (" + getId() + ")";
  }
}
