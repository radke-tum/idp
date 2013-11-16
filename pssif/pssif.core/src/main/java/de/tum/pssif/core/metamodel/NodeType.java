package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.metamodel.impl.NodeTypeImpl;
import de.tum.pssif.core.metamodel.traits.Specializable;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public interface NodeType extends ElementType,
		Specializable<NodeType, NodeTypeImpl> {
	void registerIncoming(EdgeType edge);

	void registerOutgoing(EdgeType edge);

	void registerAuxiliary(EdgeType edge);

	Collection<EdgeType> getEdgeTypes();

	Collection<EdgeType> getIncomings();

	Collection<EdgeType> getOutgoings();

	Collection<EdgeType> getAuxiliaries();

	EdgeType findEdgeType(String name);

	Node create(Model model);

	Collection<Node> apply(Model model);
}
