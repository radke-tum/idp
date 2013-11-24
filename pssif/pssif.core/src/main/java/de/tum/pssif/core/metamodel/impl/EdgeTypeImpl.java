package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.operation.CreateEdgeOperation;
import de.tum.pssif.core.util.PSSIFUtil;


public class EdgeTypeImpl extends NamedImpl implements EdgeType {
  private EdgeTypeImpl                  general;
  private Collection<EdgeTypeImpl>      specials    = Sets.newHashSet();

  private final EdgeEndImpl             incoming;
  private final EdgeEndImpl             outgoing;
  private final Collection<EdgeEndImpl> auxiliaries = Sets.newHashSet();

  public EdgeTypeImpl(String name, String inName, NodeTypeImpl inType, Multiplicity inMult, String outName, NodeTypeImpl outType, Multiplicity outMult) {
    super(name);
    incoming = new EdgeEndImpl(inName, this, inMult, inType);
    outgoing = new EdgeEndImpl(outName, this, outMult, outType);
    inType.registerOutgoing(this);
    outType.registerIncoming(this);
  }

  @Override
  public Collection<EdgeEnd> getEnds() {
    Collection<EdgeEnd> result = Sets.<EdgeEnd> newHashSet(getIncoming(), getOutgoing());
    result.addAll(getAuxiliaries());
    return Collections.<EdgeEnd> unmodifiableCollection(result);
  }

  public Collection<EdgeEndImpl> getEndImpls() {
    Collection<EdgeEndImpl> result = Sets.<EdgeEndImpl> newHashSet(incoming, outgoing);
    result.addAll(auxiliaries);
    return Collections.<EdgeEndImpl> unmodifiableCollection(result);
  }

  @Override
  public EdgeEndImpl getIncoming() {
    return incoming;
  }

  @Override
  public EdgeEndImpl getOutgoing() {
    return outgoing;
  }

  @Override
  public Collection<EdgeEnd> getAuxiliaries() {
    return Collections.<EdgeEnd> unmodifiableCollection(auxiliaries);
  }

  protected Collection<EdgeEndImpl> getAuxEndsForType(NodeTypeImpl type) {
    Collection<EdgeEndImpl> result = Sets.newHashSet();
    for (EdgeEndImpl aux : auxiliaries) {
      if (aux.getNodeType().equals(type)) {
        result.add(aux);
      }
    }
    return result;
  }

  @Override
  public EdgeEndImpl findEdgeEnd(String name) {
    if (PSSIFUtil.areSame(name, this.incoming.getName())) {
      return this.incoming;
    }
    else if (PSSIFUtil.areSame(name, this.outgoing.getName())) {
      return outgoing;
    }
    else {
      for (EdgeEndImpl aux : auxiliaries) {
        if (PSSIFUtil.areSame(name, aux.getName())) {
          return aux;
        }
      }
    }
    return null;
  }

  @Override
  public Edge create(Model model, Multimap<EdgeEnd, Node> connections) {
    //resolve edge ends impls
    Multimap<EdgeEndImpl, Node> connectionsImpl = HashMultimap.create();
    for (EdgeEnd end : connections.keySet()) {
      EdgeEndImpl endImpl = findEdgeEnd(end.getName());
      if (endImpl == null) {
        throw new PSSIFStructuralIntegrityException("cannot find EdgeEndImpl " + end.getName());
      }
      connectionsImpl.putAll(endImpl, connections.get(end));
    }
    CreateEdgeOperation createOperation = new CreateEdgeOperation(this, connectionsImpl);
    return model.createEdge(createOperation);
  }

  @Override
  public EdgeType getGeneral() {
    return general;
  }

  @Override
  public Collection<EdgeType> getSpecials() {
    return Collections.<EdgeType> unmodifiableCollection(specials);
  }

  @Override
  public void inherit(EdgeType general) {
    general.registerSpecialization(this);
  }

  @Override
  public void registerSpecialization(EdgeTypeImpl special) {
    specials.add(special);
    special.registerGeneralization(this);
  }

  @Override
  public void registerGeneralization(EdgeTypeImpl general) {
    this.general = general;
  }

  protected void registerAux(EdgeEndImpl edgeEnd) {
    this.auxiliaries.add(edgeEnd);
  }

}
