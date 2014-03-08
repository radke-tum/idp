package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.google.common.collect.Maps;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFUtil;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;
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
      PSSIFOption<NodeType> type = metamodel.getNodeType(inNode.getType());
      if (type.isOne()) {
        readNode(result, inNode, type.getOne());
      }
      else {
        System.out.println("NodeType " + inNode.getType() + " not found! Defaulting to Node");
        readNode(result, inNode, metamodel.getNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME).getOne());
      }
    }

    for (GraphMLEdge inEdge : graph.getEdges()) {
      PSSIFOption<EdgeType> type = metamodel.getEdgeType(inEdge.getType());
      if (type.isOne()) {
        readEdge(metamodel, result, graph, inEdge, type.getOne());
      }
      else {
        System.out.println("EdgeType " + inEdge.getType() + " not found! Defaulting to Edge");
        readEdge(metamodel, result, graph, inEdge, metamodel.getEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME).getOne());
      }
    }

    return result;
  }

  private void readEdge(Metamodel metamodel, Model result, GraphMLGraph graph, GraphMLEdge inEdge, EdgeType type) {
    GraphMLNode inSourceNode = graph.getNode(inEdge.getSourceId());
    GraphMLNode inTargetNode = graph.getNode(inEdge.getTargetId());

    PSSIFOption<NodeType> sourceType = metamodel.getNodeType(inSourceNode.getType());
    if (sourceType.isNone()) {
      sourceType = metamodel.getNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    }
    PSSIFOption<Node> sourceNode = sourceType.getOne().apply(result, inSourceNode.getId(), true);

    PSSIFOption<NodeType> targetType = metamodel.getNodeType(inTargetNode.getType());
    if (targetType.isNone()) {
      targetType = metamodel.getNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME);
    }
    PSSIFOption<Node> targetNode = targetType.getOne().apply(result, inTargetNode.getId(), true);

    PSSIFOption<ConnectionMapping> mapping = type.getMapping(sourceType.getOne(), targetType.getOne());
    if (mapping.isNone()) {
      System.out.println(type.getName() + ": mapping " + sourceType.getOne().getName() + "-" + targetType.getOne().getName()
          + " not found! Defaulting to Edge");
      type = metamodel.getEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME).getOne();
      mapping = type.getMapping(sourceType.getOne(), targetType.getOne());
    }
    if (!sourceNode.isOne()) {
      System.out.println("source node " + inSourceNode.getId() + " not found!");
    }
    if (!targetNode.isOne()) {
      System.out.println("target node " + inTargetNode.getId() + " not found!");
    }
    if (sourceNode.isOne() && targetNode.isOne() && mapping != null) {
      Edge edge = mapping.getOne().create(result, sourceNode.getOne(), targetNode.getOne());
      type.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne().set(edge, PSSIFOption.one(PSSIFValue.create(inEdge.getId())));
      type.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_DIRECTED).getOne().set(edge, PSSIFOption.one(PSSIFValue.create(inEdge.isDirected())));
      readAttributes(type, edge, inEdge);
    }
  }

  private void readNode(Model result, GraphMLNode inNode, NodeType type) {
    PSSIFOption<Attribute> idAttribute = type.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID);
    Node resultNode = type.create(result);
    if (idAttribute.isOne()) {
      idAttribute.getOne().set(resultNode, PSSIFOption.one(idAttribute.getOne().getType().fromObject(inNode.getId())));
    }
    else {
      System.out.println("Attribute " + PSSIFConstants.BUILTIN_ATTRIBUTE_ID + " not found!");
    }

    readAttributes(type, resultNode, inNode);
  }

  private static void readAttributes(ElementType type, Element element, GraphMLElement inElement) {
    Map<String, String> values = inElement.getValues();
    for (String key : values.keySet()) {
      PSSIFOption<Attribute> attribute = type.getAttribute(key);
      if (attribute.isOne()) {
        attribute.getOne().set(element, PSSIFOption.one(attribute.getOne().getType().fromObject(values.get(key))));
      }
      else {
        System.out.println("Attribute " + key + " not found!");
      }
    }
  }

  protected void writeInternal(Metamodel metamodel, Model model, OutputStream outputStream) {
    GraphMLGraph graph = GraphMLGraph.create();
    addAttributesToGraph(graph, metamodel);
    for (NodeTypeBase nodeType : metamodel.getNodeTypes()) {
      addNodesToGraph(graph, nodeType, model);
    }
    for (EdgeType edgeType : metamodel.getEdgeTypes()) {
      addEdgesToGraph(graph, edgeType, model);
    }
    GraphMLGraph.write(graph, outputStream);
  }

  private void addAttributesToGraph(GraphMLGraph graph, Metamodel metamodel) {
    Map<String, GraphMlAttribute> attributes = Maps.newHashMap();
    for (NodeTypeBase nodeType : metamodel.getNodeTypes()) {
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

  private void addNodesToGraph(GraphMLGraph graph, NodeTypeBase nodeType, Model model) {
    Attribute idAttribute = nodeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne();
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
    Attribute idAttribute = edgeType.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne();
    for (ConnectionMapping mapping : edgeType.getMappings().getMany()) {
      PSSIFOption<Edge> edges = mapping.apply(model);
      for (Edge pssifEdge : edges.getMany()) {
        Node sourceNode = mapping.applyFrom(pssifEdge);
        Node targetNode = mapping.applyTo(pssifEdge);
        GraphMlEdgeImpl edge = new GraphMlEdgeImpl(id(idAttribute, pssifEdge), id(idAttribute, sourceNode), id(idAttribute, targetNode), false);
        edge.setValue(GraphMLTokens.ELEMENT_TYPE, edgeType.getName());
        graph.addEdge(edge);
      }
    }
  }

  private static String id(Attribute idAttribute, Element element) {
    return idAttribute.get(element).getOne().asString();
  }

  public static Metamodel createGraphMlView(Metamodel metamodel) {
    Metamodel view = new RenameEdgeTypeTransformation(metamodel.getEdgeType("Information Flow").getOne(), "InformationFlow").apply(metamodel);
    view = new RenameEdgeTypeTransformation(view.getEdgeType("Energy Flow").getOne(), "EnergyFlow").apply(view);
    view = new RenameEdgeTypeTransformation(view.getEdgeType("Material Flow").getOne(), "MaterialFlow").apply(view);
    view = new RenameNodeTypeTransformation(view.getNodeType("Function").getOne(), "AbstractFunction").apply(view);
    view = new RenameNodeTypeTransformation(view.getNodeType("Activity").getOne(), "Function").apply(view);
    NodeType rootNode = view.getNodeType(PSSIFConstants.ROOT_NODE_TYPE_NAME).getOne();
    EdgeType rootEdge = view.getEdgeType(PSSIFConstants.ROOT_EDGE_TYPE_NAME).getOne();
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END).getOne())
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START).getOne())
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(rootNode, rootNode.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne()).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_COMMENT).getOne()).apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_END).getOne())
        .apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VALIDITY_START).getOne())
        .apply(view);
    view = new HideEdgeTypeAttributeTransformation(rootEdge, rootEdge.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_VERSION).getOne()).apply(view);

    view = new HideNodeTypeAttributeTransformation(view.getNodeType("Hardware").getOne(), view.getNodeType("Hardware").getOne()
        .getAttribute("weight").getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(view.getNodeType("Requirement").getOne(), view.getNodeType("Requirement").getOne()
        .getAttribute("type").getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(view.getNodeType("Requirement").getOne(), view.getNodeType("Requirement").getOne()
        .getAttribute("priority").getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(view.getNodeType("Function").getOne(), view.getNodeType("Function").getOne()
        .getAttribute("duration").getOne()).apply(view);
    view = new HideNodeTypeAttributeTransformation(view.getNodeType("Block").getOne(), view.getNodeType("Block").getOne().getAttribute("cost")
        .getOne()).apply(view);

    view = new CreateArtificialNodeTransformation(view.getNodeType("Function").getOne(), view.getNodeType("Block").getOne(), view.getEdgeType(
        "Relationship").getOne()).apply(view);
    view = new CreateArtificialNodeTransformation(view.getNodeType("State").getOne(), view.getNodeType("Block").getOne(), view.getEdgeType(
        "Relationship").getOne()).apply(view);

    NodeType block = view.getNodeType("Block").getOne();
    Attribute id = block.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_ID).getOne();
    view = new MoveAttributeTransformation(view.getNodeType("Function").getOne(), "functionary", block, id, view.getEdgeType("Relationship").getOne())
        .apply(view);
    Attribute name = block.getAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME).getOne();
    view = new MoveAttributeTransformation(view.getNodeType("Function").getOne(), "functionary", block, name, view.getEdgeType("Relationship")
        .getOne()).apply(view);

    view = new MoveAttributeTransformation(view.getNodeType("State").getOne(), "functionary", block, id, view.getEdgeType("Relationship").getOne())
        .apply(view);
    view = new MoveAttributeTransformation(view.getNodeType("State").getOne(), "functionary", block, name, view.getEdgeType("Relationship").getOne())
        .apply(view);

    EdgeType informationFlow = view.getEdgeType("InformationFlow").getOne();
    EdgeType energyFlow = view.getEdgeType("EnergyFlow").getOne();
    EdgeType materialFlow = view.getEdgeType("MaterialFlow").getOne();
    NodeType state = view.getNodeType("State").getOne();
    NodeType function = view.getNodeType("Function").getOne();
    block = view.getNodeType("Block").getOne();
    view = new SpecializeConnectionMappingTransformation(informationFlow, state, function, informationFlow.getMapping(block, block).getOne())
        .apply(view);
    block = view.getNodeType("Block").getOne();
    view = new SpecializeConnectionMappingTransformation(energyFlow, state, function, energyFlow.getMapping(block, block).getOne()).apply(view);
    block = view.getNodeType("Block").getOne();
    view = new SpecializeConnectionMappingTransformation(materialFlow, state, function, materialFlow.getMapping(block, block).getOne()).apply(view);

    view = new SpecializeConnectionMappingTransformation(informationFlow, function, state, informationFlow.getMapping(block, block).getOne())
        .apply(view);
    view = new SpecializeConnectionMappingTransformation(energyFlow, function, state, energyFlow.getMapping(block, block).getOne()).apply(view);
    view = new SpecializeConnectionMappingTransformation(materialFlow, function, state, materialFlow.getMapping(block, block).getOne()).apply(view);

    view = moveEdge(view, informationFlow);
    view = moveEdge(view, energyFlow);
    view = moveEdge(view, materialFlow);

    informationFlow = view.getEdgeType("InformationFlow").getOne();
    block = view.getNodeType("Block").getOne();
    view = new HideConnectionMappingTransformation(informationFlow, informationFlow.getMapping(block, block).getOne()).apply(view);
    energyFlow = view.getEdgeType("EnergyFlow").getOne();
    block = view.getNodeType("Block").getOne();
    view = new HideConnectionMappingTransformation(energyFlow, energyFlow.getMapping(block, block).getOne()).apply(view);
    materialFlow = view.getEdgeType("MaterialFlow").getOne();
    block = view.getNodeType("Block").getOne();
    view = new HideConnectionMappingTransformation(materialFlow, materialFlow.getMapping(block, block).getOne()).apply(view);

    return view;
  }

  private static Metamodel moveEdge(Metamodel view, EdgeType type) {
    type = view.getEdgeType(type.getName()).getOne();
    NodeType state = view.getNodeType("State").getOne();
    NodeType function = view.getNodeType("Function").getOne();
    NodeType block = view.getNodeType("Block").getOne();
    EdgeType relationship = view.getEdgeType("Relationship").getOne();
    view = new LeftJoinConnectionMappingTransformation(type, type.getMapping(state, function).getOne(), relationship, relationship.getMapping(state,
        block).getOne(), type.getMapping(state, block).getOne()).apply(view);

    type = view.getEdgeType(type.getName()).getOne();
    relationship = view.getEdgeType("Relationship").getOne();
    view = new RightJoinConnectionMappingTransformation(type, type.getMapping(state, function).getOne(), relationship, relationship.getMapping(
        function, block).getOne(), type.getMapping(state, function).getOne()).apply(view);

    type = view.getEdgeType(type.getName()).getOne();
    relationship = view.getEdgeType("Relationship").getOne();
    view = new LeftJoinConnectionMappingTransformation(type, type.getMapping(function, state).getOne(), relationship, relationship.getMapping(
        function, block).getOne(), type.getMapping(state, block).getOne()).apply(view);

    type = view.getEdgeType(type.getName()).getOne();
    relationship = view.getEdgeType("Relationship").getOne();
    view = new RightJoinConnectionMappingTransformation(type, type.getMapping(function, state).getOne(), relationship, relationship.getMapping(state,
        block).getOne(), type.getMapping(function, state).getOne()).apply(view);
    return view;
  }
}
