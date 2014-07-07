package model;

import graph.model.MyEdgeType;
import graph.model.MyNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pssif.consistencyDataStructures.NodeAndType;
import org.pssif.mainProcesses.Methods;
import org.pssif.mergedDataStructures.MergedNodePair;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.common.PSSIFValue;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.AttributeGroup;
import de.tum.pssif.core.metamodel.ConnectionMapping;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * Very basic Model Merger. Can merge two models into one model. Does only copy
 * everything from one model to the other. No matching at all!
 * 
 * @author Luc
 * 
 */
public class ModelMerger {

	private Model modelOrigin;
	private Model modelNew;
	private Metamodel metaModelOrigin, metaModelNew;

	private HashMap<Node, Node> oldToNewNodes;

	private NodeType rootNodeType;

	private Map<NodeAndType, Node> nodeTransferUnmatchedOldToNewModel = new HashMap<NodeAndType, Node>();
	private Map<NodeAndType, Node> nodeTransferTracedOldToNewModel = new HashMap<NodeAndType, Node>();

	private List<MergedNodePair> mergedNodes;
	private List<NodeAndType> unmatchedNodesOrigin;

	MyModelContainer activeModel, newModel;
	private List<MergedNodePair> tracedNodes;

	/**
	 * @return the oldToNewNodes
	 */
	public HashMap<Node, Node> getOldToNewNodes() {
		return oldToNewNodes;
	}

	/**
	 * merge two models into one model in respect of the given metamodel
	 * 
	 * @param model1
	 *            first model
	 * @param modelNew
	 *            second model
	 * @param metaModelOrigin
	 *            metamodel
	 * @return the merged model
	 */
	public Model mergeModels(Model model1, Model model2, Metamodel meta) {
		this.modelOrigin = model1;
		this.modelNew = model2;
		this.metaModelOrigin = meta;
		this.oldToNewNodes = new HashMap<Node, Node>();

		// printNbEdges(model1);
		// printNbNodes(model1);
		// start transformation operations
		addAllNodes();
		addAllJunctionNodes();
		addAllEdges();

		// printNbEdges(model1);
		// printNbNodes(model1);

		return this.modelOrigin;
	}

	/**
	 * This method starts the real merge between two given models.
	 * 
	 * @param modelOrigin
	 * @param modelNew
	 * @param activeModel
	 * @param metaModelOrigin
	 * @return
	 * @author Andreas
	 * @param unmatchedNodesOrigin
	 * @param tracedNodes
	 */
	public Model mergeModels(Model modelOrigin, Model modelNew,
			Metamodel metaModelOriginal, Metamodel metaModelNew,
			List<MergedNodePair> mergedNodes,
			List<NodeAndType> unmatchedNodesOrigin,
			List<MergedNodePair> tracedNodes, MyModelContainer activeModel) {
		this.modelOrigin = modelOrigin;
		this.modelNew = modelNew;
		this.metaModelOrigin = metaModelOriginal;
		this.metaModelNew = metaModelNew;
		this.mergedNodes = mergedNodes;
		this.unmatchedNodesOrigin = unmatchedNodesOrigin;
		this.activeModel = activeModel;
		this.tracedNodes = tracedNodes;

		this.rootNodeType = metaModelNew.getNodeType(
				PSSIFConstants.ROOT_NODE_TYPE_NAME).getOne();

		// addAllNodes();
		// addAllJunctionNodes();
		// addAllEdges();

		return mergeNodes();
	}

	// TODO transfer edges of nodes which are not marked as merged to the new
	// model, too
	/**
	 * This method adds every node from the old model which is not marked as to
	 * be merged to the new model. The nodes which are marked as to be merged
	 * are deleted and not transfered to the new model. But their edges are
	 * transfered to the new model.
	 * 
	 * @param activeModel
	 * @param mergedNodes
	 *            the list containing the information which nodes shall be
	 *            merged into the new model
	 * @return the new active model.
	 * @author Andreas
	 * @param unmatchedNodesOrigin
	 */
	private Model mergeNodes() {

		newModel = new MyModelContainer(modelNew, metaModelNew);

		// adding unmatched nodes to the new model
		for (NodeAndType unmergedNode : unmatchedNodesOrigin) {

			Node newNode = addNodeToNewModel(unmergedNode.getNode(),
					unmergedNode.getType());

			nodeTransferUnmatchedOldToNewModel.put(unmergedNode, newNode);
		}

		// transferring the egdes of the unmatched nodes to the new model
		transferOldEdges();

		// transferring the traced nodes to the new model
		for (MergedNodePair tracedPair : tracedNodes) {
			Node newNode = addNodeToNewModel(tracedPair.getNodeOriginalModel(),
					tracedPair.getTypeOriginModel());

			nodeTransferTracedOldToNewModel.put(
					new NodeAndType(tracedPair.getNodeOriginalModel(),
							tracedPair.getTypeOriginModel()), newNode);
		}

		setTracedLinks();

		return newModel.getModel();
	}

	/**
	 * This method creates the tracelink edges between the nodepairs given in
	 * the traceNode list.
	 * 
	 * @param tracedNodes
	 *            the list with the node pairs which shall be linked by a
	 *            tracelink
	 * @author Andreas
	 */
	private void setTracedLinks() {
		MyEdgeType edgeType = new MyEdgeType(
				metaModelNew
						.getEdgeType(
								PSSIFCanonicMetamodelCreator.E_RELATIONSHIP_CHRONOLOGICAL_EVOLVES_TO)
						.getOne(), 6);
		
		Iterator<Entry<NodeAndType, Node>> it = nodeTransferTracedOldToNewModel
				.entrySet().iterator();
		
		NodeAndType tempNodeOrigin;
		Node tempNodeTransferred;
		
		MyNode searchedFromNodeNew = null, searchedToNodeNew = null;

		// Iterate over all old nodes transferred to the new model
		while (it.hasNext()) {
			Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
					.next();
			tempNodeOrigin = pairs.getKey();
			tempNodeTransferred = pairs.getValue();
			
			for(MergedNodePair tracedPair : tracedNodes){
				if(Methods.findGlobalID(tempNodeOrigin.getNode(), tempNodeOrigin.getType()).equals(Methods.findGlobalID(tracedPair.getNodeOriginalModel(), tracedPair.getTypeOriginModel()))){
					
					for (MyNode tempNode : newModel.getAllNodes()) {
						if (Methods.findGlobalID(tempNode.getNode(),
								tempNode.getNodeType().getType())
								.equals(Methods.findGlobalID(
										pairs.getValue(),
										tempNodeOrigin.getType()))) {
							searchedFromNodeNew = tempNode;
						}
						if (Methods.findGlobalID(tempNode.getNode(),
								tempNode.getNodeType().getType())
								.equals(Methods.findGlobalID(tracedPair.getNodeNewModel(),
										tracedPair.getTypeNewModel()))) {
							searchedToNodeNew = tempNode;
						}
					}
					
					newModel.addNewEdgeGUI(searchedFromNodeNew, searchedToNodeNew, edgeType, true);
					
				}
			}
			
			
			
		}
		
		//Old tracelink method
//		for (MergedNodePair tracedPair : tracedNodes) {
//
//			
//
//			/**
//			 * searches for the nodes (in the new active model) which shall be
//			 * linked and adds new edges between them.
//			 */
//			for (MyNode actNode : newModel.getAllNodes()) {
//				if (Methods.findGlobalID(actNode.getNode(),
//						actNode.getNodeType().getType()).equals(
//						Methods.findGlobalID(tracedPair.getNodeOriginalModel(),
//								tracedPair.getTypeOriginModel()))) {
//					for (MyNode actNewNode : activeModel.getAllNodes()) {
//						if (Methods.findGlobalID(actNewNode.getNode(),
//								actNewNode.getNodeType().getType()).equals(
//								Methods.findGlobalID(
//										tracedPair.getNodeNewModel(),
//										tracedPair.getTypeNewModel()))) {
//							addNewEdgeGUI(actNode, actNewNode, edgeType, false);
//						}
//					}
//				}
//			}
//
//		}

	}

	private void transferOldEdges() {
		Iterator<Entry<NodeAndType, Node>> it = nodeTransferUnmatchedOldToNewModel
				.entrySet().iterator();
		NodeAndType tempNodeOrigin;
		Node tempNodeTransferred;

		// Iterate over all old nodes transferred to the new model
		while (it.hasNext()) {
			Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
					.next();
			tempNodeOrigin = pairs.getKey();
			tempNodeTransferred = pairs.getValue();

			checkForEdgesToTransfer(tempNodeTransferred,
					tempNodeOrigin.getType(), tempNodeOrigin.getNode(),
					tempNodeOrigin.getType());
		}
	}

	/**
	 * This method calls methods to check the incoming and outgoing edges of the
	 * node which is merged into the new model. So the sorrounding edges can be
	 * created in the new model.
	 * 
	 * @param nodeNew
	 *            the node in the new model to which the old edge shall link
	 * @param typeNodeNew
	 * @param nodeOrigin
	 *            the node of the origin model from which the old edge shall
	 *            link
	 * @param typeNodeOrigin
	 * @author Andreas
	 */
	private void checkForEdgesToTransfer(Node nodeNew,
			NodeTypeBase typeNodeNew, Node nodeOrigin,
			NodeTypeBase typeNodeOrigin) {

		checkIncomingEdges(nodeNew, typeNodeNew, nodeOrigin, typeNodeOrigin);
		checkOutgoingEdges(typeNodeOrigin, nodeOrigin, nodeNew, typeNodeNew);
	}

	/**
	 * This method transfers the destination of the incoming edges from the
	 * given old node to the given new node. So the old edge now links to the
	 * node of the new model. TODO
	 * 
	 * @param nodeNew
	 * @param nodeNewType
	 * @param nodeOrigin
	 * @param nodeTypeOrigin
	 * 
	 * @author Andreas
	 */
	private void checkIncomingEdges(Node nodeNew, NodeTypeBase nodeNewType,
			Node nodeOrigin, NodeTypeBase nodeTypeOrigin) {

		Node tempFromEdgeNode;
		NodeTypeBase tempFromEdgeNodeType;

		MyNode searchedFromNodeNew = null, searchedToNodeNew = null;

		NodeAndType tempNodeOrigin;

		MyEdgeType edgeTypeNew;

		for (EdgeType edgeType : metaModelNew.getEdgeTypes()) {
			for (ConnectionMapping incomingMapping : edgeType
					.getIncomingMappings(nodeTypeOrigin)) {
				for (Edge incomingEdge : incomingMapping
						.applyIncoming(nodeOrigin)) {

					tempFromEdgeNode = incomingMapping.applyFrom(incomingEdge);
					tempFromEdgeNodeType = incomingMapping.getFrom();

					Iterator<Entry<NodeAndType, Node>> it = nodeTransferUnmatchedOldToNewModel
							.entrySet().iterator();

					// look up whether the from node of the old edge has also
					// been transfered to the new model. If this is the case the
					// edge is created between the two nodes in the new model
					while (it.hasNext()) {
						Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
								.next();
						tempNodeOrigin = pairs.getKey();

						if (Methods.findGlobalID(tempFromEdgeNode,
								tempFromEdgeNodeType).equals(
								Methods.findGlobalID(tempNodeOrigin.getNode(),
										tempNodeOrigin.getType()))) {

							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												pairs.getValue(),
												tempNodeOrigin.getType()))) {
									searchedFromNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedToNodeNew = tempNode;
								}
							}

							// edgeTypeNew = new
							// MyEdgeType(newModel.getMetamodel().getEdgeType(edgeType.getName()).getOne(),
							// 8);
							//
							// Edge newEdge = newModel.addNewEdge(
							// searchedFromNodeNew, searchedToNodeNew,
							// edgeTypeNew,incomingMapping, true);
							//
							// transferEdgeAttributes(incomingEdge, newEdge,
							// edgeTypeNew.getType());

							Edge newEdge = incomingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							transferEdgeAttributes(incomingEdge, newEdge,
									edgeType);

							break;
						}
					}

					for (MergedNodePair actMerged : mergedNodes) {
						if (Methods.findGlobalID(tempFromEdgeNode,
								tempFromEdgeNodeType).equals(
								Methods.findGlobalID(
										actMerged.getNodeOriginalModel(),
										actMerged.getTypeOriginModel()))) {

							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												actMerged.getNodeNewModel(),
												actMerged.getTypeNewModel()))) {
									searchedFromNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedToNodeNew = tempNode;
								}
							}

							// edgeTypeNew = new
							// MyEdgeType(newModel.getMetamodel().getEdgeType(edgeType.getName()).getOne(),
							// 8);
							//
							// Edge newEdge = newModel.addNewEdge(
							// searchedFromNodeNew, searchedToNodeNew,
							// edgeTypeNew,incomingMapping, true);
							//
							// transferEdgeAttributes(incomingEdge, newEdge,
							// edgeTypeNew.getType());

							Edge newEdge = incomingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							transferEdgeAttributes(incomingEdge, newEdge,
									edgeType);

							break;
						}
					}

				}
			}
		}
	}

	/**
	 * This method transfers the origin of the outgoing edges from the given old
	 * node to the given new node. So the old edge now links to the node of the
	 * new model.
	 * 
	 * 
	 * TODO
	 * 
	 * @param nodeTypeOrigin
	 * @param nodeOrigin
	 * @param nodeNew
	 * @param nodeNewType
	 * @author Andreas
	 */
	private void checkOutgoingEdges(NodeTypeBase nodeTypeOrigin,
			Node nodeOrigin, Node nodeNew, NodeTypeBase nodeNewType) {

		Node tempToEdgeNode;
		NodeTypeBase tempToEdgeNodeType;

		MyNode searchedFromNodeNew = null, searchedToNodeNew = null;

		NodeAndType tempNodeOrigin;

		MyEdgeType edgeTypeNew;

		for (EdgeType edgeType : metaModelNew.getEdgeTypes()) {
			for (ConnectionMapping outgoingMapping : edgeType
					.getOutgoingMappings(nodeTypeOrigin)) {
				for (Edge outgoingEdge : outgoingMapping
						.applyOutgoing(nodeOrigin)) {

					tempToEdgeNode = outgoingMapping.applyTo(outgoingEdge);
					tempToEdgeNodeType = outgoingMapping.getTo();

					Iterator<Entry<NodeAndType, Node>> it = nodeTransferUnmatchedOldToNewModel
							.entrySet().iterator();

					// look up whether the from node of the old edge has also
					// been transfered to the new model. If this is the case the
					// edge is created between the two nodes in the new model
					while (it.hasNext()) {
						Map.Entry<NodeAndType, Node> pairs = (Entry<NodeAndType, Node>) it
								.next();
						tempNodeOrigin = pairs.getKey();

						if (Methods.findGlobalID(tempToEdgeNode,
								tempToEdgeNodeType).equals(
								Methods.findGlobalID(tempNodeOrigin.getNode(),
										tempNodeOrigin.getType()))) {

							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												pairs.getValue(),
												tempNodeOrigin.getType()))) {
									searchedToNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedFromNodeNew = tempNode;
								}
							}

							// edgeTypeNew = new
							// MyEdgeType(newModel.getMetamodel().getEdgeType(edgeType.getName()).getOne(),
							// 8);
							//
							// Edge newEdge = newModel.addNewEdge(
							// searchedFromNodeNew, searchedToNodeNew,
							// edgeTypeNew,outgoingMapping, true);
							//
							// transferEdgeAttributes(outgoingEdge, newEdge,
							// edgeTypeNew.getType());

							Edge newEdge = outgoingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							transferEdgeAttributes(outgoingEdge, newEdge,
									edgeType);

							break;
						}
					}

					for (MergedNodePair actMerged : mergedNodes) {
						if (Methods.findGlobalID(tempToEdgeNode,
								tempToEdgeNodeType).equals(
								Methods.findGlobalID(
										actMerged.getNodeOriginalModel(),
										actMerged.getTypeOriginModel()))) {

							for (MyNode tempNode : newModel.getAllNodes()) {
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(
												actMerged.getNodeNewModel(),
												actMerged.getTypeNewModel()))) {
									searchedToNodeNew = tempNode;
								}
								if (Methods.findGlobalID(tempNode.getNode(),
										tempNode.getNodeType().getType())
										.equals(Methods.findGlobalID(nodeNew,
												nodeNewType))) {
									searchedFromNodeNew = tempNode;
								}
							}

							// edgeTypeNew = new
							// MyEdgeType(newModel.getMetamodel().getEdgeType(edgeType.getName()).getOne(),
							// 8);
							//
							// Edge newEdge = newModel.addNewEdge(
							// searchedFromNodeNew, searchedToNodeNew,
							// edgeTypeNew,outgoingMapping, true);
							//
							// transferEdgeAttributes(outgoingEdge, newEdge,
							// edgeTypeNew.getType());

							Edge newEdge = outgoingMapping.create(modelNew,
									searchedFromNodeNew.getNode(),
									searchedToNodeNew.getNode());

							transferEdgeAttributes(outgoingEdge, newEdge,
									edgeType);

							break;
						}
					}

				}
			}
		}
	}

	private void printNbEdges(Model model) {
		int counter = 0;
		for (EdgeType t : metaModelOrigin.getEdgeTypes()) {
			PSSIFOption<ConnectionMapping> tmp = t.getMappings();

			if (tmp != null && (tmp.isMany() || tmp.isOne())) {
				Set<ConnectionMapping> mappings;

				if (tmp.isMany())
					mappings = tmp.getMany();
				else {
					mappings = new HashSet<ConnectionMapping>();
					mappings.add(tmp.getOne());
				}

				for (ConnectionMapping mapping : mappings) {
					PSSIFOption<Edge> edges = mapping.apply(model);

					if (edges.isMany()) {
						for (Edge e : edges.getMany()) {
							counter++;
						}
					} else {
						if (edges.isOne()) {
							counter++;
						}
					}
				}
			}
		}
		System.out.println("Nb edges :" + counter);
	}

	private void printNbNodes(Model model) {
		int counter = 0;
		for (NodeType t : metaModelOrigin.getNodeTypes()) {
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(model, true);

			if (tempNodes.isMany()) {
				Set<Node> many = tempNodes.getMany();
				for (Node n : many) {
					counter++;
				}
			} else if (tempNodes.isOne()) {
				counter++;
			}

		}
		System.out.println("Nb nodes :" + counter);
	}

	/**
	 * add all the Nodes from modelNew to model1
	 */
	private void addAllNodes() {
		// loop over all Node types
		for (NodeType t : metaModelOrigin.getNodeTypes()) {
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(modelNew, false);

			if (tempNodes.isMany()) {
				Set<Node> many = tempNodes.getMany();
				for (Node n : many) {
					// copy it to model1
					addNode(n, t);
				}
			} else {
				if (tempNodes.isOne()) {
					Node current = tempNodes.getOne();
					// copy it to model1
					addNode(current, t);
				}
			}
		}
	}

	/**
	 * add all the JunctionNodes from modelNew to model1
	 */
	private void addAllJunctionNodes() {
		// loop over all JunctionNode types
		for (JunctionNodeType t : metaModelOrigin.getJunctionNodeTypes()) {
			// get all the Nodes of this type
			PSSIFOption<Node> tempNodes = t.apply(modelNew, false);

			if (tempNodes.isMany()) {
				Set<Node> many = tempNodes.getMany();
				for (Node n : many) {
					// copy it to model1
					addJunctionNode(n, t);
				}
			} else {
				if (tempNodes.isOne()) {
					Node current = tempNodes.getOne();
					// copy it to model1
					addJunctionNode(current, t);
				}
			}
		}
	}

	/**
	 * Add all the Edges from modelNew to model1
	 */
	private void addAllEdges() {
		for (EdgeType t : metaModelOrigin.getEdgeTypes()) {
			PSSIFOption<ConnectionMapping> tmp = t.getMappings();

			if (tmp != null && (tmp.isMany() || tmp.isOne())) {
				Set<ConnectionMapping> mappings;

				if (tmp.isMany())
					mappings = tmp.getMany();
				else {
					mappings = new HashSet<ConnectionMapping>();
					mappings.add(tmp.getOne());
				}

				for (ConnectionMapping mapping : mappings) {
					PSSIFOption<Edge> edges = mapping.apply(modelNew);
					if (edges.isMany()) {
						for (Edge e : edges.getMany()) {
							Node source = mapping.applyFrom(e);
							Node target = mapping.applyTo(e);

							Edge newEdge = mapping.create(modelOrigin,
									oldToNewNodes.get(source),
									oldToNewNodes.get(target));
							transferEdgeAttributes(e, newEdge, t);
						}
					} else if (edges.isOne()) {
						Edge e = edges.getOne();
						Node source = mapping.applyFrom(e);
						Node target = mapping.applyTo(e);

						Edge newEdge = mapping.create(modelOrigin,
								oldToNewNodes.get(source),
								oldToNewNodes.get(target));
						transferEdgeAttributes(e, newEdge, t);
					}
				}
			}
		}
	}

	/**
	 * Add a given Node to Model1
	 * 
	 * @param dataNode
	 *            the model which should be transfered to model1
	 * @param currentType
	 *            the type of the dataNode
	 */
	private void addNode(Node dataNode, NodeType currentType) {
		// create Node
		Node newNode = currentType.create(modelOrigin);

		oldToNewNodes.put(dataNode, newNode);

		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = currentType
				.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);

					if (attrvalue != null) {
						currentType.getAttribute(a.getName()).getOne()
								.set(newNode, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newNode.annotate(a.getKey(), a.getValue());
			}
		}

		/*
		 * Collection<Annotation> annotations =
		 * currentType.getAnnotations(dataNode);
		 * 
		 * if (annotations!=null) { for (Annotation a : annotations) {
		 * PSSIFOption<String> value = a.getValue(); if (value!=null &&
		 * value.isOne()) { currentType.setAnnotation(newNode,
		 * a.getKey(),value.getOne()); }
		 * 
		 * if (value!=null && value.isMany()) { Set<String> concreteValues =
		 * value.getMany(); for (String s : concreteValues) {
		 * currentType.setAnnotation(newNode, a.getKey(),s); } } } }
		 */
	}

	private Node addNodeToNewModel(Node dataNode, NodeTypeBase nodeTypeBase) {
		Model modelNew = newModel.getModel();

		// create Node
		Node newNode = nodeTypeBase.create(modelNew);

		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = nodeTypeBase
				.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);

					if (attrvalue != null) {
						nodeTypeBase.getAttribute(a.getName()).getOne()
								.set(newNode, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newNode.annotate(a.getKey(), a.getValue());
			}
		}
		// TODO Performance improvement: don't create a new mymodel container
		// each time you copy a node from an old to a new model
		this.newModel = new MyModelContainer(modelNew, newModel.getMetamodel());

		return newNode;
	}

	/**
	 * Add a given JunctionNode to Model1
	 * 
	 * @param dataNode
	 *            the model which should be transfered to model1
	 * @param currentType
	 *            the type of the dataNode
	 */
	private void addJunctionNode(Node dataNode, JunctionNodeType currentType) {
		// create Node
		Node newNode = currentType.create(modelOrigin);

		oldToNewNodes.put(dataNode, newNode);

		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = currentType
				.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(dataNode);

					if (attrvalue != null) {
						currentType.getAttribute(a.getName()).getOne()
								.set(newNode, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = dataNode.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newNode.annotate(a.getKey(), a.getValue());
			}
		}
	}

	/**
	 * transfer all the attributes and annotations from one Edge to the other
	 * 
	 * @param oldEdge
	 *            contains all the information which should be transfered
	 * @param newEdge
	 *            the edge which should get all the information
	 * @param type
	 *            the type of both edges
	 */
	private void transferEdgeAttributes(Edge oldEdge, Edge newEdge,
			EdgeType type) {
		// transfer attribute groups
		Collection<AttributeGroup> attrgroups = type.getAttributeGroups();

		if (attrgroups != null) {
			for (AttributeGroup ag : attrgroups) {
				// transfer attribute values
				Collection<Attribute> attr = ag.getAttributes();

				for (Attribute a : attr) {
					PSSIFOption<PSSIFValue> attrvalue = a.get(oldEdge);

					if (attrvalue != null) {
						type.getAttribute(a.getName()).getOne()
								.set(newEdge, attrvalue);
					}
				}
			}
		}

		// transfer annotations

		PSSIFOption<Entry<String, String>> tmp = oldEdge.getAnnotations();

		Set<Entry<String, String>> annotations = null;

		if (tmp != null && (tmp.isMany() || tmp.isOne())) {
			if (tmp.isMany())
				annotations = tmp.getMany();
			else {
				annotations = new HashSet<Entry<String, String>>();
				annotations.add(tmp.getOne());
			}
		}

		if (annotations != null) {
			for (Entry<String, String> a : annotations) {
				newEdge.annotate(a.getKey(), a.getValue());
			}
		}

		/*
		 * Collection<Annotation> annotations = type.getAnnotations(oldEdge);
		 * 
		 * if (annotations!=null) { for (Annotation a : annotations) {
		 * PSSIFOption<String> value = a.getValue(); if (value!=null &&
		 * value.isOne()) { type.setAnnotation(newEdge,
		 * a.getKey(),value.getOne()); }
		 * 
		 * if (value!=null && value.isMany()) { Set<String> concreteValues =
		 * value.getMany(); for (String s : concreteValues) {
		 * type.setAnnotation(newEdge, a.getKey(),s); } } } }
		 */
	}
}
