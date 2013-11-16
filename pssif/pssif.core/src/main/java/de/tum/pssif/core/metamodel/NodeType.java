package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.metamodel.impl.NodeTypeImpl;
import de.tum.pssif.core.metamodel.traits.Specializable;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public interface NodeType extends ElementType, Specializable<NodeType, NodeTypeImpl> {

  Collection<EdgeType> getEdgeTypes();

  Collection<EdgeType> getIncomings();

  Collection<EdgeType> getOutgoings();

  Collection<EdgeType> getAuxiliaries();

  EdgeType findEdgeType(String name);

  Node create(Model model);

  PSSIFOption<Node> apply(Model model);
}
