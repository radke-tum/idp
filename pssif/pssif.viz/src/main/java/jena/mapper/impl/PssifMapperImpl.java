package jena.mapper.impl;

import java.util.Iterator;
import java.util.List;

import jena.database.Properties;
import jena.database.URIs;
import jena.database.impl.DatabaseImpl;
import jena.database.impl.RDFModelImpl;
import jena.mapper.PssifMapper;
import model.MyModelContainer;

import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.external.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Element;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.model.impl.ModelImpl;

//DB to Model Mapper
// TODO create Interface
public class PssifMapperImpl implements PssifMapper {
	public RDFModelImpl rdfModel;
	public DatabaseImpl db;
	public MyModelContainer modelContainer;
	private de.tum.pssif.core.model.Model pssifModel;
	private Metamodel metamodel = PSSIFCanonicMetamodelCreator.create();

	public void DBToModel(String modelname) {
		db = new DatabaseImpl(URIs.location, URIs.namespace);
		rdfModel = db.getRdfModel();
		pssifModel = new ModelImpl();

		modelContainer = new MyModelContainer(pssifModel, metamodel);

		db.begin(ReadWrite.READ);
		getNodes();
		getJunctionNodes();
		getEdges();
		db.end();
	}

	// NODES

	// get all Nodes from the database
	private void getNodes() {
		List<Resource> subjectNodes = rdfModel
				.getSubjectsOfBag(URIs.uriBagNodes);
		for (Iterator<Resource> iter = subjectNodes.iterator(); iter.hasNext();) {
			constructNode(iter.next());
		}
	}

	// reconstruct a Node like pssif.core.node from a Node subject in the db
	private void constructNode(Resource subject) {

		// find NodeType
		Statement resNodeType = subject.getProperty(Properties.PROP_TYPE);
		String nodeTypeName = resNodeType.getObject().toString();

		System.out.println("Test NodeType = " + nodeTypeName);

		NodeType nodeType = metamodel.getNodeType(nodeTypeName).getOne();

		// TODO Mit welcher Version einen neuen Knoten erstellen?
		// Node newNode = new NodeImpl(pssifModel);
		Node newNode = nodeType.create(pssifModel);

		// TODO Wie an Model binden?
		// MyNode mynode = modelContainer.addNewNodeFromGUI(name, nodeType);
		// newNode = pssifModel.apply(new CreateNodeOperation(nodeType))
		// -> Konstruktor nicht freigegeben

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
				Literal object = stmt.getObject().asLiteral();
				String value = object.getString();

				// Add the Attribute to the node
				// TODO legt man so ein neues Attribut zu einem Knoten an?
				nodeType.getAttribute(attribute)
						.getOne()
						.set(newNode, PSSIFOption.one(PSSIFValue.create(value)));
			}
			// Property is an Annotation
			else if (propURI[0].concat("#").equals(URIs.uriAnnotation)) {
				String annotKey = propURI[1];
				// get the value of the Annotation
				Literal object = stmt.getObject().asLiteral();
				String value = object.getString();

				// Add the Annotation to the node
				newNode.annotate(annotKey, value);
			}
		}
	}

	// JUNCTION NODES

	private void getJunctionNodes() {
		List<Resource> subjectJNodes = rdfModel
				.getSubjectsOfBag(URIs.uriBagJunctionNodes);
		for (Iterator<Resource> iter = subjectJNodes.iterator(); iter.hasNext();) {
			constructJunctionNode(iter.next());
		}
	}

	private void constructJunctionNode(Resource subject) {
		// TODO
	}

	// EDGES

	private void getEdges() {
		List<Resource> subjectEdges = rdfModel
				.getSubjectsOfBag(URIs.uriBagEdges);
		for (Iterator<Resource> iter = subjectEdges.iterator(); iter.hasNext();) {
			constructEdge(iter.next());
		}
	}

	private void constructEdge(Resource subject) {
		// find NodeType
		Statement resEdgeType = subject.getProperty(Properties.PROP_TYPE);
		String edgeTypeName = resEdgeType.getObject().toString();

		EdgeType edgeType = metamodel.getEdgeType(edgeTypeName).getOne();

		// Edge newEdge = new EdgeImpl(model, from, to);

		// TODO Mit welcher Version einen neuen Knoten erstellen?
		// newEdge = pssifModel.apply(new CreateEdgeOperation(edgeType));
		// Konstruktor nicht freigegeben

		// TODO
		// same as with node

		Statement resNodeIn = subject.getProperty(Properties.PROP_NODE_IN);
		Statement resNodeOut = subject.getProperty(Properties.PROP_NODE_OUT);
		if (resNodeIn != null) {

		}
		if (resNodeOut != null) {

		}
	}

	// ATTRIBUTES
	private <T extends Element, E> void createAttributeOrAnnotation(T subject,
			E type, Statement stmt) {
	}
}
