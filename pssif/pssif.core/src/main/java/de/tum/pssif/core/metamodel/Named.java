package de.tum.pssif.core.metamodel;

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
   * @return
   *    The meta-type of this entity.
   */
  Class<?> getMetaType();
}
