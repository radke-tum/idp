package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.metamodel.traits.Attributable;
import de.tum.pssif.core.metamodel.traits.Specializable;


public interface EdgeType extends ElementType, Attributable, Specializable<EdgeType> {

  //TODO class opposite edge type delegates to edge type, reverse mappings, on demand construction

  boolean isAcyclic();

  //NodeTypeMapping getNodeTypeMapping();

  void allow(NodeType from, NodeType to);

  void allow(NodeType aux);

  Collection<NodeType> getIncoming(NodeType forOutgoing);

  Collection<NodeType> getOutgoing(NodeType forIncoming);

  Collection<NodeType> getAuxiliaries(NodeType incoming, NodeType outgoing);

  boolean hasOpposite();

  /**
   * if has opposite-> opposite (e.g. contains/isContained)
   * if no opposite -> this
   * @return
   *    see above
   */
  EdgeType getOpposite();

}
