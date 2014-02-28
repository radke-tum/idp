package de.tum.pssif.transform.transformation.artificial;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;
import de.tum.pssif.transform.transformation.viewed.ViewedNodeType;


public class CreateArtificialNodeType extends ViewedNodeType {
  private final NodeType          targetType;
  private final ConnectionMapping mapping;              // the connection mapping to create an edge of for the connection of the created node and the source node

  private final Attribute         targetTypeIdAttribute;
  private final Attribute         edgeTypeIdAttribute;

  public CreateArtificialNodeType(NodeType baseType, NodeType targetType, EdgeType edgeType) {
    super(baseType);
    this.targetType = targetType;
    mapping = edgeType.getMapping(baseType, targetType);

    targetTypeIdAttribute = this.targetType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    edgeTypeIdAttribute = edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
  }

  @Override
  public Node create(Model model) {
    Node result = getBaseType().create(model);
    Node artificial = targetType.create(model);

    edgeTypeIdAttribute.set(mapping.create(model, result, artificial), PSSIFOption.one(PSSIFValue.create("temp")));
    targetTypeIdAttribute.set(artificial, PSSIFOption.one(PSSIFValue.create("temp")));

    return result;
  }

  @Override
  public PSSIFOption<Node> apply(Model model, boolean includeSubTypes) {
    return getBaseType().apply(model, includeSubTypes);
  }

  @Override
  public AttributeGroup createAttributeGroup(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeAttributeGroup(AttributeGroup attributeGroup) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible, AttributeCategory category) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, Unit unit, boolean visible, AttributeCategory category) {
    throw new UnsupportedOperationException();
  }

}
