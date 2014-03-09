package de.tum.pssif.transform.mapper.graphml;

import java.io.InputStream;
import java.io.OutputStream;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.transform.transformation.CreateArtificialEdgeTransformation;
import de.tum.pssif.transform.transformation.CreateArtificialNodeTransformation;
import de.tum.pssif.transform.transformation.HideConnectionMappingTransformation;
import de.tum.pssif.transform.transformation.HideEdgeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeTransformation;
import de.tum.pssif.transform.transformation.JoinLeftOutgoingTransformation;
import de.tum.pssif.transform.transformation.MoveAttributeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameNodeTypeTransformation;


public class UFMMapper extends GraphMLMapper {
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

    EdgeType relationship = view.getEdgeType("Relationship").getOne();
    ConnectionMapping s2b = relationship.getMapping(view.getNodeType("State").getOne(), view.getNodeType("Block").getOne()).getOne();
    ConnectionMapping f2b = relationship.getMapping(view.getNodeType("Function").getOne(), view.getNodeType("Block").getOne()).getOne();

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

    view = new CreateArtificialNodeTransformation(view.getNodeType("Function").getOne(), view.getEdgeType("Relationship").getOne(), view.getNodeType(
        "Block").getOne()).apply(view);
    view = new CreateArtificialNodeTransformation(view.getNodeType("State").getOne(), view.getEdgeType("Relationship").getOne(), view.getNodeType(
        "Block").getOne()).apply(view);

    view = new JoinLeftOutgoingTransformation(view.getEdgeType("InformationFlow").getOne(), s2b, view.getNodeType("Block").getOne(), view
        .getNodeType("State").getOne(), view.getNodeType("Function").getOne()).apply(view);
    view = new JoinLeftOutgoingTransformation(view.getEdgeType("InformationFlow").getOne(), f2b, view.getNodeType("Block").getOne(), view
        .getNodeType("Function").getOne(), view.getNodeType("State").getOne()).apply(view);

    view = new CreateArtificialEdgeTransformation(view.getNodeType("State").getOne(), view.getNodeType("Function").getOne(), view.getEdgeType(
        "InformationFlow").getOne(), view.getEdgeType("Control Flow").getOne()).apply(view);
    view = new CreateArtificialEdgeTransformation(view.getNodeType("Function").getOne(), view.getNodeType("State").getOne(), view.getEdgeType(
        "InformationFlow").getOne(), view.getEdgeType("Control Flow").getOne()).apply(view);

    relationship = view.getEdgeType("Relationship").getOne();
    view = new HideConnectionMappingTransformation(relationship, relationship.getMapping(view.getNodeType("Function").getOne(),
        view.getNodeType("Block").getOne()).getOne()).apply(view);
    relationship = view.getEdgeType("Relationship").getOne();
    view = new HideConnectionMappingTransformation(relationship, relationship.getMapping(view.getNodeType("State").getOne(),
        view.getNodeType("Block").getOne()).getOne()).apply(view);
    block = view.getNodeType("Block").getOne();

    view = new HideEdgeTypeTransformation(view.getEdgeType("Control Flow").getOne()).apply(view);

    view = new HideNodeTypeTransformation(block).apply(view);

    return view;
  }
}
