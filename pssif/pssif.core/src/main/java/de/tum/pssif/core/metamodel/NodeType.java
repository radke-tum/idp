package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public interface NodeType extends ElementType<NodeType> {
  void registerIncoming(EdgeType type);

  void registerOutgoing(EdgeType type);

  void registerAuxiliary(EdgeType type);

  Collection<EdgeType> getIncomings();

  Collection<EdgeType> getOutgoings();

  Collection<EdgeType> getAuxiliaries();

  EdgeType findIncomingEdgeType(String name);

  EdgeType findOutgoingEdgeType(String name);

  EdgeType findAuxiliaryEdgeType(String name);

  Node create(Model model);

  PSSIFOption<Node> apply(Model model);
}
