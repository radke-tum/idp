package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.ElementType;

public interface Specializable<T extends ElementType> {
	T getGeneral();

	Collection<T> getSpecials();

	void inherit(T general);
}
