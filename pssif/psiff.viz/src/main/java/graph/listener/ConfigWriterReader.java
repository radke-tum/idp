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
	
	
	private static File CONFIG_FILE;
	private static String ROOT_CONFIG = "config";
	private static String NODE_COLORS = "nodeColors";
	private static String NODE_TYPES = "nodeType";
	private static String ATTR_COLOR = "color";
	
	private static String GRAPH_VIEWS ="graphViews";
	private static String GRAPH_VIEW ="graphView";
	private static String ATTR_VIEWNAME ="viewName";
	private static String VISIBLE_NODETYPES ="visibleNodeTypes";
	private static String VISIBLE_NODETYPE ="visibleNodeType";
	private static String VISIBLE_EDGETYPES ="visibleEdgeTypes";
	private static String VISIBLE_EDGETYPE ="visibleEdgeTypes";
	
	
	public ConfigWriterReader()
	{
		CONFIG_FILE = new File("config.xml");
	}
	
	
	public void setColors(HashMap<MyNodeType,Color> colormapping)
	{
		if (!CONFIG_FILE.exists())
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
		if (!CONFIG_FILE.exists())
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
		Element rootElement = doc.createElement(ROOT_CONFIG);
		doc.appendChild(rootElement);
 
		// nodecolors elements
		Element nodecolors = doc.createElement(NODE_COLORS);
		rootElement.appendChild(nodecolors);
 
		Set<MyNodeType> nodetypes = colormapping.keySet();
		
		for (MyNodeType t :nodetypes)
		{
			Element node = doc.createElement(NODE_TYPES);
			node.appendChild(doc.createTextNode(t.getName()));
			
			Attr attr = doc.createAttribute(ATTR_COLOR);
			attr.setValue(String.valueOf(colormapping.get(t).getRGB()));
			node.setAttributeNode(attr);
			
			nodecolors.appendChild(node);
			
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(CONFIG_FILE);
 
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
			Document doc = docBuilder.parse(CONFIG_FILE);
			
			doc.getDocumentElement().normalize();
			
			// Get the root element
			Node root = doc.getFirstChild();
	 
			// Get the Nodecolors element by tag name directly
			NodeList colormapping = doc.getElementsByTagName(NODE_TYPES);
			
			Node oldNodeColors = doc.getElementsByTagName(NODE_COLORS).item(0);
			
			// Get all the values from the newColorMapping
			
			Set<MyNodeType> newNodeTypes = newColorMapping.keySet();
			
			Set<MyNodeType> checkedNodeTypes = tryUpdate(colormapping, newColorMapping);
			
			// remove all the stuff which was already checked
			newNodeTypes.removeAll(checkedNodeTypes);
			
			for (MyNodeType currentType: newNodeTypes)
			{
				Element node = doc.createElement(NODE_TYPES);
				node.appendChild(doc.createTextNode(currentType.getName()));
				
				Attr attr = doc.createAttribute(ATTR_COLOR);
				attr.setValue(String.valueOf(newColorMapping.get(currentType).getRGB()));
				node.setAttributeNode(attr);
				
				oldNodeColors.appendChild(node);
			}
			
	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(CONFIG_FILE);
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
				
				String colorValue = eElement.getAttribute(ATTR_COLOR);
				
				MyNodeType t = ModelBuilder.getNodeTypes().getValue(nodeTypeValue);
				
				checkedNodeTypes.add(t);
				
				Color newColor = newColorMapping.get(t);
				System.out.println("new Color "+newColor);
				
				Color oldColor = new Color(Integer.valueOf(colorValue));
				System.out.println("From XML Color "+oldColor);
				
				if (newColor!=null)
				{
					System.out.println("changed");
					eElement.setAttribute(ATTR_COLOR, String.valueOf(newColor.getRGB()));
				}
				System.out.println("------------------------");
				
			}
		}
		
		return checkedNodeTypes;
	}
	
	public HashMap<MyNodeType, Color> readColors ()
	{
		HashMap<MyNodeType, Color> res = new HashMap<MyNodeType, Color>();

		if (CONFIG_FILE.exists())
		{
			try
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(CONFIG_FILE);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
			 
				//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				NodeList nList = doc.getElementsByTagName(NODE_TYPES);
			 
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

						Color c = new Color(Integer.valueOf(eElement.getAttribute(ATTR_COLOR)));
						
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
		Element rootElement = doc.createElement(ROOT_CONFIG);
		doc.appendChild(rootElement);
		
		// graphViews element
		Element graphViewsNode = doc.createElement(GRAPH_VIEWS);
		rootElement.appendChild(graphViewsNode);
		
		createGraphView(rootElement,doc,graphViewsNode,view);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(CONFIG_FILE);
 
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
				Element graphViewNode = doc.createElement(GRAPH_VIEW);
				Attr attr = doc.createAttribute(ATTR_VIEWNAME);
				attr.setValue(view.getViewName());
				graphViewNode.setAttributeNode(attr);
				
				System.out.println();
				graphViewsNode.appendChild(graphViewNode);
				
				Element visibleNodeTypesNode = doc.createElement(VISIBLE_NODETYPES);
				graphViewNode.appendChild(visibleNodeTypesNode);
				
				// loop over the Node Types
				for (MyNodeType t :view.getSelectedNodeTypes())
				{
					Element visibleNodeTypeNode = doc.createElement(VISIBLE_NODETYPE);
					visibleNodeTypeNode.appendChild(doc.createTextNode(t.getName()));
					
					visibleNodeTypesNode.appendChild(visibleNodeTypeNode);
					
				}
				
				Element visibleEdgeTypesNode = doc.createElement(VISIBLE_EDGETYPES);
				graphViewNode.appendChild(visibleEdgeTypesNode);
				
				// loop over the Edge Types
				for (MyEdgeType t :view.getSelectedEdgeTypes())
				{
					Element visibleEdgeTypeNode = doc.createElement(VISIBLE_EDGETYPE);
					visibleEdgeTypeNode.appendChild(doc.createTextNode(t.getName()));
					
					visibleEdgeTypesNode.appendChild(visibleEdgeTypeNode);
					
				}
	}
	
	private void updateGraphView (GraphViewContainer view)
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(CONFIG_FILE);
			
			doc.getDocumentElement().normalize();
			
			// Get the root element
			Node root = doc.getFirstChild();
			
			// Get all the GrapView elements by tag name directly
			NodeList graphviewNodes = doc.getElementsByTagName(GRAPH_VIEW);
			
			Element currentGraphViewNode = graphViewAlreadyExists(view.getViewName(), graphviewNodes);
			
			if (currentGraphViewNode==null)
			{
				// graphView does not exist yet. Create a new
				
				//check if there is any View defined yet
				NodeList graphViewsNodes = doc.getElementsByTagName(GRAPH_VIEWS);
				
				Element graphViewsNode;
				if (graphViewsNodes==null || graphViewsNodes.getLength()==0)
				{
					// graphViews element
					graphViewsNode = doc.createElement(GRAPH_VIEWS);
					root.appendChild(graphViewsNode);
				}
				else
				{
					graphViewsNode = (Element) graphviewNodes.item(0);
					// check if graphViews has subNodes
					if (graphViewsNode==null || graphViewsNode.getChildNodes()==null || graphViewsNode.getChildNodes().getLength()==0)
					{
						// if it has none. Delete it an create a new Object
						//root.removeChild(graphViewsNode);
						
						graphViewsNode = doc.createElement(GRAPH_VIEWS);
						root.appendChild(graphViewsNode);
					}
					
				}
				
				System.out.println("graphViewsNode null ? "+graphViewsNode==null);
				createGraphView((Element)root,doc,graphViewsNode,view);
				
			}
			else
			{
				// graphView does exist. Delete the old one and insert a new one				
				root.removeChild(currentGraphViewNode);
				Element graphViewsNode = (Element) doc.getElementsByTagName(GRAPH_VIEWS).item(0);
				
				createGraphView((Element)root,doc,graphViewsNode,view);
			}	
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(CONFIG_FILE);
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
					
					String currentViewName = eElement.getAttribute(ATTR_VIEWNAME);
					
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
		
		if (CONFIG_FILE.exists())
		{
			try
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(CONFIG_FILE);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
			 
				//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
				NodeList nList = doc.getElementsByTagName(GRAPH_VIEW);
			 
				//System.out.println("----------------------------");
			 
				for (int temp = 0; temp < nList.getLength(); temp++) 
				{
			 
					Node nNode = nList.item(temp);
			 
					//System.out.println("\nCurrent Element :" + nNode);
			 
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			 
						Element graphViewElement = (Element) nNode;
						
						String viewName = String.valueOf(graphViewElement.getAttribute(ATTR_VIEWNAME));
						
						Node NodeTypes = graphViewElement.getElementsByTagName(VISIBLE_NODETYPES).item(0);
						Node EdgeTypes = graphViewElement.getElementsByTagName(VISIBLE_EDGETYPES).item(0);
						
						NodeList NodeTypeList = ((Element)NodeTypes).getElementsByTagName(VISIBLE_NODETYPE);
						NodeList EdgeTypeList = ((Element)EdgeTypes).getElementsByTagName(VISIBLE_EDGETYPE);
						
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
			Document doc = dBuilder.parse(CONFIG_FILE);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName(NODE_COLORS);
			
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
				Document doc = docBuilder.parse(CONFIG_FILE);
				
				doc.getDocumentElement().normalize();
				
				// Get the root element
				Element rootElement = (Element) doc.getFirstChild();
		 
				// nodecolors elements
				Element nodecolors = doc.createElement(NODE_COLORS);
				rootElement.appendChild(nodecolors);
				
				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(CONFIG_FILE);
		 
				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);
		 
				transformer.transform(source, result);
		 
				System.out.println("File saved!");
		 }
		    catch (Exception e) {
		    	e.printStackTrace();
		    }
	}
	
	public void deleteView(String deleteView)
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(CONFIG_FILE);
			doc.getDocumentElement().normalize();
			
			// Get the graphview elements
			NodeList viewList = doc.getElementsByTagName(GRAPH_VIEW);
			
			Node deleteNode=null;
			
			//iterate over the views
			for (int i =0; i<viewList.getLength();i++)
			{
				Node currentView = viewList.item(i);
				
				NamedNodeMap attr = currentView.getAttributes();
				Node nodeAttr = attr.getNamedItem(ATTR_VIEWNAME);
				
				System.out.println("delete test: "+nodeAttr.getNodeValue());
				if (nodeAttr.getNodeValue().equals(deleteView))
				{
					System.out.println("delete selected				: "+nodeAttr.getNodeValue());
					deleteNode=currentView;
				}
			}
			
			Node views = doc.getElementsByTagName(GRAPH_VIEWS).item(0);
			Node root = doc.getFirstChild();
			// delete the view
			if (views!=null && deleteNode!=null)
			{
				views.removeChild(deleteNode);
				
				if (views.getChildNodes().getLength()==0)
				{
					root.removeChild(views);
				}
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(CONFIG_FILE);
			transformer.transform(source, result);
			
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
}
