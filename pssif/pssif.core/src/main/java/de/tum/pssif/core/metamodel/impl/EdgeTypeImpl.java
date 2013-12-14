package de.tum.pssif.core.metamodel.impl;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;

import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeEnd;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Multiplicity;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.traits.ElementApplicable;
import de.tum.pssif.core.util.PSSIFUtil;


public class EdgeTypeImpl extends ElementTypeImpl<EdgeType> implements EdgeType {
  private Collection<ConnectionMapping> mappings    = Sets.newHashSet();
  private Collection<EdgeEnd>           auxiliaries = Sets.newHashSet();

  public EdgeTypeImpl(String name) {
    super(name);
  }

  @Override
  public ConnectionMapping createMapping(String inName, NodeType in, Multiplicity inMultiplicity, String outName, NodeType out,
                                         Multiplicity outMultiplicity) {
    EdgeEnd from = new EdgeEndImpl(inName, this, inMultiplicity, in);
    EdgeEnd to = new EdgeEndImpl(outName, this, outMultiplicity, out);

    in.registerOutgoing(this);
    out.registerIncoming(this);

    ConnectionMapping result = new ConnectionMappingImpl(from, to);

    mappings.add(result);

    return result;
  }

  @Override
  public EdgeEnd createAuxiliary(String name, Multiplicity multiplicity, NodeType to) {
    EdgeEnd result = new EdgeEndImpl(name, this, multiplicity, to);
    auxiliaries.add(result);
    to.registerAuxiliary(this);
    return result;
  }

  @Override
  public ElementApplicable getIncoming() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    for (ConnectionMapping mapping : mappings) {
      result.add(mapping.getFrom());
    }

    return new EdgeEndBundleImpl(result);
  }

  @Override
  public ElementApplicable getOutgoing() {
    Collection<EdgeEnd> result = Sets.newHashSet();

    for (ConnectionMapping mapping : mappings) {
      result.add(mapping.getTo());
    }

    return new EdgeEndBundleImpl(result);
  }

  @Override
  public ConnectionMapping getMapping(NodeType in, NodeType out) {
    ConnectionMapping result = null;

    for (ConnectionMapping candidate : mappings) {
      if (candidate.getFrom().getNodeType().equals(in) && candidate.getTo().getNodeType().equals(out)) {
        result = candidate;
        break;
      }
    }

    return result;
  }

  @Override
  public Collection<EdgeEnd> getAuxiliaries() {
    return Collections.unmodifiableCollection(auxiliaries);
  }

  @Override
  public EdgeEnd findAuxiliary(String name) {
    return PSSIFUtil.find(name, auxiliaries);
  }

}
