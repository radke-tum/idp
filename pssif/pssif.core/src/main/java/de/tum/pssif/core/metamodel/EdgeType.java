package de.tum.pssif.core.metamodel;

import java.util.Collection;

import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.traits.Specializable;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public interface EdgeType extends ElementType, Specializable<EdgeType> {
	EdgeEnd createAuxiliaryEnd(String name, Multiplicity mult, NodeType to);

	Collection<EdgeEnd> getEnds();

	EdgeEnd getIncoming();

	EdgeEnd getOutgoing();

	Collection<EdgeEnd> getAuxiliaries();

	Edge create(Model model, Multimap<EdgeEnd, Node> connections);
}
