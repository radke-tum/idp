package graph.listener;


import graph.model2.MyEdgeType;
import graph.model2.MyNodeType;
import graph.operations.GraphViewContainer;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.ModelBuilder;

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
	private String nodeColors = "nodeColors";
	private String nodeType = "nodeType";
	private String attrColor = "color";
	
	private String graphViews ="graphViews";
	private String graphView ="graphView";
	private String attrViewName ="viewName";
	private String visibleNodeTypes ="visibleNodeTypes";
	private String visibleNodeType ="visibleNodeType";
	private String visibleEdgeTypes ="visibleEdgeTypes";
	private String visibleEdgeType ="visibleEdgeTypes";
	
	
	public ConfigWriterReader()
	{
		configFile = new File("config.xml");
	}
	
	
	public void setColors(HashMap<MyNodeType,Color> colormapping)
	{
		if (!configFile.exists())
			writeNewConfig(colormapping);
		else
		{
			if (!colorConfigExists())
			{
				addBasicColorInfo();
			}
			
			updateColors(colormapping);
		}
	}
	
	public void setGraphView(GraphViewContainer view)
	{
		if (!configFile.exists())
			writeNewConfig(view);
		else
			updateGraphView(view);
	}
	
	private void writeNewConfig(HashMap<MyNodeType,Color> colormapping)
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
 
		Set<MyNodeType> nodetypes = colormapping.keySet();
		
		for (MyNodeType t :nodetypes)
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
	
	private void updateColors (HashMap<MyNodeType,Color> newColorMapping)
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
			
			Set<MyNodeType> newNodeTypes = newColorMapping.keySet();
			
			Set<MyNodeType> checkedNodeTypes = tryUpdate(colormapping, newColorMapping);
			
			// remove all the stuff which was already checked
			newNodeTypes.removeAll(checkedNodeTypes);
			
			for (MyNodeType currentType: newNodeTypes)
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

	private Set<MyNodeType> tryUpdate(NodeList colormapping, HashMap<MyNodeType,Color> newColorMapping)
	{
		Set<MyNodeType> checkedNodeTypes = new HashSet<MyNodeType>();
		
		for (int i = 0; i<colormapping.getLength();i++)
		{
			Node current = colormapping.item(i);
			
			if (current.getNodeType() == Node.ELEMENT_NODE) 
			{
				 
				Element eElement = (Element) current;
				
				String nodeTypeValue = eElement.getChildNodes().item(0).getNodeValue();
				System.out.println("From XML " +nodeTypeValue);
				
				String colorValue = eElement.getAttribute(attrColor);
				
				MyNodeType t = ModelBuilder.getNodeTypes().getValue(nodeTypeValue);
				
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
	
	public HashMap<MyNodeType, Color> readColors ()
	{
		HashMap<MyNodeType, Color> res = new HashMap<MyNodeType, Color>();

		if (configFile.exists())
		{
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
			 
					//System.out.println("\nCurrent Element :" + nNode);
			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			 
						Element eElement = (Element) nNode;
						
						String nodeTypeValue = eElement.getChildNodes().item(0).getNodeValue();	
						//System.out.println(nodeTypeValue);
						MyNodeType current = ModelBuilder.getNodeTypes().getValue(nodeTypeValue);

						Color c = new Color(Integer.valueOf(eElement.getAttribute(attrColor)));
						
						res.put(current, c);
					}
				}
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		}
		System.out.println("Found Nb Colors: "+ res.keySet().size());
		return res;
	  }
	
	
	
	//------------------------------------------------------------------------------------------
	// Views
	
	private void writeNewConfig(GraphViewContainer view)
	{
		
		 try {
			 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(rootConfig);
		doc.appendChild(rootElement);
		
		// graphViews element
		Element graphViewsNode = doc.createElement(graphViews);
		rootElement.appendChild(graphViewsNode);
		
		createGraphView(rootElement,doc,graphViewsNode,view);

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
	
	private void createGraphView(Element rootElement, Document doc, Element graphViewsNode, GraphViewContainer view)
	{
				// graphView element
				Element graphViewNode = doc.createElement(graphView);
				Attr attr = doc.createAttribute(attrViewName);
				attr.setValue(view.getViewName());
				graphViewNode.setAttributeNode(attr);
				
				graphViewsNode.appendChild(graphViewNode);
				
				Element visibleNodeTypesNode = doc.createElement(visibleNodeTypes);
				graphViewNode.appendChild(visibleNodeTypesNode);
				
				// loop over the Node Types
				for (MyNodeType t :view.getSelectedNodeTypes())
				{
					Element visibleNodeTypeNode = doc.createElement(visibleNodeType);
					visibleNodeTypeNode.appendChild(doc.createTextNode(t.getName()));
					
					visibleNodeTypesNode.appendChild(visibleNodeTypeNode);
					
				}
				
				Element visibleEdgeTypesNode = doc.createElement(visibleEdgeTypes);
				graphViewNode.appendChild(visibleEdgeTypesNode);
				
				// loop over the Edge Types
				for (MyEdgeType t :view.getSelectedEdgeTypes())
				{
					Element visibleEdgeTypeNode = doc.createElement(visibleEdgeType);
					visibleEdgeTypeNode.appendChild(doc.createTextNode(t.getName()));
					
					visibleEdgeTypesNode.appendChild(visibleEdgeTypeNode);
					
				}
	}
	
	private void updateGraphView (GraphViewContainer view)
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(configFile);
			
			doc.getDocumentElement().normalize();
			
			// Get the root element
			Node root = doc.getFirstChild();
	 
			// Get all the GrapView elements by tag name directly
			NodeList graphviewNodes = doc.getElementsByTagName(graphView);
			
			Element currentGraphViewNode = graphViewAlreadyExists(view.getViewName(), graphviewNodes);
			
			if (currentGraphViewNode==null)
			{
				// graphView does not exist yet. Create a new
				
				//check if there is any View defined yet
				NodeList graphViewsNodes = doc.getElementsByTagName(graphViews);
				
				Element graphViewsNode;
				if (graphViewsNodes==null || graphViewsNodes.getLength()==0)
				{
					// graphViews element
					graphViewsNode = doc.createElement(graphViews);
					root.appendChild(graphViewsNode);
				}
				else
					graphViewsNode = (Element) graphviewNodes.item(0);
				
				createGraphView((Element)root,doc,graphViewsNode,view);
				
			}
			else
			{
				// graphView does exist. Delete the old one and insert a new one				
				root.removeChild(currentGraphViewNode);
				Element graphViewsNode = (Element) doc.getElementsByTagName(graphViews).item(0);
				
				createGraphView((Element)root,doc,graphViewsNode,view);
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
	
	/**
	 * checks if the given viewName already exists. If not return null
	 * @param viewName
	 * @param graphviewNodes
	 * @return
	 */
	private Element graphViewAlreadyExists(String viewName, NodeList graphviewNodes)
	{
		if (graphviewNodes!=null)
		{
			for (int i=0; i<graphviewNodes.getLength();i++)
			{
				Node currentGraphView = graphviewNodes.item(i);
				
				if (currentGraphView.getNodeType() == Node.ELEMENT_NODE) 
				{
					 
					Element eElement = (Element) currentGraphView;
					
					String currentViewName = eElement.getAttribute(attrViewName);
					
					if (currentViewName.equals(viewName))
						return eElement;
				}
			}
		}
		
		return null;
	}

	public HashMap<String, GraphViewContainer> readViews ()
	{
		HashMap<String, GraphViewContainer> res = new HashMap<String, GraphViewContainer>();
		
		if (configFile.exists())
		{
			try
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(configFile);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
			 
				//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				NodeList nList = doc.getElementsByTagName(graphView);
			 
				//System.out.println("----------------------------");
			 
				for (int temp = 0; temp < nList.getLength(); temp++) 
				{
			 
					Node nNode = nList.item(temp);
			 
					//System.out.println("\nCurrent Element :" + nNode);
			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			 
						Element graphViewElement = (Element) nNode;
						
						String viewName = String.valueOf(graphViewElement.getAttribute(attrViewName));
						
						Node NodeTypes = graphViewElement.getElementsByTagName(visibleNodeTypes).item(0);
						Node EdgeTypes = graphViewElement.getElementsByTagName(visibleEdgeTypes).item(0);
						
						NodeList NodeTypeList = ((Element)NodeTypes).getElementsByTagName(visibleNodeType);
						NodeList EdgeTypeList = ((Element)EdgeTypes).getElementsByTagName(visibleEdgeType);
						
						LinkedList<MyNodeType> nodetypes = new LinkedList<MyNodeType>();
						for (int i = 0; i < NodeTypeList.getLength(); i++)
						{
							Node currentNode = NodeTypeList.item(i);
							 
							//System.out.println("\nCurrent Element :" + nNode);
					 
							if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					 
								Element eElement = (Element) currentNode;
								
								String nodetypename = eElement.getChildNodes().item(0).getNodeValue();
								MyNodeType current = ModelBuilder.getNodeTypes().getValue(nodetypename);
								
								nodetypes.add(current);
							}
						}
						
						LinkedList<MyEdgeType> edgetypes = new LinkedList<MyEdgeType>();
						for (int i = 0; i < EdgeTypeList.getLength(); i++)
						{
							Node currentNode = EdgeTypeList.item(i);
							 
							//System.out.println("\nCurrent Element :" + nNode);
					 
							if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					 
								Element eElement = (Element) currentNode;
								
								String edgetypename = eElement.getChildNodes().item(0).getNodeValue();
								MyEdgeType current = ModelBuilder.getEdgeTypes().getValue(edgetypename);
								
								edgetypes.add(current);
							}
						}
						
						GraphViewContainer view = new GraphViewContainer(nodetypes, edgetypes, viewName);
						
						res.put(viewName, view);

					}
				}
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		}
		return res;
	  }
	
	private boolean colorConfigExists()
	{
		boolean result = false;
		
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(configFile);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName(nodeColors);
			
			if (nList==null || nList.getLength()==0)
				result =false;
			else
				result=true;
				
		}
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
		
		return result;
	}
	
	private void addBasicColorInfo()
	{
		 try {
			 
			 DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(configFile);
				
				doc.getDocumentElement().normalize();
				
				// Get the root element
				Element rootElement = (Element) doc.getFirstChild();
		 
				// nodecolors elements
				Element nodecolors = doc.createElement(nodeColors);
				rootElement.appendChild(nodecolors);
				
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(configFile);
		 
				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);
		 
				transformer.transform(source, result);
		 
				System.out.println("File saved!");
		 }
		    catch (Exception e) {
		    	e.printStackTrace();
		    }
	}
}
