package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


/**
 * A type which describes nodes.
 */
public interface NodeType extends ElementType<NodeType, Node> {

  /**
   * Registers an incoming edge type to this node type.
   * @param type
   *    The incoming edge type to register.
   */
  void registerIncoming(EdgeType type);

  void deregisterIncoming(EdgeType type);

  /**
   * Registers an outgoing edge type to this node type.
   * @param type
   *    The outgoing edge type to register.
   */
  void registerOutgoing(EdgeType type);

  void deregisterOutgoing(EdgeType type);

  /**
   * Registers an auxiliary edge type to this node type.
   * @param type
   *    The auxiliary edge type to register.
   */
  void registerAuxiliary(EdgeType type);

  void deregisterAuxiliary(EdgeType type);

  /**
   * @return
   *    The collection of edge types leading to this node type.
   */
  Collection<EdgeType> getIncomings();

  /**
   * @return
   *    The collection of edge types which leave from this node type.
   */
  Collection<EdgeType> getOutgoings();

  /**
   * @return
   *    The collection of edge types for which this node type is an auxiliary type.
   */
  Collection<EdgeType> getAuxiliaries();

  /**
   * Finds an incoming edge type by its name.
   * @param name
   *    The name of the edge type.
   * @return
   *    The edge type, or <b>null</b> if none can be found.
   */
  EdgeType findIncomingEdgeType(String name);

  /**
   * Finds an outgoing edge type by its name.
   * @param name
   *    The name of the edge type.
   * @return
   *    The edge type, or <b>null</b> if none can be found.
   */
  EdgeType findOutgoingEdgeType(String name);

  /**
   * Finds an auxiliary edge type by its name.
   * @param name
   *    The name of the edge type.
   * @return
   *    The edge type, or <b>null</b> if none can be found.
   */
  EdgeType findAuxiliaryEdgeType(String name);

  /**
   * Creates a node instance of this node type in the provided model.
   * @param model
   *    The model in which to create the instance.
   * @return
   *    The created instance.
   */
  Node create(Model model);

  /**
   * Retrieves all nodes which are instances of this node type in a model.
   * @param model
   *    The model to search in.
   * @return
   *    The node instances found.
   */
  PSSIFOption<Node> apply(Model model);

  /**
   * Retrieves a node of this type with the specified id from the specified model
   * 
   * @param model the model
   * @param id the id
   * 
   * @return
   */
  PSSIFOption<Node> apply(Model model, String id);

  boolean isAssignableFrom(NodeType type);
}
