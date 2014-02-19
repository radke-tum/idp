package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.metamodel.traits.ElementApplicable;
import de.tum.pssif.core.model.Edge;


/**
 * A type which describes edges.
 */
public interface EdgeType extends ElementType<EdgeType, Edge> {

  /**
   * Creates a connection mapping through this edge end between an incoming and an outgoing node type.
   * @param inName
   *    The name of the incoming edge end.
   * @param in
   *    The incoming node type.
   * @param inMultiplicity
   *    The multiplicity of the incoming edge end.
   * @param outName
   *    The name of the outgoing edge end.
   * @param out
   *    The outgoing node type.
   * @param outMultiplicity
   *    The multiplicity of the outgoing edge end.
   * @return
   *    The created connection mapping.
   */
  ConnectionMapping createMapping(String inName, NodeType in, Multiplicity inMultiplicity, String outName, NodeType out, Multiplicity outMultiplicity);

  /**
   * Creates an auxiliary edge end for this edge type.
   * @param name
   *    The name of the auxiliary end.
   * @param multiplicity
   *    The multiplicity of the auxiliary end.
   * @param to
   *    The node type to which the auxiliary end should lead.
   * @return
   *    The created edge end.
   */
  EdgeEnd createAuxiliary(String name, Multiplicity multiplicity, NodeType to);

  /**
   * @return
   *    The incoming edge end, or bundle of edge ends, of this edge type.
   */
  ElementApplicable getIncoming();

  /**
   * @return
   *    The outgoing edge end, or bundle of edge ends, of this edge type.
   */
  ElementApplicable getOutgoing();

  /**
   * Retrieves a connection mapping of this edge type, defined by from and to node types.
   * @param in
   *    The incoming node type.
   * @param out
   *    The outgoing node type.
   * @return
   *    The connection mapping of this edge type between those two specific node types,
   *    or <b>null</b> if no such connection mapping exists.
   */
  ConnectionMapping getMapping(NodeType in, NodeType out);

  Collection<ConnectionMapping> getMappings();

  /**
   * @return
   *    The collection of auxiliary edge ends.
   */
  Collection<EdgeEnd> getAuxiliaries();

  /**
   * Finds an auxiliary edge end by name.
   * @param name
   *    The name of the auxiliary edge end.
   * @return
   *    The auxiliary edge end, or <b>null</b> if none exists.
   */
  EdgeEnd findAuxiliary(String name);
}
