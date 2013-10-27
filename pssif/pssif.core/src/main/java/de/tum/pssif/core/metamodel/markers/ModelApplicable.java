package de.tum.pssif.core.metamodel.markers;

import java.util.Collection;

import de.tum.pssif.core.model.ElementInstance;
import de.tum.pssif.core.model.Model;

public interface ModelApplicable<T extends ElementInstance> {
	Collection<T> findAll(Model model);

	T create(Model model);

	void delete(T instance, Model model);
}
