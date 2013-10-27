package de.tum.pssif.core.metamodel;

import java.util.Collection;

public interface Container {
	Node createNode(String name);

	Collection<Node> getNodes();

	Collection<Node> getAllNodes();

	Node findNode(String name);

	void delete(Node element);
}
