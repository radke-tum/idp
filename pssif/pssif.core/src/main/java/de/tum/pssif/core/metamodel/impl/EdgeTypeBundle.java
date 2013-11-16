package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;


public class EdgeTypeBundle extends NamedImpl implements EdgeType {
  private final Collection<EdgeType> bundled;

  public EdgeTypeBundle(String name, Collection<EdgeType> bundled) {
    super(name);
    this.bundled = Collections.unmodifiableCollection(bundled);
  }

  @Override
  public EdgeType getGeneral() {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<EdgeType> getSpecials() {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public void inherit(EdgeType general) {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public void registerSpecialization(EdgeTypeImpl special) {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public void registerGeneralization(EdgeTypeImpl general) {
    // TODO
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<EdgeEnd> getEnds() {
    Collection<EdgeEnd> result = Sets.newHashSet(getIncoming(), getOutgoing());
    result.addAll(getAuxiliaries());

    return Collections.unmodifiableCollection(result);
  }

  @Override
  public EdgeEnd getIncoming() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    for (EdgeType type : bundled) {
      result.add(type.getIncoming());
    }

    // FIXME name???
    return new EdgeEndBundle("", this, result);
  }

  @Override
  public EdgeEnd getOutgoing() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    for (EdgeType type : bundled) {
      result.add(type.getOutgoing());
    }

    // FIXME name???
    return new EdgeEndBundle("", this, result);
  }

  @Override
  public Collection<EdgeEnd> getAuxiliaries() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    for (EdgeType type : bundled) {
      result.addAll(type.getAuxiliaries());
    }

    return result;
  }

  @Override
  public Edge create(Model model, Multimap<EdgeEnd, Node> connections) {
    PSSIFOption<EdgeTypeImpl> matchingEdgeTypes = findMatchingEdgeTypes(connections);
    if (matchingEdgeTypes.isNone()) {
      throw new PSSIFStructuralIntegrityException("edge bundle contains no matcing edge type for the provided combination of edge ends and nodes.");
    }
    else if (matchingEdgeTypes.isMany()) {
      throw new PSSIFStructuralIntegrityException("the provided edge and and node configuration is ambiguous in this edge type bundle.");
    }
    return matchingEdgeTypes.getOne().create(model, connections);
  }

  private PSSIFOption<EdgeTypeImpl> findMatchingEdgeTypes(Multimap<EdgeEnd, Node> connections) {
    //TODO match stuff
    return PSSIFOption.none();
  }

  @Override
  public EdgeEnd findEdgeEnd(String name) {
    if (PSSIFUtil.areSame(name, getIncoming().getName())) {
      return getIncoming();
    }
    else if (PSSIFUtil.areSame(name, getOutgoing().getName())) {
      return getOutgoing();
    }
    else {
      for (EdgeEnd aux : getAuxiliaries()) {
        if (PSSIFUtil.areSame(name, aux.getName())) {
          return aux;
        }
      }
    }
    return null;
  }

}
