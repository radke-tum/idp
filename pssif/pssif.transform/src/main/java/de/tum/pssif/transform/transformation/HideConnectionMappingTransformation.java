package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;


public class HideConnectionMappingTransformation extends AbstractTransformation {
  private final EdgeType          type;
  private final ConnectionMapping mapping;

  public HideConnectionMappingTransformation(EdgeType type, ConnectionMapping mapping) {
    this.type = type;
    this.mapping = mapping;
  }

  @Override
  public void apply(View view) {
    MutableEdgeType actualType = view.getMutableEdgeType(type.getName()).getOne();
    NodeTypeBase fromType = view.getBaseNodeType(mapping.getFrom().getName()).getOne();
    NodeTypeBase toType = view.getBaseNodeType(mapping.getTo().getName()).getOne();
    PSSIFOption<ConnectionMapping> actualMapping = actualType.getMapping(fromType, toType);
    for (ConnectionMapping cm : actualMapping.getMany()) {
      actualType.removeMapping(cm);
    }
  }
}
