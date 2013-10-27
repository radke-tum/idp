package de.tum.pssif.core.metamodel;

import java.util.Collection;

public interface Metamodel extends Container {
	Edge createEdge(String name);

	EnumerationType createEnumeration(String name);

	Collection<Edge> getEdges();

	Collection<EnumerationType> getEnumerations();

	Collection<PrimitiveType> getPrimitiveTypes();

	Edge findEdge(String name);

	EnumerationType findEnumeration(String name);

	void delete(Edge element);

	void delete(EnumerationType element);
}
