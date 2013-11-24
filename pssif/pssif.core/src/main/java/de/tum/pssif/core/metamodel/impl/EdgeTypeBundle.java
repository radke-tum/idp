package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;


public class EdgeTypeBundle extends NamedImpl implements EdgeType {
  private final Collection<EdgeTypeImpl> bundled;

  public EdgeTypeBundle(String name, Collection<EdgeTypeImpl> bundled) {
    super(name);
    this.bundled = Collections.unmodifiableCollection(bundled);
  }

  @Override
  public EdgeType getGeneral() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<EdgeType> getSpecials() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void inherit(EdgeType general) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void registerSpecialization(EdgeTypeImpl special) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void registerGeneralization(EdgeTypeImpl general) {
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

    String name = null;
    for (EdgeTypeImpl type : bundled) {
      if (name == null) {
        name = type.getIncoming().getName();
      }
      else if (!name.equals(type.getIncoming().getName())) {
        throw new PSSIFStructuralIntegrityException("edge ends with different names detected for bundle " + getName());
      }
      result.add(type.getIncoming());
    }

    return new EdgeEndBundle(name, this, result);
  }

  @Override
  public EdgeEnd getOutgoing() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    String name = null;
    for (EdgeTypeImpl type : bundled) {
      if (name == null) {
        name = type.getOutgoing().getName();
      }
      else if (!name.equals(type.getOutgoing().getName())) {
        throw new PSSIFStructuralIntegrityException("edge ends with different names detected for bundle " + getName());
      }
      result.add(type.getOutgoing());
    }

    return new EdgeEndBundle(name, this, result);
  }

  @Override
  public Collection<EdgeEnd> getAuxiliaries() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    Multimap<String, EdgeEnd> ends = HashMultimap.create();

    for (EdgeTypeImpl type : bundled) {
      for (EdgeEnd end : type.getAuxiliaries()) {
        ends.put(end.getName(), end);
      }
    }

    for (String name : ends.keySet()) {
      if (ends.get(name).size() == 1) {
        result.add(ends.get(name).iterator().next());
      }
      else {
        result.add(new EdgeEndBundle(name, this, ends.get(name)));
      }
    }

    return result;
  }

  @Override
  public Edge create(Model model, Multimap<EdgeEnd, Node> connections) {
    PSSIFOption<EdgeTypeImpl> matchingEdgeTypes = findMatchingEdgeTypes(connections);

    if (matchingEdgeTypes.isNone()) {
      throw new PSSIFStructuralIntegrityException("edge bundle contains no matching edge type for the provided combination of edge ends and nodes.");
    }
    else if (matchingEdgeTypes.isMany()) {
      throw new PSSIFStructuralIntegrityException("the provided edge and and node configuration is ambiguous in this edge type bundle.");
    }

    return matchingEdgeTypes.getOne().create(model, connections);
  }

  private PSSIFOption<EdgeTypeImpl> findMatchingEdgeTypes(Multimap<EdgeEnd, Node> connections) {
    Collection<EdgeTypeImpl> result = Sets.newHashSet();

    candidates: for (EdgeTypeImpl candidate : bundled) {
      for (EdgeEndImpl end : candidate.getEndImpls()) {
        //check if connections conform to candidates EdgeEnd multiplicities
        EdgeEnd connectionsEnd = findEnd(end.getName(), end.getNodeType(), connections.keySet());
        if ((end.getEdgeEndLower() > 0 && connectionsEnd == null)
            || (connectionsEnd != null && !end.includesEdgeEnd(connections.get(connectionsEnd).size()))) {
          continue candidates;
        }

        //check if creation would violate EdgeTypeMultiplicity of incoming nodes
        EdgeEnd incomingEnd = findEnd(candidate.getIncoming(), connections.keySet());
        for (Node incoming : connections.get(incomingEnd)) {
          if (!candidate.getIncoming().includesEdgeType(incoming.get(incomingEnd).size() + 1)) {
            continue candidates;
          }
        }

        //check if creation would violate EdgeTypeMultiplicity of outgoing nodes
        EdgeEnd outgoingEnd = findEnd(candidate.getOutgoing(), connections.keySet());
        for (Node outgoing : connections.get(outgoingEnd)) {
          if (!candidate.getOutgoing().includesEdgeType(outgoing.get(outgoingEnd).size() + 1)) {
            continue candidates;
          }
        }
      }
      result.add(candidate);
    }

    return PSSIFOption.many(result);
  }

  private static EdgeEnd findEnd(String name, NodeType type, Collection<EdgeEnd> ends) {
    for (EdgeEnd end : ends) {
      if (end.equals(name, type)) {
        return end;
      }
    }
    return null;
  }

  private static EdgeEnd findEnd(EdgeEndImpl end, Collection<EdgeEnd> ends) {
    return findEnd(end.getName(), end.getNodeType(), ends);
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
