package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.AttributeCategory;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.Multiplicity.MultiplicityContainer;
import de.tum.pssif.core.metamodel.Multiplicity.UnlimitedNatural;
import de.tum.pssif.core.metamodel.MutableMetamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.util.PSSIFUtil;


public class MetamodelImpl implements MutableMetamodel {
  private Map<String, NodeTypeImpl> nodetypes    = Maps.newHashMap();
  private Map<String, EdgeTypeImpl> edgetypes    = Maps.newHashMap();
  private Map<String, Enumeration>  enumerations = Maps.newHashMap();

  public MetamodelImpl() {
    NodeTypeImpl rootNodeType = new NodeTypeImpl(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    nodetypes.put(PSSIFUtil.normalize(PSSIFConstants.ROOT_NODE_TYPE_NAME), rootNodeType);
    addDefaultAttributes(rootNodeType);
    EdgeTypeImpl rootEdgeType = new EdgeTypeImpl(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
    edgetypes.put(PSSIFUtil.normalize(PSSIFConstants.ROOT_EDGE_TYPE_NAME), rootEdgeType);
    addDefaultAttributes(rootEdgeType);
    rootEdgeType.createAttribute(rootEdgeType.getDefaultAttributeGroup(), PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED, PrimitiveDataType.BOOLEAN, true,
        AttributeCategory.METADATA);
    rootEdgeType.createMapping("from", rootNodeType, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED), "to",
        rootNodeType, MultiplicityContainer.of(1, UnlimitedNatural.UNLIMITED, 0, UnlimitedNatural.UNLIMITED));
  }

  private final void addDefaultAttributes(ElementType<?> type) {
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
    nodetypes.put(PSSIFUtil.normalize(name), result);
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
    edgetypes.put(PSSIFUtil.normalize(name), result);
    result.inherit(findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME));
    return result;
  }

  @Override
  public NodeTypeImpl findNodeType(String name) {
    return PSSIFUtil.find(name, nodetypes.values());
  }

  @Override
  public EdgeTypeImpl findEdgeType(String name) {
    return PSSIFUtil.find(name, edgetypes.values());
  }

  @Override
  public Collection<NodeType> getNodeTypes() {
    return Collections.<NodeType> unmodifiableCollection(nodetypes.values());
  }

  @Override
  public Collection<EdgeType> getEdgeTypes() {
    return Collections.<EdgeType> unmodifiableCollection(edgetypes.values());
  }

  @Override
  public Enumeration createEnumeration(String name) {
    if (!PSSIFUtil.isValidName(name)) {
      throw new PSSIFStructuralIntegrityException("name can not be null or empty");
    }
    String normalized = PSSIFUtil.normalize(name);
    if (findDataType(normalized) != null) {
      throw new PSSIFStructuralIntegrityException("duplicate data type with name " + name);
    }
    Enumeration result = new EnumerationImpl(name);
    enumerations.put(normalized, result);
    return result;
  }

  @Override
  public Collection<Enumeration> getEnumerations() {
    return Collections.unmodifiableCollection(enumerations.values());
  }

  @Override
  public Enumeration findEnumeration(String name) {
    return PSSIFUtil.find(name, enumerations.values());
  }

  @Override
  public void removeEnumeration(Enumeration enumeration) {
    enumerations.remove(PSSIFUtil.normalize(enumeration.getName()));
  }

  @Override
  public DataType findDataType(String name) {
    String normalized = PSSIFUtil.normalize(name);
    DataType dt = PSSIFUtil.find(normalized, enumerations.values());
    if (dt != null) {
      return dt;
    }
    else {
      return findPrimitiveType(normalized);
    }
  }

  @Override
  public Collection<DataType> getDataTypes() {
    Set<DataType> result = Sets.newHashSet();

    result.addAll(enumerations.values());
    result.addAll(PrimitiveDataType.TYPES);

    return Collections.unmodifiableCollection(result);
  }

  @Override
  public Collection<PrimitiveDataType> getPrimitiveTypes() {
    return Collections.unmodifiableCollection(PrimitiveDataType.TYPES);
  }

  @Override
  public PrimitiveDataType findPrimitiveType(String name) {
    return PSSIFUtil.find(name, PrimitiveDataType.TYPES);
  }

  @Override
  public void addAlias(ElementType<?> elementType, String alias) {
    Class<?> metaType = elementType.getMetaType();
    if (NodeType.class.equals(metaType)) {
      if (PSSIFUtil.find(alias, this.nodetypes.values()) != null) {
        throw new PSSIFStructuralIntegrityException("The alias " + alias + " is already used elswhere in the metamodel.");
      }
      if (!PSSIFUtil.isValidName(alias)) {
        throw new PSSIFStructuralIntegrityException("The provided alias is empty!");
      }
      findNodeType(elementType.getName()).addName(alias);
    }
    else if (EdgeType.class.equals(metaType)) {
      if (PSSIFUtil.find(alias, this.edgetypes.values()) != null) {
        throw new PSSIFStructuralIntegrityException("The alias " + alias + " is already used elswhere in the metamodel.");
      }
      if (!PSSIFUtil.isValidName(alias)) {
        throw new PSSIFStructuralIntegrityException("The provided alias is empty!");
      }
      findEdgeType(elementType.getName()).addName(alias);
    }
  }
}
