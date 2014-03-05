package de.tum.pssif.transform.model;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;
import de.tum.pssif.transform.ModelMapper;
import de.tum.pssif.transform.graph.AElement;
import de.tum.pssif.transform.graph.Edge;
import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.graph.Node;


public class EpkModelMapper implements ModelMapper {
  private static final Collection<String> HYPEREDGE_NODETYPE_NAMES = ImmutableSet.of("XOR", "OR", "AND");

  @Override
  public Model read(Metamodel metamodel, Graph graph) {
    Model result = new ModelImpl();

    Multimap<String, Node> hyperEdges = createNodes(metamodel, graph, result);
    createHyperEdges(metamodel, result, hyperEdges);
    createEdges(metamodel, graph, result);

    return result;
  }

  @Override
  public Graph write(Metamodel metamodel, Model model) {
    // TODO Auto-generated method stub
    return new Graph();
  }

  private void createEdges(Metamodel metamodel, Graph graph, Model result) {
    Collection<Edge> toCreate = Sets.newHashSet();
    for (Edge e : graph.getEdges()) {
      if (!(HYPEREDGE_NODETYPE_NAMES.contains(e.getSource().getType()) || HYPEREDGE_NODETYPE_NAMES.contains(e.getTarget().getType()))) {
        toCreate.add(e);
      }
    }

    for (Edge e : toCreate) {
      String sourceTypeName = e.getSource().getType();
      String targetTypeName = e.getTarget().getType();

      NodeType sourceType = metamodel.findNodeType(sourceTypeName);
      NodeType targetType = metamodel.findNodeType(targetTypeName);
      if (sourceType != null && targetType != null) {
        if ("Organizational unit".equals(sourceTypeName)) {
          createEdge(result, e, sourceType, targetType, metamodel.findEdgeType("Performs"), false);
        }
        else if ("Organizational unit".equals(targetTypeName)) {
          createEdge(result, e, sourceType, targetType, metamodel.findEdgeType("Performs"), true);
        }
        else if ("Information/ Material".equals(sourceTypeName)) {
          createEdge(result, e, sourceType, targetType, metamodel.findEdgeType("Information Flow"), false);
        }
        else if ("Information/ Material".equals(targetTypeName)) {
          createEdge(result, e, sourceType, targetType, metamodel.findEdgeType("Information Flow"), true);
        }
        else {
          EdgeType type = metamodel.findEdgeType(e.getType());

          if (type != null) {
            createEdge(result, e, sourceType, targetType, type, false);
          }
          else {
            System.out.println("missed " + e.getType());
          }
        }
      }
      else {
        System.out.println("missed source/target type of " + e + "(" + e.getSource().getType() + "," + e.getTarget().getType() + ")");
      }
    }
  }

  private void createEdge(Model result, Edge e, NodeType sourceType, NodeType targetType, EdgeType type, boolean swap) {
    ConnectionMapping mapping = type.getMapping(sourceType, targetType);
    PSSIFOption<de.tum.pssif.core.model.Node> source = sourceType.apply(result, e.getSource().getId());
    PSSIFOption<de.tum.pssif.core.model.Node> target = targetType.apply(result, e.getTarget().getId());
    if (source.isOne() && target.isOne() && mapping != null) {
      de.tum.pssif.core.model.Edge edge = null;
      if (swap) {
        edge = mapping.create(result, target.getOne(), source.getOne());
      }
      else {
        edge = mapping.create(result, source.getOne(), target.getOne());
      }
      setAttributes(e, type, edge);
    }
    else {
      System.out.println("missed mapping for " + e.getType() + "(" + sourceType + "," + targetType + ")");
    }
  }

  private void setAttributes(AElement e, EdgeType type, de.tum.pssif.core.model.Edge edge) {
    Attribute id = type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    Attribute directed = type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED);
    id.set(edge, PSSIFOption.one(PSSIFValue.create(e.getId())));
    directed.set(edge, PSSIFOption.one(PSSIFValue.create(Boolean.TRUE)));
  }

  /**
   * TODO Expects no edges between nodes representing hyperedges.
   * If this should be allowed, our hyperedges are insufficient to describe the external models
   * 
   * @param metamodel
   * @param result
   * @param hyperEdges
   */
  private void createHyperEdges(Metamodel metamodel, Model result, Multimap<String, Node> hyperEdges) {
    //this expects no edges between nodes representing hyperedges 
    //if this should be allowed, our hyperedges are insufficient to describe the external models
    EdgeType connector = metamodel.findEdgeType("Dynamic connector");
    for (String conjunction : hyperEdges.keySet()) {
      for (Node representative : hyperEdges.get(conjunction)) {
        Collection<Node> froms = Sets.newHashSet();
        Collection<Node> tos = Sets.newHashSet();
        for (Edge incoming : representative.getIncoming()) {
          froms.add(incoming.getSource());
        }
        for (Edge outgoing : representative.getOutgoing()) {
          tos.add(outgoing.getTarget());
        }

        Iterator<Node> fromsIt = froms.iterator();
        Iterator<Node> tosIt = tos.iterator();

        Node firstFrom = fromsIt.next();
        Node firstTo = tosIt.next();

        NodeType sourceType = metamodel.findNodeType(firstFrom.getType());
        NodeType targetType = metamodel.findNodeType(firstTo.getType());
        ConnectionMapping mapping = connector.getMapping(sourceType, targetType);
        de.tum.pssif.core.model.Edge edge = mapping.create(result, sourceType.apply(result, firstFrom.getId()).getOne(),
            targetType.apply(result, firstTo.getId()).getOne());

        while (fromsIt.hasNext()) {
          mapping.connectFrom(edge, sourceType.apply(result, fromsIt.next().getId()).getOne());
        }
        while (tosIt.hasNext()) {
          mapping.connectTo(edge, targetType.apply(result, tosIt.next().getId()).getOne());
        }

        setAttributes(representative, connector, edge);
      }
    }
  }

  /**
   * @param metamodel
   * @param graph
   * @param result
   * @return returns a multimap containing nodes that represent hyperedges grouped by their typename
   */
  private Multimap<String, Node> createNodes(Metamodel metamodel, Graph graph, Model result) {
    Multimap<String, Node> hyperEdges = HashMultimap.create();
    for (Node n : graph.getNodes()) {
      if (!HYPEREDGE_NODETYPE_NAMES.contains(n.getType())) {
        NodeType type = metamodel.findNodeType(n.getType());
        if (type == null) {
          System.out.println("missed " + n.getType());
          continue;
        }
        Attribute id = type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
        de.tum.pssif.core.model.Node node = type.create(result);
        id.set(node, PSSIFOption.one(PSSIFValue.create(n.getId())));
        for (String attrName : n.getAttributeNames()) {
          Attribute attr = type.findAttribute(attrName);
          attr.set(node, PSSIFOption.one(attr.getType().fromObject(n.getAttributeValue(attrName))));
        }
      }
      else {
        hyperEdges.put(n.getType(), n);
      }
    }
    return hyperEdges;
  }

}
