package de.tum.pssif.core.metamodel.constraints;

import java.util.Collection;

import de.tum.pssif.core.metamodel.NodeType;

public interface NodeTypeMapping {
	static NodeTypeMapping ANY = new BaseNodeTypeMapping(Constraint.ANY,
			Constraint.ANY, Constraint.ANY);
	static NodeTypeMapping NONE = new BaseNodeTypeMapping(Constraint.NONE,
			Constraint.NONE, Constraint.NONE);

	boolean satisfies(NodeType from, NodeType to, NodeType aux);

	Collection<NodeType> getIncoming(NodeType forOutgoing);

	Collection<NodeType> getOutgoing(NodeType forIncoming);

	Collection<NodeType> getAuxiliaries(NodeType incoming, NodeType outgoing);
}
