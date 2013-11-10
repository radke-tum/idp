package de.tum.pssif.core.metamodel.constraints;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.NodeType;


public abstract class CompositeMapping implements NodeTypeMapping {
  private final Collection<NodeTypeMapping> baseMappings;

  public static NodeTypeMapping andMapping(Collection<NodeTypeMapping> baseMappings) {
    return new AndCompositeMapping(baseMappings);
  }

  public static NodeTypeMapping orMapping(Collection<NodeTypeMapping> baseMappings) {
    return new OrCompositeMapping(baseMappings);
  }

  protected CompositeMapping(Collection<NodeTypeMapping> mappings) {
    this.baseMappings = Sets.newHashSet(mappings);
  }

  @Override
  public boolean satisfies(NodeType from, NodeType to) {
    return operate(from, to);
  }

  @Override
  public boolean satisfies(NodeType type) {
    return operate(type);
  }

  protected Collection<NodeTypeMapping> getMappings() {
    return this.baseMappings;
  }

  protected abstract boolean operate(NodeType from, NodeType to);

  protected abstract boolean operate(NodeType aux);

  private static final class AndCompositeMapping extends CompositeMapping {

    protected AndCompositeMapping(Collection<NodeTypeMapping> mappings) {
      super(mappings);
    }

    @Override
    protected boolean operate(NodeType from, NodeType to) {
      for (NodeTypeMapping mapping : getMappings()) {
        if (!(mapping.satisfies(from, to))) {
          return false;
        }
      }
      return true;
    }

    @Override
    protected boolean operate(NodeType aux) {
      for (NodeTypeMapping mapping : getMappings()) {
        if (!mapping.satisfies(aux)) {
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
    protected boolean operate(NodeType from, NodeType to) {
      for (NodeTypeMapping mapping : getMappings()) {
        if (mapping.satisfies(from, to)) {
          return true;
        }
      }
      return false;
    }

    @Override
    protected boolean operate(NodeType aux) {
      for (NodeTypeMapping mapping : getMappings()) {
        if (mapping.satisfies(aux)) {
          return true;
        }
      }
      return false;
    }
  }
}
