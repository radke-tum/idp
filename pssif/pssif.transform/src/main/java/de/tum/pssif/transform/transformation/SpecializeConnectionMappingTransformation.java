package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.viewed.ViewedConnectionMapping;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeEnd;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class SpecializeConnectionMappingTransformation extends AbstractTransformation {
  private final EdgeType          type;
  private final NodeType          from;
  private final NodeType          to;
  private final ConnectionMapping baseMapping;

  public SpecializeConnectionMappingTransformation(EdgeType type, NodeType from, NodeType to, ConnectionMapping baseMapping) {
    this.type = type;
    this.from = from;
    this.to = to;
    this.baseMapping = baseMapping;
  }

  @Override
  public void apply(View view) {
    ViewedEdgeType actualType = view.findEdgeType(type.getName());
    ViewedNodeType actualFrom = view.findNodeType(from.getName());
    ViewedNodeType actualTo = view.findNodeType(to.getName());
    ViewedNodeType baseFromType = view.findNodeType(baseMapping.getFrom().getNodeType().getName());
    ViewedNodeType baseToType = view.findNodeType(baseMapping.getTo().getNodeType().getName());

    ConnectionMapping actualBaseMapping = actualType.getMapping(baseFromType, baseToType);
    EdgeEnd from = actualBaseMapping.getFrom();
    Multiplicity fromMultiplicity = MultiplicityContainer.of(from.getEdgeEndLower(), from.getEdgeEndUpper(), from.getEdgeTypeLower(),
        from.getEdgeTypeUpper());
    EdgeEnd to = actualBaseMapping.getTo();
    Multiplicity toMultiplicity = MultiplicityContainer.of(to.getEdgeEndLower(), to.getEdgeEndUpper(), to.getEdgeTypeLower(), to.getEdgeTypeUpper());

    EdgeEnd newFrom = new ViewedEdgeEnd(from, from.getName(), actualType, fromMultiplicity, actualFrom);
    EdgeEnd newTo = new ViewedEdgeEnd(to, to.getName(), actualType, toMultiplicity, actualTo);
    actualType.addMapping(new ViewedConnectionMapping(actualBaseMapping, newFrom, newTo));
  }
}
