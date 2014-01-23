package de.tum.pssif.transform.transformation.viewed;

import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.impl.base.AbstractNodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;


public class ViewedNodeType extends AbstractNodeType {
  private final NodeType baseType;

  public ViewedNodeType(NodeType baseType) {
    this(baseType, baseType.getName());
  }

  public ViewedNodeType(NodeType baseType, String name) {
    super(name);
    this.baseType = baseType;

    for (AttributeGroup g : baseType.getAttributeGroups()) {
      addAttributeGroup(new ViewedAttributeGroup(g, g.getName(), this));
    }
  }

  @Override
  public Node create(Model model) {
    return baseType.create(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model) {
    return baseType.apply(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model, String id) {
    return baseType.apply(model, id);
  }

  @Override
  public AttributeGroup createAttributeGroup(String name) {
    //FIXME separate mutable nodetype interface from interface nodetype
    return null;
  }

  @Override
  public void removeAttributeGroup(AttributeGroup attributeGroup) {
    //FIXME separate mutable nodetype interface from interface nodetype

  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible, AttributeCategory category) {
    //FIXME separate mutable nodetype interface from interface nodetype
    return null;
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, Unit unit, boolean visible, AttributeCategory category) {
    //FIXME separate mutable nodetype interface from interface nodetype
    return null;
  }
}
