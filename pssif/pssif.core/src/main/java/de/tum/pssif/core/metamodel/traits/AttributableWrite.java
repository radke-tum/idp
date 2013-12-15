package de.tum.pssif.core.metamodel.traits;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.Unit;


public interface AttributableWrite {

  Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible);

  Attribute createAttribute(AttributeGroup group, String name, DataType dataType, Unit unit, boolean visible);
}
