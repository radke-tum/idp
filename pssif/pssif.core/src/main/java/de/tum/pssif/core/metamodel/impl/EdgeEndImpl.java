package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Node;

public class EdgeEndImpl extends NamedImpl implements EdgeEnd {
	private final int lower;
	private final UnlimitedNatural upper;
	private final EdgeType edge;
	private final NodeType type;

	public EdgeEndImpl(String name, EdgeType edge, Multiplicity multiplicity,
			NodeType type) {
		super(name);
		this.edge = edge;
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

	@Override
	public EdgeType getType() {
		return edge;
	}

	@Override
	public Collection<Node> nodes(Collection<Edge> edges) {
		Collection<Node> result = Sets.newHashSet();

		for (Edge edge : edges) {
			result.addAll(edge.get(this));
		}

		return Collections.unmodifiableCollection(result);
	}

	@Override
	public Collection<Edge> edges(Collection<Node> nodes) {
		Collection<Edge> result = Sets.newHashSet();

		for (Node node : nodes) {
			result.addAll(node.get(this));
		}

		return Collections.unmodifiableCollection(result);
	}
}
