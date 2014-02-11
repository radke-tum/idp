package de.tum.pssif.transform.mapper.graphml;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.transform.transformation.HideEdgeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.NodifyTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;
import de.tum.pssif.transform.transformation.RenameNodeTypeTransformation;


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

    view = new NodifyTransformation(view.findNodeType("Function"), view.findNodeType("Block"), view.findEdgeType("Relationship"), "functionary")
        .apply(view);

    return view;
  }
}
