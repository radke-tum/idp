package de.tum.pssif.core.model;

import java.util.Collection;

import de.tum.pssif.core.metamodel.EdgeEnd;

public interface Edge extends Element {
	Collection<Node> get(EdgeEnd end);
}
