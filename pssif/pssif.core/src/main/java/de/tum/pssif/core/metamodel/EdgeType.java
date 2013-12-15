package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.metamodel.traits.ElementApplicable;


public interface EdgeType extends ElementType<EdgeType> {
  ConnectionMapping createMapping(String inName, NodeType in, Multiplicity inMultiplicity, String outName, NodeType out, Multiplicity outMultiplicity);

  EdgeEnd createAuxiliary(String name, Multiplicity multiplicity, NodeType to);

  ElementApplicable getIncoming();

  ElementApplicable getOutgoing();

  ConnectionMapping getMapping(NodeType in, NodeType out);

  Collection<EdgeEnd> getAuxiliaries();

  EdgeEnd findAuxiliary(String name);
}
