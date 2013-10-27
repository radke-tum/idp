package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.markers.Attributable;
import de.tum.pssif.core.metamodel.markers.Connectable;
import de.tum.pssif.core.metamodel.markers.Specializable;

public interface Edge extends Element, Specializable<Edge>,
		Connectable<Edge, Node>, Attributable {
}
