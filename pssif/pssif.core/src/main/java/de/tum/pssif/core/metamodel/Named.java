package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.util.PSSIFOption;


/**
 * An entity with a name and a meta-type.
 * The meta-type is the type of metamodel element, described
 * through its corresponding interface class. For example,
 * the meta-type of a NodeType is the NodeType.class.
 */
public interface Named {

  /**
   * @return
   *    The name of this entity.
   */
  String getName();

  /**
   * @param name
   * @return
   *    Whether the name of one of the aliases of this named is the same as the provided name.
   */
  boolean hasName(String name);

  /**
   * @return
   *    The name and all aliases of this named.
   */
  PSSIFOption<String> getNames();

  /**
   * @return
   *    The meta-type of this entity.
   */
  Class<?> getMetaType();
}
