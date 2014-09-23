package jena.mapper.impl;

import graph.model.MyEdge;
import graph.model.MyJunctionNode;
import graph.model.MyNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import jena.database.Properties;
import jena.database.URIs;
import jena.database.impl.DatabaseImpl;
import jena.database.impl.RDFModelImpl;
import jena.mapper.DBMapper;
import model.MyModelContainer;

import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.impl.GetValueOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Node;

// Model to DB Mapper
// TODO write Interface
public class DBMapperImpl implements DBMapper {

	public RDFModelImpl rdfModel;
	public static DatabaseImpl db;

	public DBMapperImpl() {
		super();
		if (db == null)
			db = new DatabaseImpl(URIs.location, URIs.namespace);
		rdfModel = db.getRdfModel();
	}

	// MODEL

	public void modelToDB(MyModelContainer model, String modelname) {
		db.begin(ReadWrite.WRITE);
		saveNodes(model.getAllNodes());
		saveJunctionNodes(model.getAllJunctionNodes());
		saveEdges(model.getAllEdges());
		// TODO Testing
		rdfModel.writeModelToFile("TestPSSIF", "C:\\Users\\Andrea\\Desktop\\");
		db.commit();
		db.end();
	}

	public void removeModel() {
		db.begin(ReadWrite.WRITE);
		rdfModel.removeAll();
		db.removeNamedModel(rdfModel.toString());
		db.commit();
		db.end();
	}

	// NODES

	private void saveNodes(LinkedList<MyNode> nodes) {
		for (Iterator<MyNode> it = nodes.iterator(); it.hasNext();) {
			MyNode myNode = it.next();
			addNode(myNode);
		}
	}

	private void addNode(MyNode mynode) {
		Node n = mynode.getNode();
		// Falls Edge noch nicht vorhanden
		String uri = URIs.uriNode.concat(n.getId());
		if (!rdfModel.containsNode(uri)) {
			// create Subject with URI from NodeID
			Resource subjectNode = rdfModel.createResource(URIs.uriNode
					.concat(n.getId()));

			// Add subject to Bag
			rdfModel.addToBag(URIs.uriBagNodes, subjectNode);

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

	private void removeNode(MyNode mynode) {
		Node node = mynode.getNode();

		if (rdfModel.containsNode(URIs.uriNode.concat(node.getId()))) {
			// get Subject with URI from NodeID
			Resource subjectNode = rdfModel.getResource(URIs.uriNode
					.concat(node.getId()));

			// Remove NodeType
			rdfModel.removeTripleLiteral(subjectNode.getURI(),
					Properties.PROP_TYPE.getURI(), mynode.getNodeType()
							.getName());

			// Remove Attributes from Node
			removeAllAttributes(mynode.getAttributesHashMap(), subjectNode,
					URIs.uriNodeAttribute);

			// Remove all Annotations to Model
			removeAllAnnotations(node.getAnnotations(), subjectNode);
		}
	}

	// private void removeNode(Node node, String nodeType) {
	// if (rdfModel.containsNode(URIs.uriNode.concat(node.getId()))) {
	// // get Subject with URI from NodeID
	// Resource subjectNode = rdfModel.getResource(URIs.uriNode
	// .concat(node.getId()));
	//
	// // Remove NodeType
	// rdfModel.removeTripleLiteral(subjectNode.getURI(),
	// Properties.PROP_TYPE.getURI(), nodeType);
	//
	// // Remove all Annotations to Model
	// removeAllAnnotations(node.getAnnotations(), subjectNode);
	// }
	// }

	// JUNCTION NODES

	private void saveJunctionNodes(LinkedList<MyJunctionNode> nodes) {
		for (Iterator<MyJunctionNode> it = nodes.iterator(); it.hasNext();) {
			MyJunctionNode myJNode = it.next();
			addJunctionNode(myJNode);
		}
	}

	private void addJunctionNode(MyJunctionNode myJNode) {
		Node jn = myJNode.getNode();
		// Falls Edge noch nicht vorhanden
		if (!rdfModel.containsJunctionNode(URIs.uriJunctionNode.concat(jn
				.getId()))) {

			// create Subject with URI from NodeID
			Resource subjectJNode = rdfModel
					.createResource(URIs.uriJunctionNode.concat(jn.getId()));

			// Add subject to Bag
			rdfModel.addToBag(URIs.uriBagJunctionNodes, subjectJNode);

			// Add JNodeType
			subjectJNode.addProperty(Properties.PROP_TYPE, myJNode
					.getNodeType().getName());

			// Add Attributes from JNode
			// TODO Welche Attribute kommen vor??

			// Add all Annotations to Model
			addAllAnnotations(jn.getAnnotations(), subjectJNode);
		}
	}

	private void removeJunctionNode(MyJunctionNode mynode) {
		Node jn = mynode.getNode();

		if (rdfModel.containsNode(URIs.uriJunctionNode.concat(jn.getId()))) {

			// get Subject with URI from NodeID
			Resource subjectJNode = rdfModel.getResource(URIs.uriJunctionNode
					.concat(jn.getId()));

			// Remove NodeType
			rdfModel.removeTripleLiteral(subjectJNode.getURI(),
					Properties.PROP_TYPE.getURI(), mynode.getNodeType()
							.getName());

			// Remove Attributes from Node
			// TODO What Attributes? and Where From?

			// Remove all Annotations to Model
			removeAllAnnotations(jn.getAnnotations(), subjectJNode);
		}
	}

	// private void addJunctionNode(Node jn, String jNodeType) {
	// // Falls Edge noch nicht vorhanden
	// if (rdfModel.containsJunctionNode(URIs.uriJunctionNode.concat(jn
	// .getId()))) {
	//
	// // create Subject with URI from NodeID
	// Resource subjectJNode = rdfModel
	// .createResource(URIs.uriJunctionNode.concat(jn.getId()));
	//
	// // Add subject to Bag
	// rdfModel.addToBag(URIs.uriBagJunctionNodes, subjectJNode);
	//
	// // Add JNodeType
	// subjectJNode.addProperty(Properties.PROP_TYPE, jNodeType);
	//
	// // Add Attributes from Edge
	// // -> doesn't have any attributes
	// // addAllAttributes(jn.getValues(), subjectJNode);
	//
	// // Add all Annotations to Model
	// addAllAnnotations(jn.getAnnotations(), subjectJNode);
	// }
	// }
	//
	// private void removeJunctionNode(Node jn, String jNodeType) {
	// if (rdfModel.containsNode(URIs.uriJunctionNode.concat(jn.getId()))) {
	// // get Subject with URI from NodeID
	// Resource subjectJNode = rdfModel.getResource(URIs.uriJunctionNode
	// .concat(jn.getId()));
	//
	// // Remove NodeType
	// rdfModel.removeTripleLiteral(subjectJNode.getURI(),
	// Properties.PROP_TYPE.getURI(), jNodeType);
	//
	// // Remove Attributes from Node
	// // removeAllAttributes(jn.getValues(), subjectJNode);
	//
	// // Remove all Annotations to Model
	// removeAllAnnotations(jn.getAnnotations(), subjectJNode);
	// }
	// }

	// EDGES

	private void saveEdges(LinkedList<MyEdge> edges) {
		for (Iterator<MyEdge> it = edges.iterator(); it.hasNext();) {
			MyEdge myEdge = it.next();
			addEdge(myEdge);
		}
	}

	private void addEdge(MyEdge myedge) {
		Edge e = myedge.getEdge();

		// Falls Edge noch nicht vorhanden
		if (!rdfModel.containsEdge(URIs.uriEdge.concat(e.getId()))) {

			// create Subject with URI from NodeID
			Resource subjectEdge = rdfModel.createResource(URIs.uriEdge
					.concat(e.getId()));

			// Add subject to Bag
			rdfModel.addToBag(URIs.uriBagEdges, subjectEdge);

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
			Resource objectNode = rdfModel.createResource(URIs.uriNode
					.concat(out.getId()));
			rdfModel.insert(subjectEdge, Properties.PROP_NODE_OUT, objectNode);

			// Add incoming Nodes to Edge
			Node in = myedge.getSourceNode().getNode();
			objectNode = rdfModel.getResource(URIs.uriNode.concat(in.getId()));
			rdfModel.insert(subjectEdge, Properties.PROP_NODE_IN, objectNode);
		}
	}

	private void removeEdge(MyEdge myedge) {
		Edge edge = myedge.getEdge();

		if (rdfModel.containsNode(URIs.uriEdge.concat(edge.getId()))) {
			// get Subject with URI from NodeID
			Resource subjectEdge = rdfModel.getResource(URIs.uriEdge
					.concat(edge.getId()));

			// Remove NodeType
			rdfModel.removeTripleLiteral(subjectEdge.getURI(),
					Properties.PROP_TYPE.getURI(), myedge.getEdgeType()
							.getName());

			// Remove all Attributes from Edge
			removeAllAttributes(myedge.getAttributesHashMap(), subjectEdge,
					URIs.uriEdge);

			// Remove all Annotations to Model
			removeAllAnnotations(edge.getAnnotations(), subjectEdge);
		}
	}

	// private void addEdge(Edge e, String edgeType) {
	// // Falls Edge noch nicht vorhanden
	// if (rdfModel.containsEdge(URIs.uriEdge.concat(e.getId()))) {
	//
	// // create Subject with URI from NodeID
	// Resource subjectEdge = rdfModel.createResource(URIs.uriEdge
	// .concat(e.getId()));
	//
	// // Add subject to Bag
	// rdfModel.addToBag(URIs.uriBagEdges, subjectEdge);
	//
	// // Add EdgeType
	// subjectEdge.addProperty(Properties.PROP_TYPE, edgeType);
	//
	// // Add Attributes from Edge
	// // TODO added getter to Class Element in pssif.core
	// // addAllAttributes(e.getValues(), subjectEdge);
	//
	// // Add all Annotations to Model
	// addAllAnnotations(e.getAnnotations(), subjectEdge);
	//
	// // Add outgoing Nodes to Edge
	// // TODO right Method to get Node?
	// Node out = e.apply(new ReadFromNodesOperation());
	// Resource objectNode = rdfModel.getResource(URIs.uriNode.concat(out
	// .getId()));
	// rdfModel.insert(subjectEdge, Properties.PROP_NODE_OUT, objectNode);
	//
	// // Add incoming Nodes to Edge
	// // TODO right Method to get Node?
	// Node in = e.apply(new ReadToNodesOperation());
	// // in MyEdge .getOutgoing
	// objectNode = rdfModel.getResource(URIs.uriNode.concat(in.getId()));
	// rdfModel.insert(subjectEdge, Properties.PROP_NODE_IN, objectNode);
	// }
	// }
	//
	// private void removeEdge(Edge edge, String edgeType) {
	// if (rdfModel.containsNode(URIs.uriEdge.concat(edge.getId()))) {
	// // get Subject with URI from NodeID
	// Resource subjectEdge = rdfModel.getResource(URIs.uriEdge
	// .concat(edge.getId()));
	//
	// // Remove NodeType
	// rdfModel.removeTripleLiteral(subjectEdge.getURI(),
	// Properties.PROP_TYPE.getURI(), edgeType);
	//
	// // Remove all Attributes from Edge
	// // removeAllAttributes(edge.getValues(), subjectEdge);
	//
	// // Remove all Annotations to Model
	// removeAllAnnotations(edge.getAnnotations(), subjectEdge);
	// }
	// }

	// ATTRIBUTES

	// add all Attributes from a Node/Edge
	private <T> void addAllAttributes(HashMap<String, Attribute> attr,
			Resource subject, String prop, T n) {
		for (Iterator<Entry<String, Attribute>> it = attr.entrySet().iterator(); it
				.hasNext();) {
			Entry<String, Attribute> attrEntry = it.next();
			addAttribute(attrEntry, subject, prop, n);
		}
	}

	// add Attribute from one Entry-Set
	// TODO make generic for Node/Edge
	private <T> void addAttribute(Entry<String, Attribute> attrEntry,
			Resource subject, String propURI, T n) {
		Attribute attr = attrEntry.getValue();
		String datatype = attr.getType().getName();
		String unit = attr.getUnit().getName();

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
		Resource subjectAttr = rdfModel.createResource(propURI
				+ attrEntry.getKey() + id);
		Property prop = rdfModel.createProperty(URIs.uriAttribute
				.concat(attrEntry.getKey()));
		rdfModel.insert(subject, prop, subjectAttr);

		// Add Value
		// You have to differ weather the Attribute isOne or isMany
		if (value.isOne()) {
			PSSIFValue v = value.getOne();
			String attrValue = v.getValue().toString();
			addPSSIFValue(subjectAttr, attrValue, unit, datatype);
			return;
		}

		if (value.isMany()) {
			Set<PSSIFValue> values = value.getMany();
			for (PSSIFValue val : values)
				addPSSIFValue(subjectAttr, val.getValue().toString(), unit,
						datatype);
		}
	}

	// adds PSSIF Attribute to subject
	private void addPSSIFValue(Resource subjectAttr, String value, String unit,
			String datatype) {
		// Add Value
		subjectAttr.addProperty(Properties.PROP_ATTR_VALUE, value);
		// Add Unit
		subjectAttr.addProperty(Properties.PROP_ATTR_UNIT, unit);
		// Add Datatype
		subjectAttr.addProperty(Properties.PROP_ATTR_DATATYPE, datatype);
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

	// removes a given Attributes with its Value, Unit and Datatype
	private void removeAttribute(Entry<String, Attribute> attrEntry,
			Resource subject, String propURI) {

		// Get the ID of subject to add it to the Attribute Node URI
		String[] uri = subject.getURI().split("#");
		String id = uri[1];

		// Get Attribute Node + Property
		Resource subjectAttr = rdfModel.getResource(propURI
				+ attrEntry.getKey() + id);
		Property prop = rdfModel.getProperty(URIs.uriAttribute.concat(attrEntry
				.getKey()));

		// remove Value, Unit and Datatype
		for (StmtIterator it = subjectAttr.listProperties(); it.hasNext();)
			rdfModel.removeStatement(it.nextStatement());

		// remove the Attribute Node
		rdfModel.removeTriple(subject, prop, subjectAttr);
	}

	// // add all Attributes from a Node/Edge
	// private void addAllAttributes(Map<String, PSSIFOption<PSSIFValue>> attr,
	// Resource subject) {
	// for (Iterator<Entry<String, PSSIFOption<PSSIFValue>>> it = attr
	// .entrySet().iterator(); it.hasNext();) {
	// Entry<String, PSSIFOption<PSSIFValue>> attrEntry = it.next();
	// addAttribute(attrEntry, subject);
	// }
	// }

	// // removes all Attributes
	// private void removeAllAttributes(Map<String, PSSIFOption<PSSIFValue>>
	// attr,
	// Resource subject) {
	// for (Iterator<Entry<String, PSSIFOption<PSSIFValue>>> it = attr
	// .entrySet().iterator(); it.hasNext();) {
	// Entry<String, PSSIFOption<PSSIFValue>> attrEntry = it.next();
	// removeAttribute(attrEntry, subject);
	// }
	// }

	// // add all Attributes from one Entry-Set
	// private void addAttribute(Entry<String, PSSIFOption<PSSIFValue>>
	// attrEntry,
	// Resource subject) {
	// PSSIFOption<PSSIFValue> attribute = attrEntry.getValue();
	// if (attrEntry.getValue().isOne()) {
	// PSSIFValue value = attribute.getOne();
	// addAttribute(attrEntry.getKey(), value.getValue().toString(),
	// subject);
	// } else if (attrEntry.getValue().isMany()) {
	// Set<PSSIFValue> values = attribute.getMany();
	// for (Iterator<PSSIFValue> it = values.iterator(); it.hasNext();) {
	// PSSIFValue value = it.next();
	// addAttribute(attrEntry.getKey(), value.getValue().toString(),
	// subject);
	// }
	// }
	// }

	// // removes all Attributes with a given key
	// private void removeAttribute(
	// Entry<String, PSSIFOption<PSSIFValue>> attrEntry, Resource subject) {
	// PSSIFOption<PSSIFValue> attribute = attrEntry.getValue();
	// if (attrEntry.getValue().isOne()) {
	// PSSIFValue value = attribute.getOne();
	// removeAttribute(attrEntry.getKey(), value.getValue().toString(),
	// subject);
	// } else if (attrEntry.getValue().isMany()) {
	// Set<PSSIFValue> values = attribute.getMany();
	// for (Iterator<PSSIFValue> it = values.iterator(); it.hasNext();) {
	// PSSIFValue value = it.next();
	// removeAttribute(attrEntry.getKey(),
	// value.getValue().toString(), subject);
	// }
	// }
	// }

	// ANNOTATIONS

	private void addAllAnnotations(PSSIFOption<Entry<String, String>> annots,
			Resource subject) {
		for (Iterator<Entry<String, String>> it1 = annots.iterator(); it1
				.hasNext();) {
			Entry<String, String> annot = it1.next();
			addAnnotation(annot, subject);
		}
	}

	private void removeAllAnnotations(
			PSSIFOption<Entry<String, String>> annots, Resource subject) {
		for (Iterator<Entry<String, String>> it1 = annots.iterator(); it1
				.hasNext();) {
			Entry<String, String> annot = it1.next();
			removeAnnotation(annot, subject);
		}
	}

	private void addAnnotation(Entry<String, String> annot, Resource subject) {
		String key = annot.getKey();
		String value = annot.getValue();
		Property prop = rdfModel.createProperty(URIs.uriAnnotation.concat(key));
		subject.addProperty(prop, value);
	}

	private void removeAnnotation(Entry<String, String> annot, Resource subject) {
		String key = annot.getKey();
		String value = annot.getValue();

		rdfModel.removeTripleLiteral(subject.getURI(),
				URIs.uriAnnotation.concat(key), value);
	}
}
