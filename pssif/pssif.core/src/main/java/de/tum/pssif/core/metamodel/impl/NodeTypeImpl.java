package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.operation.CreateNodeOperation;
import de.tum.pssif.core.util.PSSIFOption;


public class NodeTypeImpl extends ElementTypeImpl implements NodeType {
  private NodeType                 general;
  private Collection<NodeType>     specials    = Sets.newHashSet();

  private Collection<EdgeTypeImpl> incomings   = Sets.newHashSet();
  private Collection<EdgeTypeImpl> outgoings   = Sets.newHashSet();
  private Collection<EdgeTypeImpl> auxiliaries = Sets.newHashSet();

  public NodeTypeImpl(String name) {
    super(name);
  }

  @Override
  public Collection<EdgeType> getEdgeTypes() {
    Collection<EdgeType> result = Sets.newHashSet();
    result.addAll(getIncomings());
    result.addAll(getOutgoings());
    result.addAll(getAuxiliaries());

    return Collections.unmodifiableCollection(result);
  }

  @Override
  public Collection<EdgeType> getIncomings() {
    return Collections.<EdgeType> unmodifiableCollection(incomings);
  }

  @Override
  public Collection<EdgeType> getOutgoings() {
    return Collections.<EdgeType> unmodifiableCollection(outgoings);
  }

  @Override
  public Collection<EdgeType> getAuxiliaries() {
    return Collections.<EdgeType> unmodifiableCollection(auxiliaries);
  }

  @Override
  public EdgeType findEdgeType(String name) {
    for (EdgeType element : getEdgeTypes()) {
      if (element.getName().equals(name)) {
        return element;
      }
    }
    return null;
  }

  @Override
  public Node create(Model model) {
    return model.createNode(new CreateNodeOperation(this));
  }

  @Override
  public NodeType getGeneral() {
    return general;
  }

  @Override
  public Collection<NodeType> getSpecials() {
    return Collections.unmodifiableCollection(specials);
  }

  @Override
  public void inherit(NodeType general) {
    general.registerSpecialization(this);
  }

  @Override
  public void registerSpecialization(NodeTypeImpl special) {
    specials.add(special);
    special.registerGeneralization(this);
  }

  @Override
  public void registerGeneralization(NodeTypeImpl general) {
    this.general = general;
  }

  protected void registerIncoming(EdgeTypeImpl edge) {
    incomings.add(edge);
  }

  protected void registerOutgoing(EdgeTypeImpl edge) {
    outgoings.add(edge);
  }

  protected void registerAuxiliary(EdgeTypeImpl edge) {
    auxiliaries.add(edge);
  }

  @Override
  public PSSIFOption<Node> apply(Model model) {
    PSSIFOption<Node> result = PSSIFOption.none();
    for (NodeType special : getSpecials()) {
      result = PSSIFOption.merge(result, special.apply(model));
    }
    return result;
  }

  //  public Collection<EdgeEndImpl> getEdgeEndsImpl() {
  //    Collection<EdgeEndImpl> result = Sets.newHashSet();
  //    for (EdgeTypeImpl in : incomings) {
  //      result.add(in.getOutgoing());
  //    }
  //    for (EdgeTypeImpl out : outgoings) {
  //      result.add(out.getIncoming());
  //    }
  //    for (EdgeTypeImpl aux : auxiliaries) {
  //      result.addAll(aux.getAuxEndsForType(this));
  //    }
  //    return result;
  //  }

}
