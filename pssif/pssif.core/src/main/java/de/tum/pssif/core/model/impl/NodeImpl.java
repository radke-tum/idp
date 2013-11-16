package de.tum.pssif.core.model.impl;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.EdgeEndImpl;
import de.tum.pssif.core.metamodel.impl.NodeTypeImpl;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class NodeImpl extends ElementImpl implements Node {

  private final Multimap<EdgeEndImpl, EdgeImpl> edges;

  public NodeImpl(NodeTypeImpl nodeType) {
    this.edges = HashMultimap.create();
    //    for (EdgeEndImpl edgeEndImpl : nodeType.getEdgeEndsImpl()) {
    //      edges.put(edgeEndImpl, null);
    //    }
  }

  protected void set(EdgeEndImpl edgeEnd, EdgeImpl edge) {
    this.edges.put(edgeEnd, edge);
  }

  @Override
  public PSSIFOption<Edge> get(EdgeEnd end) {
    Set<EdgeEndImpl> localEdgeEnds = locateEdgeEnds(end, edges.keySet());
    Set<Edge> result = Sets.newHashSet();
    for (EdgeEndImpl endImpl : localEdgeEnds) {
      result.addAll(edges.get(endImpl));
    }
    return PSSIFOption.many(result);
  }
}
