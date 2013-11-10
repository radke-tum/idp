package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.metamodel.traits.Attributable;
import de.tum.pssif.core.metamodel.traits.Specializable;


public interface NodeType extends ElementType, Attributable, Specializable<NodeType> {

  Collection<EdgeType> getIncomming();

  Collection<EdgeType> getOutgoing();

}
