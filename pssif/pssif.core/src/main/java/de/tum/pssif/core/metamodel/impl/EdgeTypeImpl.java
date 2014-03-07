package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;


public class EdgeTypeImpl extends ElementTypeImpl<EdgeType> implements MutableEdgeType {
  private Collection<ConnectionMapping> mappings        = Sets.newHashSet();
  private EdgeType                      general         = null;
  private final Set<EdgeType>           specializations = Sets.newHashSet();

  public EdgeTypeImpl(String name) {
    super(name);
  }

  @Override
  public ConnectionMapping createMapping(NodeTypeBase from, NodeTypeBase to, Collection<JunctionNodeType> junctions) {
    for (JunctionNodeType junction : junctions) {
      mappings.add(new ConnectionMappingImpl(this, junction, junction));
      for (JunctionNodeType other : junctions) {
        if (other != junction) {
          mappings.add(new ConnectionMappingImpl(this, junction, other));
        }
      }
      mappings.add(new ConnectionMappingImpl(this, from, junction));
      mappings.add(new ConnectionMappingImpl(this, junction, to));
    }

    ConnectionMappingImpl result = new ConnectionMappingImpl(this, from, to);
    mappings.add(result);
    return result;
  }

  @Override
  public PSSIFOption<ConnectionMapping> getMappings() {
    return PSSIFOption.many(mappings);
  }

  @Override
  public PSSIFOption<ConnectionMapping> getMapping(NodeTypeBase from, NodeTypeBase to) {
    ConnectionMapping result = null;

    for (ConnectionMapping candidate : mappings) {
      if (candidate.getFrom().isAssignableFrom(from) && candidate.getTo().isAssignableFrom(to)) {
        if (result != null) {
          if (result.getFrom().isAssignableFrom(candidate.getFrom()) && result.getTo().isAssignableFrom(candidate.getTo())) {
            result = candidate;
          }
        }
        else {
          result = candidate;
        }
      }
    }

    return PSSIFOption.one(result);
  }

  @Override
  public PSSIFOption<ConnectionMapping> getOutgoingMappings(final NodeTypeBase from) {
    return PSSIFOption.many(Collections2.filter(mappings, new Predicate<ConnectionMapping>() {
      @Override
      public boolean apply(ConnectionMapping input) {
        return input.getFrom().isAssignableFrom(from);
      }
    }));
  }

  @Override
  public PSSIFOption<ConnectionMapping> getIncomingMappings(final NodeTypeBase to) {
    return PSSIFOption.many(Collections2.filter(mappings, new Predicate<ConnectionMapping>() {
      @Override
      public boolean apply(ConnectionMapping input) {
        return input.getTo().isAssignableFrom(to);
      }
    }));
  }

  @Override
  public boolean isAssignableFrom(ElementType type) {
    if (this.equals(type)) {
      return true;
    }
    else {
      for (EdgeType special : getSpecials()) {
        if (special.isAssignableFrom(type)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public void inherit(EdgeType general) {
    if (general.isAssignableFrom(this)) {
      throw new PSSIFStructuralIntegrityException("inheritance cycle detected");
    }
    if (this.general != null) {
      this.general.unregisterSpecialization(this);
    }
    this.general = general;
    this.general.registerSpecialization(this);
  }

  @Override
  public EdgeType getGeneral() {
    return general;
  }

  @Override
  public Collection<EdgeType> getSpecials() {
    return ImmutableSet.copyOf(specializations);
  }

  @Override
  public void registerSpecialization(EdgeType special) {
    specializations.add(special);
  }

  @Override
  public void unregisterSpecialization(EdgeType special) {
    specializations.remove(special);
  }
}
