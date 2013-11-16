package de.tum.pssif.core.model;

import java.util.Collection;

import de.tum.pssif.core.metamodel.EdgeEnd;

public interface Node extends Element {
	Collection<Edge> get(EdgeEnd end);
}
