package jena.mapper.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.ElementType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.metamodel.external.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.JunctionNode;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.JunctionNodeImpl;
import de.tum.pssif.core.model.impl.ModelImpl;

//DB to Model Mapper
public class PssifMapperImpl implements PssifMapper {
	public RDFModelImpl rdfModel;
	public static DatabaseImpl db;
	private de.tum.pssif.core.model.Model pssifModel;
	private Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
	private HashMap<String, Node> nodes = new HashMap<>();
	private HashMap<String, Edge> edges = new HashMap<>();

	@Override
	public void DBToModel() {
		db = new DatabaseImpl();
		pssifModel = new ModelImpl();
		rdfModel = db.getRdfModel();

		// db.begin(ReadWrite.READ);
		getNodes();
		getJunctionNodes();
		getEdges();
		// db.end();

		ModelBuilder.addModel(ModelBuilder.getMetamodel(), pssifModel);
	}

	// NODES

	/**
	 * Get all Nodes from the database
	 */
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

	/**
	 * Reconstruct a Node like pssif.core.Node from a Node subject in the
	 * database
	 * 
	 * @param subject
	 *            Resource of the Node
	 */
	private void constructNode(Resource subject) {
		try {
			String id = "";
			Node newNode = null;

			// find NodeType
			String nodeTypeName = getType(subject);

			// Create new Node and NodeType
			NodeType nodeType = metamodel.getNodeType(nodeTypeName).getOne();
			newNode = nodeType.create(pssifModel);

			// add attributes and annotations to Node
			id = createAttributeOrAnnotation(subject, nodeType, newNode);

			// add node to hashmap
			nodes.put(id, newNode);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem with constructing a Node", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// JUNCTION NODES

	/**
	 * Get all JunctionNodes from the database
	 */
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

	/**
	 * Reconstruct a JunctionNode like pssif.core.JunctionNode from a
	 * JunctionNode subject in the database
	 * 
	 * @param subject
	 *            Resource of the JunctionNode
	 */
	private void constructJunctionNode(Resource subject) {
		try {
			String id = "";
			Node newJNode = null;

			// find JNodeType
			String jNodeTypeName = getType(subject);

			// create new JNode and JNodeType
			JunctionNodeType jNodeType = metamodel.getJunctionNodeType(
					jNodeTypeName).getOne();
			newJNode = jNodeType.create(pssifModel);

			// add annotations and attributes to JNode
			id = createAttributeOrAnnotation(subject, jNodeType, newJNode);

			// add JNode to Hashmap
			nodes.put(id, newJNode);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem with constructing an JunctionNode", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// EDGES

	/**
	 * Get all Edges from the database
	 */
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

	/**
	 * Reconstruct a Edge like pssif.core.Edge from a Edge subject in the
	 * database
	 * 
	 * @param subject
	 *            Resource of the Edge
	 */
	private void constructEdge(Resource subject) {
		try {
			// find EdgeType
			String edgeTypeName = getType(subject);

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

	/**
	 * Gets the incoming and outgoing Nodes of an Edge. Adds the Nodes to the
	 * Edge. Adds the Edge to the Nodes.
	 * 
	 * @param subject
	 *            Resource of the Edge
	 * @param edgeType
	 *            EdgeType of the resource Edge
	 */
	private Edge constructInOutNodes(Resource subject, EdgeType edgeType) {
		Node nodeIn = null;
		Node nodeOut = null;
		NodeTypeBase nodeTypeIn = null;
		NodeTypeBase nodeTypeOut = null;
		Resource resNodeID;

		// get Statements for incoming and outgoing Nodes
		Statement stmtNodeIn = subject.getProperty(Properties.PROP_NODE_IN);
		Statement stmtNodeOut = subject.getProperty(Properties.PROP_NODE_OUT);

		// get incoming Node from statement
		if (stmtNodeIn != null) {
			// get Node
			Resource resNodeIn = stmtNodeIn.getObject().asResource();

			// get NodeType
			String nodeTypeName = getType(resNodeIn);
			// if node is a Junction node you have to get the JunctionNodeType
			// and ID
			if (resNodeIn.toString().contains(URIs.uriJunctionNode)) {
				PSSIFOption<NodeTypeBase> nodeTypeIn1 = metamodel
						.getBaseNodeType(nodeTypeName);
				nodeTypeIn = nodeTypeIn1.getOne();
				resNodeID = resNodeIn.getProperty(Properties.PROP_ATTR_ID)
						.getObject().asResource();
			} else {
				nodeTypeIn = metamodel.getNodeType(nodeTypeName).getOne();
				resNodeID = resNodeIn
						.getProperty(Properties.Prop_ATTR_GLOBAL_ID)
						.getObject().asResource();
			}

			// get ID
			Statement st = resNodeID.getProperty(Properties.PROP_ATTR_VALUE);
			String id = st.getObject().toString();

			nodeIn = nodes.get(id);
		}

		// get outgoing Node from statement
		if (stmtNodeOut != null) {
			// get Node
			Resource resNodeOut = stmtNodeOut.getObject().asResource();

			// get NodeType
			String nodeTypeName = getType(resNodeOut);
			// if node is a Junction node you have to get the JunctionNodeType
			// and ID
			if (resNodeOut.toString().contains(URIs.uriJunctionNode)) {
				PSSIFOption<NodeTypeBase> nodeTypeOut1 = metamodel
						.getBaseNodeType(nodeTypeName);
				nodeTypeOut = nodeTypeOut1.getOne();
				resNodeID = resNodeOut.getProperty(Properties.PROP_ATTR_ID)
						.getObject().asResource();
			} else {
				nodeTypeOut = metamodel.getNodeType(nodeTypeName).getOne();
				resNodeID = resNodeOut
						.getProperty(Properties.Prop_ATTR_GLOBAL_ID)
						.getObject().asResource();
			}

			// get ID
			Statement st = resNodeID.getProperty(Properties.PROP_ATTR_VALUE);
			String id = st.getObject().toString();

			// get existing Node from ID
			nodeOut = nodes.get(id);
		}

		PSSIFOption<ConnectionMapping> mapping = edgeType.getMapping(
				nodeTypeIn, nodeTypeOut);
		Edge newEdge = mapping.getOne().create(pssifModel, nodeIn, nodeOut);

		return newEdge;
	}

	// ATTRIBUTES

	/**
	 * Adds Attributes and Annotations to a Node/Edge/JunctionNode from a given
	 * resource
	 * 
	 * @param subject
	 *            Resource of the Node/Edge/JunctionNode
	 * @param type
	 *            Type of the resource
	 * @param elem
	 *            Node/Edge/JunctionNode
	 */
	private String createAttributeOrAnnotation(Resource subject,
			ElementType type, Element elem) {

		String id = "";
		// we need to distringuish between Attribute or Annotation Property
		// later on, so we have to get the value of the URIS. We could just
		// compare with "Attr" or "Annot". But if we work with the URIs class
		// this is more flexible for changes
		String[] attrSTR = URIs.uriAttribute.split("/");
		String[] annotSTR = URIs.uriAnnotation.split("/");
		String attr = attrSTR[attrSTR.length - 1];
		String annot = annotSTR[annotSTR.length - 1];

		// find Attributes an Annotations by iterating over all Statements of
		// the subject element
		for (StmtIterator iter = subject.listProperties(); iter.hasNext();) {
			Statement stmt = iter.nextStatement();
			// to get the Attribute/Annotation Name you have to get the URI of
			// the Property
			// and take the String behind the last / sign
			String[] propURI = stmt.getPredicate().getURI().split("/");

			// Property is an Attribute
			if (propURI[propURI.length - 2].equals(attr)) {
				String attribute = propURI[propURI.length - 1];

				// get the value of the Attribute
				Resource object = stmt.getObject().asResource();
				String value = "";
				Statement st = object.getProperty(Properties.PROP_ATTR_VALUE);
				value = st.getObject().toString();

				// Add the Attribute to the element
				PSSIFOption<Attribute> attributePssif = type
						.getAttribute(attribute);
				saveAttribute(elem, attributePssif, value);

				// if attribute is the ID -> save it
				if (elem instanceof JunctionNode
						|| elem instanceof JunctionNodeImpl) {
					if (PSSIFConstants.BUILTIN_ATTRIBUTE_ID.contains(attribute))
						id = value;
				} else {
					if (PSSIFConstants.BUILTIN_ATTRIBUTE_GLOBAL_ID
							.contains(attribute))
						id = value;
				}
			}
			// Property is an Annotation
			else if (propURI[propURI.length - 2].equals(annot)) {
				String annotKey = propURI[propURI.length - 1];
				// get the value of the Annotation
				Literal object = stmt.getObject().asLiteral();
				String value = object.getString();

				// Add the Annotation to the element
				elem.annotate(annotKey, value);
			}
		}
		return id;
	}

	/**
	 * Saves the value of a given attribute
	 * 
	 * @param elem
	 *            Node/Edge/JunctionNode
	 * @param tmp
	 * @param value
	 * @return true if everything went fine, otherwise false
	 */
	public boolean saveAttribute(Element elem, PSSIFOption<Attribute> tmp,
			String value) {
		if (tmp.isOne()) {
			Attribute attribute = tmp.getOne();
			DataType attrType = attribute.getType();

			if (attrType.equals(PrimitiveDataType.DATE)) {
				try {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"dd.MM.yyyy");
					Date date = formatter.parse(value);

					PSSIFValue res = PrimitiveDataType.DATE.fromObject(date);

					attribute.set(elem, PSSIFOption.one(res));
				} catch (IllegalArgumentException | ParseException e) {
					e.printStackTrace();
					return false;
				}
			}

			else if (attrType.equals(PrimitiveDataType.DECIMAL)) {
				try {
					PSSIFValue res = PrimitiveDataType.DECIMAL
							.fromObject(new BigDecimal(value));

					attribute.set(elem, PSSIFOption.one(res));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}

			else {
				try {

					attribute.set(elem,
							PSSIFOption.one(attrType.fromObject(value)));

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * gets the Type of a Node/Edge/JunctionNode from a Resource
	 * 
	 * @param subject
	 *            Resource to get Type
	 * @return name of the Type
	 */
	private String getType(Resource subject) {
		try {
			Statement stmt = subject.getProperty(Properties.PROP_TYPE);
			// to get the Type you have to split the URI and take the last
			// part of it
			String[] propURI = stmt.getObject().toString().split("/");

			return propURI[propURI.length - 1];
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"There is no NodeType or JunctionNodeType", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}
}
