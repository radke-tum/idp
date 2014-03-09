package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.transform.transformation.reversed.ReversedConnectionMapping;


public class ReverseConnectionMappingTransformation extends AbstractTransformation {
  private final ConnectionMapping baseMapping;

  public ReverseConnectionMappingTransformation(ConnectionMapping baseMapping) {
    this.baseMapping = baseMapping;
  }

  @Override
  public void apply(View view) {
    MutableEdgeType baseType = view.getMutableEdgeType(baseMapping.getType().getName()).getOne();
    NodeTypeBase from = view.getBaseNodeType(baseMapping.getFrom().getName()).getOne();
    NodeTypeBase to = view.getBaseNodeType(baseMapping.getTo().getName()).getOne();
    baseType.addMapping(new ReversedConnectionMapping(baseType.getMapping(from, to).getOne()));
  }
}
