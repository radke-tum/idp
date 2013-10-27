package de.tum.pssif.core.metamodel;

import de.tum.pssif.core.metamodel.markers.Attributable;
import de.tum.pssif.core.metamodel.markers.Connectable;
import de.tum.pssif.core.metamodel.markers.ModelApplicable;
import de.tum.pssif.core.metamodel.markers.Specializable;
import de.tum.pssif.core.model.NodeInstance;

public interface Node extends Element, Specializable<Node>,
		Connectable<Node, Edge>, Attributable, Container,
		ModelApplicable<NodeInstance> {
}
