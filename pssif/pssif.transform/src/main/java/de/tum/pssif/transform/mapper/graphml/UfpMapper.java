package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
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


public class UfpMapper extends GraphMLMapper {
  @Override
  public Model read(Metamodel metamodel, InputStream inputStream) {
    return readInternal(createUfpView(metamodel), inputStream);
  }

  @Override
  public void write(Metamodel metamodel, Model model, OutputStream outputStream) {
    writeInternal(createUfpView(metamodel), model, outputStream, false);
  }

  public static Metamodel createUfpView(Metamodel metamodel) {
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
