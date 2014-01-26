package de.tum.pssif.transform.transformation;

import java.util.Collection;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeEnd;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;


public class RenameEdgeTypeTransformation extends RenameTransformation<EdgeType> {

  public RenameEdgeTypeTransformation(EdgeType target, String name) {
    super(target, name);
  }

  @Override
  public Metamodel apply(View view) {
    ViewedEdgeType actualTarget = view.findEdgeType(getTarget().getName());
    view.removeEdgeType(actualTarget);
    ViewedEdgeType renamed = new ViewedEdgeType(actualTarget, getName());
    view.addEdgeType(renamed);

    if (actualTarget.getGeneral() != null) {
      actualTarget.getGeneral().unregisterSpecialization(actualTarget);
      renamed.inherit(actualTarget.getGeneral());
    }

    Collection<EdgeType> specials = Sets.newHashSet(actualTarget.getSpecials());
    for (EdgeType special : specials) {
      special.inherit(renamed);
    }

    Collection<ConnectionMapping> toRemove = Sets.newHashSet();
    for (ConnectionMapping mapping : actualTarget.getMappings()) {
      toRemove.add(mapping);
      EdgeEnd from = mapping.getFrom();
      EdgeEnd to = mapping.getTo();
      ViewedEdgeEnd viewedFrom = new ViewedEdgeEnd(from, from.getName(), renamed, MultiplicityContainer.of(from.getEdgeEndLower(),
          from.getEdgeEndUpper(), from.getEdgeTypeLower(), from.getEdgeTypeUpper()), from.getNodeType());
      ViewedEdgeEnd viewedTo = new ViewedEdgeEnd(to, to.getName(), renamed, MultiplicityContainer.of(to.getEdgeEndLower(), to.getEdgeEndUpper(),
          to.getEdgeTypeLower(), to.getEdgeTypeUpper()), to.getNodeType());
      from.getNodeType().registerOutgoing(renamed);
      to.getNodeType().registerIncoming(renamed);
      renamed.addMapping(new ViewedConnectionMapping(mapping, viewedFrom, viewedTo));
    }
    for (ConnectionMapping mapping : toRemove) {
      mapping.getFrom().getNodeType().deregisterOutgoing(actualTarget);
      mapping.getTo().getNodeType().deregisterIncoming(actualTarget);
      actualTarget.removeMapping(mapping);
    }

    for (EdgeEnd end : actualTarget.getAuxiliaries()) {
      end.getNodeType().deregisterAuxiliary(actualTarget);
      end.getNodeType().registerAuxiliary(renamed);
      renamed.removeAuxiliary(end);
      renamed.addAuxiliary(new ViewedEdgeEnd(end, end.getName(), renamed, MultiplicityContainer.of(end.getEdgeEndLower(), end.getEdgeEndUpper(),
          end.getEdgeTypeLower(), end.getEdgeTypeUpper()), end.getNodeType()));
    }
    return view;
  }
}
