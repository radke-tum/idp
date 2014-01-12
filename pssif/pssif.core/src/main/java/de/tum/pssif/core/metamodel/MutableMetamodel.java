package de.tum.pssif.core.metamodel;

public interface MutableMetamodel extends Metamodel {
  /**
   * Creates a node type with the provided name.
   * @param name
   *    The name of the node type.
   * @return
   *    The created node type.
   */
  NodeType createNodeType(String name);

  /**
   * Creates an edge type with the provided name.
   * @param name
   *    The name of the edge type.
   * @return
   *    The created edge type.
   */
  EdgeType createEdgeType(String name);

  /**
   * Creates an enumeration with the provided name in this metamodel.
   * @param name
   *    The name of the enumeration to create.
   * @return
   *    The created enumeration.
   */
  Enumeration createEnumeration(String name);

  /**
   * Removes an enumeration, and all attribute types which have it as a data type, from this metamodel.
   * @param enumeration
   *    The enumeration to remove.
   */
  void removeEnumeration(Enumeration enumeration);
}
