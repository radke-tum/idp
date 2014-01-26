package de.tum.pssif.core.metamodel.impl;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.MutableMetamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.util.PSSIFUtil;


public class MetamodelImpl extends AbstractMetamodel<NodeTypeImpl, EdgeTypeImpl> implements MutableMetamodel {
  public MetamodelImpl() {
    NodeTypeImpl rootNodeType = new NodeTypeImpl(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    addNodeType(rootNodeType);
    addDefaultAttributes(rootNodeType);
    EdgeTypeImpl rootEdgeType = new EdgeTypeImpl(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
    addEdgeType(rootEdgeType);
    addDefaultAttributes(rootEdgeType);
    rootEdgeType.createAttribute(rootEdgeType.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED, PrimitiveDataType.BOOLEAN, true,
        AttributeCategory.METADATA);
    rootEdgeType.createMapping("from", rootNodeType, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "to",
        rootNodeType, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
  }

  private final void addDefaultAttributes(ElementType<?, ?> type) {
    type.createAttribute(type.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_ID, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);
    type.createAttribute(type.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_NAME, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);
    type.createAttribute(type.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START, PrimitiveDataType.DATE, true,
        AttributeCategory.TIME);
    type.createAttribute(type.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END, PrimitiveDataType.DATE, true,
        AttributeCategory.TIME);
    type.createAttribute(type.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);
    type.createAttribute(type.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT, PrimitiveDataType.STRING, true,
        AttributeCategory.METADATA);
  }

  @Override
  public NodeType createNodeType(String name) {
    if (!PSSIFUtil.isValidName(name)) {
      throw new PSSIFStructuralIntegrityException("a node can not have an empty name");
    }
    if (findNodeType(name) != null) {
      throw new PSSIFStructuralIntegrityException("a node type with the name " + name + " already exists");
    }
    NodeTypeImpl result = new NodeTypeImpl(name);
    addNodeType(result);
    result.inherit(findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME));
    return result;
  }

  @Override
  public EdgeType createEdgeType(String name) {
    if (!PSSIFUtil.isValidName(name)) {
      throw new PSSIFStructuralIntegrityException("an edge can not have an empty name");
    }
    if (findEdgeType(name) != null) {
      throw new PSSIFStructuralIntegrityException("an edge type with name " + name + " already exitsts");
    }
    EdgeTypeImpl result = new EdgeTypeImpl(name);
    addEdgeType(result);
    result.inherit(findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME));
    return result;
  }

  @Override
  public Enumeration createEnumeration(String name) {
    if (!PSSIFUtil.isValidName(name)) {
      throw new PSSIFStructuralIntegrityException("name can not be null or empty");
    }
    if (findDataType(name) != null) {
      throw new PSSIFStructuralIntegrityException("duplicate data type with name " + name);
    }
    Enumeration result = new EnumerationImpl(name);
    addEnumeration(result);
    return result;
  }

  @Override
  public void removeEnumeration(Enumeration enumeration) {
    super.removeEnumeration(enumeration);
  }
}
