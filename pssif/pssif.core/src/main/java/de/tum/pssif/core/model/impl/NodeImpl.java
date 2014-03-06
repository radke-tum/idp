package de.tum.pssif.core.model.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.impl.ReadIncomingNodesOperation;
import de.tum.pssif.core.metamodel.impl.ReadOutgoingNodesOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;


public class NodeImpl extends ElementImpl implements Node {
  private Multimap<ConnectionMappingSerialization, Edge> outgoingEdges = HashMultimap.create();
  private Multimap<ConnectionMappingSerialization, Edge> incomingEdges = HashMultimap.create();

  @Override
  public PSSIFOption<Edge> apply(ReadOutgoingNodesOperation op) {
    for (ConnectionMappingSerialization candidate : outgoingEdges.keySet()) {
      if (candidate.isCompatibleWith(op.getMapping())) {
        return PSSIFOption.many(outgoingEdges.get(candidate));
      }
    }
    return PSSIFOption.none();
  }

  @Override
  public PSSIFOption<Edge> apply(ReadIncomingNodesOperation op) {
    for (ConnectionMappingSerialization candidate : incomingEdges.keySet()) {
      if (candidate.isCompatibleWith(op.getMapping())) {
        return PSSIFOption.many(incomingEdges.get(candidate));
      }
    }
    return PSSIFOption.none();
  }

  @Override
  public void registerOutgoingEdge(ConnectionMapping mapping, Edge edge) {
    outgoingEdges.put(new ConnectionMappingSerialization(mapping), edge);
  }

  @Override
  public void registerIncomingEdge(ConnectionMapping mapping, Edge edge) {
    incomingEdges.put(new ConnectionMappingSerialization(mapping), edge);
  }
}
