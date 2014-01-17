package de.tum.pssif.core.metamodel.traits;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;


/**
 * Describes the create operations allowed for
 * Attribute Types in a given context element.
 */
public interface AttributableWrite {

  /**
   * Creates an Attribute Type with the following parameters:
   * @param group
   *    The Attribute Group in which the Attribute Type should be created.
   *    TODO clarify uniqueness in groups and overloading.
   * @param name
   *    The name of the Attribute Type to create.
   * @param dataType
   *    The Data Type of the Attribute Type to create.
   * @param visible
   *    Whether the Attribute Type should be visible in UI and visualizations.
   * @param category
   *    The Attribute Category of the Attribute Type. See {@link AttributeCategory}.
   * @return
   *    The newly created Attribute Type.
   */
  Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible, AttributeCategory category);

  /**
   * Creates an Attribute Type with the following parameters:
   * @param group
   *    The Attribute Group in which the Attribute Type should be created.
   *    TODO clarify uniqueness in groups and overloading.
   * @param name
   *    The name of the Attribute Type to create.
   * @param dataType
   *    The Data Type of the Attribute Type to create.
   * @param unit
   *    The Unit to use for this attribute type. Note that Units are only allowed for numeric Attribute Types (Data Types INTEGER and DECIMAL).
   * @param visible
   *    Whether the Attribute Type should be visible in UI and visualizations.
   * @param category
   *    The Attribute Category of the Attribute Type. See {@link AttributeCategory}.
   * @return
   *    The newly created Attribute Type.
   */
  Attribute createAttribute(AttributeGroup group, String name, DataType dataType, Unit unit, boolean visible, AttributeCategory category);
}
