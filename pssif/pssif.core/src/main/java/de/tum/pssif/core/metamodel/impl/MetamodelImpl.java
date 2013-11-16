package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;

public class MetamodelImpl implements Metamodel {
	private Collection<NodeType> nodes = Sets.newHashSet();
	private Collection<EdgeType> edges = Sets.newHashSet();

	@Override
	public NodeType create(String name) {
		// TODO should we require name uniqueness? NodeTypes may have unique
		// names
		NodeType result = new NodeTypeImpl(name);
		nodes.add(result);
		return result;
	}

	@Override
	public EdgeType create(String name, String inName, NodeType inType,
			Multiplicity inMult, String outName, NodeType outType,
			Multiplicity outMult) {
		EdgeType result = new EdgeTypeImpl(name, inName, inType, inMult,
				outName, outType, outMult);
		edges.add(result);
		return result;
	}

	@Override
	public NodeType findNodeType(String name) {
		return findElement(name, nodes);
	}

	@Override
	public EdgeType findEdgeType(String name) {
		Collection<EdgeType> result = Sets.newHashSet();

		for (EdgeType edge : edges) {
			if (edge.getName().equals(name)) {
				result.add(edge);
			}
		}

		if (result.size() > 0) {
			return new EdgeTypeBundle(name, result);
		} else {
			return null;
		}
	}

	private <T extends ElementType> T findElement(String name,
			Collection<T> collection) {
		for (T element : collection) {
			if (element.getName().equals(name)) {
				return element;
			}
		}
		return null;
	}
}
