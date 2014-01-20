package de.tum.pssif.core.metamodel.impl.base;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.EdgeEndBundleImpl;
import de.tum.pssif.core.metamodel.traits.ElementApplicable;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class AbstractEdgeType extends AbstractElementType<EdgeType> implements EdgeType {
  private Collection<ConnectionMapping> mappings    = Sets.newHashSet();
  private Collection<EdgeEnd>           auxiliaries = Sets.newHashSet();

  public AbstractEdgeType(String name) {
    super(name);
  }

  protected final void addMapping(ConnectionMapping mapping) {
    mappings.add(mapping);
  }

  protected final void removeMapping(ConnectionMapping mapping) {
    mappings.remove(mapping);
  }

  protected final void addAuxiliary(EdgeEnd end) {
    auxiliaries.add(end);
  }

  protected final void removeAuxiliary(EdgeEnd end) {
    auxiliaries.remove(end);
  }

  @Override
  public final ElementApplicable getIncoming() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    for (ConnectionMapping mapping : mappings) {
      result.add(mapping.getFrom());
    }

    for (EdgeType gen : PSSIFUtil.generalizationsClosure((EdgeType) this)) {
      for (ConnectionMapping genMapping : gen.getMappings()) {
        if (!PSSIFUtil.hasSpecializationIn(genMapping.getFrom(), result)) {
          result.add(genMapping.getFrom());
        }
      }
    }

    return new EdgeEndBundleImpl(result);
  }

  @Override
  public final ElementApplicable getOutgoing() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    for (ConnectionMapping mapping : mappings) {
      result.add(mapping.getTo());
    }

    for (EdgeType gen : PSSIFUtil.generalizationsClosure((EdgeType) this)) {
      for (ConnectionMapping genMapping : gen.getMappings()) {
        if (!PSSIFUtil.hasSpecializationIn(genMapping.getTo(), result)) {
          result.add(genMapping.getTo());
        }
      }
    }

    return new EdgeEndBundleImpl(result);
  }

  @Override
  public final ConnectionMapping getMapping(NodeType in, NodeType out) {
    ConnectionMapping result = null;

    for (ConnectionMapping candidate : mappings) {
      if (candidate.getFrom().getNodeType().isAssignableFrom(in) && candidate.getTo().getNodeType().isAssignableFrom(out)) {
        result = candidate;
        break;
      }
    }

    return result;
  }

  @Override
  public final Collection<ConnectionMapping> getMappings() {
    return Collections.unmodifiableCollection(mappings);
  }

  @Override
  public final Collection<EdgeEnd> getAuxiliaries() {
    Set<EdgeEnd> auxes = Sets.newHashSet(auxiliaries);
    for (EdgeType gen : PSSIFUtil.generalizationsClosure((EdgeType) this)) {
      for (EdgeEnd genEnd : gen.getAuxiliaries()) {
        if (!PSSIFUtil.hasSpecializationIn(genEnd, auxes)) {
          auxes.add(genEnd);
        }
      }
    }
    return Collections.unmodifiableCollection(auxes);
  }

  @Override
  public final EdgeEnd findAuxiliary(String name) {
    return PSSIFUtil.find(name, getAuxiliaries());
  }

  @Override
  public final Class<?> getMetaType() {
    return EdgeType.class;
  }

  @Override
  public final String toString() {
    return "EdgeType:" + this.getName();
  }
}
