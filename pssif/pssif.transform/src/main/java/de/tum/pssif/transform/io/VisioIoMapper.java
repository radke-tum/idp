package de.tum.pssif.transform.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import de.iteratec.visio.model.Document;
import de.iteratec.visio.model.DocumentLoader;
import de.iteratec.visio.model.Page;
import de.iteratec.visio.model.Shape;
import de.iteratec.visio.model.ShapeContainer;
import de.iteratec.visio.model.exceptions.MasterNotFoundException;
import de.iteratec.visio.model.exceptions.NoSuchElementException;
import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.transform.IoMapper;
import de.tum.pssif.transform.graph.Edge;
import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.graph.Node;


/**
 * TODOs
 *  - fetching of inner shapes, in case we need it...
 * @author kgo
 *
 */
public class VisioIoMapper implements IoMapper {

  private static final String     PSSIF_PREFIX                  = "pssif";

  private static final String     PSSIF_CONNECTOR_MASTER        = PSSIF_PREFIX + ".connector";
  private static final String     PSSIF_CONNECTOR_TYPE_PROPERTY = PSSIF_PREFIX + ".connector.type";
  private static final String     PSSIF_ID_PROPERTY             = PSSIF_PREFIX + ".id";

  private final String            templateFile;

  private final Map<Shape, Node>  shapeToNode                   = Maps.newHashMap();
  private final Map<Shape, Shape> shapeToParent                 = Maps.newHashMap();

  public VisioIoMapper(String templateFile) {
    this.templateFile = templateFile;
  }

  public VisioIoMapper() {
    this.templateFile = "";
  }

  @Override
  public Graph read(InputStream in) {
    Document document = loadDocument(in);
    Graph graph = new Graph();
    try {
      readNodes(graph, document.getPage(0));
      readEdges(graph, document);

    } catch (NoSuchElementException e) {
      throw new PSSIFIoException("No page found in Visio document.");
    }
    return graph;
  }

  @Override
  public void write(Graph graph, OutputStream out) {
    Document document = loadFromTemplate();
    Page page = null;
    try {
      page = document.getPage(0);
    } catch (NoSuchElementException e) {
      throw new PSSIFIoException("No page found in template.", e);
    }

    Map<Node, Shape> nodesMap = Maps.newHashMap();
    for (Node node : graph.getNodes()) {
      createShape(node, page, nodesMap);
    }
    for (Edge edge : graph.getEdges()) {
      createConnector(edge, page, nodesMap.get(edge.getSource()), nodesMap.get(edge.getTarget()));
    }

    try {
      document.write(out);
    } catch (IOException e) {
      throw new PSSIFIoException("Failed to write generated visio document to output stream.", e);
    }
  }

  private void readNodes(Graph graph, ShapeContainer shapeContainer) {

    for (Shape shape : shapeContainer.getShapes()) {
      String masterName = null;
      try {
        masterName = shape.getMaster().getName();
      } catch (MasterNotFoundException e) {
        //ignore, considered to be a Node
        masterName = PSSIFConstants.ROOT_NODE_TYPE_NAME;
      }

      //TODO any way to recognize connectors?...
      //connects: each connect has shapeIds as from part and to-part
      //-> some of the nodes become edges...
      // type is provided by the master

      String pssifId = null;
      Object oId = shape.getCustomProperties().get(PSSIF_ID_PROPERTY);
      if (oId == null) {
        pssifId = shape.getID().toString();
      }
      Node node = graph.createNode(pssifId);
      node.setType(masterName);
      for (Entry<String, Object> entry : shape.getCustomProperties().entrySet()) {
        if (entry.getValue() != null) {
          node.setAttribute(entry.getKey(), entry.getValue().toString());
        }
      }

      shapeToNode.put(shape, node);

      for (Shape innerShape : shape.getInnerShapes()) {
        shapeToParent.put(innerShape, shape);
        readNodes(graph, innerShape);
      }
    }

  }

  private void readEdges(Graph graph, Document document) {
    //TODO
  }

  private Shape createShape(Node node, ShapeContainer shapeContainer, Map<Node, Shape> nodesMap) {
    Shape shape = null;
    try {
      shape = shapeContainer.createNewInnerShape(node.getType());
      nodesMap.put(node, shape);
    } catch (MasterNotFoundException e) {
      throw new PSSIFIoException("Master shape not found for node of type: " + node.getType(), e);
    }

    for (Node inner : node.getInnerNodes()) {
      createShape(inner, shape, nodesMap);
    }

    for (String attributeName : node.getAttributeNames()) {
      if (node.getAttributeValue(attributeName) != null) {
        shape.setCustomProperty(attributeName, node.getAttributeValue(attributeName));
      }
    }

    return shape;
  }

  private Shape createConnector(Edge edge, Page page, Shape source, Shape target) {
    Shape connector = null;
    try {
      connector = page.createNewConnector(edge.getType(), source, target);
    } catch (MasterNotFoundException e) {
      throw new PSSIFIoException("Master shape not found for node of type: " + edge.getType(), e);
    }

    for (String attributeName : edge.getAttributeNames()) {
      if (edge.getAttributeValue(attributeName) != null) {
        connector.setCustomProperty(attributeName, edge.getAttributeValue(attributeName));
      }
    }

    return connector;
  }

  private Document loadFromTemplate() {
    InputStream stream = getClass().getResourceAsStream(templateFile);
    return loadDocument(stream);
  }

  private Document loadDocument(InputStream in) {
    try {
      return DocumentLoader.getVdxLoader().loadDocument(in);
    } catch (IOException | ParserConfigurationException | SAXException e) {
      throw new PSSIFIoException("Failed to load template file.", e);
    }
  }

}
