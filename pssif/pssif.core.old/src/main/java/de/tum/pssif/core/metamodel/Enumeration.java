package de.tum.pssif.core.metamodel;

import java.util.Collection;


/**
 * An erumeration data type within a PSS-IF Metamodel.
 * Can be used as the Data Type of many Attribute Types.
 */
public interface Enumeration extends DataType {

  /**
   * Creates a literal in this enumeration.
   * @param name
   *    The name of the literal.
   * @return
   *    The created literal.
   */
  EnumerationLiteral createLiteral(String name);

  /**
   * Removes a literal from this enumeration.
   * @param literal
   *    The literal to remove.
   */
  void removeLiteral(EnumerationLiteral literal);

  /**
   * @return
   *    The collection of all literals of this enumeration.
   */
  Collection<EnumerationLiteral> getLiterals();

  /**
   * Finds a literal by its name.
   * @param name
   *    The name of the literal.
   * @return
   *    The literal with this name, or <b>null</b> if it can not be found in this enumeration.
   */
  EnumerationLiteral findLiteral(String name);

}
