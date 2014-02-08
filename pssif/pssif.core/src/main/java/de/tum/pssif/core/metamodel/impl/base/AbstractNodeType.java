package de.tum.pssif.core.metamodel.impl.base;

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


public abstract class AbstractNodeType extends AbstractElementType<NodeType, Node> implements NodeType {
  private Set<EdgeType> incomings   = Sets.newHashSet();
  private Set<EdgeType> outgoings   = Sets.newHashSet();
  private Set<EdgeType> auxiliaries = Sets.newHashSet();

  public AbstractNodeType(String name) {
    super(name);
  }

  @Override
  public final PSSIFOption<Node> apply(Model model, String id) {
    for (Node candidate : apply(model).getMany()) {
      if (id.equals(candidate.getId())) {
        return PSSIFOption.one(candidate);
      }
    }
    return PSSIFOption.none();
  }

  @Override
  public final void registerIncoming(EdgeType type) {
    incomings.add(type);
  }

  @Override
  public void deregisterIncoming(EdgeType type) {
    incomings.remove(type);
  }

  @Override
  public final void registerOutgoing(EdgeType type) {
    outgoings.add(type);
  }

  @Override
  public void deregisterOutgoing(EdgeType type) {
    outgoings.remove(type);
  }

  @Override
  public final void registerAuxiliary(EdgeType type) {
    auxiliaries.add(type);
  }

  @Override
  public void deregisterAuxiliary(EdgeType type) {
    auxiliaries.remove(type);
  }

  @Override
  public final Collection<EdgeType> getIncomings() {
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
  public final Collection<EdgeType> getOutgoings() {
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
  public final Collection<EdgeType> getAuxiliaries() {
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
  public final EdgeType findIncomingEdgeType(String name) {
    return PSSIFUtil.find(name, getIncomings());
  }

  @Override
  public final EdgeType findOutgoingEdgeType(String name) {
    return PSSIFUtil.find(name, getOutgoings());
  }

  @Override
  public final EdgeType findAuxiliaryEdgeType(String name) {
    return PSSIFUtil.find(name, getAuxiliaries());
  }

  @Override
  public final Class<?> getMetaType() {
    return NodeType.class;
  }

  @Override
  public final String toString() {
    return "NodeType:" + this.getName();
  }

  @Override
  public final boolean isAssignableFrom(NodeType type) {
    if (this.equals(type)) {
      return true;
    }
    else {
      for (NodeType special : getSpecials()) {
        if (special.isAssignableFrom(type)) {
          return true;
        }
      }
    }

    return false;
  }
}
