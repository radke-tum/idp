package de.tum.pssif.core.metamodel.constraints;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.NodeType;

public abstract class CompositeMapping implements NodeTypeMapping {
	private final Collection<NodeTypeMapping> baseMappings;

	public static NodeTypeMapping andMapping(
			Collection<NodeTypeMapping> baseMappings) {
		return new AndCompositeMapping(baseMappings);
	}

	public static NodeTypeMapping orMapping(
			Collection<NodeTypeMapping> baseMappings) {
		return new OrCompositeMapping(baseMappings);
	}

	protected CompositeMapping(Collection<NodeTypeMapping> mappings) {
		this.baseMappings = Sets.newHashSet(mappings);
	}

	@Override
	public final boolean satisfies(NodeType from, NodeType to, NodeType aux) {
		return operate(from, to, aux);
	}

	protected final Collection<NodeTypeMapping> getMappings() {
		return this.baseMappings;
	}

	protected abstract boolean operate(NodeType from, NodeType to, NodeType aux);

	private static final class AndCompositeMapping extends CompositeMapping {

		protected AndCompositeMapping(Collection<NodeTypeMapping> mappings) {
			super(mappings);
		}

		@Override
		protected boolean operate(NodeType from, NodeType to, NodeType aux) {
			for (NodeTypeMapping mapping : getMappings()) {
				if (!(mapping.satisfies(from, to, aux))) {
					return false;
				}
			}
			return true;
		}
	}

	private static final class OrCompositeMapping extends CompositeMapping {

		protected OrCompositeMapping(Collection<NodeTypeMapping> mappings) {
			super(mappings);
		}

		@Override
		protected boolean operate(NodeType from, NodeType to, NodeType aux) {
			for (NodeTypeMapping mapping : getMappings()) {
				if (mapping.satisfies(from, to, aux)) {
					return true;
				}
			}
			return false;
		}
	}
}
