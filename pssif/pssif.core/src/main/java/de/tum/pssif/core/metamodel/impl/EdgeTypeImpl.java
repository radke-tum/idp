package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.constraints.NodeTypeMapping;

public class EdgeTypeImpl extends ElementTypeImpl implements EdgeType {
	private String oppositeName = null;
	private EdgeType generalization;
	private Collection<EdgeType> specializations;
	private final Collection<Attribute> attributes = Sets.newHashSet();
	private NodeTypeMapping mapping = NodeTypeMapping.ANY;

	public EdgeTypeImpl(MetamodelImpl metamodel, String name) {
		super(metamodel, name);
	}

	public EdgeTypeImpl(MetamodelImpl metamodel, String name,
			String oppositeName) {
		super(metamodel, name);
		this.oppositeName = oppositeName;
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
	public EdgeType getGeneralization() {
		return generalization;
	}

	@Override
	public Collection<EdgeType> getSpecializations() {
		return Collections.unmodifiableCollection(specializations);
	}

	@Override
	public void inherit(EdgeType other) {
		inherit(getMetamodel().findEdgeType(other.getName()));
	}

	private void inherit(EdgeTypeImpl other) {
		other.specializations.add(this);
		this.generalization = other;
	}

	@Override
	public boolean isAcyclic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void allow(NodeType from, NodeType to) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowAuxiliaryFor(NodeType from, NodeType to, NodeType aux) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<NodeType> getIncoming(NodeType forOutgoing) {
		return mapping.getIncoming(forOutgoing);
	}

	@Override
	public Collection<NodeType> getOutgoing(NodeType forIncoming) {
		return mapping.getOutgoing(forIncoming);
	}

	@Override
	public Collection<NodeType> getAuxiliaries(NodeType incoming,
			NodeType outgoing) {
		return mapping.getAuxiliaries(incoming, outgoing);
	}

	@Override
	public boolean hasOpposite() {
		return oppositeName != null;
	}

	@Override
	public EdgeType getOpposite() {
		return new OppositeEdgeType(this);
	}

	@Override
	public void delete() {
		getMetamodel().delete(this);
	}

	private final class OppositeEdgeType implements EdgeType {
		private final EdgeTypeImpl baseEdge;

		public OppositeEdgeType(EdgeTypeImpl baseEdge) {
			this.baseEdge = baseEdge;
		}

		@Override
		public Attribute createAttribute(String name) {
			return baseEdge.createAttribute(name);
		}

		@Override
		public Collection<Attribute> getAttributes() {
			return baseEdge.getAttributes();
		}

		@Override
		public Attribute findAttribute(String name) {
			return baseEdge.findAttribute(name);
		}

		@Override
		public void deleteAttribute(Attribute attribute) {
			baseEdge.deleteAttribute(attribute);
		}

		@Override
		public EdgeType getGeneralization() {
			return baseEdge.getGeneralization();
		}

		@Override
		public Collection<EdgeType> getSpecializations() {
			return baseEdge.getSpecializations();
		}

		@Override
		public void inherit(EdgeType other) {
			baseEdge.inherit(other);
		}

		@Override
		public boolean isAcyclic() {
			return baseEdge.isAcyclic();
		}

		@Override
		public void allow(NodeType from, NodeType to) {
			baseEdge.allow(to, from);
		}

		@Override
		public void allowAuxiliaryFor(NodeType from, NodeType to, NodeType aux) {
			baseEdge.allowAuxiliaryFor(to, from, aux);
		}

		@Override
		public Collection<NodeType> getIncoming(NodeType forOutgoing) {
			return baseEdge.getOutgoing(forOutgoing);
		}

		@Override
		public Collection<NodeType> getOutgoing(NodeType forIncoming) {
			return baseEdge.getIncoming(forIncoming);
		}

		@Override
		public Collection<NodeType> getAuxiliaries(NodeType incoming,
				NodeType outgoing) {
			return baseEdge.getAuxiliaries(incoming, outgoing);
		}

		@Override
		public boolean hasOpposite() {
			return baseEdge.hasOpposite();
		}

		@Override
		public EdgeType getOpposite() {
			return baseEdge;
		}

		@Override
		public String getName() {
			return baseEdge.oppositeName;
		}

		@Override
		public void delete() {
			baseEdge.delete();
		}
	}
}
