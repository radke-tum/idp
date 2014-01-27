package de.tum.pssif.transform.mapper.graphml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.mapper.Mapper;
import de.tum.pssif.transform.mapper.MapperFactory;
import de.tum.pssif.transform.transformation.HideEdgeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.HideNodeTypeAttributeTransformation;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;


public class GraphMlWriteTest {

  @Test
  public void testWrite() {

    InputStream in = getClass().getResourceAsStream("/flow.graphml");
    GraphMLMapper importer = new GraphMLMapper();

    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Metamodel view = new RenameEdgeTypeTransformation(metamodel.findEdgeType("Information Flow"), "InformationFlow").apply(metamodel);
    view = new RenameEdgeTypeTransformation(view.findEdgeType("Energy Flow"), "EnergyFlow").apply(view);
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
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Activity"), view.findNodeType("Activity").findAttribute("duration"))
        .apply(view);
    view = new HideNodeTypeAttributeTransformation(view.findNodeType("Block"), view.findNodeType("Block").findAttribute("cost")).apply(view);

    Model model = importer.read(view, in);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Mapper mapper = MapperFactory.getMapper(MapperFactory.GRAPHML);
    mapper.write(view, model, out);

    byte[] result = out.toByteArray();
    String str = new String(result);
    System.out.println(str);
    //TODO
  }
}
