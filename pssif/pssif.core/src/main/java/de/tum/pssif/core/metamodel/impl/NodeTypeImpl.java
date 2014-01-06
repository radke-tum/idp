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
    Set<EdgeType> result = Sets.newHashSet(incomings);
    for (NodeType gen : PSSIFUtil.generalizationsClosure((NodeType) this)) {
      for (EdgeType genEdge : gen.getIncomings()) {
        if (!PSSIFUtil.hasSpecializationIn(genEdge, result)) {
          result.add(genEdge);
        }
      }
    }
    return Collections.unmodifiableCollection(result);
  }

  @Override
  public Collection<EdgeType> getOutgoings() {
    Set<EdgeType> result = Sets.newHashSet(outgoings);
    for (NodeType gen : PSSIFUtil.generalizationsClosure((NodeType) this)) {
      for (EdgeType genEdge : gen.getOutgoings()) {
        if (!PSSIFUtil.hasSpecializationIn(genEdge, result)) {
          result.add(genEdge);
        }
      }
    }
    return Collections.unmodifiableCollection(result);
  }

  @Override
  public Collection<EdgeType> getAuxiliaries() {
    Set<EdgeType> result = Sets.newHashSet(auxiliaries);
    for (NodeType gen : PSSIFUtil.generalizationsClosure((NodeType) this)) {
      for (EdgeType genEdge : gen.getAuxiliaries()) {
        if (!PSSIFUtil.hasSpecializationIn(genEdge, result)) {
          result.add(genEdge);
        }
      }
    }
    return Collections.unmodifiableCollection(result);
  }

  @Override
  public EdgeType findIncomingEdgeType(String name) {
    return PSSIFUtil.find(name, getIncomings());
  }

  @Override
  public EdgeType findOutgoingEdgeType(String name) {
    return PSSIFUtil.find(name, getOutgoings());
  }

  @Override
  public EdgeType findAuxiliaryEdgeType(String name) {
    return PSSIFUtil.find(name, getAuxiliaries());
  }

  @Override
  public Node create(Model model) {
    return new CreateNodeOperation(this).apply(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model) {
    PSSIFOption<Node> result = PSSIFOption.none();
    for (NodeType currentType : PSSIFUtil.specializationsClosure((NodeType) this)) {
      result = PSSIFOption.merge(result, new ReadNodesOperation(currentType).apply(model));
    }
    return result;
  }

  @Override
  public Class<?> getMetaType() {
    return NodeType.class;
  }
}
