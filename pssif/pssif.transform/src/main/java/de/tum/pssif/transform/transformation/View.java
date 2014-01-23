package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.impl.base.AbstractMetamodel;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeEnd;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class View extends AbstractMetamodel {
  private Metamodel baseMetamodel;

  public View(Metamodel baseMetamodel) {
    this.baseMetamodel = baseMetamodel;

    for (NodeType nt : baseMetamodel.getNodeTypes()) {
      addNodeTypeInternal(new ViewedNodeType(nt));
    }
    for (NodeType nt : baseMetamodel.getNodeTypes()) {
      if (nt.getGeneral() != null) {
        NodeType owned = findNodeType(nt.getName());
        NodeType ownedGeneral = findNodeType(nt.getGeneral().getName());
        owned.inherit(ownedGeneral);
      }
    }
    for (EdgeType et : baseMetamodel.getEdgeTypes()) {
      ViewedEdgeType viewed = new ViewedEdgeType(et);
      addEdgeTypeInternal(viewed);

      for (ConnectionMapping mapping : et.getMappings()) {
        EdgeEnd from = mapping.getFrom();
        EdgeEnd to = mapping.getTo();
        ViewedEdgeEnd viewedFrom = new ViewedEdgeEnd(from, from.getName(), viewed, MultiplicityContainer.of(from.getEdgeEndLower(),
            from.getEdgeEndUpper(), from.getEdgeTypeLower(), from.getEdgeTypeUpper()), findNodeType(from.getNodeType().getName()));
        ViewedEdgeEnd viewedTo = new ViewedEdgeEnd(to, to.getName(), viewed, MultiplicityContainer.of(to.getEdgeEndLower(), to.getEdgeEndUpper(),
            to.getEdgeTypeLower(), to.getEdgeTypeUpper()), findNodeType(to.getNodeType().getName()));
        viewed.addMapping(new ViewedConnectionMapping(mapping, viewedFrom, viewedTo));
      }
    }
    for (EdgeType et : baseMetamodel.getEdgeTypes()) {
      if (et.getGeneral() != null) {
        EdgeType owned = findEdgeType(et.getName());
        EdgeType ownedGeneral = findEdgeType(et.getGeneral().getName());
        owned.inherit(ownedGeneral);
      }
    }
  }

  public Metamodel transform(AbstractTransformation transformation) {
    return transformation.apply(this);
  }

  protected void addNodeType(NodeType type) {
    addNodeTypeInternal(type);
  }

  protected void removeNodeType(NodeType type) {
    removeNodeTypeInternal(type);
  }
}
