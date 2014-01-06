package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.util.PSSIFValue;


/**
 * Data types describe the values of Attribute Types
 * in a PSS-IF Metamodel.
 */
public interface DataType extends Named {
  /**
   * Try to create a {@link PSSIFValue} of this {@link DataType} out of the specified {@link Object}
   * 
   * @param object 
   * @return
   */
  PSSIFValue fromObject(Object object);
}
