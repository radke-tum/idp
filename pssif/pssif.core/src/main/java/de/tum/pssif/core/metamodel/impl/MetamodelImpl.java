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
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.util.PSSIFUtil;


public class MetamodelImpl implements Metamodel {
  private Map<String, NodeType>    nodetypes    = Maps.newHashMap();
  private Map<String, EdgeType>    edgetypes    = Maps.newHashMap();
  private Map<String, Enumeration> enumerations = Maps.newHashMap();

  public MetamodelImpl() {
    NodeType rootNodeType = createNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    addDefaultAttributes(rootNodeType);
    EdgeType rootEdgeType = createEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
    addDefaultAttributes(rootEdgeType);
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
    NodeType result = new NodeTypeImpl(name);
    nodetypes.put(PSSIFUtil.normalize(name), result);
    return result;
  }

  @Override
  public EdgeType createEdgeType(String name) {
    EdgeType result = new EdgeTypeImpl(name);
    edgetypes.put(PSSIFUtil.normalize(name), result);
    return result;
  }

  @Override
  public NodeType findNodeType(String name) {
    return nodetypes.get(PSSIFUtil.normalize(name));
  }

  @Override
  public EdgeType findEdgeType(String name) {
    return edgetypes.get(PSSIFUtil.normalize(name));
  }

  @Override
  public Collection<NodeType> getNodeTypes() {
    return Collections.unmodifiableCollection(nodetypes.values());
  }

  @Override
  public Collection<EdgeType> getEdgeTypes() {
    return Collections.unmodifiableCollection(edgetypes.values());
  }

  @Override
  public Enumeration createEnumeration(String name) {
    if (name == null || name.trim().isEmpty()) {
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
    return enumerations.get(PSSIFUtil.normalize(name));
  }

  @Override
  public void removeEnumeration(Enumeration enumeration) {
    enumerations.remove(PSSIFUtil.normalize(enumeration.getName()));
  }

  @Override
  public DataType findDataType(String name) {
    String normalized = PSSIFUtil.normalize(name);
    if (enumerations.containsKey(normalized)) {
      return enumerations.get(normalized);
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
}
