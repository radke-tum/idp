package de.tum.pssif.transform.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.swing.JOptionPane;

import cern.colt.matrix.linalg.Property;

import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
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

public class RDFGraphIOMapper {

	private com.hp.hpl.jena.rdf.model.Model rdfModel;
	private de.tum.pssif.core.model.Model pssifModel;
	private Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();
	private HashMap<String, Node> nodes = new HashMap<>();
	private HashMap<String, Edge> edges = new HashMap<>();
	private static int id = 0;

	public RDFGraphIOMapper(com.hp.hpl.jena.rdf.model.Model rdfModel,
			Metamodel metamodel) {

		this.rdfModel = rdfModel;
		this.metamodel = metamodel;
		setPssifModel(new ModelImpl());
		createNodes();
		// TODO: Junction Nodes
		// getJunctionNodes();
		createEdges();
	}

	// NODES

	/**
	 * Get all Nodes from the database
	 */
	private void createNodes() {
		ResIterator subjectNodes = rdfModel.listSubjectsWithProperty(RDF.type,
				ResourceFactory.createProperty(URIs.namespace, "Node"));

		while (subjectNodes.hasNext()) {
			Resource subject = (subjectNodes.next());
			try {
				String id;

				// find NodeType

				String nodeTypeName = subject.getProperty(Properties.PROP_TYPE)
						.getObject().toString();

				// Create new Node and NodeType
				NodeType nodeType = metamodel.getNodeType(nodeTypeName)
						.getOne();
				Node newNode = nodeType.create(getPssifModel());

				// add attributes and annotations to Node
				Statement s = subject.getProperty(Properties.PROP_ID);
				if (s != null) {
					id = s.getResource()
							.getProperty(Properties.PROP_ATTR_VALUE)
							.getObject().toString();
				} else
					id = generateId(subject);

				createAttributeOrAnnotation(subject, nodeType, newNode);

				// add node to hashmap
				nodes.put(id, newNode);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Problem with constructing a Node", "PSSIF",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	// JUNCTION NODES

	/**
	 * Get all JunctionNodes from the database
	 */
	// private void getJunctionNodes() {
	// try {
	// List<Resource> subjectJNodes =
	// getSubjectsOfBag(rdfModel,URIs.uriBagJunctionNodes);
	// for (Iterator<Resource> iter = subjectJNodes.iterator(); iter
	// .hasNext();) {
	// constructJunctionNode(iter.next());
	// }
	// } catch (NullPointerException e) {
	// }
	// }

	// /**
	// * Reconstruct a JunctionNode like pssif.core.JunctionNode from a
	// * JunctionNode subject in the database
	// *
	// * @param subject
	// * Resource of the JunctionNode
	// */
	// private void constructJunctionNode(Resource subject) {
	// try {
	// String id;
	// Node newJNode = null;
	//
	// // find JNodeType
	// Statement resJNodeType = subject.getProperty(Properties.PROP_TYPE);
	// String jNodeTypeName = resJNodeType.getObject().toString();
	//
	// // create new JNode and JNodeType
	// JunctionNodeType jNodeType = metamodel.getJunctionNodeType(
	// jNodeTypeName).getOne();
	// newJNode = jNodeType.create(getPssifModel());
	//
	// // add annotations and attributes to Edge
	//
	// Statement s = subject.getProperty(Properties.PROP_ID);
	// if (s != null) {
	// id = s.getResource().getProperty(Properties.PROP_ATTR_VALUE)
	// .getObject().toString();
	// } else {
	// if (subject.getURI() != null)
	// id = subject.getURI().split("#")[1];
	// else
	// id = UUID.randomUUID().toString();
	// Resource att = rdfModel.createResource();
	// subject.addProperty(Properties.PROP_ID, att);
	// att.addProperty(RDF.type, URIs.namespace + "Attribute");
	// att.addProperty(Properties.PROP_ATTR_DATATYPE, "String");
	// att.addProperty(Properties.PROP_ATTR_VALUE, id);
	// att.addProperty(Properties.PROP_ATTR_UNIT, "None");
	// att.addProperty(Properties.PROP_ATTR_CATEGORY, "MetaData");
	// }
	// createAttributeOrAnnotation(subject, jNodeType, newJNode);
	//
	// // add edge to Hashmap
	// nodes.put(id, newJNode);
	//
	// } catch (Exception e) {
	// JOptionPane.showMessageDialog(null,
	// "Problem with constructing an JunctionNode", "PSSIF",
	// JOptionPane.ERROR_MESSAGE);
	// }
	// }

	// EDGES

	/**
	 * Get all Edges from the database
	 */
	private void createEdges() {
		ResIterator subjectEdges = rdfModel.listSubjectsWithProperty(RDF.type,
				ResourceFactory.createProperty(URIs.namespace, "Edge"));

		try {
			while (subjectEdges.hasNext()) {
				Resource subject = subjectEdges.next();

				// find EdgeType
				Statement resEdgeType = subject
						.getProperty(Properties.PROP_TYPE);
				if (resEdgeType != null) {
					String edgeTypeName = resEdgeType.getObject().asLiteral()
							.getString();

					String id;
					Statement s = subject.getProperty(Properties.PROP_ID);
					if (s != null) {
						id = s.getResource()
								.getProperty(Properties.PROP_ATTR_VALUE)
								.getObject().toString();
					} else {
						id = generateId(subject);
					}

					// create new Edge and EdgeType
					EdgeType edgeType = metamodel.getEdgeType(edgeTypeName)
							.getOne();

					// get incoming and outgoing nodes
					Edge newEdge = constructInOutNodes(subject, edgeType);

					// add annotations and attributes to Edge
					createAttributeOrAnnotation(subject, edgeType, newEdge);

					// add edge to Hashmap
					edges.put(id, newEdge);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem with constructing an Edge", "PSSIF",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private String generateId(Resource subject) {
		String id;
		// if (subject.getURI() != null)
		// id = subject.getURI().split("#")[1];
		// else {
		// id = UUID.randomUUID().toString();
		id = String.valueOf(RDFGraphIOMapper.id);
		RDFGraphIOMapper.id += 1;
		// }
		// Resource att = rdfModel.createResource();
		subject.addProperty(Properties.PROP_ID, id);

		// rdfModel.add(att,RDF.type, ResourceFactory.createResource(
		// URIs.namespace+"Attribute"));
		// rdfModel.add(att,Properties.PROP_ATTR_NAME, "name");
		// rdfModel.add(att,Properties.PROP_ATTR_DATATYPE, "String");
		// rdfModel.add(att,Properties.PROP_ATTR_VALUE, id);
		// rdfModel.add(att,Properties.PROP_ATTR_UNIT, "none");
		// rdfModel.add(att,Properties.PROP_ATTR_CATEGORY, "MetaData");
		return id;
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
			// TODO JunctionNodeType
			// if (resNodeIn.toString().contains(URIs.uriJunctionNode)) {
			// PSSIFOption<NodeTypeBase> nodeTypeIn1 = metamodel
			// .getBaseNodeType(nodeTypeName);
			// nodeTypeIn = nodeTypeIn1.getOne();
			// } else
			nodeTypeIn = metamodel.getNodeType(nodeTypeName).getOne();
			// get ID
			// Statement resNodeID = resNodeIn
			// .getProperty(Properties.PROP_ATTR_ID);
			String id = resNodeIn.getProperty(Properties.PROP_ID).getString();
			// if (resNodeID != null) {
			// Statement st = resNodeID.getObject().asResource()
			// .getProperty(Properties.PROP_ATTR_VALUE);
			// id = st.getObject().toString();
			// }
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
			// TODO JunctionNodeType

			// if (resNodeOut.toString().contains(URIs.uriJunctionNode)) {
			// PSSIFOption<NodeTypeBase> nodeTypeOut1 = metamodel
			// .getBaseNodeType(nodeTypeName);
			// nodeTypeOut = nodeTypeOut1.getOne();
			// } else
			nodeTypeOut = metamodel.getNodeType(nodeTypeName).getOne();
			// get ID
			// Statement resNodeID = resNodeOut
			// .getProperty(Properties.PROP_ATTR_ID);
			String id = resNodeOut.getProperty(Properties.PROP_ID).getString();
			// if (resNodeID != null) {
			// Statement st = resNodeID.getObject().asResource()
			// .getProperty(Properties.PROP_ATTR_VALUE);
			// id = st.getObject().toString();
			// }
			// get existing Node from ID
			nodeOut = nodes.get(id);
		}

		PSSIFOption<ConnectionMapping> mapping = edgeType.getMapping(
				nodeTypeIn, nodeTypeOut);
		Edge newEdge = mapping.getOne()
				.create(getPssifModel(), nodeIn, nodeOut);

		return newEdge;
	}
	Object getAttributeValue(Resource subject, com.hp.hpl.jena.rdf.model.Property att) {
		 Object stmt = rdfModel.getProperty(subject, att);
		String value = stmt.toString();

		return value;
		
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
	private void createAttributeOrAnnotation(Resource subject,
			ElementType type, Element elem) {

		// find Attributes an Annotations by iterating over all Statements of
		// the subject element
		for (StmtIterator iter = subject.listProperties(); iter.hasNext();) {
			Statement stmt = iter.nextStatement();
			// to get the Attribute/Annotation Name you have to get the URI of
			// the Property
			// and take the String behind the # sign
			// String[] propURI = stmt.getPredicate().getURI().split("#");

			if (stmt.getPredicate().getURI()
					.equals(Properties.PROP_ID.getURI())) {
				String name = "id";
				// get the value of the Attribute
				// TODO Datatypes
				String value = stmt.getObject().toString();

				// Add the Attribute to the element
				PSSIFOption<Attribute> attributePssif = type.getAttribute(name);
				if (attributePssif.isOne()) {
					attributePssif.getOne().set(
							elem,
							PSSIFOption.one(attributePssif.getOne().getType()
									.fromObject(value)));
				}

			}

			// Property is an Attribute
			if (stmt.getPredicate().getURI()
					.equals(Properties.PROP_ATTR.getURI())
					&& stmt.getObject().isResource()) {

				// if (propURI[0].concat("#").equals(URIs.uriAttribute)) {
				Resource attribute = stmt.getObject().asResource();

				 Statement name = attribute.getProperty(Properties.PROP_ATTR_NAME);
				// get the value of the Attribute
				// TODO Datatypes
				 Statement value = attribute
						.getProperty(Properties.PROP_ATTR_VALUE);
				if (name != null && value != null) {
					// Add the Attribute to the element
					PSSIFOption<Attribute> attributePssif = type
							.getAttribute(name.getObject()
									.toString());
					if (attributePssif.isOne()) {
						attributePssif.getOne().set(
								elem,
								PSSIFOption.one(attributePssif.getOne()
										.getType().fromObject(value.getObject()
												.toString())));
					}
				}

			}
			// Property is an Annotation
			else if (stmt.getPredicate().getURI().equals(Properties.PROP_ANNOT)) {
				// get the value of the Annotation
				Literal object = stmt.getObject().asLiteral();
				String value = object.getString();

				// Add the Annotation to the element
				elem.annotate(Properties.PROP_ANNOT.getLocalName(), value);
			}
		}

	}

	public de.tum.pssif.core.model.Model getPssifModel() {
		return pssifModel;
	}

	public void setPssifModel(de.tum.pssif.core.model.Model pssifModel) {
		this.pssifModel = pssifModel;
	}
}
