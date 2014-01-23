package de.tum.pssif.core.metamodel.impl.base;

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


public abstract class AbstractMetamodel implements Metamodel {
  private Map<String, NodeType>    nodetypes    = Maps.newHashMap();
  private Map<String, EdgeType>    edgetypes    = Maps.newHashMap();
  private Map<String, Enumeration> enumerations = Maps.newHashMap();

  protected final void addNodeTypeInternal(NodeType type) {
    nodetypes.put(PSSIFUtil.normalize(type.getName()), type);
  }

  protected final void removeNodeTypeInternal(NodeType type) {
    nodetypes.remove(PSSIFUtil.normalize(type.getName()));
  }

  protected final void addEdgeTypeInternal(EdgeType type) {
    edgetypes.put(PSSIFUtil.normalize(type.getName()), type);
  }

  protected final void removeEdgeTypeInternal(EdgeType type) {
    edgetypes.remove(PSSIFUtil.normalize(type.getName()));
  }

  protected final void addEnumerationInternal(Enumeration enumeration) {
    enumerations.put(PSSIFUtil.normalize(enumeration.getName()), enumeration);
  }

  protected final void removeEnumerationInternal(Enumeration enumeration) {
    enumerations.remove(PSSIFUtil.normalize(enumeration.getName()));
  }

  @Override
  public final NodeType findNodeType(String name) {
    return PSSIFUtil.find(name, nodetypes.values());
  }

  @Override
  public final EdgeType findEdgeType(String name) {
    return PSSIFUtil.find(name, edgetypes.values());
  }

  @Override
  public final Collection<NodeType> getNodeTypes() {
    return Collections.<NodeType> unmodifiableCollection(nodetypes.values());
  }

  @Override
  public final Collection<EdgeType> getEdgeTypes() {
    return Collections.<EdgeType> unmodifiableCollection(edgetypes.values());
  }

  @Override
  public final Collection<Enumeration> getEnumerations() {
    return Collections.unmodifiableCollection(enumerations.values());
  }

  @Override
  public final Enumeration findEnumeration(String name) {
    return PSSIFUtil.find(name, enumerations.values());
  }

  @Override
  public final DataType findDataType(String name) {
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
  public final Collection<DataType> getDataTypes() {
    Set<DataType> result = Sets.newHashSet();

    result.addAll(enumerations.values());
    result.addAll(PrimitiveDataType.TYPES);

    return Collections.unmodifiableCollection(result);
  }

  @Override
  public final Collection<PrimitiveDataType> getPrimitiveTypes() {
    return Collections.unmodifiableCollection(PrimitiveDataType.TYPES);
  }

  @Override
  public final PrimitiveDataType findPrimitiveType(String name) {
    return PSSIFUtil.find(name, PrimitiveDataType.TYPES);
  }
}
