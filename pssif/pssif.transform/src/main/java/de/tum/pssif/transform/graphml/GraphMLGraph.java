package de.tum.pssif.transform.graphml;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class GraphMLGraph {
  /*package*/enum EdgeDefault {
    DIRECTED, UNDIRECTED
  }

  private EdgeDefault       edgeDefault = EdgeDefault.DIRECTED;

  private Map<String, Node> nodes       = Maps.newHashMap();
  private Map<String, Edge> edges       = Maps.newHashMap();   //TODO a set might be sufficient here

  private Element           current;

  public static GraphMLGraph read(InputStream in) {
    GraphMLGraph result = new GraphMLGraph();

    result.readInternal(in);

    return result;
  }

  private GraphMLGraph() {
  }

  public Set<GraphMLNode> getNodes() {
    Set<GraphMLNode> result = Sets.newHashSet();
    result.addAll(nodes.values());
    return result;
  }

  public GraphMLNode getNode(String id) {
    return nodes.get(id);
  }

  public Set<GraphMLEdge> getEdges() {
    Set<GraphMLEdge> result = Sets.newHashSet();
    result.addAll(edges.values());
    return result;
  }

  public EdgeDefault getEdgeDefault() {
    return edgeDefault;
  }

  private void readInternal(InputStream in) {
    XMLInputFactory factory = XMLInputFactory.newInstance();

    try {
      XMLStreamReader reader = factory.createXMLStreamReader(in);

      while (reader.hasNext()) {
        int event = reader.next();

        switch (event) {
          case XMLEvent.START_ELEMENT:
            startElement(reader);
            break;
          case XMLEvent.END_ELEMENT:
            endElement(reader);
            break;
        }
      }

      reader.close();
    } catch (XMLStreamException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void startElement(XMLStreamReader reader) throws XMLStreamException {
    String elementName = reader.getName().getLocalPart();
    if (GraphMLTokens.GRAPH.equals(elementName)) {
      if (GraphMLTokens.UNDIRECTED.equals(reader.getAttributeValue(null, GraphMLTokens.EDGEDEFAULT))) {
        edgeDefault = EdgeDefault.UNDIRECTED;
      }
    }
    else if (GraphMLTokens.KEY.equals(elementName)) {
      //TODO what to do here? nothing?
    }
    else if (GraphMLTokens.NODE.equals(elementName)) {
      current = new Node(reader.getAttributeValue(null, GraphMLTokens.ID));
    }
    else if (GraphMLTokens.EDGE.equals(elementName)) {
      current = new Edge(reader.getAttributeValue(null, GraphMLTokens.ID), reader.getAttributeValue(null, GraphMLTokens.SOURCE),
          reader.getAttributeValue(null, GraphMLTokens.TARGET));
    }
    else if (GraphMLTokens.DATA.equals(elementName)) {
      String key = reader.getAttributeValue(null, GraphMLTokens.KEY);
      if (GraphMLTokens.ID.equals(key)) {
        key = "__" + key + "__";
      }
      current.setValue(key, reader.getElementText());
    }
  }

  private void endElement(XMLStreamReader reader) {
    String elementName = reader.getName().getLocalPart();
    if (GraphMLTokens.NODE.equals(elementName)) {
      if (!(current instanceof GraphMLNode)) {
        throw new IllegalStateException();
      }
      nodes.put(current.getId(), (Node) current);
      current = null;
    }
    else if (GraphMLTokens.EDGE.equals(elementName)) {
      if (!(current instanceof GraphMLEdge)) {
        throw new IllegalStateException();
      }
      edges.put(current.getId(), (Edge) current);
      current = null;
    }
  }

  private class Node extends Element implements GraphMLNode {
    private Node(String id) {
      super(id);
    }
  }

  private class Edge extends Element implements GraphMLEdge {
    private final String source;
    private final String target;

    private Edge(String id, String source, String target) {
      super(id);
      this.source = source;
      this.target = target;
    }

    @Override
    public String getSourceId() {
      return source;
    }

    @Override
    public String getTargetId() {
      return target;
    }
  }

  private class Element implements GraphMLElement {
    private final String        id;
    private String              type;
    private Map<String, String> values = Maps.newHashMap();

    private Element(String id) {
      this.id = id;
    }

    private void setValue(String key, String value) {
      if (GraphMLTokens.ELEMENT_TYPE.equals(key)) {
        type = value;
      }
      else {
        values.put(key, value);
      }
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public Map<String, String> getValues() {
      return ImmutableMap.copyOf(values);
    }

    @Override
    public String getType() {
      return type;
    }
  }
}
