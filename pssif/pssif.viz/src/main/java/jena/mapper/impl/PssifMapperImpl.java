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
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.external.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;

//DB to Model Mapper
// TODO create Interface
public class PssifMapperImpl implements PssifMapper {
	public RDFModelImpl rdfModel;
	public static DatabaseImpl db;
	private de.tum.pssif.core.model.Model pssifModel;
	private Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
	private HashMap<String, Node> nodes = new HashMap<>();
	private HashMap<String, Edge> edges = new HashMap<>();
	private HashMap<String, JunctionNode> junctionNodes = new HashMap<>();

	public void DBToModel() {
		if (db == null)
			db = new DatabaseImpl(URIs.location, URIs.namespace);
		rdfModel = db.getRdfModel();
		pssifModel = new ModelImpl();

		db.begin(ReadWrite.READ);
		getNodes();
		getJunctionNodes();
		getEdges();
		db.end();

		ModelBuilder.addModel(ModelBuilder.getMetamodel(), pssifModel);
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
		try {
			String id = "";
			Node newNode = null;

			// find NodeType
			Statement resNodeType = subject.getProperty(Properties.PROP_TYPE);
			String nodeTypeName = resNodeType.getObject().toString();

			// Create new Node and NodeType
			NodeType nodeType = metamodel.getNodeType(nodeTypeName).getOne();
			newNode = nodeType.create(pssifModel);

			// add attributes and annotations to Node
			id = createAttributeOrAnnotation(subject, nodeType, newNode);

			// add node to hashmap
			nodes.put(id, newNode);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem with constructing an Node", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
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
		try {
			String id = "";
			Node newJNode = null;

			// find JNodeType
			Statement resJNodeType = subject.getProperty(Properties.PROP_TYPE);
			String jNodeTypeName = resJNodeType.getObject().toString();

			// create new JNode and JNodeType
			JunctionNodeType jNodeType = metamodel.getJunctionNodeType(
					jNodeTypeName).getOne();
			newJNode = jNodeType.create(pssifModel);

			// add annotations and attributes to Edge
			id = createAttributeOrAnnotation(subject, jNodeType, newJNode);

			// add edge to Hashmap
			nodes.put(id, newJNode);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem with constructing an JunctionNode", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
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
		try {
			// find EdgeType
			Statement resEdgeType = subject.getProperty(Properties.PROP_TYPE);
			String edgeTypeName = resEdgeType.getObject().toString();

			// create new Edge and EdgeType
			EdgeType edgeType = metamodel.getEdgeType(edgeTypeName).getOne();

			// get incoming and outgoing nodes
			Edge newEdge = constructInOutNodes(subject, edgeType);

			// add annotations and attributes to Edge
			String id = createAttributeOrAnnotation(subject, edgeType, newEdge);

			// add edge to Hashmap
			edges.put(id, newEdge);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem with constructing an Edge", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private Edge constructInOutNodes(Resource subject, EdgeType edgeType) {
		Node nodeIn = null;
		Node nodeOut = null;
		NodeTypeBase nodeTypeIn = null;
		NodeTypeBase nodeTypeOut = null;

		// get Statements for incoming and outgoing Nodes
		Statement stmtNodeIn = subject.getProperty(Properties.PROP_NODE_IN);
		Statement stmtNodeOut = subject.getProperty(Properties.PROP_NODE_OUT);

		// get incoming Node from statement
		if (stmtNodeIn != null) {
			// get Node
			Resource resNodeIn = stmtNodeIn.getObject().asResource();
			// get NodeType
			Statement stmtNodeType = resNodeIn
					.getProperty(Properties.PROP_TYPE);
			String nodeTypeName = stmtNodeType.getObject().toString();
			// if node is a Junction node you have to get the JunctionNodeType
			if (resNodeIn.toString().contains(URIs.uriJunctionNode)) {
				PSSIFOption<NodeTypeBase> nodeTypeIn1 = metamodel
						.getBaseNodeType(nodeTypeName);
				nodeTypeIn = nodeTypeIn1.getOne();
			} else
				nodeTypeIn = metamodel.getNodeType(nodeTypeName).getOne();
			// get ID
			Resource resNodeID = resNodeIn.getProperty(Properties.PROP_ATTR_ID)
					.getObject().asResource();
			Statement st = resNodeID.getProperty(Properties.PROP_ATTR_VALUE);
			String id = st.getObject().toString();
			// get existing Node from ID
			nodeIn = nodes.get(id);
		}
		// get outgoing Node from statement
		if (stmtNodeOut != null) {
			// get Node
			Resource resNodeOut = stmtNodeOut.getObject().asResource();
			// get NodeType
			Statement stmtNodeType = resNodeOut
					.getProperty(Properties.PROP_TYPE);
			String nodeTypeName = stmtNodeType.getObject().toString();
			// if node is a Junction node you have to get the JunctionNodeType
			if (resNodeOut.toString().contains(URIs.uriJunctionNode)) {
				PSSIFOption<NodeTypeBase> nodeTypeOut1 = metamodel
						.getBaseNodeType(nodeTypeName);
				nodeTypeOut = nodeTypeOut1.getOne();
			} else
				nodeTypeOut = metamodel.getNodeType(nodeTypeName).getOne();
			// get ID
			Resource resNodeID = resNodeOut
					.getProperty(Properties.PROP_ATTR_ID).getObject()
					.asResource();
			Statement st = resNodeID.getProperty(Properties.PROP_ATTR_VALUE);
			String id = st.getObject().toString();
			// get existing Node from ID
			nodeOut = nodes.get(id);
		}

		// TODO
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
				nodeTypeIn, nodeTypeOut);
		Edge newEdge = mapping.getOne().create(pssifModel, nodeIn, nodeOut);

		return newEdge;
	}

	// ATTRIBUTES
	private String createAttributeOrAnnotation(Resource subject,
			ElementType type, Element elem) {

		String id = "";

		// find Attributes an Annotations by iterating over all Statements of
		// the subject element
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

				// Add the Attribute to the element
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

				// Add the Annotation to the element
				elem.annotate(annotKey, value);
			}
		}
		return id;
	}
}
