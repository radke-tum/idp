package de.tum.pssif.core.metamodel;

import java.util.Collection;

public interface Port<I extends Element, O extends Element> {
	Collection<O> getOutgoing();

	Collection<I> getIncoming();

	void addIncoming(I incoming);

	void removeIncoming(I incoming);

	void addOutgoing(O outgoing);

	void removeOutgoing(O outgoing);
}
