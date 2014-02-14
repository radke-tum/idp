package de.tum.pssif.transform.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.iteratec.visio.model.Shape;
import de.tum.pssif.transform.IoMapper;
import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.graph.Node;
import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxDocumentLoader;
import de.tum.pssif.vsdx.exception.VsdxException;
import de.tum.pssif.vsdx.impl.VsdxDocumentLoaderFactory;


public class VisioIoMapper implements IoMapper {

  //  private static final String     PSSIF_PREFIX  = "pssif";
  //
  //  private static final String     PSSIF_CONNECTOR_MASTER        = PSSIF_PREFIX + ".connector";
  //  private static final String     PSSIF_CONNECTOR_TYPE_PROPERTY = PSSIF_PREFIX + ".connector.type";
  //  private static final String     PSSIF_ID_PROPERTY             = PSSIF_PREFIX + ".id";

  private final String            templateFile;
  private final Set<String>       nodeMasters;

  private final Map<Shape, Node>  shapeToNode   = Maps.newHashMap();
  private final Map<Shape, Shape> shapeToParent = Maps.newHashMap();

  public VisioIoMapper(String templateFile) {
    this.templateFile = templateFile;
    this.nodeMasters = Sets.newHashSet();
  }

  public VisioIoMapper(Set<String> nodeMasters) {
    this.templateFile = "";
    this.nodeMasters = Sets.newHashSet(nodeMasters);
  }

  @Override
  public Graph read(InputStream in) {
    VsdxDocument document = null;
    try {
      VsdxDocumentLoader loader = VsdxDocumentLoaderFactory.INSTANCE.create();
      document = loader.loadDocument(in);
    } catch (VsdxException e) {
      throw new PSSIFIoException("Failed to load VSDX document.", e);
    }

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(Graph graph, OutputStream out) {
    // TODO Auto-generated method stub

  }

  //  @Override
  //  public Graph read(InputStream in) {
  //    Document document = loadDocument(in);
  //    Graph graph = new Graph();
  //    try {
  //      readNodes(graph, document.getPage(0));
  //      readEdges(graph, document);
  //
  //    } catch (NoSuchElementException e) {
  //      throw new PSSIFIoException("No page found in Visio document.");
  //    }
  //    return graph;
  //  }
  //
  //  @Override
  //  public void write(Graph graph, OutputStream out) {
  //    Document document = loadFromTemplate();
  //    Page page = null;
  //    try {
  //      page = document.getPage(0);
  //    } catch (NoSuchElementException e) {
  //      throw new PSSIFIoException("No page found in template.", e);
  //    }
  //
  //    Map<Node, Shape> nodesMap = Maps.newHashMap();
  //    for (Node node : graph.getNodes()) {
  //      createShape(node, page, nodesMap);
  //    }
  //    for (Edge edge : graph.getEdges()) {
  //      createConnector(edge, page, nodesMap.get(edge.getSource()), nodesMap.get(edge.getTarget()));
  //    }
  //
  //    try {
  //      document.write(out);
  //    } catch (IOException e) {
  //      throw new PSSIFIoException("Failed to write generated visio document to output stream.", e);
  //    }
  //  }
  //
  //  private void readNodes(Graph graph, ShapeContainer shapeContainer) {
  //
  //    for (Shape shape : shapeContainer.getShapes()) {
  //      String masterName = null;
  //      try {
  //        masterName = shape.getMaster().getName();
  //      } catch (MasterNotFoundException e) {
  //        //ignore, considered to be a Node
  //        masterName = PSSIFConstants.ROOT_NODE_TYPE_NAME;
  //      }
  //
  //      if (!masterName.equals(PSSIFConstants.ROOT_NODE_TYPE_NAME) && !nodeMasters.contains(masterName)) {
  //        continue;
  //      }
  //
  //      //TODO any way to recognize connectors?...
  //      //connects: each connect has shapeIds as from part and to-part
  //      //-> some of the nodes become edges...
  //      // type is provided by the master
  //
  //      String pssifId = null;
  //      Object oId = shape.getCustomProperties().get(PSSIF_ID_PROPERTY);
  //      if (oId == null) {
  //        pssifId = shape.getID().toString();
  //      }
  //      Node node = graph.createNode(pssifId);
  //      node.setType(masterName);
  //      node.setAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME, shape.getShapeText().getText());
  //      for (Entry<String, Object> entry : shape.getCustomProperties().entrySet()) {
  //        if (entry.getValue() != null) {
  //          node.setAttribute(entry.getKey(), entry.getValue().toString());
  //        }
  //      }
  //
  //      shapeToNode.put(shape, node);
  //
  //      for (Shape innerShape : shape.getInnerShapes()) {
  //        shapeToParent.put(innerShape, shape);
  //        readNodes(graph, innerShape);
  //      }
  //    }
  //
  //  }
  //
  //  private void readEdges(Graph graph, Document document) {
  //    //TODO
  //  }
  //
  //  private Shape createShape(Node node, ShapeContainer shapeContainer, Map<Node, Shape> nodesMap) {
  //    Shape shape = null;
  //    try {
  //      shape = shapeContainer.createNewInnerShape(node.getType());
  //      nodesMap.put(node, shape);
  //    } catch (MasterNotFoundException e) {
  //      throw new PSSIFIoException("Master shape not found for node of type: " + node.getType(), e);
  //    }
  //
  //    for (Node inner : node.getInnerNodes()) {
  //      createShape(inner, shape, nodesMap);
  //    }
  //
  //    for (String attributeName : node.getAttributeNames()) {
  //      if (node.getAttributeValue(attributeName) != null) {
  //        shape.setCustomProperty(attributeName, node.getAttributeValue(attributeName));
  //      }
  //    }
  //
  //    return shape;
  //  }
  //
  //  private Shape createConnector(Edge edge, Page page, Shape source, Shape target) {
  //    Shape connector = null;
  //    try {
  //      connector = page.createNewConnector(edge.getType(), source, target);
  //    } catch (MasterNotFoundException e) {
  //      throw new PSSIFIoException("Master shape not found for node of type: " + edge.getType(), e);
  //    }
  //
  //    for (String attributeName : edge.getAttributeNames()) {
  //      if (edge.getAttributeValue(attributeName) != null) {
  //        connector.setCustomProperty(attributeName, edge.getAttributeValue(attributeName));
  //      }
  //    }
  //
  //    return connector;
  //  }
  //
  //  private Document loadFromTemplate() {
  //    InputStream stream = getClass().getResourceAsStream(templateFile);
  //    return loadDocument(stream);
  //  }
  //
  //  private Document loadDocument(InputStream in) {
  //    try {
  //      return DocumentLoader.getVdxLoader().loadDocument(in);
  //    } catch (IOException | ParserConfigurationException | SAXException e) {
  //      throw new PSSIFIoException("Failed to load template file.", e);
  //    }
  //  }

}
