package de.tum.pssif.transform.mapper.visio;

import org.junit.Test;

import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.io.VisioIoMapper;


public class MyTestClass {

  @Test
  public void testReadToGraph() {
    VisioIoMapper mapper = new VisioIoMapper();
    Graph graph = mapper.read(getClass().getResourceAsStream("/visio/EPK-1.vdx"));
    System.out.println(graph);
  }

  //  @Test
  //  public void testTheBugger() throws SAXException, MasterNotFoundException {
  //    try {
  //      InputStream stream = getClass().getResourceAsStream("/visio/EPK-1.vdx");
  //      //      InputStream stream = getClass().getResourceAsStream("/visio/iteraplanLandscapeDiagram.vdx");
  //      //      ByteArrayOutputStream o = new ByteArrayOutputStream();
  //      //      int current = stream.read();
  //      //      while (current != -1) {
  //      //        o.write(current);
  //      //        current = stream.read();
  //      //      }
  //      //      byte[] bytes = o.toByteArray();
  //      //      javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
  //      //      factory.setNamespaceAware(false);
  //      //      javax.xml.parsers.DocumentBuilder builder = null;
  //      //      try {
  //      //        builder = factory.newDocumentBuilder();
  //      //      } catch (javax.xml.parsers.ParserConfigurationException ex) {
  //      //      }
  //      //      org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(bytes));
  //      //Document doc = Document.get
  //      //System.out.println(stream.read(bytes));
  //      //System.out.println(doc);
  //      Document doc = DocumentLoader.getVdxLoader().loadDocument(stream);
  //      //      Master process = doc.getMaster("Process");
  //      Master event = doc.getMaster("Event");
  //      System.out.println(event);
  //      System.out.println("------------------------------");
  //      for (Master master : doc.getMasters()) {
  //        System.out.println(master);
  //      }
  //    } catch (IOException e) {
  //      // TODO Auto-generated catch block
  //      e.printStackTrace();
  //    } catch (ParserConfigurationException e) {
  //      // TODO Auto-generated catch block
  //      e.printStackTrace();
  //    } catch (SAXException e) {
  //      // TODO Auto-generated catch block
  //      e.printStackTrace();
  //    }
  //
  //  }
}
