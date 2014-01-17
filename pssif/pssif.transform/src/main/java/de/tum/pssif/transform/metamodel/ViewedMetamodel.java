package de.tum.pssif.transform.metamodel;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;


public interface ViewedMetamodel extends Metamodel {

  <T extends ElementType<T>> T rename(T elementType, String name);

  //implicit contains relation?
  //multiplicity for attributes required
  //type is stirng, group is default
  Attribute typify(String name);

  //produces an aux edge/node type. also multiplicities, node type should be from base metamodel (for the ports)
  <T extends ElementType<T>> T auxiliarify(Attribute attribute, NodeType nodeType);

  <T extends ElementType<T>> T abstractify(T concreteType, T abstractType, String annotationKey);

}
