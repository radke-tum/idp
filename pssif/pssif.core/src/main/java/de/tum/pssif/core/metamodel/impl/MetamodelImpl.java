package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;

public class MetamodelImpl implements Metamodel {
	private Collection<NodeTypeImpl> nodeTypes = Sets.newHashSet();
	private Collection<EdgeTypeImpl> edgeTypes = Sets.newHashSet();

	@Override
	public NodeTypeImpl createNode(String name) {
		NodeTypeImpl result = new NodeTypeImpl(this, name);
		nodeTypes.add(result);
		return result;
	}

	@Override
	public EdgeTypeImpl createEdge(String name) {
		EdgeTypeImpl result = new EdgeTypeImpl(this, name);
		edgeTypes.add(result);
		return result;
	}

	@Override
	public EdgeTypeImpl createEdge(String name, String oppositeName) {
		EdgeTypeImpl result = new EdgeTypeImpl(this, name, oppositeName);
		edgeTypes.add(result);
		return result;
	}

	@Override
	public Collection<ElementType> getElementTypes() {
		return Collections.unmodifiableCollection(Sets
				.<ElementType> newHashSet(getElementTypeImpls()));
	}

	protected Collection<ElementTypeImpl> getElementTypeImpls() {
		Collection<ElementTypeImpl> result = Sets
				.<ElementTypeImpl> newHashSet(nodeTypes);
		result.addAll(edgeTypes);
		return Collections.unmodifiableCollection(result);
	}

	protected Collection<NodeTypeImpl> getNodeTypeImpls() {
		return Collections.unmodifiableCollection(nodeTypes);
	}

	protected Collection<EdgeTypeImpl> getEdgeTypeImpls() {
		return Collections.unmodifiableCollection(edgeTypes);
	}

	@Override
	public Collection<NodeType> getNodeTypes() {
		return Collections.unmodifiableCollection(Sets
				.<NodeType> newHashSet(nodeTypes));
	}

	@Override
	public Collection<EdgeType> getEdgeTypes() {
		return Collections.unmodifiableCollection(Sets
				.<EdgeType> newHashSet(edgeTypes));
	}

	@Override
	public ElementTypeImpl findElementType(String name) {
		return find(name, getElementTypeImpls());
	}

	@Override
	public NodeTypeImpl findNodeType(String name) {
		return find(name, getNodeTypeImpls());
	}

	@Override
	public EdgeTypeImpl findEdgeType(String name) {
		return find(name, getEdgeTypeImpls());
	}

	private <T extends ElementTypeImpl> T find(String name, Collection<T> source) {
		for (T candidate : source) {
			if (candidate.getName().equals(name)) {
				return candidate;
			}
		}
		return null;
	}

	@Override
	public void delete(ElementType elementType) {
		elementType.delete();
	}

	protected void delete(NodeTypeImpl nodeType) {
		nodeTypes.remove(nodeType);
	}

	protected final void delete(EdgeTypeImpl edgeType) {
		edgeTypes.remove(edgeType);
	}
}
