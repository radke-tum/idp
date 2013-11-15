package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public interface NodeType extends ElementType {
	Collection<EdgeType> getEdgeTypes();

	Collection<EdgeType> getIncomings();

	Collection<EdgeType> getOutgoings();

	Collection<EdgeType> getAuxiliaries();

	EdgeType findEdgeType(String name);

	Node create(Model model);
}
