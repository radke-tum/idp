package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.google.common.collect.Maps;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFUtil;
import de.tum.pssif.core.util.PSSIFValue;
import de.tum.pssif.transform.mapper.Mapper;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlAttrImpl;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlEdgeImpl;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlNodeImpl;


public class GraphMLMapper implements Mapper {
  @Override
  public Model read(Metamodel metamodel, InputStream in) {
    Model result = new ModelImpl();

    GraphMLGraph graph = GraphMLGraph.read(in);

    for (GraphMLNode inNode : graph.getNodes()) {
      NodeType type = metamodel.findNodeType(inNode.getType());
      if (type != null) {
        readNode(result, inNode, type);
      }
      else {
        System.out.println("NodeType " + inNode.getType() + " not found! Defaulting to Node");
        readNode(result, inNode, metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME));
      }
    }

    for (GraphMLEdge inEdge : graph.getEdges()) {
      EdgeType type = metamodel.findEdgeType(inEdge.getType());
      if (type != null) {
        readEdge(metamodel, result, graph, inEdge, type);
      }
      else {
        System.out.println("EdgeType " + inEdge.getType() + " not found! Defaulting to Edge");
        readEdge(metamodel, result, graph, inEdge, metamodel.findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME));
      }
    }

    return result;
  }

  private void readEdge(Metamodel metamodel, Model result, GraphMLGraph graph, GraphMLEdge inEdge, EdgeType type) {
    GraphMLNode inSourceNode = graph.getNode(inEdge.getSourceId());
    GraphMLNode inTargetNode = graph.getNode(inEdge.getTargetId());

    NodeType sourceType = metamodel.findNodeType(inSourceNode.getType());
    if (sourceType == null) {
      sourceType = metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    }
    PSSIFOption<Node> sourceNode = sourceType.apply(result, inSourceNode.getId());

    NodeType targetType = metamodel.findNodeType(inTargetNode.getType());
    if (targetType == null) {
      targetType = metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    }
    PSSIFOption<Node> targetNode = targetType.apply(result, inTargetNode.getId());

    ConnectionMapping mapping = type.getMapping(sourceType, targetType);
    if (mapping == null) {
      System.out.println(type.getName() + ": mapping " + sourceType.getName() + "-" + targetType.getName() + " not found! Defaulting to Edge");
      type = metamodel.findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
      mapping = type.getMapping(sourceType, targetType);
    }
    if (!sourceNode.isOne()) {
      System.out.println("source node " + inSourceNode.getId() + " not found!");
    }
    if (!targetNode.isOne()) {
      System.out.println("target node " + inTargetNode.getId() + " not found!");
    }
    if (sourceNode.isOne() && targetNode.isOne() && mapping != null) {
      Edge edge = mapping.create(result, sourceNode.getOne(), targetNode.getOne());
      type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).set(edge, PSSIFOption.one(PSSIFValue.create(inEdge.getId())));
      type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED).set(edge, PSSIFOption.one(PSSIFValue.create(inEdge.isDirected())));
      readAttributes(type, edge, inEdge);
    }
  }

  private void readNode(Model result, GraphMLNode inNode, NodeType type) {
    Attribute idAttribute = type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    Node resultNode = type.create(result);
    if (idAttribute != null) {
      idAttribute.set(resultNode, PSSIFOption.one(idAttribute.getType().fromObject(inNode.getId())));
    }
    else {
      System.out.println("Attribute " + PSSIFConstants.BUILTIN_ATTRIBUTE_ID + " not found!");
    }

    readAttributes(type, resultNode, inNode);
  }

  private static void readAttributes(ElementType<?, ?> type, Element element, GraphMLElement inElement) {
    Map<String, String> values = inElement.getValues();
    for (String key : values.keySet()) {
      Attribute attribute = type.findAttribute(key);
      if (attribute != null) {
        attribute.set(element, PSSIFOption.one(attribute.getType().fromObject(values.get(key))));
      }
      else {
        System.out.println("Attribute " + key + " not found!");
      }
    }
  }

  @Override
  public void write(Metamodel metamodel, Model model, OutputStream outputStream) {
    GraphMLGraph graph = GraphMLGraph.create();
    addAttributesToGraph(graph, metamodel);
    for (NodeType nodeType : metamodel.getNodeTypes()) {
      addNodesToGraph(graph, nodeType, model);
    }
    for (EdgeType edgeType : metamodel.getEdgeTypes()) {
      addEdgesToGraph(graph, edgeType, model);
    }
    GraphMLGraph.write(graph, outputStream);
  }

  private void addAttributesToGraph(GraphMLGraph graph, Metamodel metamodel) {
    Map<String, GraphMlAttribute> attributes = Maps.newHashMap();
    for (NodeType nodeType : metamodel.getNodeTypes()) {
      for (Attribute attribute : nodeType.getAttributes()) {
        if (!attributes.containsKey(PSSIFUtil.normalize(attribute.getName()))
            && !PSSIFUtil.areSame(attribute.getName(), PSSIFConstants.BUILTIN_ATTRIBUTE_ID)) {
          attributes.put(PSSIFUtil.normalize(attribute.getName()), new GraphMlAttrImpl(attribute.getName(), attribute.getType().getName()));
        }
      }
    }
    graph.addNodeAttributes(attributes.values());
    attributes = Maps.newHashMap();
    for (EdgeType edgeType : metamodel.getEdgeTypes()) {
      for (Attribute attribute : edgeType.getAttributes()) {
        if (!attributes.containsKey(PSSIFUtil.normalize(attribute.getName()))
            && !PSSIFUtil.areSame(attribute.getName(), PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED)
            && !PSSIFUtil.areSame(attribute.getName(), PSSIFConstants.BUILTIN_ATTRIBUTE_ID)) {
          attributes.put(PSSIFUtil.normalize(attribute.getName()), new GraphMlAttrImpl(attribute.getName(), attribute.getType().getName()));
        }
      }
    }
    graph.addEdgeAttributes(attributes.values());
  }

  private void addNodesToGraph(GraphMLGraph graph, NodeType nodeType, Model model) {
    Attribute idAttribute = nodeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    for (Node pssifNode : nodeType.apply(model).getMany()) {
      GraphMlNodeImpl node = new GraphMlNodeImpl(id(idAttribute, pssifNode));
      node.setValue(GraphMLTokens.ELEMENT_TYPE, nodeType.getName());
      for (Attribute attr : nodeType.getAttributes()) {
        PSSIFOption<PSSIFValue> val = attr.get(pssifNode);
        if (!idAttribute.equals(attr) && val.isOne()) {
          node.setValue(attr.getName(), val.getOne().getValue().toString());
        }
      }
      graph.addNode(node);
    }
  }

  private void addEdgesToGraph(GraphMLGraph graph, EdgeType edgeType, Model model) {
    Attribute idAttribute = edgeType.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    for (ConnectionMapping mapping : edgeType.getMappings()) {
      PSSIFOption<Edge> edges = mapping.apply(model);
      for (Edge pssifEdge : edges.getMany()) {
        Node sourceNode = mapping.getFrom().apply(pssifEdge).getMany().iterator().next();
        Node targetNode = mapping.getTo().apply(pssifEdge).getMany().iterator().next();
        GraphMlEdgeImpl edge = new GraphMlEdgeImpl(id(idAttribute, pssifEdge), id(idAttribute, sourceNode), id(idAttribute, targetNode), false);
        edge.setValue(GraphMLTokens.ELEMENT_TYPE, edgeType.getName());
        graph.addEdge(edge);
      }
    }
  }

  private static String id(Attribute idAttribute, Element element) {
    return idAttribute.get(element).getOne().asString();
  }
}
