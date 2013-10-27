package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.markers.Attributable;
import de.tum.pssif.core.metamodel.markers.Connectable;
import de.tum.pssif.core.metamodel.markers.ModelApplicable;
import de.tum.pssif.core.metamodel.markers.Specializable;
import de.tum.pssif.core.model.EdgeInstance;

public interface Edge extends Element, Specializable<Edge>,
		Connectable<Edge, Node>, Attributable, ModelApplicable<EdgeInstance> {
}
