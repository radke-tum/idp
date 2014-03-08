package de.tum.pssif.transform.io;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import de.tum.pssif.transform.IoMapper;
import de.tum.pssif.transform.graph.Graph;


public class SysMlIoMapper implements IoMapper {

  @Override
  public Graph read(InputStream in) {

    XMLInputFactory factory = XMLInputFactory.newInstance();

    try {
      XMLStreamReader reader = factory.createXMLStreamReader(in, "UTF-8");
      Graph graph = new Graph();

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
      return graph;
    } catch (XMLStreamException e) {
      throw new PSSIFIoException("Failed to read SysML XML", e);
    }
  }

  private void startElement(XMLStreamReader reader) throws XMLStreamException {
    String elementName = reader.getName().getLocalPart();
    String elementNs = reader.getName().getPrefix();
    //TODO
    System.out.println("Ëlement: " + elementNs + ":" + elementName);
  }

  private void endElement(XMLStreamReader reader) throws XMLStreamException {
    //TODO
  }

  @Override
  public void write(Graph graph, OutputStream out) {
    // TODO Auto-generated method stub

  }

}
