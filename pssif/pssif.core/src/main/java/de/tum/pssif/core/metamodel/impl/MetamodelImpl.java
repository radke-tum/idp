package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.exception.PSSIFStructuralIntegrityException;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Enumeration;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.Unit;
import de.tum.pssif.core.util.PSSIFUtil;


public class MetamodelImpl implements Metamodel {
  private Collection<NodeTypeImpl>      nodes        = Sets.newHashSet();
  private Collection<EdgeTypeImpl>      edges        = Sets.newHashSet();
  private Collection<EnumerationImpl>   enumerations = Sets.newHashSet();
  private Collection<PrimitiveDataType> primitives   = Sets.newHashSet();
  private Collection<Unit>              units        = Sets.newHashSet();

  @Override
  public NodeType create(String name) {
    if (findNodeType(name) != null) {
      throw new PSSIFStructuralIntegrityException("names of nodetypes must be unique");
    }
    NodeTypeImpl result = new NodeTypeImpl(name);
    nodes.add(result);
    return result;
  }

  @Override
  public EdgeType create(String name, String inName, NodeType inType, Multiplicity inMult, String outName, NodeType outType, Multiplicity outMult) {
    NodeTypeImpl inTypeImpl = findNodeType(inType.getName());
    NodeTypeImpl outTypeImpl = findNodeType(outType.getName());
    if (inTypeImpl == null || outType == null) {
      throw new PSSIFStructuralIntegrityException("can not create edge when at least one of the defining node types does not exist");
    }
    EdgeType existing = findEdgeType(name);
    if (existing != null) {
      if (!existing.getIncoming().getName().equals(inName) || !existing.getOutgoing().getName().equals(outName)) {
        throw new PSSIFStructuralIntegrityException("EdgeEnd-NamingConflict!");
      }
    }
    EdgeTypeImpl result = new EdgeTypeImpl(name, inName, inTypeImpl, inMult, outName, outTypeImpl, outMult);
    edges.add(result);
    return result;
  }

  @Override
  public EdgeEnd createAuxiliaryEnd(EdgeType onType, String name, Multiplicity mult, NodeType to) {
    EdgeTypeImpl edgeTypeImpl = null;
    for (EdgeTypeImpl et : this.edges) {
      if (et.equals(onType)) {
        edgeTypeImpl = et;
      }
    }
    if (edgeTypeImpl == null) {
      throw new PSSIFStructuralIntegrityException("provided edge type is not part of this metamodel, or ia a bundle");
    }
    NodeTypeImpl toImpl = findNodeType(to.getName());
    EdgeEndImpl result = new EdgeEndImpl(name, edgeTypeImpl, mult, toImpl);
    edgeTypeImpl.registerAux(result);
    toImpl.registerAuxiliary(edgeTypeImpl);
    return result;
  }

  @Override
  public Collection<NodeType> getNodeTypes() {
    return Sets.<NodeType> newHashSet(this.nodes);
  }

  @Override
  public Collection<EdgeType> getEdgeTypes() {
    //TODO bundles or flat edge types here?
    return Sets.<EdgeType> newHashSet(this.edges);
  }

  @Override
  public NodeTypeImpl findNodeType(String name) {
    return findElement(name, nodes);
  }

  @Override
  public EdgeType findEdgeType(String name) {
    Collection<EdgeTypeImpl> result = Sets.newHashSet();

    for (EdgeTypeImpl edge : edges) {
      if (edge.getName().equals(name)) {
        result.add(edge);
      }
    }

    if (result.size() > 0) {
      return new EdgeTypeBundle(name, result);
    }
    else {
      return null;
    }
  }

  private <T extends ElementType> T findElement(String name, Collection<T> collection) {
    for (T element : collection) {
      if (PSSIFUtil.areSame(element.getName(), name)) {
        return element;
      }
    }
    return null;
  }

  @Override
  public Enumeration createEnumeration(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new PSSIFStructuralIntegrityException("name can not be null or empty");
    }
    if (findDataType(name) != null) {
      throw new PSSIFStructuralIntegrityException("duplicate data type with name " + name);
    }
    EnumerationImpl result = new EnumerationImpl(name);
    enumerations.add(result);
    return result;
  }

  @Override
  public Unit findUnit(String name) {
    for (Unit candidate : units) {
      if (PSSIFUtil.areSame(name, candidate.getName())) {
        return candidate;
      }
    }
    return null;
  }

  @Override
  public Collection<Unit> getUnits() {
    return Collections.unmodifiableCollection(units);
  }

  @Override
  public DataType findDataType(String name) {
    for (DataType candidate : getDataTypes()) {
      if (PSSIFUtil.areSame(name, candidate.getName())) {
        return candidate;
      }
    }
    return null;
  }

  @Override
  public Collection<DataType> getDataTypes() {
    Collection<DataType> result = Sets.<DataType> newHashSet(primitives);
    result.addAll(enumerations);
    return Collections.unmodifiableCollection(result);
  }

  @Override
  public Collection<PrimitiveDataType> getPrimitiveTypes() {
    return Collections.unmodifiableCollection(primitives);
  }

  @Override
  public Collection<Enumeration> getEnumerations() {
    return Collections.<Enumeration> unmodifiableCollection(enumerations);
  }

  @Override
  public void removeEnumeration(Enumeration enumeration) {
    enumerations.remove(enumeration);
  }

  @Override
  public PrimitiveDataType findPrimitiveType(String name) {
    for (PrimitiveDataType candidate : primitives) {
      if (candidate.getName().equals(name)) {
        return candidate;
      }
    }
    return null;
  }

  @Override
  public Enumeration findEnumeration(String name) {
    for (Enumeration candidate : enumerations) {
      if (candidate.getName().equals(name)) {
        return candidate;
      }
    }
    return null;
  }
}
