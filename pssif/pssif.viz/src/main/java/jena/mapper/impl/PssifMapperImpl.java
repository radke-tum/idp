package jena.mapper.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import jena.database.Properties;
import jena.database.URIs;
import jena.database.impl.DatabaseImpl;
import jena.database.impl.RDFModelImpl;
import jena.mapper.PssifMapper;
import model.ModelBuilder;
import model.MyModelContainer;

import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.external.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;

//DB to Model Mapper
// TODO create Interface
public class PssifMapperImpl implements PssifMapper {
	public RDFModelImpl rdfModel;
	public DatabaseImpl db;
	// public MyModelContainer modelContainer;
	private de.tum.pssif.core.model.Model pssifModel;
	private Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
	private HashMap<String, Node> nodes = new HashMap<>();

	// private HashMap<String, JunctionNode> junctionNodes = new HashMap<>();
	// private HashMap<String, Edge> edges = new HashMap<>();

	public void DBToModel() {
		db = new DatabaseImpl(URIs.location, URIs.namespace);
		rdfModel = db.getRdfModel();
		pssifModel = new ModelImpl();

		// modelContainer = new MyModelContainer(pssifModel, metamodel);

		db.begin(ReadWrite.READ);
		getNodes();
		// getJunctionNodes();
		getEdges();
		db.end();

		ModelBuilder.addModel(ModelBuilder.getMetamodel(), pssifModel);

		MyModelContainer modelContainer = ModelBuilder.activeModel;
	}

	// NODES

	// get all Nodes from the database
	private void getNodes() {
		try {
			List<Resource> subjectNodes = rdfModel
					.getSubjectsOfBag(URIs.uriBagNodes);
			for (Iterator<Resource> iter = subjectNodes.iterator(); iter
					.hasNext();) {
				constructNode(iter.next());
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null,
					"There are no Nodes in the Database", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// reconstruct a Node like pssif.core.node from a Node subject in the db
	private void constructNode(Resource subject) {
		String id = "";
		Node newNode = null;

		// find NodeType
		Statement resNodeType = subject.getProperty(Properties.PROP_TYPE);
		String nodeTypeName = resNodeType.getObject().toString();

		NodeType nodeType = metamodel.getNodeType(nodeTypeName).getOne();

		// TODO Mit welcher Version einen neuen Knoten erstellen?
		// Node newNode = new NodeImpl(pssifModel);
		newNode = nodeType.create(pssifModel);

		// TODO Wie an Model binden?
		// MyNode mynode = modelContainer.addNewNodeFromGUI(name, nodeType);
		// newNode = pssifModel.apply(new CreateNodeOperation(nodeType))
		// -> Konstruktor nicht freigegeben

		id = createAttributeOrAnnotation(subject, nodeType, newNode);

		// // find Attributes an Annotations by iterating over all Statements of
		// // the subject Node
		// for (StmtIterator iter = subject.listProperties(); iter.hasNext();) {
		// Statement stmt = iter.nextStatement();
		// // to get the Attribute/Annotation Name you have to get the URI of
		// // the Property
		// // and take the String behind the # sign
		// String[] propURI = stmt.getPredicate().getURI().split("#");
		//
		// // Property is an Attribute
		// if (propURI[0].concat("#").equals(URIs.uriAttribute)) {
		// String attribute = propURI[1];
		//
		// // get the value of the Attribute
		// Resource object = stmt.getObject().asResource();
		// String value = "";
		// Statement st = object.getProperty(Properties.PROP_ATTR_VALUE);
		// value = st.getObject().toString();
		//
		// // Add the Attribute to the node
		// // TODO legt man so ein neues Attribut zu einem Knoten an?
		// nodeType.getAttribute(attribute)
		// .getOne()
		// .set(newNode, PSSIFOption.one(PSSIFValue.create(value)));
		//
		// // if attribute is the ID -> save it
		// if (PSSIFConstants.BUILTIN_ATTRIBUTE_ID.contains(attribute))
		// id = value;
		// }
		// // Property is an Annotation
		// else if (propURI[0].concat("#").equals(URIs.uriAnnotation)) {
		// String annotKey = propURI[1];
		// // get the value of the Annotation
		// Literal object = stmt.getObject().asLiteral();
		// String value = object.getString();
		//
		// // Add the Annotation to the node
		// newNode.annotate(annotKey, value);
		// }
		// }
		// TODO add node to Container
		// modelContainer.addNode(new MyNode(newNode, new
		// MyNodeType(nodeType)));

		// add node to hashmap
		nodes.put(id, newNode);
	}

	// JUNCTION NODES

	private void getJunctionNodes() {
		try {
			List<Resource> subjectJNodes = rdfModel
					.getSubjectsOfBag(URIs.uriBagJunctionNodes);
			for (Iterator<Resource> iter = subjectJNodes.iterator(); iter
					.hasNext();) {
				constructJunctionNode(iter.next());
			}
		} catch (NullPointerException e) {
		}
	}

	private void constructJunctionNode(Resource subject) {
		// TODO
	}

	// EDGES

	private void getEdges() {
		try {
			List<Resource> subjectEdges = rdfModel
					.getSubjectsOfBag(URIs.uriBagEdges);
			for (Iterator<Resource> iter = subjectEdges.iterator(); iter
					.hasNext();) {
				constructEdge(iter.next());
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null,
					"There are no Edges in the Database", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void constructEdge(Resource subject) {
		// find NodeType
		Statement resEdgeType = subject.getProperty(Properties.PROP_TYPE);
		String edgeTypeName = resEdgeType.getObject().toString();

		EdgeType edgeType = metamodel.getEdgeType(edgeTypeName).getOne();

		// get incoming and outgoing nodes
		Edge newEdge = constructInOutNodes(subject, edgeType);

		createAttributeOrAnnotation(subject, edgeType, newEdge);

		// // find Attributes an Annotations by iterating over all Statements of
		// // the subject Node
		// for (StmtIterator iter = subject.listProperties(); iter.hasNext();) {
		// Statement stmt = iter.nextStatement();
		// // to get the Attribute/Annotation Name you have to get the URI of
		// // the Property
		// // and take the String behind the # sign
		// String[] propURI = stmt.getPredicate().getURI().split("#");
		//
		// // Property is an Attribute
		// if (propURI[0].concat("#").equals(URIs.uriAttribute)) {
		// String attribute = propURI[1];
		//
		// // get the value of the Attribute
		// Resource object = stmt.getObject().asResource();
		// String value = "";
		// Statement st = object.getProperty(Properties.PROP_ATTR_VALUE);
		// value = st.getObject().toString();
		//
		// // Add the Attribute to the node
		// // TODO legt man so ein neues Attribut zu einem Knoten an?
		// // edgeType.getAttribute(attribute)
		// // .getOne()
		// // .set(newEdge, PSSIFOption.one(PSSIFValue.create(value)));
		//
		// PSSIFOption<Attribute> attributePssif = edgeType
		// .getAttribute(attribute);
		// if (attributePssif.isOne()) {
		// attributePssif.getOne().set(
		// newEdge,
		// PSSIFOption.one(attributePssif.getOne().getType()
		// .fromObject(value)));
		// }
		// }
		// // Property is an Annotation
		// else if (propURI[0].concat("#").equals(URIs.uriAnnotation)) {
		// String annotKey = propURI[1];
		// // get the value of the Annotation
		// Literal object = stmt.getObject().asLiteral();
		// String value = object.getString();
		//
		// // Add the Annotation to the node
		// newEdge.annotate(annotKey, value);
		// }
		// }

	}

	private Edge constructInOutNodes(Resource subject, EdgeType edgeType) {
		// Multimap<String, Node> nodes = pssifModel.getNodes();
		Node nodeIn = null;
		Node nodeOut = null;
		NodeType nodeTypeIn = null;
		NodeType nodeTypeOut = null;
		Statement stmtNodeIn = subject.getProperty(Properties.PROP_NODE_IN);
		Statement stmtNodeOut = subject.getProperty(Properties.PROP_NODE_OUT);
		if (stmtNodeIn != null) {
			// get Node
			Resource resNodeIn = stmtNodeIn.getObject().asResource();
			// get NodeType
			Statement stmtNodeType = resNodeIn
					.getProperty(Properties.PROP_TYPE);
			String nodeTypeName = stmtNodeType.getObject().toString();
			nodeTypeIn = metamodel.getNodeType(nodeTypeName).getOne();
			// get ID
			Resource resNodeID = resNodeIn.getProperty(Properties.PROP_ATTR_ID)
					.getObject().asResource();
			Statement st = resNodeID.getProperty(Properties.PROP_ATTR_VALUE);
			String id = st.getObject().toString();
			// get Node from ID
			nodeIn = nodes.get(id);
		}
		if (stmtNodeOut != null) {
			// get Node
			Resource resNodeOut = stmtNodeOut.getObject().asResource();
			// get NodeType
			Statement stmtNodeType = resNodeOut
					.getProperty(Properties.PROP_TYPE);
			String nodeTypeName = stmtNodeType.getObject().toString();
			nodeTypeOut = metamodel.getNodeType(nodeTypeName).getOne();
			// get ID
			Resource resNodeID = resNodeOut
					.getProperty(Properties.PROP_ATTR_ID).getObject()
					.asResource();
			Statement st = resNodeID.getProperty(Properties.PROP_ATTR_VALUE);
			String id = st.getObject().toString();
			// get Node from ID
			// Collection<Node> nodeColl =
			// Iterator<Node> it = nodeColl.iterator();
			nodeOut = nodes.get(id);
		}

		// TODO Edge wird aktuell nicht angezeigt, aber in pssifModel und Nodes
		// vorhanden
		// Edge newEdge = new EdgeImpl(pssifModel, nodeIn, nodeOut);
		// Edge newEdge = edgeType.create(pssifModel);

		// ConnectionMappingImpl mapping = new ConnectionMappingImpl(edgeType,
		// nodeTypeIn, nodeTypeOut);
		//
		// nodeIn.registerIncomingEdge(mapping, newEdge);
		// nodeOut.registerOutgoingEdge(mapping, newEdge);

		// Edge newEdge = new CreateEdgeOperation(mapping, nodeIn, nodeOut)
		// .apply(pssifModel);

		// Fehler: could not find one of the nodes to connect
		// newEdge = mapping.create(pssifModel, nodeOut, nodeIn);

		// Edge newEdge = pssifModel.apply(new CreateEdgeOperation(mapping,
		// nodeOut, nodeIn));

		PSSIFOption<ConnectionMapping> mapping = edgeType.getMapping(
				nodeTypeOut, nodeTypeIn);
		Edge newEdge = mapping.getOne().create(pssifModel, nodeOut, nodeIn);

		return newEdge;
	}

	// ATTRIBUTES
	private String createAttributeOrAnnotation(Resource subject,
			ElementType type, Element elem) {

		String id = "";

		// find Attributes an Annotations by iterating over all Statements of
		// the subject Node
		for (StmtIterator iter = subject.listProperties(); iter.hasNext();) {
			Statement stmt = iter.nextStatement();
			// to get the Attribute/Annotation Name you have to get the URI of
			// the Property
			// and take the String behind the # sign
			String[] propURI = stmt.getPredicate().getURI().split("#");

			// Property is an Attribute
			if (propURI[0].concat("#").equals(URIs.uriAttribute)) {
				String attribute = propURI[1];

				// get the value of the Attribute
				Resource object = stmt.getObject().asResource();
				String value = "";
				Statement st = object.getProperty(Properties.PROP_ATTR_VALUE);
				value = st.getObject().toString();

				// Add the Attribute to the node
				PSSIFOption<Attribute> attributePssif = type
						.getAttribute(attribute);
				if (attributePssif.isOne()) {
					attributePssif.getOne().set(
							elem,
							PSSIFOption.one(attributePssif.getOne().getType()
									.fromObject(value)));
				}

				// if attribute is the ID -> save it
				if (PSSIFConstants.BUILTIN_ATTRIBUTE_ID.contains(attribute))
					id = value;
			}
			// Property is an Annotation
			else if (propURI[0].concat("#").equals(URIs.uriAnnotation)) {
				String annotKey = propURI[1];
				// get the value of the Annotation
				Literal object = stmt.getObject().asLiteral();
				String value = object.getString();

				// Add the Annotation to the node
				elem.annotate(annotKey, value);
			}
		}
		return id;
	}
}
