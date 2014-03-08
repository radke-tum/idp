package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.mutable.MutableNodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;


public class NodeTypeImpl extends NodeTypeBaseImpl implements MutableNodeType {
  private NodeType            general         = null;
  private final Set<NodeType> specializations = Sets.newHashSet();

  public NodeTypeImpl(String name) {
    super(name);
  }

  @Override
  public PSSIFOption<Node> apply(Model model, boolean includeSubtypes) {
    PSSIFOption<Node> result = PSSIFOption.none();
    if (includeSubtypes) {
      for (NodeType special : getSpecials()) {
        result = PSSIFOption.merge(result, special.apply(model, includeSubtypes));
      }
    }
    return PSSIFOption.merge(result, new ReadNodesOperation(this).apply(model));
  }

  @Override
  public PSSIFOption<Node> apply(Model model, String id, boolean includeSubtypes) {
    PSSIFOption<Node> result = PSSIFOption.none();
    if (includeSubtypes) {
      for (NodeType special : getSpecials()) {
        result = PSSIFOption.merge(result, special.apply(model, id, includeSubtypes));
      }
    }
    return PSSIFOption.merge(result, new ReadNodeOperation(this, id).apply(model));
  }

  @Override
  public boolean isAssignableFrom(ElementType type) {
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

  @Override
  public void inherit(NodeType general) {
    if (general.isAssignableFrom(this)) {
      throw new PSSIFStructuralIntegrityException("inheritance cycle detected");
    }
    if (this.general != null) {
      this.general.unregisterSpecialization(this);
    }
    this.general = general;
    this.general.registerSpecialization(this);
  }

  @Override
  public NodeType getGeneral() {
    return general;
  }

  @Override
  public Collection<NodeType> getSpecials() {
    return ImmutableSet.copyOf(specializations);
  }

  @Override
  public void registerSpecialization(NodeType special) {
    specializations.add(special);
  }

  @Override
  public void unregisterSpecialization(NodeType special) {
    specializations.remove(special);
  }

  @Override
  public Class<?> getMetaType() {
    return NodeType.class;
  }
}
