package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.markers.Attributable;
import de.tum.pssif.core.metamodel.markers.Connectable;
import de.tum.pssif.core.metamodel.markers.Specializable;

public interface Node extends Element, Specializable<Node>,
		Connectable<Node, Edge>, Attributable, Container {
}
