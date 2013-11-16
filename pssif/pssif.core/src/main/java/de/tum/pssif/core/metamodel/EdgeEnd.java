package de.tum.pssif.core.metamodel;

import java.util.Collection;

public interface EdgeEnd extends Named, Multiplicity {
	Collection<NodeType> getTypes();

	EdgeType getType();
}
