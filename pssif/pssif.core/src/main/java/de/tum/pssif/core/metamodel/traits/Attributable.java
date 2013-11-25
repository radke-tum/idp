package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;


public interface Attributable {

  Attribute createAttribute(String name, DataType type, Unit unit);

  Attribute findAttribute(String name);

  Collection<Attribute> getAttributes();

  void removeAttribute(Attribute attribute);
}
