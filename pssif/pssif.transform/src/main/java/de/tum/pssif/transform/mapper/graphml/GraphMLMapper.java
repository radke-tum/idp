package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

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
import de.tum.pssif.transform.Mapper;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlAttrImpl;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlEdgeImpl;
import de.tum.pssif.transform.mapper.graphml.GraphMLGraph.GraphMlNodeImpl;
import de.tum.pssif.transform.transformation.CreateArtificialNodeTransformation;
import de.tum.pssif.transform.transformation.HideConnectionMappingTransformation;
import de.tum.pssif.transform.transformation.HideEdgeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.LeftJoinConnectionMappingTransformation;
import de.tum.pssif.transform.transformation.MoveAttributeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameNodeTypeTransformation;
import de.tum.pssif.transform.transformation.RightJoinConnectionMappingTransformation;
import de.tum.pssif.transform.transformation.SpecializeConnectionMappingTransformation;


public abstract class GraphMLMapper implements Mapper {
  protected final Model readInternal(Metamodel metamodel, InputStream in) {
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

    for (GraphMlHyperedge inEdge : graph.getHyperedges()) {
      EdgeType type = metamodel.findEdgeType(inEdge.getType());
      if (type != null) {
        readHyperedge(metamodel, result, graph, inEdge, type);
      }
      else {
        System.out.println("EdgeType " + inEdge.getType() + " not found! Defaulting to Edge");
        readHyperedge(metamodel, result, graph, inEdge, metamodel.findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME));
      }
    }

    return result;
  }

  private void readHyperedge(Metamodel metamodel, Model result, GraphMLGraph graph, GraphMlHyperedge inEdge, EdgeType type) {
    NodeType sourceType = metamodel.findNodeType(graph.getNode(inEdge.getSourceIds().iterator().next()).getType());
    if (sourceType == null) {
      sourceType = metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    }
    NodeType targetType = metamodel.findNodeType(graph.getNode(inEdge.getTargetIds().iterator().next()).getType());
    if (targetType == null) {
      targetType = metamodel.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    }
    Collection<Node> sourceNodes = Sets.newHashSet();
    for (String inSourceNodeId : inEdge.getSourceIds()) {
      sourceNodes.add(sourceType.apply(result, inSourceNodeId).getOne());
    }
    Collection<Node> targetNodes = Sets.newHashSet();
    for (String inTargetNodeId : inEdge.getTargetIds()) {
      targetNodes.add(targetType.apply(result, inTargetNodeId).getOne());
    }

    ConnectionMapping mapping = type.getMapping(sourceType, targetType);
    if (mapping == null) {
      System.out.println(type.getName() + ": mapping " + sourceType.getName() + "-" + targetType.getName() + " not found! Defaulting to Edge");
      type = metamodel.findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
      mapping = type.getMapping(sourceType, targetType);
    }
    else {
      Iterator<Node> sourceIt = sourceNodes.iterator();
      Iterator<Node> targetIt = targetNodes.iterator();
      Edge edge = mapping.create(result, sourceIt.next(), targetIt.next());

      while (sourceIt.hasNext()) {
        mapping.connectFrom(edge, sourceIt.next());
      }
      while (targetIt.hasNext()) {
        mapping.connectTo(edge, targetIt.next());
      }

      type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).set(edge, PSSIFOption.one(PSSIFValue.create(inEdge.getId())));
      type.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED).set(edge, PSSIFOption.one(PSSIFValue.create(inEdge.isDirected())));
      readAttributes(type, edge, inEdge);
    }
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

  protected void writeInternal(Metamodel metamodel, Model model, OutputStream outputStream) {
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
    for (Node pssifNode : nodeType.apply(model, false).getMany()) {
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
      if (mapping.getFrom().getEdgeEndUpper().compareTo(Integer.valueOf(1)) > 0
          || mapping.getTo().getEdgeEndUpper().compareTo(Integer.valueOf(1)) > 0) {
        for (Edge pssifEdge : edges.getMany()) {
          GraphMLGraph.GraphMlHyperedgeImpl edge = new GraphMLGraph.GraphMlHyperedgeImpl(pssifEdge.getId(), true);
          Collection<Node> sourceNodes = mapping.getFrom().apply(pssifEdge).getMany();
          for (Node sourceNode : sourceNodes) {
            edge.connectFrom(sourceNode.getId());
          }
          Collection<Node> targetNodes = mapping.getTo().apply(pssifEdge).getMany();
          for (Node targetNode : targetNodes) {
            edge.connectTo(targetNode.getId());
          }
          edge.setValue(GraphMLTokens.ELEMENT_TYPE, edgeType.getName());
          graph.addHyperedge(edge);
        }
      }
      else {
        for (Edge pssifEdge : edges.getMany()) {
          Node sourceNode = mapping.getFrom().apply(pssifEdge).getOne();
          Node targetNode = mapping.getTo().apply(pssifEdge).getOne();
          GraphMlEdgeImpl edge = new GraphMlEdgeImpl(id(idAttribute, pssifEdge), id(idAttribute, sourceNode), id(idAttribute, targetNode), false);
          edge.setValue(GraphMLTokens.ELEMENT_TYPE, edgeType.getName());
          graph.addEdge(edge);
        }
      }
    }
  }

  private static String id(Attribute idAttribute, Element element) {
    return idAttribute.get(element).getOne().asString();
  }

  public static Metamodel createGraphMlView(Metamodel metamodel) {
    Metamodel view = new RenameEdgeTypeTransformation(metamodel.findEdgeType("Information Flow"), "InformationFlow").apply(metamodel);
    view = new RenameEdgeTypeTransformation(view.findEdgeType("Energy Flow"), "EnergyFlow").apply(view);
    view = new RenameEdgeTypeTransformation(view.findEdgeType("Material Flow"), "MaterialFlow").apply(view);
    view = new RenameNodeTypeTransformation(view.findNodeType("Function"), "AbstractFunction").apply(view);
    view = new RenameNodeTypeTransformation(view.findNodeType("Activity"), "Function").apply(view);
    NodeType rootNode = view.findNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    EdgeType rootEdge = view.findEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT)).apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END)).apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START)).apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START)).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION)).apply(view);

    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Hardware"), view.findNodeType("Hardware").findAttribute("weight")).apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Requirement"), view.findNodeType("Requirement").findAttribute("type"))
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Requirement"), view.findNodeType("Requirement").findAttribute("priority"))
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Function"), view.findNodeType("Function").findAttribute("duration"))
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Block"), view.findNodeType("Block").findAttribute("cost")).apply(view);

    view = new CreateArtificialNodeTransformation(view.findNodeType("Function"), view.findNodeType("Block"), view.findEdgeType("Relationship"))
        .apply(view);
    view = new CreateArtificialNodeTransformation(view.findNodeType("State"), view.findNodeType("Block"), view.findEdgeType("Relationship"))
        .apply(view);

    NodeType block = view.findNodeType("Block");
    Attribute id = block.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    view = new MoveAttributeTransformation(view.findNodeType("Function"), "functionary", block, id, view.findEdgeType("Relationship")).apply(view);
    Attribute name = block.findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
    view = new MoveAttributeTransformation(view.findNodeType("Function"), "functionary", block, name, view.findEdgeType("Relationship")).apply(view);

    view = new MoveAttributeTransformation(view.findNodeType("State"), "functionary", block, id, view.findEdgeType("Relationship")).apply(view);
    view = new MoveAttributeTransformation(view.findNodeType("State"), "functionary", block, name, view.findEdgeType("Relationship")).apply(view);

    EdgeType informationFlow = view.findEdgeType("InformationFlow");
    EdgeType energyFlow = view.findEdgeType("EnergyFlow");
    EdgeType materialFlow = view.findEdgeType("MaterialFlow");
    NodeType state = view.findNodeType("State");
    NodeType function = view.findNodeType("Function");
    block = view.findNodeType("Block");
    view = new SpecializeConnectionMappingTransformation(informationFlow, state, function, informationFlow.getMapping(block, block)).apply(view);
    block = view.findNodeType("Block");
    view = new SpecializeConnectionMappingTransformation(energyFlow, state, function, energyFlow.getMapping(block, block)).apply(view);
    block = view.findNodeType("Block");
    view = new SpecializeConnectionMappingTransformation(materialFlow, state, function, materialFlow.getMapping(block, block)).apply(view);

    view = new SpecializeConnectionMappingTransformation(informationFlow, function, state, informationFlow.getMapping(block, block)).apply(view);
    view = new SpecializeConnectionMappingTransformation(energyFlow, function, state, energyFlow.getMapping(block, block)).apply(view);
    view = new SpecializeConnectionMappingTransformation(materialFlow, function, state, materialFlow.getMapping(block, block)).apply(view);

    view = moveEdge(view, informationFlow);
    view = moveEdge(view, energyFlow);
    view = moveEdge(view, materialFlow);

    informationFlow = view.findEdgeType("InformationFlow");
    block = view.findNodeType("Block");
    view = new HideConnectionMappingTransformation(informationFlow, informationFlow.getMapping(block, block)).apply(view);
    energyFlow = view.findEdgeType("EnergyFlow");
    block = view.findNodeType("Block");
    view = new HideConnectionMappingTransformation(energyFlow, energyFlow.getMapping(block, block)).apply(view);
    materialFlow = view.findEdgeType("MaterialFlow");
    block = view.findNodeType("Block");
    view = new HideConnectionMappingTransformation(materialFlow, materialFlow.getMapping(block, block)).apply(view);

    return view;
  }

  private static Metamodel moveEdge(Metamodel view, EdgeType type) {
    type = view.findEdgeType(type.getName());
    NodeType state = view.findNodeType("State");
    NodeType function = view.findNodeType("Function");
    NodeType block = view.findNodeType("Block");
    EdgeType relationship = view.findEdgeType("Relationship");
    view = new LeftJoinConnectionMappingTransformation(type, type.getMapping(state, function), relationship, relationship.getMapping(state, block),
        type.getMapping(state, block)).apply(view);

    type = view.findEdgeType(type.getName());
    relationship = view.findEdgeType("Relationship");
    view = new RightJoinConnectionMappingTransformation(type, type.getMapping(state, function), relationship,
        relationship.getMapping(function, block), type.getMapping(state, function)).apply(view);

    type = view.findEdgeType(type.getName());
    relationship = view.findEdgeType("Relationship");
    view = new LeftJoinConnectionMappingTransformation(type, type.getMapping(function, state), relationship,
        relationship.getMapping(function, block), type.getMapping(state, block)).apply(view);

    type = view.findEdgeType(type.getName());
    relationship = view.findEdgeType("Relationship");
    view = new RightJoinConnectionMappingTransformation(type, type.getMapping(function, state), relationship, relationship.getMapping(state, block),
        type.getMapping(function, state)).apply(view);
    return view;
  }
}
