package de.tum.pssif.core.model.impl;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.impl.EdgeEndImpl;


abstract class ElementImpl {

  protected Set<EdgeEndImpl> locateEdgeEnds(EdgeEnd edgEnd, Collection<EdgeEndImpl> candidates) {
    //TODO magic with names and types and stuff. will need more from the impl
    return Sets.newHashSet();
  }
}
