package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.traits.ElementApplicable;
import de.tum.pssif.core.util.PSSIFUtil;


public class EdgeTypeImpl extends ElementTypeImpl<EdgeType> implements EdgeType {
  private Collection<ConnectionMapping> mappings    = Sets.newHashSet();
  private Collection<EdgeEnd>           auxiliaries = Sets.newHashSet();

  public EdgeTypeImpl(String name) {
    super(name);
  }

  @Override
  public ConnectionMapping createMapping(String inName, NodeType in, Multiplicity inMultiplicity, String outName, NodeType out,
                                         Multiplicity outMultiplicity) {
    checkMappingConsistency(inName, in, inMultiplicity, outName, out, outMultiplicity);

    EdgeEnd from = new EdgeEndImpl(inName, this, inMultiplicity, in);
    EdgeEnd to = new EdgeEndImpl(outName, this, outMultiplicity, out);

    in.registerOutgoing(this);
    out.registerIncoming(this);

    ConnectionMapping result = new ConnectionMappingImpl(from, to);

    mappings.add(result);

    return result;
  }

  private void checkMappingConsistency(String inName, NodeType in, Multiplicity inMultiplicity, String outName, NodeType out,
                                       Multiplicity outMultiplicity) {
    if (inMultiplicity.getEdgeEndLower() < 1 || outMultiplicity.getEdgeEndLower() < 1) {
      throw new PSSIFStructuralIntegrityException("cannot create mapping with edge end lower multiplicity < 1");
    }
    for (ConnectionMapping mapping : this.mappings) {
      mapping.getFrom();
      if (mapping.getFrom().getNodeType().equals(in) && PSSIFUtil.areSame(mapping.getFrom().getName(), inName)
          && mapping.getTo().getNodeType().equals(out) && PSSIFUtil.areSame(mapping.getTo().getName(), outName)) {
        throw new PSSIFStructuralIntegrityException("A connction mapping between the provided types with the provided names already exists.");
      }
    }
  }

  @Override
  public EdgeEnd createAuxiliary(String name, Multiplicity multiplicity, NodeType to) {
    EdgeEnd aux = findAuxiliary(name);
    if (aux != null) {
      throw new PSSIFStructuralIntegrityException("An auxiliary edge end with this name already exists.");
    }
    EdgeEnd result = new EdgeEndImpl(name, this, multiplicity, to);
    auxiliaries.add(result);
    to.registerAuxiliary(this);
    return result;
  }

  @Override
  public ElementApplicable getIncoming() {
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
  public ElementApplicable getOutgoing() {
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
  public ConnectionMapping getMapping(NodeType in, NodeType out) {
    ConnectionMapping result = null;

    for (ConnectionMapping candidate : mappings) {
      if (candidate.getFrom().getNodeType().isAssignableFrom(in) && candidate.getTo().getNodeType().isAssignableFrom(out)) {
        result = candidate;
        break;
      }
    }

    return result;
  }

  public Collection<ConnectionMapping> getMappings() {
    return Collections.unmodifiableCollection(mappings);
  }

  @Override
  public Collection<EdgeEnd> getAuxiliaries() {
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
  public EdgeEnd findAuxiliary(String name) {
    return PSSIFUtil.find(name, getAuxiliaries());
  }

  @Override
  public Class<?> getMetaType() {
    return EdgeType.class;
  }

  public String toString() {
    return "EdgeType:" + this.getName();
  }

}
