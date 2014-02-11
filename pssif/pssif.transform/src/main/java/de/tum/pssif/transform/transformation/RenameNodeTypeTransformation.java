package de.tum.pssif.transform.transformation;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeEnd;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class RenameNodeTypeTransformation extends RenameTransformation<NodeType> {

  public RenameNodeTypeTransformation(NodeType target, String name) {
    super(target, name);
  }

  @Override
  public void apply(View view) {
    ViewedNodeType actualTarget = view.findNodeType(getTarget().getName());
    view.removeNodeType(actualTarget);
    ViewedNodeType renamed = new ViewedNodeType(actualTarget, getName());
    view.addNodeType(renamed);

    if (actualTarget.getGeneral() != null) {
      NodeType general = actualTarget.getGeneral();
      actualTarget.getGeneral().unregisterSpecialization(actualTarget);
      renamed.inherit(general);
    }

    Collection<NodeType> specials = Sets.newHashSet(actualTarget.getSpecials());
    for (NodeType special : specials) {
      special.inherit(renamed);
    }

    Collection<ViewedEdgeType> connected = Sets.newHashSet(actualTarget.getIncomingsInternal());
    connected.addAll(actualTarget.getOutgoingsInternal());

    for (ViewedEdgeType et : connected) {
      Map<ConnectionMapping, ConnectionMapping> toReplace = Maps.newHashMap();
      for (ConnectionMapping mapping : et.getMappings()) {
        EdgeEnd from = mapping.getFrom();
        EdgeEnd to = mapping.getTo();
        NodeType fromType = from.getNodeType();
        NodeType toType = to.getNodeType();
        boolean affected = false;
        if (from.getNodeType().equals(actualTarget)) {
          fromType = renamed;
          affected = true;
        }
        if (to.getNodeType().equals(actualTarget)) {
          toType = renamed;
          affected = true;
        }

        if (affected) {
          from = new ViewedEdgeEnd(from, from.getName(), et, MultiplicityContainer.of(from.getEdgeEndLower(), from.getEdgeEndUpper(),
              from.getEdgeTypeLower(), from.getEdgeTypeUpper()), fromType);
          to = new ViewedEdgeEnd(to, to.getName(), et, MultiplicityContainer.of(to.getEdgeEndLower(), to.getEdgeEndUpper(), to.getEdgeTypeLower(),
              to.getEdgeTypeUpper()), toType);
          toReplace.put(mapping, new ViewedConnectionMapping(mapping, from, to));
        }
      }

      for (Entry<ConnectionMapping, ConnectionMapping> entry : toReplace.entrySet()) {
        et.removeMapping(entry.getKey());
        et.addMapping(entry.getValue());
      }
    }

    for (ViewedEdgeType et : actualTarget.getAuxiliariesInternal()) {
      for (EdgeEnd e : et.getAuxiliaries()) {
        if (e.getNodeType().equals(actualTarget)) {
          et.removeAuxiliary(e);
          et.addAuxiliary(new ViewedEdgeEnd(e, e.getName(), et, MultiplicityContainer.of(e.getEdgeEndLower(), e.getEdgeEndUpper(),
              e.getEdgeTypeLower(), e.getEdgeTypeUpper()), renamed));
        }
      }
    }
  }
}
