package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;

public class NodeTypeImpl extends ElementTypeImpl implements NodeType {
	private NodeType generalization;
	private Collection<NodeType> specializations = Sets.newHashSet();
	private final Collection<Attribute> attributes = Sets.newHashSet();

	public NodeTypeImpl(MetamodelImpl metamodel, String name) {
		super(metamodel, name);
	}

	@Override
	public Attribute createAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Attribute> getAttributes() {
		return Collections.unmodifiableCollection(attributes);
	}

	@Override
	public Attribute findAttribute(String name) {
		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(name)) {
				return attribute;
			}
		}
		return null;
	}

	@Override
	public void deleteAttribute(Attribute attribute) {
		attributes.remove(attribute);
	}

	@Override
	public NodeType getGeneralization() {
		return generalization;
	}

	@Override
	public Collection<NodeType> getSpecializations() {
		return Collections.unmodifiableCollection(specializations);
	}

	@Override
	public void inherit(NodeType other) {
		inherit(getMetamodel().findNodeType(other.getName()));
	}

	private void inherit(NodeTypeImpl other) {
		other.specializations.add(this);
		generalization = other;
	}

	@Override
	public Collection<EdgeType> getIncomming() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EdgeType> getOutgoing() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EdgeType> getAuxiliaryEdgeTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete() {
		getMetamodel().delete(this);
	}

}
