package de.tum.pssif.transform.transformation.artificial;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.ConnectOperation;
import de.tum.pssif.core.metamodel.impl.DisconnectOperation;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.metamodel.impl.ReadConnectedOperation;
import de.tum.pssif.core.metamodel.impl.SetValueOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


public class ArtificialEdge implements Edge {
  private final Edge              base;
  private Multimap<EdgeEnd, Node> nodes = HashMultimap.create();

  public ArtificialEdge(Edge base, EdgeEnd from, Collection<Node> froms, EdgeEnd to, Collection<Node> tos) {
    this.base = base;
    nodes.putAll(from, froms);
    nodes.putAll(to, tos);
  }

  @Override
  public String getId() {
    return base.getId();
  }

  @Override
  public void apply(SetValueOperation op) {
    base.apply(op);
  }

  @Override
  public PSSIFOption<PSSIFValue> apply(GetValueOperation op) {
    return base.apply(op);
  }

  @Override
  public Model getModel() {
    return base.getModel();
  }

  @Override
  public PSSIFOption<Node> apply(ReadConnectedOperation op) {
    if (nodes.containsKey(op.getEnd())) {
      return PSSIFOption.many(nodes.get(op.getEnd()));
    }
    else {
      return base.apply(op);
    }
  }

  @Override
  public void apply(ConnectOperation op) {
    if (nodes.containsKey(op.getEnd())) {
      nodes.put(op.getEnd(), op.getNode());
    }
    else {
      base.apply(op);
    }
  }

  @Override
  public void apply(DisconnectOperation op) {
    if (nodes.containsKey(op.getEnd())) {
      nodes.remove(op.getEnd(), op.getNode());
    }
    else {
      base.apply(op);
    }
  }

}
