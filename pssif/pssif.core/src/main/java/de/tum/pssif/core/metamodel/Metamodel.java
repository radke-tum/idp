package de.tum.pssif.core.metamodel;

import java.util.Collection;


/**
 * A PSS-IF Metamodel.
 */
public interface Metamodel {
  /**
   * Finds a node type by name.
   * @param name
   *    The name of the node type.
   * @return
   *    The node type, or <b>null</b> if it does not exist in this metamodel.
   */
  NodeType findNodeType(String name);

  /**
   * Finds an edge type by name.
   * @param name
   *    The name of the edge type.
   * @return
   *     The edge type, or <b>null</b> if it does not exist in this metamodel.
   */
  EdgeType findEdgeType(String name);

  /**
   * @return
   *    The collection of all node types in this metamodel. May not be <b>null</b>, but may be empty.
   */
  Collection<NodeType> getNodeTypes();

  /**
   * @return
   *    The collection of all edge types in this metamodel. May not be <b>null</b>, but may be empty.
   */
  Collection<EdgeType> getEdgeTypes();

  /**
   * @return
   *    The collection of all enumerations in this metamodel. May not be <b>null</b>, but may be empty.
   */
  Collection<Enumeration> getEnumerations();

  /**
   * Finds an enumeration by name.
   * @param name
   *    The name of the enumeration.
   * @return
   *     The enumeration , or <b>null</b> if it does not exist in this metamodel.
   */
  Enumeration findEnumeration(String name);

  /**
   * Finds a data type by name.
   * @param name
   *    The name of the data type.
   * @return
   *     The data type, or <b>null</b> if it does not exist in this metamodel.
   */
  DataType findDataType(String name);

  /**
   * @return
   *    The collection of all data types within this metamodel.
   */
  Collection<DataType> getDataTypes();

  /**
   * @return
   *    The collection of all primitive types within this metamodel.
   */
  Collection<PrimitiveDataType> getPrimitiveTypes();

  /**
   * Finds a primitive type by name.
   * @param name
   *    The name of the primitive type.
   * @return
   *     The primitive type, or <b>null</b> if it does not exist in this metamodel.
   */
  PrimitiveDataType findPrimitiveType(String name);

  /**
   * Adds an alias to an element type if no naming uniqueness
   * violation is caused by the alias.
   * @param elementType
   *    The element type to add an alias to.
   * @param alias
   *    The new alias.
   */
  void addAlias(ElementType<?> elementType, String alias);

  //TODO node and edge remove?

}
