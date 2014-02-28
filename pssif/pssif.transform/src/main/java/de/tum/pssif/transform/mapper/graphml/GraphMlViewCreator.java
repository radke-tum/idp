package de.tum.pssif.transform.mapper.graphml;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.CreateArtificialNodeTransformation;
import de.tum.pssif.transform.transformation.HideEdgeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.LeftJoinConnectionMappingTransformation;
import de.tum.pssif.transform.transformation.MoveAttributeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameNodeTypeTransformation;
import de.tum.pssif.transform.transformation.RightJoinConnectionMappingTransformation;
import de.tum.pssif.transform.transformation.SpecializeConnectionMappingTransformation;


public class GraphMlViewCreator {
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
    view = new SpecializeConnectionMappingTransformation(informationFlow, state, function, informationFlow.getMapping(block, block)).apply(view);
    view = new SpecializeConnectionMappingTransformation(energyFlow, state, function, energyFlow.getMapping(block, block)).apply(view);
    view = new SpecializeConnectionMappingTransformation(materialFlow, state, function, materialFlow.getMapping(block, block)).apply(view);

    view = new SpecializeConnectionMappingTransformation(informationFlow, function, state, informationFlow.getMapping(block, block)).apply(view);
    view = new SpecializeConnectionMappingTransformation(energyFlow, function, state, energyFlow.getMapping(block, block)).apply(view);
    view = new SpecializeConnectionMappingTransformation(materialFlow, function, state, materialFlow.getMapping(block, block)).apply(view);

    view = moveEdge(view, informationFlow);
    view = moveEdge(view, energyFlow);
    view = moveEdge(view, materialFlow);

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
