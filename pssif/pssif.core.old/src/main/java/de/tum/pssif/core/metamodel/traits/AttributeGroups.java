package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.Named;


/**
 * Describes the operations allowed for {@link AttributeGroup}s.
 */
public interface AttributeGroups extends AttributableRead, AttributableWrite, Named {

  /**
   * Creates an Attribute Group with the provided name in the context element.
   * @param name
   *    The name of the Attribute Group to create.
   * @return
   *    The created Attribute Group.
   */
  AttributeGroup createAttributeGroup(String name);

  /**
   * Retrieves all Attribute Groups of the current context element.
   * TODO are Attribute Groups inherited?
   * @return
   *    The Attribute Groups of the context element.
   */
  Collection<AttributeGroup> getAttributeGroups();

  /**
   * Retrieves the default Attribute Group of the context element.
   * The default Attribute Group is predefined for all elements.
   * @return
   *    The default Attribute Group.
   */
  AttributeGroup getDefaultAttributeGroup();

  /**
   * Finds an Attribute Group by its name (case insensitive).
   * @param name
   *    The name of the Attribute Group.
   * @return
   *    The Attribute Group with the provided name, or <b>null</b> if it does not exist in the context element.
   */
  AttributeGroup findAttributeGroup(String name);

  void removeAttributeGroup(AttributeGroup attributeGroup);

}