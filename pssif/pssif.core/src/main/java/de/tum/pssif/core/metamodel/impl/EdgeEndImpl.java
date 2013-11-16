package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;

public class EdgeEndImpl extends NamedImpl implements EdgeEnd {
	private final int lower;
	private final UnlimitedNatural upper;
	private final NodeType type;

	public EdgeEndImpl(String name, Multiplicity multiplicity, NodeType type) {
		super(name);
		lower = multiplicity.getLower();
		upper = multiplicity.getUpper();
		this.type = type;
	}

	@Override
	public int getLower() {
		return lower;
	}

	@Override
	public UnlimitedNatural getUpper() {
		return upper;
	}

	@Override
	public Collection<NodeType> getTypes() {
		return Sets.newHashSet(type);
	}
}
