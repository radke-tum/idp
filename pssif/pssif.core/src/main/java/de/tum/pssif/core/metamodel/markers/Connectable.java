package de.tum.pssif.core.metamodel.markers;

import de.tum.pssif.core.metamodel.Element;
import de.tum.pssif.core.metamodel.Port;

public interface Connectable<L extends Element, R extends Element> {
	Port<R, L> getInPort();

	Port<L, R> getOutPort();
}
