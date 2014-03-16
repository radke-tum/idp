package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.traits.Specializable;


public interface EdgeType extends ElementType, Specializable<EdgeType> {
  PSSIFOption<ConnectionMapping> getMappings();

  PSSIFOption<ConnectionMapping> getMapping(NodeTypeBase from, NodeTypeBase to);

  PSSIFOption<ConnectionMapping> getOutgoingMappings(NodeTypeBase from);

  PSSIFOption<ConnectionMapping> getIncomingMappings(NodeTypeBase to);
}
