package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.metamodel.Named;

public class NamedImpl implements Named {
	private final String name;

	public NamedImpl(String name) {
		this.name = name;
	}

	@Override
	public final String getName() {
		return name;
	}
}
