package de.tum.pssif.transform.transformation;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.mutable.MutableAttributeGroup;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableNodeTypeBase;
import de.tum.pssif.transform.transformation.moved.CopiedAttribute;
import de.tum.pssif.transform.transformation.moved.MovedAttribute;


public class MoveAttributeTransformation extends AbstractTransformation {
  private final NodeTypeBase sourceType;
  private final String       attributeName;
  private final NodeTypeBase targetType;
  private final Attribute    targetAttribute;
  private final EdgeType     edgeType;

  public MoveAttributeTransformation(NodeTypeBase sourceType, String name, NodeTypeBase targetType, Attribute targetAttribute, EdgeType edgeType) {
    this.sourceType = sourceType;
    attributeName = name;
    this.targetType = targetType;
    this.targetAttribute = targetAttribute;
    this.edgeType = edgeType;
  }

  @Override
  public void apply(View view) {
    MutableNodeTypeBase actualSourceType = view.getMutableBaseNodeType(sourceType.getName()).getOne();
    MutableNodeTypeBase actualTargetType = view.getMutableBaseNodeType(targetType.getName()).getOne();
    MutableEdgeType actualEdgeType = view.getMutableEdgeType(edgeType.getName()).getOne();
    Attribute actualTargetAttribute = actualTargetType.getAttribute(targetAttribute.getName()).getOne();

    MutableAttributeGroup defaultGroup = actualSourceType.getMutableDefaultAttributeGroup();
    ConnectionMapping mapping = actualEdgeType.getMapping(actualSourceType, actualTargetType).getOne();

    PSSIFOption<Attribute> sourceAttribute = actualSourceType.getAttribute(attributeName);
    if (sourceAttribute.isOne()) {
      defaultGroup.removeAttribute(sourceAttribute.getOne());
      defaultGroup.addAttribute(new CopiedAttribute(attributeName, sourceAttribute.getOne(), actualTargetAttribute, mapping));
    }
    else {
      defaultGroup.addAttribute(new MovedAttribute(attributeName, actualTargetAttribute, mapping));
    }
  }
}
