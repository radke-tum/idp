package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Attribute;


public interface AttributableRead {

  Attribute findAttribute(String name);

  Collection<Attribute> getAttributes();

  void removeAttribute(Attribute attribute);

}
