package de.tum.pssif.transform.transformation;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class HideNodeTypeTransformation extends HideTypeTransformation<NodeType> {
  public HideNodeTypeTransformation(NodeType type) {
    super(type);
  }

  @Override
  public Metamodel apply(View view) {
    if (getType() != null) {
      removeType(view, getType());
    }
    return view;
  }

  private void removeType(View view, NodeType type) {
    for (NodeType special : type.getSpecials()) {
      removeType(view, special);
    }

    ViewedNodeType actualTarget = view.findNodeType(type.getName());

    Collection<ViewedEdgeType> connected = Sets.newHashSet(actualTarget.getIncomingsInternal());
    connected.addAll(actualTarget.getOutgoingsInternal());

    for (ViewedEdgeType et : connected) {
      Collection<ConnectionMapping> toRemove = Sets.newHashSet();
      for (ConnectionMapping mapping : et.getMappings()) {
        if (mapping.getFrom().getNodeType().equals(type) || mapping.getTo().getNodeType().equals(type)) {
          toRemove.add(mapping);
        }
      }
      for (ConnectionMapping mapping : toRemove) {
        mapping.getFrom().getNodeType().deregisterOutgoing(et);
        mapping.getTo().getNodeType().deregisterIncoming(et);
        et.removeMapping(mapping);
      }
    }

    for (ViewedEdgeType et : actualTarget.getAuxiliariesInternal()) {
      for (EdgeEnd e : et.getAuxiliaries()) {
        if (e.getNodeType().equals(type)) {
          e.getNodeType().deregisterAuxiliary(et);
          et.removeAuxiliary(e);
        }
      }
    }

    view.removeNodeType(actualTarget);
  }
}
