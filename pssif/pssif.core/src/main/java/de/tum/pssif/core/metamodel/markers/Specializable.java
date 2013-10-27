package de.tum.pssif.core.metamodel.markers;

import java.util.Collection;

import de.tum.pssif.core.metamodel.Element;

public interface Specializable<T extends Element> {
	T getParent();

	Collection<T> getChildren();

	void addChild(T child);

	void removeChild(T child);
}
