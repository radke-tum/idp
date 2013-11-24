package de.tum.pssif.core.model.impl;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.EdgeEndImpl;
import de.tum.pssif.core.metamodel.impl.EdgeTypeImpl;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class EdgeImpl extends ElementImpl implements Edge {

  private final Multimap<EdgeEndImpl, NodeImpl> nodes;

  public EdgeImpl(EdgeTypeImpl edgeType, Multimap<EdgeEndImpl, NodeImpl> connections) {
    nodes = HashMultimap.create(connections);
  }

  @Override
  public PSSIFOption<Node> get(EdgeEnd end) {
    Set<EdgeEndImpl> localEdgeEnds = locateEdgeEnds(end, nodes.keySet());
    Set<Node> result = Sets.newHashSet();
    for (EdgeEndImpl endImpl : localEdgeEnds) {
      result.addAll(nodes.get(endImpl));
    }
    return PSSIFOption.many(result);
  }

}
