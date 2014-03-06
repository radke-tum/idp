package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;


public class EdgeTypeImpl extends ElementTypeImpl<EdgeType> implements MutableEdgeType {
  private Collection<ConnectionMapping> mappings = Sets.newHashSet();

  public EdgeTypeImpl(String name) {
    super(name);
  }

  @Override
  public ConnectionMapping createMapping(NodeType from, NodeType to, Collection<JunctionNodeType> junctions) {
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
  public PSSIFOption<ConnectionMapping> getMapping(NodeType from, NodeType to) {
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
  public PSSIFOption<ConnectionMapping> getOutgoingMappings(final NodeType from) {
    return PSSIFOption.many(Collections2.filter(mappings, new Predicate<ConnectionMapping>() {
      @Override
      public boolean apply(ConnectionMapping input) {
        return input.getFrom().isAssignableFrom(from);
      }
    }));
  }

  @Override
  public PSSIFOption<ConnectionMapping> getIncomingMappings(final NodeType to) {
    return PSSIFOption.many(Collections2.filter(mappings, new Predicate<ConnectionMapping>() {
      @Override
      public boolean apply(ConnectionMapping input) {
        return input.getTo().isAssignableFrom(to);
      }
    }));
  }

}
