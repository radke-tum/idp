package de.tum.pssif.core.metamodel.constraints;

import java.util.Collection;

import de.tum.pssif.core.metamodel.NodeType;

public class BaseNodeTypeMapping implements NodeTypeMapping {
	private final Constraint from;
	private final Constraint to;
	private final Constraint aux;

	public BaseNodeTypeMapping(Constraint from, Constraint to, Constraint aux) {
		this.from = from;
		this.to = to;
		this.aux = aux;
	}

	@Override
	public boolean satisfies(NodeType from, NodeType to, NodeType aux) {
		return this.from.satisfies(from) && this.to.satisfies(to)
				&& this.aux.satisfies(aux);
	}

	@Override
	public Collection<NodeType> getIncoming(NodeType forOutgoing) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<NodeType> getOutgoing(NodeType forIncoming) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<NodeType> getAuxiliaries(NodeType incoming,
			NodeType outgoing) {
		// TODO Auto-generated method stub
		return null;
	}
}
