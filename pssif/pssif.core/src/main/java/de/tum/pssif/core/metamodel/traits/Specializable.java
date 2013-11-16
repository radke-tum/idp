package de.tum.pssif.core.metamodel.traits;

import java.util.Collection;

import de.tum.pssif.core.metamodel.ElementType;

public interface Specializable<T extends ElementType, I extends T> {
	T getGeneral();

	Collection<T> getSpecials();

	void inherit(T general);

	// FIXME remove this from public api
	void registerSpecialization(I special);

	// FIXME remove this from public api
	void registerGeneralization(I general);
}
