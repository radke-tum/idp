package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Attribute;


public interface Attributable {

  Attribute createAttribute(String name);

  Collection<Attribute> getAttributes();

  Attribute findAttribute(String name);

  void deleteAttribute(Attribute attribute);
}
