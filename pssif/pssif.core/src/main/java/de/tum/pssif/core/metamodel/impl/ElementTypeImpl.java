package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.ElementType;

public abstract class ElementTypeImpl implements ElementType {
	private final MetamodelImpl metamodel;
	private final String name;

	public ElementTypeImpl(MetamodelImpl metamodel, String name) {
		this.metamodel = metamodel;
		this.name = name;
	}

	@Override
	public final String getName() {
		return name;
	}

	protected final MetamodelImpl getMetamodel() {
		return metamodel;
	}
}
