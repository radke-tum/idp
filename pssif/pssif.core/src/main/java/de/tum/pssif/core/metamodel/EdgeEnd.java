package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;

public interface EdgeEnd extends Named, Multiplicity {
	Collection<NodeType> getTypes();

	EdgeType getType();

	Collection<Node> nodes(Collection<Edge> edges);

	Collection<Edge> edges(Collection<Node> nodes);
}
