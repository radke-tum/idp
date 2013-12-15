package graph.listener;

import graph.model.NodeType;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigWriterReader {
	
	
	private File configFile;
	private String rootConfig = "config";
	private String nodeColors = "nodecolors";
	private String nodeType = "nodetype";
	private String attrColor = "color";
	
	public ConfigWriterReader()
	{
		configFile = new File("config.xml");
	}
	
	
	public void setColors(HashMap<NodeType,Color> colormapping)
	{
		if (!configFile.exists())
			writeNewConfig(colormapping);
		else
			updateColors(colormapping);
	}
	
	private void writeNewConfig(HashMap<NodeType,Color> colormapping)
	{
		
		 try {
			 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(rootConfig);
		doc.appendChild(rootElement);
 
		// nodecolors elements
		Element nodecolors = doc.createElement(nodeColors);
		rootElement.appendChild(nodecolors);
 
		Set<NodeType> nodetypes = colormapping.keySet();
		
		for (NodeType t :nodetypes)
		{
			Element node = doc.createElement(nodeType);
			node.appendChild(doc.createTextNode(t.getName()));
			
			Attr attr = doc.createAttribute(attrColor);
			attr.setValue(String.valueOf(colormapping.get(t).getRGB()));
			node.setAttributeNode(attr);
			
			nodecolors.appendChild(node);
			
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(configFile);
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		System.out.println("File saved!");
 
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
	
	private void updateColors (HashMap<NodeType,Color> newColorMapping)
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(configFile);
			
			doc.getDocumentElement().normalize();
			
			// Get the root element
			Node root = doc.getFirstChild();
	 
			// Get the Nodecolors element by tag name directly
			NodeList colormapping = doc.getElementsByTagName(nodeType);
			
			Node oldNodeColors = doc.getElementsByTagName(nodeColors).item(0);
			
			// Get all the values from the newColorMapping
			
			Set<NodeType> newNodeTypes = newColorMapping.keySet();
			
			Set<NodeType> checkedNodeTypes = tryUpdate(colormapping, newColorMapping);
			
			// remove all the stuff which was already checked
			newNodeTypes.removeAll(checkedNodeTypes);
			
			for (NodeType currentType: newNodeTypes)
			{
				Element node = doc.createElement(nodeType);
				node.appendChild(doc.createTextNode(currentType.getName()));
				
				Attr attr = doc.createAttribute(attrColor);
				attr.setValue(String.valueOf(newColorMapping.get(currentType).getRGB()));
				node.setAttributeNode(attr);
				
				oldNodeColors.appendChild(node);
			}
			
	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(configFile);
			transformer.transform(source, result);
	 
			System.out.println("Done");
	 
		   } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		   } catch (TransformerException tfe) {
			tfe.printStackTrace();
		   } catch (IOException ioe) {
			ioe.printStackTrace();
		   } catch (SAXException sae) {
			sae.printStackTrace();
		   }
	}

	private Set<NodeType> tryUpdate(NodeList colormapping, HashMap<NodeType,Color> newColorMapping)
	{
		Set<NodeType> checkedNodeTypes = new HashSet<NodeType>();
		
		for (int i = 0; i<colormapping.getLength();i++)
		{
			Node current = colormapping.item(i);
			
			if (current.getNodeType() == Node.ELEMENT_NODE) 
			{
				 
				Element eElement = (Element) current;
				
				String nodeTypeValue = eElement.getChildNodes().item(0).getNodeValue();
				System.out.println("From XML " +nodeTypeValue);
				
				String colorValue = eElement.getAttribute(attrColor);
				
				NodeType t = NodeType.getValue(nodeTypeValue);
				
				checkedNodeTypes.add(t);
				
				Color newColor = newColorMapping.get(t);
				System.out.println("new Color "+newColor);
				
				Color oldColor = new Color(Integer.valueOf(colorValue));
				System.out.println("From XML Color "+oldColor);
				
				if (newColor!=null)
				{
					System.out.println("changed");
					eElement.setAttribute(attrColor, String.valueOf(newColor.getRGB()));
				}
				System.out.println("------------------------");
				
			}
		}
		
		return checkedNodeTypes;
	}
	
	public HashMap<NodeType, Color> readColors ()
	{
		HashMap<NodeType, Color> res = new HashMap<NodeType, Color>();
		
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(configFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName(nodeType);
		 
			//System.out.println("----------------------------");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
		 
				Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					String nodeTypeValue = eElement.getChildNodes().item(0).getNodeValue();	
					System.out.println(nodeTypeValue);
					NodeType current = NodeType.getValue(nodeTypeValue);
					Color c = new Color(Integer.valueOf(eElement.getAttribute(attrColor)));
					
					res.put(current, c);
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		return res;
	  }

}
