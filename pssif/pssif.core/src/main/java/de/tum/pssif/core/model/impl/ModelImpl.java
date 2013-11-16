package de.tum.pssif.core.model.impl;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

public class ModelImpl implements Model {
	private final Multimap<NodeType, Node> nodes = ArrayListMultimap.create();

	@Override
	public Node createNode(NodeType type) {
		Node result = new NodeImpl();
		nodes.put(type, result);
		return result;
	}

	@Override
	public Collection<Node> findAll(NodeType type) {
		return nodes.get(type);
	}
}
