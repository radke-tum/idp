package jena.mapper.impl;

import graph.model.MyEdge;
import graph.model.MyJunctionNode;
import model.MyModelContainer;
import graph.model.MyNode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import jena.database.Properties;
import jena.database.URIs;
import jena.database.impl.DatabaseImpl;
import jena.database.impl.RDFModelImpl;
import jena.mapper.DBMapper;

import org.pssif.mainProcesses.Methods;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Node;

// Model to DB Mapper
public class DBMapperImpl implements DBMapper {

	public RDFModelImpl rdfModel;
	public static DatabaseImpl db;

	// Variables to record changes that have to be saved in the Database
	public static LinkedList<MyNode> newNodes = new LinkedList<>();
	public static LinkedList<MyEdge> newEdges = new LinkedList<>();
	public static LinkedList<MyJunctionNode> newJunctionNodes = new LinkedList<>();
	public static LinkedList<MyNode> changedNodes = new LinkedList<>();
	public static LinkedList<MyEdge> changedEdges = new LinkedList<>();
	public static LinkedList<MyJunctionNode> changedJunctionNodes = new LinkedList<>();
	public static LinkedList<MyNode> deletedNodes = new LinkedList<>();
	public static LinkedList<MyEdge> deletedEdges = new LinkedList<>();
	public static LinkedList<MyJunctionNode> deletedJunctionNodes = new LinkedList<>();
	// Flag if Database should be deleted completely
	public static boolean deleteAll = false;

	public DBMapperImpl() {
		super();
		db = new DatabaseImpl();
		rdfModel = db.getRdfModel();
	}

	// MODEL

	@Override
	public void modelToDB(MyModelContainer model, String modelname) {
		// db.begin(ReadWrite.WRITE);
		// rdfModel.begin();
		rdfModel.removeAll();
		saveNodes(model.getAllNodes());
		saveJunctionNodes(model.getAllJunctionNodes());
		saveEdges(model.getAllEdges());

		db.saveModel(rdfModel.getModel());
		clearAll();

		// db.commit();
		// db.end();
		// db.close();
	}

	@Override
	public void saveToDB(String modelname) {
		// db.begin(ReadWrite.WRITE);
		// rdfModel.begin();

		// save first then delete because of Version Management -> so it can
		// happen that Edges are in new and deleted List, so delete should be
		// your last operation.
		saveNodes(newNodes);
		saveEdges(newEdges);
		saveJunctionNodes(newJunctionNodes);

		for (MyNode node : deletedNodes)
			removeNode(node);
		for (MyJunctionNode junctionnode : deletedJunctionNodes)
			removeJunctionNode(junctionnode);
		for (MyEdge edge : deletedEdges)
			removeEdge(edge);

		changeElements();

		db.saveModel(rdfModel.getModel());
		// rdfModel.writeModelToFile("Test", "C:\\Users\\Andrea\\Desktop\\");

		// db.commit();
		// db.end();
		// db.close();

		// changes are in DB -> clear all Lists
		clearAll();
	}

	@Override
	public void removeModel() {
		// db.begin(ReadWrite.WRITE);
		// rdfModel.begin();
		rdfModel.removeAll();
		// rdfModel.commit();
		db.removeNamedModel(rdfModel.toString());
		// db.commit();
		// db.end();
	}

	// NODES

	/**
	 * Saves all given Nodes of the Class MyNode in Database
	 * 
	 * @param nodes
	 *            List of Class MyNode to be saved
	 */
	private void saveNodes(LinkedList<MyNode> nodes) {
		if (nodes != null) {
			for (Iterator<MyNode> it = nodes.iterator(); it.hasNext();) {
				MyNode myNode = it.next();
				addNode(myNode);
			}
		}
	}

	/**
	 * Saves the given Node of Class MyNode in Database
	 * 
	 * @param mynode
	 *            Node of Class MyNode to be saved
	 */
	private void addNode(MyNode mynode) {
		Node n = mynode.getNode();
		String globalID = Methods.findGlobalID(n, mynode.getNodeType()
				.getType());

		// Falls Node noch nicht vorhanden
		String uri = URIs.uriNode.concat(globalID);
		if (!rdfModel.bagContainsResource(uri, URIs.uriBagNodes)) {
			// create Subject with URI from NodeID
			Resource subjectNode = rdfModel.createResource(uri);

			// Add subject to Bag
			rdfModel.addToBag(URIs.uriBagNodes, subjectNode);

			// Add NodeType
			Resource type = rdfModel.createResource(URIs.uriNodeType
					.concat(mynode.getNodeType().getName()));
			rdfModel.insert(subjectNode, Properties.PROP_TYPE, type);

			// Add Attributes from Node
			addAllAttributes(mynode.getAttributesHashMap(), subjectNode,
					URIs.uriNodeAttribute, n);

			// Add all Annotations to Model
			addAllAnnotations(n.getAnnotations(), subjectNode);
		}
	}

	/**
	 * Removes a given Node of Class MyNode from Database
	 * 
	 * @param mynode
	 *            Node of Class MyNode to be removed
	 */
	private void removeNode(MyNode mynode) {
		Node node = mynode.getNode();
		String globalID = Methods.findGlobalID(node, mynode.getNodeType()
				.getType());

		if (rdfModel.bagContainsResource(URIs.uriNode.concat(globalID),
				URIs.uriBagNodes)) {
			// get Subject with URI from NodeID
			Resource subjectNode = rdfModel.getResource(URIs.uriNode
					.concat(globalID));

			// Remove NodeType
			Resource type = subjectNode
					.getPropertyResourceValue(Properties.PROP_TYPE);
			rdfModel.removeTriple(subjectNode, Properties.PROP_TYPE, type);

			// Remove Attributes from Node
			removeAllAttributes(mynode.getAttributesHashMap(), subjectNode,
					URIs.uriNodeAttribute);

			// Remove all Annotations to Model
			removeAllAnnotations(node.getAnnotations(), subjectNode);

			// Remove Node from Bag
			rdfModel.removeFromBag(URIs.uriBagNodes,
					URIs.uriNode.concat(globalID));
		}
	}

	// JUNCTION NODES

	/**
	 * Saves all given JunctionNodes of the Class MyJunctionNode in Database
	 * 
	 * @param nodes
	 *            List of Class MyJunctionNode to be saved
	 */
	private void saveJunctionNodes(LinkedList<MyJunctionNode> nodes) {
		if (nodes != null) {
			for (Iterator<MyJunctionNode> it = nodes.iterator(); it.hasNext();) {
				MyJunctionNode myJNode = it.next();
				addJunctionNode(myJNode);
			}
		}
	}

	/**
	 * Saves the given JunctionNode of Class MyJunctionNode in Database
	 * 
	 * @param myJNode
	 *            Node of Class MyJunctionNode to be saved
	 */
	private void addJunctionNode(MyJunctionNode myJNode) {
		Node jn = myJNode.getNode();

		// Falls JunctionNode noch nicht vorhanden
		if (!rdfModel.containsJunctionNode(URIs.uriJunctionNode.concat(jn
				.getId()))) {

			// create Subject with URI from NodeID
			Resource subjectJNode = rdfModel
					.createResource(URIs.uriJunctionNode.concat(jn.getId()));

			// Add subject to Bag
			rdfModel.addToBag(URIs.uriBagJunctionNodes, subjectJNode);

			// Add JNodeType
			Resource type = rdfModel.createResource(URIs.uriJunctionNodeType
					.concat(myJNode.getNodeType().getName()));
			rdfModel.insert(subjectJNode, Properties.PROP_TYPE, type);

			// Add Attributes from JNode
			addAllAttributes(myJNode.getAttributesHashMap(), subjectJNode,
					URIs.uriJunctionNode, jn);

			// Add all Annotations to Model
			addAllAnnotations(jn.getAnnotations(), subjectJNode);
		}
	}

	/**
	 * Removes a given JunctionNode of Class MyJunctionNode from Database
	 * 
	 * @param mynode
	 *            Node of Class MyJunctionNode to be removed
	 */
	private void removeJunctionNode(MyJunctionNode mynode) {
		Node jn = mynode.getNode();

		if (rdfModel.bagContainsResource(
				URIs.uriJunctionNode.concat(jn.getId()),
				URIs.uriBagJunctionNodes)) {

			// get Subject with URI from NodeID
			Resource subjectJNode = rdfModel.getResource(URIs.uriJunctionNode
					.concat(jn.getId()));

			// Remove NodeType
			Resource type = subjectJNode
					.getPropertyResourceValue(Properties.PROP_TYPE);
			rdfModel.removeTriple(subjectJNode, Properties.PROP_TYPE, type);

			// Remove Attributes from Node
			removeAllAttributes(mynode.getAttributesHashMap(), subjectJNode,
					URIs.uriJunctionNode);

			// Remove all Annotations to Model
			removeAllAnnotations(jn.getAnnotations(), subjectJNode);

			// Remove JunctionNode from Bag
			rdfModel.removeFromBag(URIs.uriBagJunctionNodes,
					URIs.uriJunctionNode.concat(jn.getId()));
		}
	}

	// EDGES

	/**
	 * Saves all given Edges of the Class MyEdge in Database
	 * 
	 * @param edges
	 *            List of Class MyEdge to be saved
	 */
	private void saveEdges(LinkedList<MyEdge> edges) {
		if (edges != null) {
			for (Iterator<MyEdge> it = edges.iterator(); it.hasNext();) {
				MyEdge myEdge = it.next();
				addEdge(myEdge);
			}
		}
	}

	/**
	 * Saves the given Edge of Class MyEdge in Database
	 * 
	 * @param myedge
	 *            Edge of Class MyEdge to be saved
	 */
	private void addEdge(MyEdge myedge) {
		Edge e = myedge.getEdge();
		Resource objectNode;
		
		String globalID = Methods.findGlobalID(e, myedge.getEdgeType().getType());
		String uri = URIs.uriEdge.concat(globalID);

		// Falls Edge noch nicht vorhanden
		if (!rdfModel.bagContainsResource(uri, URIs.uriBagEdges)) {

			// create Subject with URI from NodeID
			Resource subjectEdge = rdfModel.createResource(URIs.uriEdge
					.concat(globalID));

			// Add subject to Bag
			rdfModel.addToBag(URIs.uriBagEdges, subjectEdge);

			// Add EdgeType
			Resource type = rdfModel.createResource(URIs.uriEdgeType
					.concat(myedge.getEdgeType().getName()));
			rdfModel.insert(subjectEdge, Properties.PROP_TYPE, type);

			// Add Attributes from Edge
			addAllAttributes(myedge.getAttributesHashMap(), subjectEdge,
					URIs.uriEdgeAttribute, e);

			// Add all Annotations to Model
			addAllAnnotations(e.getAnnotations(), subjectEdge);

			// Add outgoing Nodes to Edge
			Node out = myedge.getDestinationNode().getNode();

			// check if outgoing Node is Node or JunctionNode
			// you have to differ between them because of the URI and the ID
			// (JunctionNodes don't have global IDs)
			if (out instanceof JunctionNode) {
				uri = URIs.uriJunctionNode;
				objectNode = rdfModel.createResource(uri.concat(out.getId()));
			} else {
				uri = URIs.uriNode;
				objectNode = rdfModel.createResource(uri.concat(Methods
						.findGlobalID(out, myedge.getDestinationNode()
								.getBaseNodeType())));
			}
			rdfModel.insert(subjectEdge, Properties.PROP_NODE_OUT, objectNode);

			// Add incoming Nodes to Edge
			Node in = myedge.getSourceNode().getNode();

			// check if outgoing Node is Node or JunctionNode
			// you have to differ between them because of the URI and the ID
			// (JunctionNodes don't have global IDs)
			if (in instanceof JunctionNode) {
				uri = URIs.uriJunctionNode;
				objectNode = rdfModel.createResource(uri.concat(in.getId()));
			} else {
				uri = URIs.uriNode;
				objectNode = rdfModel.getResource(uri.concat(Methods
						.findGlobalID(in, myedge.getSourceNode()
								.getBaseNodeType())));
			}
			rdfModel.insert(subjectEdge, Properties.PROP_NODE_IN, objectNode);
		}
	}

	/**
	 * Removes a given Edge of Class MyEdge from Database
	 * 
	 * @param myedge
	 *            Edge of Class MyEdge to be removed
	 */
	private void removeEdge(MyEdge myedge) {
		Edge edge = myedge.getEdge();
		String globalID = Methods.findGlobalID(edge, myedge.getEdgeType()
				.getType());
		String uri = URIs.uriEdge.concat(globalID);

		if (rdfModel.bagContainsResource(uri, URIs.uriBagEdges)) {
			// get Subject with URI from EdgeID
			Resource subjectEdge = rdfModel.getResource(uri);

			// Remove EdgeType
			Resource type = subjectEdge
					.getPropertyResourceValue(Properties.PROP_TYPE);
			rdfModel.removeTriple(subjectEdge, Properties.PROP_TYPE, type);

			// Remove all Attributes from Edge
			removeAllAttributes(myedge.getAttributesHashMap(), subjectEdge,
					URIs.uriEdgeAttribute);

			// Remove all Annotations to Model
			removeAllAnnotations(edge.getAnnotations(), subjectEdge);

			// Remove Edge from Bag
			rdfModel.removeFromBag(URIs.uriBagEdges, uri);
		}
	}

	// ATTRIBUTES

	/**
	 * Adds all Attributes from a Node/Edge/JunctionNode to a
	 * Node/Edge/JunctionNode
	 * 
	 * @param attr
	 *            HashMap of Attributes to be saved
	 * @param subject
	 *            Resource to which Attribute should be added
	 * @param prop
	 *            Property URI of the Triple: Node Property Attribute
	 * @param n
	 *            Node/Edge/JunctionNode to which Attribute belongs
	 */
	private <T> void addAllAttributes(HashMap<String, Attribute> attr,
			Resource subject, String prop, T n) {
		for (Iterator<Entry<String, Attribute>> it = attr.entrySet().iterator(); it
				.hasNext();) {
			Entry<String, Attribute> attrEntry = it.next();
			addAttribute(attrEntry, subject, prop, n);
		}
	}

	/**
	 * Add Attribute from one Entry-Set to a Node/Edge/JunctionNode
	 * 
	 * @param attrEntry
	 *            Entry-Set to be saved
	 * @param subject
	 *            Resource to which Attribute should be added
	 * @param propURI
	 *            Property URI of the Triple: Node Property Attribute
	 * @param n
	 *            Node/Edge/JunctionNode to which Attribute belongs
	 */
	private <T> void addAttribute(Entry<String, Attribute> attrEntry,
			Resource subject, String propURI, T n) {
		Attribute attr = attrEntry.getValue();
		String datatype = attr.getType().getName();
		String unit = attr.getUnit().getName();
		String category = attr.getCategory().getName();

		// Get Value and define what Type n is
		PSSIFOption<PSSIFValue> value = null;

		if (n instanceof Node)
			value = ((Node) n).apply(new GetValueOperation(attr));
		if (n instanceof JunctionNode)
			value = ((JunctionNode) n).apply(new GetValueOperation(attr));
		if (n instanceof Edge)
			value = ((Edge) n).apply(new GetValueOperation(attr));

		// if there is no Attribute value don't save anything
		if (value.isNone())
			return;

		// Get the ID of subject to add it to the Attribute Node URI
		String id = getID(subject.getURI());

		// Add Attribute Node
		Resource subjectAttr = rdfModel.createResource(propURI
				+ attrEntry.getKey() + "/" + id);
		Property prop = rdfModel.createProperty(URIs.uriAttribute
				.concat(attrEntry.getKey()));
		rdfModel.insert(subject, prop, subjectAttr);

		// Add Value
		// You have to differ weather the Attribute isOne or isMany
		if (value.isOne()) {
			PSSIFValue v = value.getOne();
			String attrValue = v.getValue().toString();
			if (datatype.compareTo("Date") == 0) {
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				Date date = (Date) v.getValue();
				attrValue = df.format(date);
			}
			addPSSIFValue(subjectAttr, attrValue, unit, datatype, category);
			return;
		}

		if (value.isMany()) {
			Set<PSSIFValue> values = value.getMany();
			for (PSSIFValue val : values) {

				String attrValue = val.getValue().toString();
				if (datatype.compareTo("Date") == 0) {
					DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
					Date date = (Date) val.getValue();
					attrValue = df.format(date);
				}
				addPSSIFValue(subjectAttr, attrValue, unit, datatype, category);
			}
		}
	}

	/**
	 * Adds PSSIF Attribute Values to Attribute
	 * 
	 * @param subjectAttr
	 *            Resource of Attribute
	 * @param value
	 *            Value of Attribute to be saved
	 * @param unit
	 *            Unit of Attribute to be saved
	 * @param datatype
	 *            Datatype of Attribute to be saved
	 * @param category
	 *            Category of Attribute to be saved
	 */
	private void addPSSIFValue(Resource subjectAttr, String value, String unit,
			String datatype, String category) {

		// Add Value
		subjectAttr.addProperty(Properties.PROP_ATTR_VALUE, value);
		// Add Unit
		subjectAttr.addProperty(Properties.PROP_ATTR_UNIT, unit);
		// Add Datatype
		subjectAttr.addProperty(Properties.PROP_ATTR_DATATYPE, datatype);
		// Add Category
		subjectAttr.addProperty(Properties.PROP_ATTR_CATEGORY, category);
	}

	// removes all Attributes
	private void removeAllAttributes(HashMap<String, Attribute> attr,
			Resource subject, String prop) {
		for (Iterator<Entry<String, Attribute>> it = attr.entrySet().iterator(); it
				.hasNext();) {
			Entry<String, Attribute> attrEntry = it.next();
			removeAttribute(attrEntry, subject, prop);
		}
	}

	/**
	 * Removes a given Attributes with its Value, Unit, Datatype and Category
	 * 
	 * @param attrEntry
	 *            Entry-Set to be saved
	 * @param subject
	 *            Resource from which Attribute should be removed
	 * @param propURI
	 *            Property URI of the Triple: Node Property Attribute
	 */
	private void removeAttribute(Entry<String, Attribute> attrEntry,
			Resource subject, String propURI) {

		// Get the ID of subject to add it to the Attribute Node URI
		String id = getID(subject.getURI());

		// Get Attribute Node + Property
		Resource subjectAttr = rdfModel.getResource(propURI
				+ attrEntry.getKey() + id);
		Property prop = rdfModel.getProperty(URIs.uriAttribute.concat(attrEntry
				.getKey()));

		// remove Value, Unit and Datatype
		List<Statement> list = subjectAttr.listProperties().toList();
		for (Statement stmt : list)
			rdfModel.removeStatement(stmt);

		// remove the Attribute Node
		rdfModel.removeTriple(subject, prop, subjectAttr);
	}

	// ANNOTATIONS

	/**
	 * Adds all Annotation to a Node/Edge/JunctionNode
	 * 
	 * @param annots
	 *            List of Annotations to be saved
	 * @param subject
	 *            Resource to which Annotation should be added
	 */
	private void addAllAnnotations(PSSIFOption<Entry<String, String>> annots,
			Resource subject) {
		for (Iterator<Entry<String, String>> it1 = annots.iterator(); it1
				.hasNext();) {
			Entry<String, String> annot = it1.next();
			addAnnotation(annot, subject);
		}
	}

	/**
	 * Removes all Annotation from a Node/Edge/JunctionNode
	 * 
	 * @param annots
	 *            List of Annotations to be removed
	 * @param subject
	 *            Resource from which Annotation should be removed
	 */
	private void removeAllAnnotations(
			PSSIFOption<Entry<String, String>> annots, Resource subject) {
		for (Iterator<Entry<String, String>> it1 = annots.iterator(); it1
				.hasNext();) {
			Entry<String, String> annot = it1.next();
			removeAnnotation(annot, subject);
		}
	}

	/**
	 * Adds a given Annotation to a Node/Edge/JunctionNode
	 * 
	 * @param annot
	 *            Entry-Set of Annotation to be added
	 * @param subject
	 *            Resource to which Annotation should be added
	 */
	private void addAnnotation(Entry<String, String> annot, Resource subject) {
		String key = annot.getKey();
		String value = annot.getValue();
		Property prop = rdfModel.createProperty(URIs.uriAnnotation.concat(key));
		subject.addProperty(prop, value);
	}

	/**
	 * Removes a given Annotation from a Node/Edge/JunctionNode
	 * 
	 * @param annot
	 *            Entry-Set of Annotation to be removed
	 * @param subject
	 *            Resource from which Annotation should be removed
	 */
	private void removeAnnotation(Entry<String, String> annot, Resource subject) {
		String key = annot.getKey();
		String value = annot.getValue();

		rdfModel.removeTripleLiteral(subject.getURI(),
				URIs.uriAnnotation.concat(key), value);
	}

	/**
	 * Clears all Lists of Nodes/Edges/JunctionNodes that are changed or deleted
	 */
	public static void clearAll() {
		newNodes.clear();
		newEdges.clear();
		newJunctionNodes.clear();
		changedNodes.clear();
		changedEdges.clear();
		changedJunctionNodes.clear();
		deletedNodes.clear();
		deletedEdges.clear();
		deletedJunctionNodes.clear();
	}

	/**
	 * returns the ID of a certain URI. Doesn't look if this really is an ID
	 * 
	 * @param uri
	 *            URI with ID in it
	 * @return ID from URI
	 */
	private String getID(String uri) {
		String[] uriSTR = uri.split("/");
		return uriSTR[uriSTR.length - 1];
	}

	/**
	 * Saves changed Edges/Nodes/JunctionNodes
	 */
	private void changeElements() {
		// first deletes changed Nodes from DB and than add them to DB
		for (MyNode node : changedNodes) {
			removeNode(node);
			addNode(node);
		}

		for (MyEdge edge : changedEdges) {
			removeEdge(edge);
			addEdge(edge);
		}

		for (MyJunctionNode jNode : changedJunctionNodes) {
			removeJunctionNode(jNode);
			addJunctionNode(jNode);
		}
	}
}
