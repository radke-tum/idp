package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.metamodel.Units;
import de.tum.pssif.core.metamodel.impl.base.AbstractEdgeType;
import de.tum.pssif.core.util.PSSIFUtil;


public class EdgeTypeImpl extends AbstractEdgeType {
  public EdgeTypeImpl(String name) {
    super(name);
    addAttributeGroup(new AttributeGroupImpl(PSSIFConstants.DEFAULT_ATTRIBUTE_GROUP_NAME, this));
  }

  @Override
  public ConnectionMapping createMapping(String inName, NodeType in, Multiplicity inMultiplicity, String outName, NodeType out,
                                         Multiplicity outMultiplicity) {
    EdgeEnd from = new EdgeEndImpl(inName, this, inMultiplicity, in);
    EdgeEnd to = new EdgeEndImpl(outName, this, outMultiplicity, out);
    ConnectionMapping result = new ConnectionMappingImpl(from, to);
    addMappingInternal(result);

    return result;
  }

  @Override
  public EdgeEnd createAuxiliary(String name, Multiplicity multiplicity, NodeType to) {
    EdgeEnd aux = findAuxiliary(name);
    if (aux != null) {
      throw new PSSIFStructuralIntegrityException("An auxiliary edge end with this name already exists.");
    }
    EdgeEnd result = new EdgeEndImpl(name, this, multiplicity, to);
    addAuxiliaryInternal(result);
    to.registerAuxiliary(this);
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
    for (EdgeType specialization : getSpecials()) {
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
