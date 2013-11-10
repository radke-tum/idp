package de.tum.pssif.core.metamodel.constraints;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import de.tum.pssif.core.metamodel.NodeType;


public abstract class Constraint {
  public static Constraint ANY  = new Constraint() {
                                  @Override
                                  public boolean satisfies(NodeType type) {
                                    return true;
                                  }

                                  @Override
                                  public Constraint deny(NodeType type) {
                                    return new DenialConstraint(this, type);
                                  }

                                  @Override
                                  public Constraint allow(NodeType type) {
                                    return this;
                                  }
                                };

  public static Constraint NONE = new Constraint() {
                                  @Override
                                  public boolean satisfies(NodeType type) {
                                    return false;
                                  }

                                  @Override
                                  public Constraint deny(NodeType type) {
                                    return this;
                                  }

                                  @Override
                                  public Constraint allow(NodeType type) {
                                    return new AllowanceConstraint(this, type);
                                  }
                                };

  public abstract boolean satisfies(NodeType type);

  public abstract Constraint deny(NodeType type);

  public abstract Constraint allow(NodeType type);

  private final class DenialConstraint extends Constraint {
    private final Constraint           baseConstraint;
    private final Collection<NodeType> denied;

    public DenialConstraint(Constraint baseConstraint, NodeType denied) {
      this.baseConstraint = baseConstraint;
      this.denied = ImmutableSet.<NodeType> of(denied);
    }

    private DenialConstraint(Constraint baseConstraint, Collection<NodeType> denied) {
      this.baseConstraint = baseConstraint;
      this.denied = denied;
    }

    @Override
    public boolean satisfies(NodeType type) {
      return !denied.contains(type) && baseConstraint.satisfies(type);
    }

    @Override
    public Constraint deny(NodeType type) {
      return new DenialConstraint(baseConstraint, ImmutableSet.<NodeType> builder().addAll(denied).add(type).build());
    }

    @Override
    public Constraint allow(NodeType type) {
      return new AllowanceConstraint(this, type);
    }

  }

  private final class AllowanceConstraint extends Constraint {
    private final Constraint           baseConstraint;
    private final Collection<NodeType> allowed;

    public AllowanceConstraint(Constraint baseConstraint, NodeType allowed) {
      this.baseConstraint = baseConstraint;
      this.allowed = ImmutableSet.<NodeType> of(allowed);
    }

    private AllowanceConstraint(Constraint baseConstraint, Collection<NodeType> allowed) {
      this.baseConstraint = baseConstraint;
      this.allowed = allowed;
    }

    @Override
    public boolean satisfies(NodeType type) {
      return allowed.contains(type) || baseConstraint.satisfies(type);
    }

    @Override
    public Constraint deny(NodeType type) {
      return new DenialConstraint(this, type);
    }

    @Override
    public Constraint allow(NodeType type) {
      return new AllowanceConstraint(baseConstraint, ImmutableSet.<NodeType> builder().addAll(allowed).add(type).build());
    }

  }
}
