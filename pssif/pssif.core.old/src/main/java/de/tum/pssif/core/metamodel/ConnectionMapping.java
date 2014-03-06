package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


/**
 * An association between two node types over an (implicit) edge type.
 */
public interface ConnectionMapping {

  /**
   * @return
   *    The edge end associating the edge type with the incoming node type. 
   */
  EdgeEnd getFrom();

  /**
   * @return
   *    The edge end associating the edge type with the outgoing node type.
   */
  EdgeEnd getTo();

  /**
   * Creates a new edge.
   * 
   * @param model
   *    The model in which the edge should be created.
   * @param from
   *    The node which is instance of the from node type, associated over the from edge end.
   * @param to
   *    The node which is instance of the to node type, associated over the to edge end.
   * @return
   *    The created edge instance.
   */
  Edge create(Model model, Node from, Node to);

  /**
   * Get all nodes of this mapping for the specified model
   * 
   * @return
   */
  PSSIFOption<Edge> apply(Model model);

  /**
   * Connects an edge with a node, from which it starts.
   * <br><br>Note: Used for edges like, for example, XOR.
   * @param edge
   *    The edge to connect.
   * @param node
   *    The node from which the edge starts.
   */
  void connectFrom(Edge edge, Node node);

  /**
   * Connects an edge with a node, at which it leads.
   * <br><br>Note: Used for edges like, for example, XOR.
   * @param edge
   *    The edge to connect.
   * @param node
   *    the node at which the edge leads.
   */
  void connectTo(Edge edge, Node node);

  /**
   * Disconnects an edge from its starting (from) node.
   * <br><br>Note: Used for edges like, for example, XOR.
   * @param edge
   *    The edge to disconnect.
   * @param node
   *    The starting node.
   */
  void disconnectFrom(Edge edge, Node node);

  /**
   * Disconnects an edge from this destination (to) node.
   * <br><br>Note: Used for edges like, for example, XOR.
   * @param edge
   *    The edge to disconnect.
   * @param node
   *    The node to disconnect from.
   */
  void disconnectTo(Edge edge, Node node);
}
