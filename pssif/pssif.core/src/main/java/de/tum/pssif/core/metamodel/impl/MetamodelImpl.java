package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFUtil;
import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.mutable.MutableEdgeType;
import de.tum.pssif.core.metamodel.mutable.MutableJunctionNodeType;
import de.tum.pssif.core.metamodel.mutable.MutableMetamodel;
import de.tum.pssif.core.metamodel.mutable.MutableNodeType;
import de.tum.pssif.core.metamodel.mutable.MutableNodeTypeBase;


public class MetamodelImpl implements MutableMetamodel {
  private Map<String, MutableNodeTypeBase> nodeTypes = Maps.newHashMap();
  private Map<String, MutableEdgeType>     edgeTypes = Maps.newHashMap();

  @Override
  public MutableNodeType createNodeType(String name) {
    PSSIFUtil.checkNameValidity(name);
    if (!getNodeType(name).isNone()) {
      throw new PSSIFStructuralIntegrityException("a nodetype with name '" + name + "' already exists");
    }
    MutableNodeType result = new NodeTypeImpl(PSSIFUtil.normalize(name));
    nodeTypes.put(result.getName(), result);
    return result;
  }

  @Override
  public MutableJunctionNodeType createJunctionNodeType(String name) {
    PSSIFUtil.checkNameValidity(name);
    if (!getNodeType(name).isNone()) {
      throw new PSSIFStructuralIntegrityException("a nodetype with name '" + name + "' already exists");
    }
    MutableJunctionNodeType result = new JunctionNodeTypeImpl(PSSIFUtil.normalize(name));
    nodeTypes.put(result.getName(), result);
    return result;
  }

  @Override
  public MutableEdgeType createEdgeType(String name) {
    PSSIFUtil.checkNameValidity(name);
    if (!getEdgeType(name).isNone()) {
      throw new PSSIFStructuralIntegrityException("an edgetype with name '" + name + "' already exists");
    }
    EdgeTypeImpl result = new EdgeTypeImpl(PSSIFUtil.normalize(name));
    edgeTypes.put(result.getName(), result);
    return result;
  }

  @Override
  public Collection<NodeTypeBase> getNodeTypes() {
    return ImmutableSet.<NodeTypeBase> copyOf(nodeTypes.values());
  }

  @Override
  public Collection<EdgeType> getEdgeTypes() {
    return ImmutableSet.<EdgeType> copyOf(edgeTypes.values());
  }

  @Override
  public PSSIFOption<NodeTypeBase> getNodeType(String name) {
    return PSSIFOption.<NodeTypeBase> one(nodeTypes.get(PSSIFUtil.normalize(name)));
  }

  @Override
  public PSSIFOption<EdgeType> getEdgeType(String name) {
    return PSSIFOption.<EdgeType> one(edgeTypes.get(PSSIFUtil.normalize(name)));
  }

  @Override
  public Collection<MutableNodeTypeBase> getMutableNodeTypes() {
    return ImmutableSet.copyOf(nodeTypes.values());
  }

  @Override
  public Collection<MutableEdgeType> getMutableEdgeTypes() {
    return ImmutableSet.copyOf(edgeTypes.values());
  }

  @Override
  public PSSIFOption<MutableNodeTypeBase> getMutableNodeType(String name) {
    return PSSIFOption.one(nodeTypes.get(PSSIFUtil.normalize(name)));
  }

  @Override
  public PSSIFOption<MutableEdgeType> getMutableEdgeType(String name) {
    return PSSIFOption.one(edgeTypes.get(PSSIFUtil.normalize(name)));
  }
}
