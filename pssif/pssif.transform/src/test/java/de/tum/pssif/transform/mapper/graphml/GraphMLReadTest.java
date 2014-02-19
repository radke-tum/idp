package de.tum.pssif.transform.mapper.graphml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.util.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.transform.transformation.RenameEdgeTypeTransformation;


public class GraphMLReadTest {
  @Test
  public void testReadGraph() throws IOException {
    InputStream in = getClass().getResourceAsStream("/flow.graphml");

    GraphMLGraph graph = GraphMLGraph.read(in);

    for (GraphMLNode node : graph.getNodes()) {
      System.out.println("node: " + node.getId());

      System.out.println("attributes:");
      Map<String, String> attributes = node.getValues();
      for (String key : attributes.keySet()) {
        System.out.println("\t" + key + ": " + attributes.get(key));
      }
    }

    for (GraphMLEdge edge : graph.getEdges()) {
      System.out.println("edge: " + edge.getId() + "(" + edge.getSourceId() + "," + edge.getTargetId() + ")");

      System.out.println("attributes:");
      Map<String, String> attributes = edge.getValues();
      for (String key : attributes.keySet()) {
        System.out.println("\t" + key + ": " + attributes.get(key));
      }
    }

    //    while (vertices.hasNext()) {
    //      Vertex vertex = vertices.next();
    //      System.out.println("vertex: " + vertex.getId());
    //
    //      Iterator<Edge> outEdges = vertex.getEdges(Direction.OUT).iterator();
    //      Iterator<Edge> inEdges = vertex.getEdges(Direction.IN).iterator();
    //
    //      System.out.println("properties:");
    //      for (String key : vertex.getPropertyKeys()) {
    //        System.out.println("\t" + key + ": " + vertex.getProperty(key));
    //      }
    //
    //      System.out.println("out edges:");
    //      while (outEdges.hasNext()) {
    //        Edge edge = outEdges.next();
    //        System.out.println("\t" + edge.getId());
    //      }
    //
    //      System.out.println("in edges:");
    //      while (inEdges.hasNext()) {
    //        Edge edge = inEdges.next();
    //        System.out.println("\t" + edge.getId());
    //      }
    //
    //      System.out.println();
    //    }
  }

  @Test
  public void testReadFlowIntoPSSIF() {
    InputStream in = getClass().getResourceAsStream("/flow.graphml");
    GraphMLMapper importer = new GraphMLMapper();

    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Metamodel view = new RenameEdgeTypeTransformation(metamodel.findEdgeType("Information Flow"), "InformationFlow").apply(metamodel);
    view = new RenameEdgeTypeTransformation(view.findEdgeType("Energy Flow"), "EnergyFlow").apply(view);
    Model model = importer.read(view, in);

    NodeType viewedState = view.findNodeType("State");
    NodeType viewedFunction = view.findNodeType("Function");

    EdgeType viewedInformationFlow = view.findEdgeType("InformationFlow");
    EdgeType viewedEnergyFlow = view.findEdgeType("EnergyFlow");

    NodeType canonicState = metamodel.findNodeType("State");
    NodeType canonicFunction = metamodel.findNodeType("Function");

    EdgeType canonicInformationFlow = metamodel.findEdgeType("Information Flow");
    EdgeType canonicEnergyFlow = metamodel.findEdgeType("Energy Flow");

    Assert.assertEquals(8, viewedState.apply(model).size());
    Assert.assertEquals(5, viewedFunction.apply(model).size());
    Assert.assertEquals(8, canonicState.apply(model).size());
    Assert.assertEquals(5, canonicFunction.apply(model).size());

    for (Node node : canonicState.apply(model).getMany()) {
      Assert.assertEquals(canonicInformationFlow.getIncoming().apply(node), viewedInformationFlow.getIncoming().apply(node));
      Assert.assertEquals(canonicInformationFlow.getOutgoing().apply(node), viewedInformationFlow.getOutgoing().apply(node));
      Assert.assertEquals(canonicEnergyFlow.getIncoming().apply(node), viewedEnergyFlow.getIncoming().apply(node));
      Assert.assertEquals(canonicEnergyFlow.getOutgoing().apply(node), viewedEnergyFlow.getOutgoing().apply(node));
    }
    for (Node node : canonicFunction.apply(model).getMany()) {
      Assert.assertEquals(canonicInformationFlow.getIncoming().apply(node), viewedInformationFlow.getIncoming().apply(node));
      Assert.assertEquals(canonicInformationFlow.getOutgoing().apply(node), viewedInformationFlow.getOutgoing().apply(node));
      Assert.assertEquals(canonicEnergyFlow.getIncoming().apply(node), viewedEnergyFlow.getIncoming().apply(node));
      Assert.assertEquals(canonicEnergyFlow.getOutgoing().apply(node), viewedEnergyFlow.getOutgoing().apply(node));
    }
  }

  @Test
  public void testReadHierarchicIntoPSSIF() {
    InputStream in = getClass().getResourceAsStream("/hierarchic.graphml");
    GraphMLMapper importer = new GraphMLMapper();

    Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
    Model model = importer.read(metamodel, in);
  }
}
