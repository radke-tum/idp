package de.tum.pssif.transform.mapper.visio;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.transform.graph.Graph;
import de.tum.pssif.transform.graph.Node;
import de.tum.pssif.transform.io.VisioIoMapper;


public class MyTestClass {

  public static final Set<String> EPK_NODE_MASTERS = Sets.newHashSet("Event", "Function", "Organizational Unit", "Process path", "XOR", "OR", "AND",
                                                       "Information/ Material", "Main process", "Component", "Enterprise area", "Process group");

  @Test
  public void testReadToGraph() {
    VisioIoMapper mapper = new VisioIoMapper(EPK_NODE_MASTERS);
    Graph graph = mapper.read(getClass().getResourceAsStream("/visio/EPK-1.vdx"));
    System.out.println(graph);
    for (Node node : graph.getNodes()) {
      System.out.println("nodeId: " + node.getId() + "|name : " + node.getAttributeValue(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME));
    }
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
