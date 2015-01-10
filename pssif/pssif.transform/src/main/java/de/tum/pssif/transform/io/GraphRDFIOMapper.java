package de.tum.pssif.transform.io;

import graph.model.MyEdge;
import graph.model.MyJunctionNode;
import graph.model.MyNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import model.MyModelContainer;

import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.external.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Node;

public class GraphRDFIOMapper {
	public 	Model rdfmodel;

	// Variables to record changes that have to be saved in the Database
	public static LinkedList<MyNode> changedNodes = new LinkedList<>();
	public static LinkedList<MyEdge> changedEdges = new LinkedList<>();
	public static LinkedList<MyJunctionNode> changedJunctionNodes = new LinkedList<>();
	public static LinkedList<MyNode> deletedNodes = new LinkedList<>();
	public static LinkedList<MyEdge> deletedEdges = new LinkedList<>();
	public static LinkedList<MyJunctionNode> deletedJunctionNodes = new LinkedList<>();
	public static boolean deleteAll = false;

	public GraphRDFIOMapper(de.tum.pssif.core.model.Model model, Metamodel metamodel) {
		MyModelContainer mymodelContainer = new MyModelContainer(model, PSSIFCanonicMetamodelCreator.create());
//		MyModelContainer mymodelContainer = new MyModelContainer(model, metamodel);

		rdfmodel =  ModelFactory.createDefaultModel();
		modelToDB(mymodelContainer);
	}

	// MODEL

	public void modelToDB(MyModelContainer model) {
		rdfmodel.removeAll();
		saveNodes(model.getAllNodes());
		saveJunctionNodes(model.getAllJunctionNodes());
		saveEdges(model.getAllEdges());
		// TODO Testing

	}

	public void saveToDB() {
		if (deleteAll) {
			rdfmodel.removeAll();
			deleteAll = false;
		}
		saveNodes(changedNodes);
		saveEdges(changedEdges);
		saveJunctionNodes(changedJunctionNodes);

		for (MyNode node : deletedNodes)
			removeNode(node);
		for (MyJunctionNode junctionnode : deletedJunctionNodes)
			removeJunctionNode(junctionnode);
		for (MyEdge edge : deletedEdges)
			removeEdge(edge);
		// TODO Testing


		// changes are in DB -> clear all Lists
		clearAll();
	}

	public void removeModel() {
		rdfmodel.removeAll();

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
	
	public boolean bagContainsResource(Model model, String uri, String bag) {
		Bag b = model.getBag(bag);
		return b.hasProperty(Properties.PROP_BAG, model.createResource(uri));
	}

	
	public void removeTripleLiteral(Model model, String s, String p, String o) {
		removeStatement(model, new SimpleSelector(model.getResource(s),
				model.getProperty(p), o));
	}
	public void removeTriple(Model model, Resource s, Property p, Resource r) {
		removeStatement( model, new SimpleSelector(s, p, r));

	}
	/**
	 * Removes a Statement.
	 * 
	 * @param selector
	 *            contains the Statements to be removed
	 */
	private void removeStatement(Model model, SimpleSelector selector) {
		List<Statement> statementsToRemove = new ArrayList<Statement>();

		// find all statements with given uris of s,p,r
		// normally there should be only 1 statement
		StmtIterator state = model.listStatements(selector);

		// add statements that should be removed to a list
		while (state.hasNext()) {
			Statement stmt = state.nextStatement();
			statementsToRemove.add(stmt);
		}
		// remove all listed statements from model
		for (Statement stmt : statementsToRemove) {
			model.remove(stmt);
		}
	}
	public void removeStatement(Model model, Statement st) {
		model.remove(st);
	}

	public void addToBag(Model model, String uri, Resource subject) {
		// create bag if not existing otherwise get existing bag
		Bag b = model.createBag(uri);
		// add subject to bag if it doesn't already exist
		if (!b.contains(subject))
			b.addProperty(Properties.PROP_BAG, subject);
	}
	
	public void removeFromBag(Model model, String uri, String subject) {
		// create bag if not existing otherwise get existing bag
		Bag b = model.createBag(uri);
		// add subject to bag if it doesn't already exist
		if (!b.contains(subject)) {
			List<Statement> list = b.listProperties(Properties.PROP_BAG)
					.toList();
			for (Statement stmt : list) {
				String test = stmt.getObject().toString();
				if (subject.contains(test))
					model.remove(stmt);
			}
		}
	}

	public boolean containsJunctionNode(Model model, String uri) {
		Bag b = model.getBag(URIs.uriBagJunctionNodes);
		return b.contains(model.createResource(uri));
	}
	
	public void insert(Model model, Resource subject, Property predicate, Resource object) {
		// write triples to model
		model.add(subject, predicate, object);
	}
	
	/**
	 * Saves the given Node of Class MyNode in Database
	 * 
	 * @param mynode
	 *            Node of Class MyNode to be saved
	 */
	private void addNode(MyNode mynode) {
		Node n = mynode.getNode();
		// Falls Edge noch nicht vorhanden
		String uri = URIs.uriNode.concat(n.getId());
		if (!bagContainsResource(rdfmodel,uri, URIs.uriBagNodes)) {
			// create Subject with URI from NodeID
			Resource subjectNode = rdfmodel.createResource(URIs.uriNode
					.concat(n.getId()));
			
			rdfmodel.add(subjectNode, RDF.type, URIs.namespace+"Node");

			// Add subject to Bag
			addToBag(rdfmodel, URIs.uriBagNodes, subjectNode);

			// Add NodeType
			subjectNode.addProperty(Properties.PROP_TYPE, mynode.getNodeType()
					.getName());

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
		if (bagContainsResource(rdfmodel, URIs.uriNode.concat(node.getId()),
				URIs.uriBagNodes)) {
			// get Subject with URI from NodeID
			Resource subjectNode = rdfmodel.getResource(URIs.uriNode
					.concat(node.getId()));

			// Remove NodeType
			removeTripleLiteral(rdfmodel, subjectNode.getURI(),
					Properties.PROP_TYPE.getURI(), mynode.getNodeType()
							.getName());

			// Remove Attributes from Node
			removeAllAttributes(mynode.getAttributesHashMap(), subjectNode,
					URIs.uriNodeAttribute);

			// Remove all Annotations to Model
			removeAllAnnotations(node.getAnnotations(), subjectNode);

			// Remove Node from Bag
			removeFromBag(rdfmodel, URIs.uriBagNodes,
					URIs.uriNode.concat(node.getId()));
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

		// Falls Edge noch nicht vorhanden
		if (!containsJunctionNode(rdfmodel, URIs.uriJunctionNode.concat(jn
				.getId()))) {

			// create Subject with URI from NodeID
			Resource subjectJNode = rdfmodel
					.createResource(URIs.uriJunctionNode.concat(jn.getId()));

			// Add subject to Bag
			addToBag(rdfmodel, URIs.uriBagJunctionNodes, subjectJNode);

			// Add JNodeType
			subjectJNode.addProperty(Properties.PROP_TYPE, myJNode
					.getNodeType().getName());

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

		if (bagContainsResource(rdfmodel,
				URIs.uriJunctionNode.concat(jn.getId()),
				URIs.uriBagJunctionNodes)) {

			// get Subject with URI from NodeID
			Resource subjectJNode = rdfmodel.getResource(URIs.uriJunctionNode
					.concat(jn.getId()));

			// Remove NodeType
			removeTripleLiteral(rdfmodel,subjectJNode.getURI(),
					Properties.PROP_TYPE.getURI(), mynode.getNodeType()
							.getName());

			// Remove Attributes from Node
			removeAllAttributes(mynode.getAttributesHashMap(), subjectJNode,
					URIs.uriJunctionNode);

			// Remove all Annotations to Model
			removeAllAnnotations(jn.getAnnotations(), subjectJNode);

			// Remove JunctionNode from Bag
			removeFromBag(rdfmodel,URIs.uriBagJunctionNodes,
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
		String uri;

		// Falls Edge noch nicht vorhanden
		if (!bagContainsResource(rdfmodel,URIs.uriEdge.concat(e.getId()),
				URIs.uriBagEdges)) {

			// create Subject with URI from NodeID
			Resource subjectEdge = rdfmodel.createResource(URIs.uriEdge
					.concat(e.getId()));
			
			rdfmodel.add(subjectEdge, RDF.type, URIs.namespace+"Edge");


			// Add subject to Bag
			addToBag(rdfmodel,URIs.uriBagEdges, subjectEdge);

			// Add EdgeType
			subjectEdge.addProperty(Properties.PROP_TYPE, myedge.getEdgeType()
					.getName());

			// Add Attributes from Edge
			addAllAttributes(myedge.getAttributesHashMap(), subjectEdge,
					URIs.uriEdge, e);

			// Add all Annotations to Model
			addAllAnnotations(e.getAnnotations(), subjectEdge);

			// Add outgoing Nodes to Edge
			Node out = myedge.getDestinationNode().getNode();
			// check if outgoing Node is Node or JunctionNode
			if (out instanceof JunctionNode)
				uri = URIs.uriJunctionNode;
			else
				uri = URIs.uriNode;
			Resource objectNode = rdfmodel.createResource(uri.concat(out
					.getId()));
			insert(rdfmodel,subjectEdge, Properties.PROP_NODE_OUT, objectNode);

			// Add incoming Nodes to Edge
			Node in = myedge.getSourceNode().getNode();
			// check if incoming Node is Node or JunctionNode
			if (in instanceof JunctionNode)
				uri = URIs.uriJunctionNode;
			else
				uri = URIs.uriNode;
			objectNode = rdfmodel.getResource(uri.concat(in.getId()));
			insert(rdfmodel,subjectEdge, Properties.PROP_NODE_IN, objectNode);
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

		if (bagContainsResource(rdfmodel,URIs.uriEdge.concat(edge.getId()),
				URIs.uriBagEdges)) {
			// get Subject with URI from NodeID
			Resource subjectEdge = rdfmodel.getResource(URIs.uriEdge
					.concat(edge.getId()));

			// Remove NodeType
			removeTripleLiteral(rdfmodel,subjectEdge.getURI(),
					Properties.PROP_TYPE.getURI(), myedge.getEdgeType()
							.getName());

			// Remove all Attributes from Edge
			removeAllAttributes(myedge.getAttributesHashMap(), subjectEdge,
					URIs.uriEdge);

			// Remove all Annotations to Model
			removeAllAnnotations(edge.getAnnotations(), subjectEdge);

			// Remove Edge from Bag
			removeFromBag(rdfmodel,URIs.uriBagEdges,
					URIs.uriEdge.concat(edge.getId()));
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
		String[] uri = subject.getURI().split("#");
		String id = uri[1];

		// Add Attribute Node
		Resource subjectAttr = rdfmodel.createResource(propURI
				+ attrEntry.getKey() + id);
//		Property prop = rdfmodel.createProperty(URIs.uriAttribute
//		.concat(attrEntry.getKey()));
		Property prop = rdfmodel.createProperty(URIs.namespace, attrEntry.getKey());
		rdfmodel.add(subject, prop, subjectAttr);
		rdfmodel.createResource(propURI
				+ attrEntry.getKey() + id);
		
		
		// Add Value
		// You have to differ weather the Attribute isOne or isMany
		if (value.isOne()) {
			PSSIFValue v = value.getOne();
			String attrValue = v.getValue().toString();
			addPSSIFValue(subjectAttr, attrValue, unit, datatype, category);
			subjectAttr.addProperty(RDF.type, URIs.namespace+"Attribute");
			return;
		}

		if (value.isMany()) {
			Set<PSSIFValue> values = value.getMany();
			for (PSSIFValue val : values)
				addPSSIFValue(subjectAttr, val.getValue().toString(), unit,
						datatype, category);
			subjectAttr.addProperty(RDF.type, URIs.namespace+"Attribute");
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
		String[] uri = subject.getURI().split("#");
		String id = uri[1];

		// Get Attribute Node + Property
		Resource subjectAttr = rdfmodel.getResource(propURI
				+ attrEntry.getKey() + id);
		Property prop = rdfmodel.getProperty(URIs.uriAttribute.concat(attrEntry
				.getKey()));

		// remove Value, Unit and Datatype
		List<Statement> list = subjectAttr.listProperties().toList();
		for (Statement stmt : list)
			removeStatement(rdfmodel,stmt);

		// remove the Attribute Node
		removeTriple(rdfmodel,subject, prop, subjectAttr);
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
		Property prop = rdfmodel.createProperty(URIs.namespace,key);
//		Property prop = rdfmodel.createProperty(URIs.uriAnnotation.concat(key));
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

		removeTripleLiteral(rdfmodel,subject.getURI(),
				URIs.uriAnnotation.concat(key), value);
	}

	/**
	 * Clears all Lists of Nodes/Edges/JunctionNodes that are changed or deleted
	 */
	public static void clearAll() {
		changedNodes.clear();
		changedEdges.clear();
		changedJunctionNodes.clear();
		deletedNodes.clear();
		deletedEdges.clear();
		deletedJunctionNodes.clear();
	}
}
