package de.tum.pssif.core.metamodel;

import java.util.Collection;

import de.tum.pssif.core.metamodel.markers.Connectable;
import de.tum.pssif.core.model.NodeInstance;

public interface Attribute extends Element, Connectable<Attribute, DataType> {
	void setValue(NodeInstance instance, Object value);

	Collection<Object> getValue(NodeInstance instance);
}
