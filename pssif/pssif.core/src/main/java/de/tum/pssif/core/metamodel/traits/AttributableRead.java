package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.AttributeType;


/**
 * Describes the read and delete operations
 * allowed for {@link AttributeType}s
 * throughout a PSS-IF Metamodel.
 */
public interface AttributableRead {

  /**
   * Finds an Attribute Type by its name (case insensitive).
   * @param name
   *    The name of the Attribute Type.
   * @return
   *    The Attribute Type, of <b>null</b> if it does not exist in this context element.
   */
  AttributeType findAttribute(String name);

  /**
   * Retrieves all Attribute Types in this context,
   * including inherited Attribute Types.
   * @return
   *    A collection of all Attribute Types in the current context element.
   */
  Collection<AttributeType> getAttributes();

  /**
   * Removes an Attribute Type from this context element.
   * If the Attribute Type is inherited in this context, 
   * an exception is thrown.
   * @param attribute
   *    The attriubte to remove.
   */
  void removeAttribute(AttributeType attribute);

}
