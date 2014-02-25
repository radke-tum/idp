package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.Units;
import de.tum.pssif.core.metamodel.impl.base.AbstractNodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;


public class NodeTypeImpl extends AbstractNodeType {
  public NodeTypeImpl(String name) {
    super(name);
    addAttributeGroup(new AttributeGroupImpl(PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME, this));
  }

  @Override
  public Node create(Model model) {
    return new CreateNodeOperation(this).apply(model);
  }

  @Override
  public PSSIFOption<Node> apply(Model model, boolean includeSubTypes) {
    PSSIFOption<Node> result = new ReadNodesOperation(this).apply(model);
    if (includeSubTypes) {
      for (NodeType currentType : PSSIFUtil.specializationsClosure((NodeType) this)) {
        result = PSSIFOption.merge(result, new ReadNodesOperation(currentType).apply(model));
      }
    }
    return result;
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType type, Unit unit, boolean visible, AttributeCategory category) {
    if (name == null || name.trim().isEmpty()) {
      throw new PSSIFStructuralIntegrityException("name can not be null or empty");
    }
    //Note: this disables attribute overloading. If we want
    //to overload attributes in specialization element types
    //we need to find only locally, and filter on getAttributes
    //so that inherited attributes are only taken when no local ones exist.
    if (findAttribute(name) != null || findAttributeInSpecializations(name) != null) {
      throw new PSSIFStructuralIntegrityException("duplicate attribute with name " + name);
    }
    if (!(PrimitiveDataType.DECIMAL.equals(type) || PrimitiveDataType.INTEGER.equals(type)) && !Units.NONE.equals(unit)) {
      throw new PSSIFStructuralIntegrityException("Only numeric attributes can have units!");
    }
    AttributeImpl result = new AttributeImpl(name, type, unit, visible, category);
    addAttribute(group, result);
    return result;
  }

  private Attribute findAttributeInSpecializations(String name) {
    for (NodeType specialization : getSpecials()) {
      Attribute attr = specialization.findAttribute(name);
      if (attr != null) {
        return attr;
      }
    }
    return null;
  }

  @Override
  public Attribute createAttribute(AttributeGroup group, String name, DataType dataType, boolean visible, AttributeCategory category) {
    return createAttribute(group, name, dataType, Units.NONE, visible, category);
  }

  @Override
  public AttributeGroup createAttributeGroup(String name) {
    if (PSSIFUtil.normalize(name).isEmpty()) {
      throw new PSSIFStructuralIntegrityException("The name of an attrobute group can not be null or empty!");
    }
    if (findAttributeGroup(name) != null) {
      throw new PSSIFStructuralIntegrityException("An attribute group with the name " + name + " already exists for element type " + getName());
    }
    AttributeGroupImpl result = new AttributeGroupImpl(name, this);
    addAttributeGroup(result);
    return result;
  }

  @Override
  public void removeAttributeGroup(AttributeGroup group) {
    if (PSSIFUtil.areSame(group.getName(), PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME)) {
      throw new PSSIFStructuralIntegrityException("The default attribute group can not be removed!");
    }
    super.removeAttributeGroup(findAttributeGroup(group.getName()));
  }
}
