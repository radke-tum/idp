package de.tum.pssif.core.model.impl;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.EdgeEndImpl;
import de.tum.pssif.core.util.PSSIFUtil;


abstract class ElementImpl {

  protected Set<EdgeEndImpl> locateEdgeEnds(EdgeEnd edgeEnd, Collection<EdgeEndImpl> candidates) {
    Set<EdgeEndImpl> result = Sets.newHashSet();

    for (EdgeEndImpl impl : candidates) {
      if (PSSIFUtil.areSame(edgeEnd.getName(), impl.getName())) {
        result.add(impl);
      }
    }

    return result;
  }
}
