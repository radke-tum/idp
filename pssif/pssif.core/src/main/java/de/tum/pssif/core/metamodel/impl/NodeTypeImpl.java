package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;


public class NodeTypeImpl extends ElementTypeImpl<NodeType> implements NodeType {
  private Set<EdgeType> incomings   = Sets.newHashSet();
  private Set<EdgeType> outgoings   = Sets.newHashSet();
  private Set<EdgeType> auxiliaries = Sets.newHashSet();

  public NodeTypeImpl(String name) {
    super(name);
  }

  @Override
  public void registerIncoming(EdgeType type) {
    incomings.add(type);
  }

  @Override
  public void registerOutgoing(EdgeType type) {
    outgoings.add(type);
  }

  @Override
  public void registerAuxiliary(EdgeType type) {
    auxiliaries.add(type);
  }

  @Override
  public Collection<EdgeType> getIncomings() {
    return Collections.unmodifiableCollection(incomings);
  }

  @Override
  public Collection<EdgeType> getOutgoings() {
    return Collections.unmodifiableCollection(outgoings);
  }

  @Override
  public Collection<EdgeType> getAuxiliaries() {
    return Collections.unmodifiableCollection(auxiliaries);
  }

  @Override
  public EdgeType findIncomingEdgeType(String name) {
    return PSSIFUtil.find(name, incomings);
  }

  @Override
  public EdgeType findOutgoingEdgeType(String name) {
    return PSSIFUtil.find(name, outgoings);
  }

  @Override
  public EdgeType findAuxiliaryEdgeType(String name) {
    return PSSIFUtil.find(name, auxiliaries);
  }

  @Override
  public Node create(Model model) {
    return new CreateNodeOperation(this).apply(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model) {
    return new ReadNodesOperation(this).apply(model);
  }

  @Override
  public Class<?> getMetaType() {
    return NodeType.class;
  }
}
