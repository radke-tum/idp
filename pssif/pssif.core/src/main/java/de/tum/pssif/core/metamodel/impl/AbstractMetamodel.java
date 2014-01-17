package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.util.PSSIFUtil;


public abstract class AbstractMetamodel<N extends NodeType, E extends EdgeType> implements Metamodel {
  private Map<String, N>           nodetypes    = Maps.newHashMap();
  private Map<String, E>           edgetypes    = Maps.newHashMap();
  private Map<String, Enumeration> enumerations = Maps.newHashMap();

  protected void addNodeType(N type) {
    nodetypes.put(PSSIFUtil.normalize(type.getName()), type);
  }

  protected void removeNodeType(N type) {
    nodetypes.remove(PSSIFUtil.normalize(type.getName()));
  }

  protected void addEdgeType(E type) {
    edgetypes.put(PSSIFUtil.normalize(type.getName()), type);
  }

  protected void removeEdgeType(E type) {
    edgetypes.remove(PSSIFUtil.normalize(type.getName()));
  }

  protected void addEnumeration(Enumeration enumeration) {
    enumerations.put(PSSIFUtil.normalize(enumeration.getName()), enumeration);
  }

  protected void removeEnumeration(Enumeration enumeration) {
    enumerations.remove(PSSIFUtil.normalize(enumeration.getName()));
  }

  @Override
  public N findNodeType(String name) {
    return PSSIFUtil.find(name, nodetypes.values());
  }

  @Override
  public E findEdgeType(String name) {
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
  public Collection<Enumeration> getEnumerations() {
    return Collections.unmodifiableCollection(enumerations.values());
  }

  @Override
  public Enumeration findEnumeration(String name) {
    return PSSIFUtil.find(name, enumerations.values());
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
}
