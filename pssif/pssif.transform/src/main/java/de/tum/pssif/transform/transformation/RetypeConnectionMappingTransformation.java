package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.transform.transformation.retyped.RetypedConnectionMapping;
import de.tum.pssif.transform.transformation.retyped.RetypedEdgeEnd;
import de.tum.pssif.transform.transformation.viewed.ViewedEdgeType;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class RetypeConnectionMappingTransformation extends AbstractTransformation {
  private final EdgeType          baseType;
  private final ConnectionMapping baseMapping;
  private final EdgeType          targetType;
  private final ConnectionMapping originalMapping;

  public RetypeConnectionMappingTransformation(EdgeType baseType, ConnectionMapping baseMapping, EdgeType targetType,
      ConnectionMapping originalMapping) {
    this.baseType = baseType;
    this.baseMapping = baseMapping;
    this.targetType = targetType;
    this.originalMapping = originalMapping;
  }

  @Override
  public void apply(View view) {
    ViewedEdgeType actualBaseType = view.findEdgeType(baseType.getName());
    ViewedEdgeType actualTargetType = view.findEdgeType(targetType.getName());
    ViewedNodeType actualFrom = view.findNodeType(baseMapping.getFrom().getNodeType().getName());
    ViewedNodeType actualTo = view.findNodeType(baseMapping.getTo().getNodeType().getName());
    ConnectionMapping actualBaseMapping = actualBaseType.getMapping(actualFrom, actualTo);
    ConnectionMapping actualTargetMapping = actualTargetType.getMapping(actualFrom, actualTo);

    //replace edge ends by retyped edge ends

    actualFrom.deregisterOutgoing(actualBaseType);
    actualFrom.registerOutgoing(actualTargetType);
    actualTo.deregisterIncoming(actualBaseType);
    actualTo.registerIncoming(actualTargetType);

    EdgeEnd actualTargetFrom = actualTargetMapping.getFrom();
    EdgeEnd actualTargetTo = actualTargetMapping.getTo();
    EdgeEnd from = new RetypedEdgeEnd(actualTargetFrom, actualTargetFrom.getName(), actualTargetType, MultiplicityContainer.of(1,
        UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), actualTargetFrom.getNodeType(), originalMapping.getFrom());
    EdgeEnd to = new RetypedEdgeEnd(actualTargetTo, actualTargetTo.getName(), actualTargetType, MultiplicityContainer.of(1,
        UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), actualTargetTo.getNodeType(), originalMapping.getTo());

    //TODO find actual original mapping
    actualBaseType.removeMapping(actualBaseMapping);
    actualBaseType.addMapping(new RetypedConnectionMapping(actualTargetMapping, from, to, originalMapping));
  }

}
