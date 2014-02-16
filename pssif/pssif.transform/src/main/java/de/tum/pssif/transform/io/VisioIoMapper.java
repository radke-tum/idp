package de.tum.pssif.transform.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import com.google.common.collect.Sets;

import de.tum.pssif.transform.IoMapper;
import de.tum.pssif.transform.graph.AElement;
import de.tum.pssif.transform.graph.Edge;
import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.graph.Node;
import de.tum.pssif.vsdx.VsdxConnector;
import de.tum.pssif.vsdx.VsdxDocument;
import de.tum.pssif.vsdx.VsdxDocumentLoader;
import de.tum.pssif.vsdx.VsdxShape;
import de.tum.pssif.vsdx.exception.VsdxException;
import de.tum.pssif.vsdx.impl.VsdxDocumentLoaderFactory;


public class VisioIoMapper implements IoMapper {

  private static final String VISIO_MASTER_SUFFIX_REGEX = "(\\.(\\d)*+)?";
  private static final String VISIO_MASTER_SPLIT_REGEX  = "\\.";

  //  private static final String     PSSIF_PREFIX  = "pssif";
  //
  //  private static final String     PSSIF_CONNECTOR_MASTER        = PSSIF_PREFIX + ".connector";
  //  private static final String     PSSIF_CONNECTOR_TYPE_PROPERTY = PSSIF_PREFIX + ".connector.type";
  //  private static final String     PSSIF_ID_PROPERTY             = PSSIF_PREFIX + ".id";

  private final String        templateFile;
  private final Set<String>   nodeMasters;
  private final Set<String>   edgeMasters;

  public VisioIoMapper(String templateFile) {
    this.templateFile = templateFile;
    this.nodeMasters = Sets.newHashSet();
    this.edgeMasters = Sets.newHashSet();
  }

  public VisioIoMapper(Set<String> nodeMasters, Set<String> edgeMasters) {
    this.templateFile = "";
    this.nodeMasters = Sets.newHashSet(nodeMasters);
    this.edgeMasters = Sets.newHashSet(edgeMasters);
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
    Graph graph = new Graph();
    readNodes(document, graph);
    readEdges(document, graph);

    return graph;
  }

  @Override
  public void write(Graph graph, OutputStream out) {
    // TODO Auto-generated method stub

  }

  private void readNodes(VsdxDocument document, Graph graph) {
    for (VsdxShape shape : document.getPage().getShapes()) {
      readNode(shape, graph);
    }
  }

  private Node readNode(VsdxShape shape, Graph graph) {
    if (isNodeMasterSupported(shape.getMaster().getName())) {
      Node node = graph.createNode(String.valueOf(shape.getId()));
      node.setType(getValidMasterName(shape.getMaster().getName()));
      readAttributes(node, shape);
      for (VsdxShape inner : shape.getShapes()) {
        Node innerNode = readNode(inner, graph);
        if (innerNode != null) {
          node.addInnerNode(innerNode);
        }
      }
      return node;
    }
    return null;
  }

  private void readEdges(VsdxDocument document, Graph graph) {
    for (VsdxConnector connector : document.getPage().getConnectors()) {
      if (isEdgeMasterSupported(connector.getMaster().getName())) {
        Node source = graph.findNode(String.valueOf(connector.getSourceShape().getId()));
        Node target = graph.findNode(String.valueOf(connector.getTargetShape().getId()));
        if (source != null && target != null) {
          Edge edge = graph.createEdge(String.valueOf(connector.getId()));
          edge.setType(getValidMasterName(connector.getMaster().getName()));
          graph.connect(source, edge, target);
          readAttributes(edge, connector);
        }
      }
    }
  }

  private void readAttributes(AElement intoElement, VsdxShape source) {
    for (String pName : source.getCustomPropertyNames()) {
      if (source.getCustomPropertyValue(pName) != null) {
        intoElement.setAttribute(pName, source.getCustomPropertyValue(pName));
      }
    }
  }

  private boolean isEdgeMasterSupported(String master) {
    return masterIsSupported(master, edgeMasters);
  }

  private boolean isNodeMasterSupported(String master) {
    return masterIsSupported(master, nodeMasters);
  }

  private boolean masterIsSupported(String master, Set<String> masters) {
    if (masters.contains(master)) {
      return true;
    }
    for (String candidate : masters) {
      if (master.matches(candidate + VISIO_MASTER_SUFFIX_REGEX)) {
        return true;
      }
    }
    return false;
  }

  private String getValidMasterName(String masterName) {
    return masterName.split(VISIO_MASTER_SPLIT_REGEX)[0];
  }

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

}
