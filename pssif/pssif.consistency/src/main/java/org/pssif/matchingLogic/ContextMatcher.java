package org.pssif.matchingLogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pssif.comparedDataStructures.ComparedNodePair;
import org.pssif.consistencyDataStructures.ConsistencyData;
import org.pssif.consistencyDataStructures.NodeAndType;
import org.pssif.consistencyDataStructures.Token;
import org.pssif.mainProcesses.Methods;
import org.pssif.textNormalization.Normalizer;

import de.tum.pssif.core.common.PSSIFConstants;
import de.tum.pssif.core.common.PSSIFOption;
import de.tum.pssif.core.metamodel.EdgeType;
import de.tum.pssif.core.metamodel.JunctionNodeType;
import de.tum.pssif.core.metamodel.Metamodel;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.metamodel.NodeTypeBase;
import de.tum.pssif.core.metamodel.PSSIFCanonicMetamodelCreator;
import de.tum.pssif.core.metamodel.impl.ReadFromNodesOperation;
import de.tum.pssif.core.metamodel.impl.ReadToNodesOperation;
import de.tum.pssif.core.model.Edge;
import de.tum.pssif.core.model.Model;
import de.tum.pssif.core.model.Node;

/**
 * This class represents the implementation of the context matching metric. It
 * compares the nodes sorrounding the two given ones with the chosen metrics.
 * 
 * @author Andreas
 * 
 */
public class ContextMatcher extends MatchMethod {

	/**
	 * These are the subclasses of PSIFFDevArtifacts that are checked for
	 * consistency
	 */
	private final String[] PSIFFDevArtifactSubClasses = {
			PSSIFCanonicMetamodelCreator.N_FUNCTIONALITY,
			PSSIFCanonicMetamodelCreator.N_REQUIREMENT,
			PSSIFCanonicMetamodelCreator.N_USE_CASE,
			PSSIFCanonicMetamodelCreator.N_TEST_CASE,
			PSSIFCanonicMetamodelCreator.N_VIEW,
			PSSIFCanonicMetamodelCreator.N_EVENT,
			PSSIFCanonicMetamodelCreator.N_ISSUE,
			PSSIFCanonicMetamodelCreator.N_DECISION,
			PSSIFCanonicMetamodelCreator.N_CHANGE_EVENT };

	/**
	 * These are the subclasses of PSIFFSolArtifacts that are checked for
	 * consistency
	 */
	private final String[] PSIFFSolArtifactSubClasses = {
			PSSIFCanonicMetamodelCreator.N_BLOCK,
			PSSIFCanonicMetamodelCreator.N_FUNCTION,
			PSSIFCanonicMetamodelCreator.N_ACTIVITY,
			PSSIFCanonicMetamodelCreator.N_STATE,
			PSSIFCanonicMetamodelCreator.N_ACTOR,
			PSSIFCanonicMetamodelCreator.N_SERVICE,
			PSSIFCanonicMetamodelCreator.N_SOFTWARE,
			PSSIFCanonicMetamodelCreator.N_HARDWARE,
			PSSIFCanonicMetamodelCreator.N_MECHANIC,
			PSSIFCanonicMetamodelCreator.N_ELECTRONIC,
			PSSIFCanonicMetamodelCreator.N_MODULE };

	private static final int depth = 1;

	private List<MatchMethod> matchMethods;
	private ConsistencyData consistencyData;
	private Normalizer normalizer;

	private Model originalModel;
	private Model newModel;
	private Metamodel metaModel;

	private ComparedNodePair tempNodePair;

	private Set<Edge> edgesOriginIncoming;
	private Set<Edge> edgesOriginOutgoing;

	private Set<Edge> edgesNewIncoming;
	private Set<Edge> edgesNewOutgoing;

	private List<NodeAndType> sorroundingNodesOrigin;

	private List<NodeAndType> sorroundingNodesNew;

	public ContextMatcher(MatchingMethods matchMethod, boolean isActive,
			double weigth) {
		super(matchMethod, isActive, weigth);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param matchMethods
	 *            the matchMethods to set
	 */
	public void setMatchMethods(List<MatchMethod> matchMethods) {
		this.matchMethods = matchMethods;
	}

	/**
	 * @param consistencyData
	 *            the consistencyData to set
	 */
	public void setConsistencyData(ConsistencyData consistencyData) {
		this.consistencyData = consistencyData;
	}

	/**
	 * @param normalizer
	 *            the normalizer to set
	 */
	public void setNormalizer(Normalizer normalizer) {
		this.normalizer = normalizer;
	}

	/**
	 * this method iterates over every node type of the pssif metamodel and
	 * calls the method findSorroundingNodes() with the current type. This
	 * ensures that all nodes are compared with all from/to nodes of the edges.
	 * 
	 * It starts with the children of the Type DevelopmentArtifact and then
	 * continues with the children of the Type Solution Artifact. After that the
	 * Development and Solution Artifact Type and finally the Root Node Type are
	 * iterated. At the end the type of conjunctions is given to the method
	 * findSorroundingNodes(). Then the neighbours of the conjunction are added
	 * to the node list if the conjunction is a sorrounding node.
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original model which sorrounding nodes shall
	 *            be found
	 * @param tempNodeNew
	 *            the node from the new model which sorrounding nodes shall be
	 *            found
	 */
	public void typeIteration(Node tempNodeOrigin, Node tempNodeNew) {

		for (int i = 0; i < PSIFFDevArtifactSubClasses.length; i++) {
			findSorroundingNodes(PSIFFDevArtifactSubClasses[i], tempNodeOrigin,
					tempNodeNew);
		}

		for (int i = 0; i < PSIFFSolArtifactSubClasses.length; i++) {
			findSorroundingNodes(PSIFFSolArtifactSubClasses[i], tempNodeOrigin,
					tempNodeNew);
		}

		findSorroundingNodes(PSSIFCanonicMetamodelCreator.N_DEV_ARTIFACT,
				tempNodeOrigin, tempNodeNew);
		findSorroundingNodes(PSSIFCanonicMetamodelCreator.N_SOL_ARTIFACT,
				tempNodeOrigin, tempNodeNew);
		findSorroundingNodes(PSSIFConstants.ROOT_NODE_TYPE_NAME,
				tempNodeOrigin, tempNodeNew);

//		findSorroundingNodes(PSSIFCanonicMetamodelCreator.N_CONJUNCTION,
//				tempNodeOrigin, tempNodeNew);
	}

	@Override
	public double executeMatching(Node tempNodeOrigin, Node tempNodeNew,
			Model originalModel, Model newModel, Metamodel metaModel,
			NodeType actTypeOriginModel, NodeType actTypeNewModel,
			String labelOrigin, String labelNew, List<Token> tokensOrigin,
			List<Token> tokensNew) {

		this.originalModel = originalModel;
		this.newModel = newModel;
		this.metaModel = metaModel;

		tempNodePair = null;

		sorroundingNodesOrigin = new LinkedList<NodeAndType>();
		sorroundingNodesNew = new LinkedList<NodeAndType>();

		edgesOriginIncoming = new HashSet<Edge>();
		edgesOriginOutgoing = new HashSet<Edge>();
		edgesNewIncoming = new HashSet<Edge>();
		edgesNewOutgoing = new HashSet<Edge>();

		Collection<EdgeType> edgeTypes = metaModel.getEdgeTypes();

		/**
		 * getting all incoming/outgoing edges of the two nodes
		 */
		for (EdgeType actEdgeTyp : edgeTypes) {
			PSSIFOption<Edge> edgesOriginOptionIncoming = actEdgeTyp
					.applyIncoming(tempNodeOrigin, false);
			PSSIFOption<Edge> edgesOriginOptionOutgoing = actEdgeTyp
					.applyOutgoing(tempNodeOrigin, false);

			PSSIFOption<Edge> edgesNewOptionIncoming = actEdgeTyp
					.applyIncoming(tempNodeNew, false);
			PSSIFOption<Edge> edgesNewOptionOutgoing = actEdgeTyp
					.applyOutgoing(tempNodeNew, false);

			initializeEdgeSets(edgesOriginOptionIncoming,
					edgesOriginOptionOutgoing, edgesNewOptionIncoming,
					edgesNewOptionOutgoing);

		}

		/**
		 * retrieving all from/to nodes connected with the two nodes
		 */
		typeIteration(tempNodeOrigin, tempNodeNew);

		return compareSorroundingNodes(tempNodeOrigin, actTypeOriginModel,
				tempNodeNew, actTypeNewModel, labelOrigin, labelNew);
	}

	/**
	 * This method iterates over all found nodes of the two models and compares
	 * them based on the set matching methods. After every iteration the
	 * similarity value is added to the result.
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param actTypeOriginModel
	 * @param tempNodeNew
	 *            the node from the new model
	 * @param actTypeNewModel
	 * @param labelOrigin
	 * @param labelNew
	 * @return
	 */
	private double compareSorroundingNodes(Node tempNodeOrigin,
			NodeType actTypeOriginModel, Node tempNodeNew,
			NodeType actTypeNewModel, String labelOrigin, String labelNew) {
		double similaritySum = 0;
		double result = 0;

		for (NodeAndType tempNodeSorroundingOrigin : sorroundingNodesOrigin) {
			for (NodeAndType tempNodeSorroundingNew : sorroundingNodesNew) {
				if (nodesAlreadyCompared(tempNodeSorroundingOrigin.getNode(),
						tempNodeSorroundingNew.getNode())) {
					similaritySum += calculateWeightedSimilarities();
				} else {
					similaritySum += computeSimilarity(
							tempNodeSorroundingOrigin.getNode(),
							tempNodeSorroundingOrigin.getType(),
							tempNodeSorroundingNew.getNode(),
							tempNodeSorroundingNew.getType());
				}
			}
		}
		result = (similaritySum / (Math.max(sorroundingNodesOrigin.size(),
				sorroundingNodesNew.size())));
		
		System.out.println("The node: " + labelOrigin +"(origin)" + " and the new node: " + labelNew + "have the contextSim: " + result);

		return result;
	}

	/**
	 * This method calculates the similarity of two nodes based on the given
	 * match methods. It work's similar as to the method in
	 * MatchingProcess.java.
	 * 
	 * @param tempNodeOrigin
	 *            the node from the original model
	 * @param actTypeOriginModel
	 *            the node type of the original node
	 * @param tempNodeNew
	 *            the node from the new model
	 * @param actTypeNewModel
	 *            the node type of the new node
	 * @return the similarity of two given nodes based on the chosen matching
	 *         methods
	 */
	private double computeSimilarity(Node tempNodeOrigin,
			NodeTypeBase actTypeOriginModelBase, Node tempNodeNew,
			NodeTypeBase actTypeNewModelBase) {

		double result = 0;
		double currentMetricResult = 0;

		NodeType actTypeOriginModel = (NodeType) actTypeOriginModelBase;
		NodeType actTypeNewModel = (NodeType) actTypeNewModelBase;

		/**
		 * here the strings of the old and the new node are read from the model
		 */
		String labelOriginNode = Methods.findName(actTypeOriginModel,
				tempNodeOrigin);
		String labelNewNode = Methods.findName(actTypeNewModel, tempNodeNew);

		System.out.println("Origin: " + labelOriginNode + " New: "
				+ labelNewNode);

		String labelOriginNodeNormalized, labelNewNodeNormalized;

		/**
		 * variables to store the different normalized tokens
		 */
		List<Token> tokensOriginNodeNormalized = normalizer
				.createNormalizedTokensFromLabel(labelOriginNode, true, true,
						true, true);
		List<Token> tokensNewNodeNormalized = normalizer
				.createNormalizedTokensFromLabel(labelNewNode, true, true,
						true, true);
		List<Token> tokensOriginNodeNormalizedCompundedUnstemmed = normalizer
				.createNormalizedTokensFromLabel(labelOriginNode, true, true,
						false, false);
		List<Token> tokensNewNodeNormalizedCompundedUnstemmed = normalizer
				.createNormalizedTokensFromLabel(labelNewNode, true, true,
						false, false);

		labelOriginNodeNormalized = normalizer.normalizeLabel(labelOriginNode);

		labelNewNodeNormalized = normalizer.normalizeLabel(labelNewNode);

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		/**
		 * Here the tokenized forms are created if necessary. Thereby it's
		 * important to note that the syntactic metrics require a normalization
		 * without compund splitting and without stemming. Whereas the semantic
		 * metrics require full normalization.
		 * 
		 * Then every active match Method is applied to the two nodes here.
		 */
		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();
			currentMetricResult = 0;

			/**
			 * Depending on the current match method different token sets of the
			 * labels have to be given to the match method. If the method is
			 * StringEditDistance or Hyphen we don't use token normalization
			 * with compoundsplitting or stemming. Thatfore we have to separate
			 * these two cases.
			 */
			if (currentMethod.isActive()) {
				if (currentMethod.getMatchMethod() == MatchingMethods.CONTEXT_MATCHING) {
					break;
				}
				if ((currentMethod.getMatchMethod() == MatchingMethods.STRING_EDIT_DISTANCE_MATCHING)
						|| (currentMethod.getMatchMethod() == MatchingMethods.HYPHEN_MATCHING)) {

					currentMetricResult = currentMethod.executeMatching(
							tempNodeOrigin, tempNodeNew, originalModel,
							newModel, metaModel, actTypeOriginModel,
							actTypeNewModel, labelOriginNodeNormalized,
							labelNewNodeNormalized,
							tokensOriginNodeNormalizedCompundedUnstemmed,
							tokensNewNodeNormalizedCompundedUnstemmed);
				} else {

					currentMetricResult = currentMethod.executeMatching(
							tempNodeOrigin, tempNodeNew, originalModel,
							newModel, metaModel, actTypeOriginModel,
							actTypeNewModel, labelOriginNodeNormalized,
							labelNewNodeNormalized, tokensOriginNodeNormalized,
							tokensNewNodeNormalized);
				}
				result += currentMetricResult * currentMethod.getWeigth();
			}
		}

		return result;
	}

	/**
	 * @return the result of similarity analysis from past matches if two nodes
	 *         have already been compared.
	 */
	private double calculateWeightedSimilarities() {
		double result = 0;

		result += getWeightedSyntacticSimilarity();
		result += getWeightedSemanticSimilarity();

		return result;
	}

	/**
	 * This method get's all nodes from the given type of the new and the
	 * original model. It then calls a method to compare if the found nodes are
	 * relevant for the found edges. (the found Nodes are compared with to/from
	 * of the edges)
	 * 
	 * @param type
	 *            the type of which nodes are searched
	 * @param nodeOrigin
	 *            the node of the original model which sorrounding nodes shall
	 *            be found
	 * @param nodeNew
	 *            the node of the new model which sorrounding nodes shall be
	 *            found
	 */
	private void findSorroundingNodes(String type, Node nodeOrigin, Node nodeNew) {
		/**
		 * find all nodes sorrounding the original node here
		 */
		NodeTypeBase actType;

		PSSIFOption<Node> actNodesOriginalModel = null;

		if (metaModel.getNodeType(type).isOne()) {
			actType = metaModel.getNodeType(type).getOne();
		} else {
			actType = metaModel.getJunctionNodeType(type).getOne();
		}

		actNodesOriginalModel = actType.apply(originalModel, false);

		if (nodeOrigin != null) {
			if (actNodesOriginalModel.isNone()) {
			} else {
				if (actNodesOriginalModel.isOne()) {
					Node tempNodeOrigin = actNodesOriginalModel.getOne();

					checkFoundNodeAgainstNodeOrigin(tempNodeOrigin, actType,
							nodeOrigin);

				} else {

					Set<Node> tempNodesOrigin = actNodesOriginalModel.getMany();

					Iterator<Node> tempNodeOrigin = tempNodesOrigin.iterator();

					while (tempNodeOrigin.hasNext()) {

						checkFoundNodeAgainstNodeOrigin(tempNodeOrigin.next(),
								actType, nodeOrigin);
					}
				}
			}
		}

		if (nodeNew != null) {
			PSSIFOption<Node> actNodesNewModel = actType.apply(newModel, false);

			if (actNodesNewModel.isNone()) {
			} else {
				if (actNodesNewModel.isOne()) {
					Node tempNodeNew = actNodesNewModel.getOne();

					checkFoundNodeAgainstNodeNew(tempNodeNew, actType, nodeNew);

				} else {

					Set<Node> tempNodesNew = actNodesNewModel.getMany();

					Iterator<Node> tempNodeNew = tempNodesNew.iterator();

					while (tempNodeNew.hasNext()) {

						checkFoundNodeAgainstNodeNew(tempNodeNew.next(),
								actType, nodeNew);
					}
				}
			}
		}
	}

	private void addEdgesIncomingToSet(Node tempNode, Set<Edge> setToAddTo) {
		Collection<EdgeType> edgeTypes = metaModel.getEdgeTypes();

		for (EdgeType actType : edgeTypes) {
			Set<Edge> result = new HashSet<Edge>();
			findEdges(actType.applyIncoming(tempNode, true), result);
			if(result != null){
				setToAddTo.addAll(result);
			}
		}
	}

	private void addEdgesOutgoingToSet(Node tempNode, Set<Edge> setToAddTo) {
		Collection<EdgeType> edgeTypes = metaModel.getEdgeTypes();

		for (EdgeType actType : edgeTypes) {
			Set<Edge> result =  new HashSet<Edge>();
			findEdges(actType.applyOutgoing(tempNode, true), result);
			if(result != null){
				setToAddTo.addAll(result);
			}
		}
	}

	/**
	 * This method compares the given node with the from/to nodes of every
	 * incoming/outgoing edge. If a match is found, the node and its type are
	 * added to the relevant nodes. If the given node is of type concjunction
	 * it's sorrounding nodes are looked up until they are no conjunctions but
	 * nodes.
	 * 
	 * @param tempNodeOrigin
	 *            the given node to compare with to/from of edges
	 * @param actNodeType
	 *            the type of the given node
	 * @param nodeOrigin
	 *            the node for which sorrounding nodes shall be found
	 */
	private void checkFoundNodeAgainstNodeOrigin(Node tempNodeOrigin,
			NodeTypeBase actNodeType, Node nodeOrigin) {
		Node tempFrom, tempTo;
		JunctionNodeType junctionNodeType = metaModel.getJunctionNodeType(
				PSSIFCanonicMetamodelCreator.N_CONJUNCTION).getOne();

		if (tempNodeOrigin.equals(nodeOrigin)) {
			// TODO does this prevent circular behavior?
		} else {

			for (Edge edgeIn : edgesOriginIncoming) {
				tempFrom = edgeIn.apply(new ReadFromNodesOperation());
				tempTo = edgeIn.apply(new ReadToNodesOperation());

				if (junctionNodeType.equals(actNodeType)) {
					addEdgesIncomingToSet(tempNodeOrigin, edgesOriginIncoming);
					typeIteration(tempNodeOrigin, null);
				} else {

					if (tempTo.equals(nodeOrigin)) {

						if (tempFrom.equals(tempNodeOrigin)) {
							sorroundingNodesOrigin.add(new NodeAndType(
									tempNodeOrigin, actNodeType));
						}
					}
				}
			}

			for (Edge edgeOut : edgesOriginOutgoing) {
				tempFrom = edgeOut.apply(new ReadFromNodesOperation());
				tempTo = edgeOut.apply(new ReadToNodesOperation());

				if (junctionNodeType.equals(actNodeType)) {
					addEdgesIncomingToSet(tempNodeOrigin, edgesOriginOutgoing);
					typeIteration(tempNodeOrigin, null);
				} else {

					if (tempFrom.equals(nodeOrigin)) {

						if (tempTo.equals(tempNodeOrigin)) {
							sorroundingNodesOrigin.add(new NodeAndType(
									tempNodeOrigin, actNodeType));
						}
					}
				}
			}
		}
	}

	/**
	 * This method compares the given node with the from/to nodes of every
	 * incoming/outgoing edge. If a match is found, the node and its type are
	 * added to the relevant nodes. If the given node is of type concjunction
	 * it's sorrounding nodes are looked up until they are no conjunctions but
	 * nodes.
	 * 
	 * @param tempNodeNew
	 *            the given node to compare with to/from of edges
	 * @param actType
	 *            the type of the given node
	 * @param nodeNew
	 *            the node for which sorrounding nodes shall be found
	 */
	private void checkFoundNodeAgainstNodeNew(Node tempNodeNew,
			NodeTypeBase actType, Node nodeNew) {

		Node tempFrom, tempTo;
		JunctionNodeType junctionNodeType = metaModel.getJunctionNodeType(
				PSSIFCanonicMetamodelCreator.N_CONJUNCTION).getOne();

		if (tempNodeNew.equals(nodeNew)) {
			// TODO does this prevent circular behavior?
		} else {

			for (Edge edgeIn : edgesNewIncoming) {
				tempFrom = edgeIn.apply(new ReadFromNodesOperation());
				tempTo = edgeIn.apply(new ReadToNodesOperation());

				if (junctionNodeType.equals(actType)) {
					addEdgesIncomingToSet(tempNodeNew, edgesNewIncoming);
					typeIteration(null, tempNodeNew);
				} else {
					if (tempTo.equals(nodeNew)) {

						if (tempFrom.equals(tempNodeNew)) {
							sorroundingNodesNew.add(new NodeAndType(
									tempNodeNew, actType));
						}
					}
				}
			}

			for (Edge edgeOut : edgesNewOutgoing) {
				tempFrom = edgeOut.apply(new ReadFromNodesOperation());
				tempTo = edgeOut.apply(new ReadToNodesOperation());

				if (junctionNodeType.equals(actType)) {
					addEdgesIncomingToSet(tempNodeNew, edgesNewOutgoing);
					typeIteration(null, tempNodeNew);
				} else {
					if (tempFrom.equals(nodeNew)) {

						if (tempTo.equals(tempNodeNew)) {
							sorroundingNodesNew.add(new NodeAndType(
									tempNodeNew, actType));
						}
					}
				}
			}
		}
	}

	/**
	 * @param edgesOriginOptionIncoming
	 * @param edgesOriginOptionOutgoing
	 * @param edgesNewOptionIncoming
	 * @param edgesNewOptionOutgoing
	 */
	private void initializeEdgeSets(
			PSSIFOption<Edge> edgesOriginOptionIncoming,
			PSSIFOption<Edge> edgesOriginOptionOutgoing,
			PSSIFOption<Edge> edgesNewOptionIncoming,
			PSSIFOption<Edge> edgesNewOptionOutgoing) {
		findEdges(edgesOriginOptionIncoming, edgesOriginIncoming);
		findEdges(edgesOriginOptionOutgoing, edgesOriginOutgoing);
		findEdges(edgesNewOptionIncoming, edgesNewIncoming);
		findEdges(edgesNewOptionOutgoing, edgesNewOutgoing);
	}

	/**
	 * This method get's real edges from a PSSIFOption.
	 * 
	 * @param edgesOriginOption
	 *            option to convert
	 * @param edgesNewOption
	 *            option to convert
	 * @param edgesOrigin
	 *            saving place
	 * @param edgesNew
	 *            saving place
	 */
	private void findEdges(PSSIFOption<Edge> edgesOption, Set<Edge> edges) {
		if (edgesOption.isNone()) {
			edges = null;
		} else {
			if (edgesOption.isOne()) {
				edges.add(edgesOption.getOne());
			} else {
				edges.addAll(edgesOption.getMany());
			}
		}
	}

	/**
	 * This method looks up whether two nodes have already been compared. If yes
	 * the result is stored in the variable tempNodePair. It can then be used by
	 * the context matcher to look up results from past compairsons.
	 * 
	 * @param tempNodeOrigin
	 * @param tempNodeNew
	 * @return
	 */
	private boolean nodesAlreadyCompared(Node tempNodeOrigin, Node tempNodeNew) {

		this.tempNodePair = consistencyData.getNodeMatch(tempNodeOrigin,
				tempNodeNew);

		return (this.tempNodePair != null);
	}

	/**
	 * @return The weighted combination of all results of the syntactic match
	 *         methods.
	 */
	public double getWeightedSyntacticSimilarity() {
		double result = 0;

		double exactMatch = tempNodePair.getLabelComparison()
				.getExactMatchResult();
		double depthMatch = tempNodePair.getDepthMatchResult();
		double stringEditDistanceMatch = tempNodePair.getTokensComparison()
				.getStringEditDistanceResult();
		double hyphenMatch = tempNodePair.getTokensComparison()
				.getHyphenMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case EXACT_STRING_MATCHING:
				result += currentMethod.getWeigth() * exactMatch;
				break;
			case DEPTH_MATCHING:
				result += currentMethod.getWeigth() * depthMatch;
				break;
			case STRING_EDIT_DISTANCE_MATCHING:
				result += currentMethod.getWeigth() * stringEditDistanceMatch;
				break;
			case HYPHEN_MATCHING:
				result += currentMethod.getWeigth() * hyphenMatch;
				break;
			default:
				;
			}
		}
		return result;
	}

	/**
	 * @return The weighted combination of all results of the semantic match
	 *         methods.
	 */
	public double getWeightedSemanticSimilarity() {
		double result = 0;

		double linguisticMatch = tempNodePair.getTokensComparison()
				.getLinguisticMatchResult();
		double vsmMatch = tempNodePair.getTokensComparison()
				.getVsmMatchResult();
		double lsiMatch = tempNodePair.getTokensComparison()
				.getLsiMatchResult();
		double attributeMatch = tempNodePair.getAttributeMatchResult();

		Iterator<MatchMethod> currentMatchMethod = matchMethods.iterator();

		while (currentMatchMethod.hasNext()) {
			MatchMethod currentMethod = currentMatchMethod.next();

			switch (currentMethod.getMatchMethod()) {
			case LINGUISTIC_MATCHING:
				result += currentMethod.getWeigth() * linguisticMatch;
				break;
			case VECTOR_SPACE_MODEL_MATCHING:
				result += currentMethod.getWeigth() * vsmMatch;
				break;
			case LATENT_SEMANTIC_INDEXING_MATCHING:
				result += currentMethod.getWeigth() * lsiMatch;
				break;
			case ATTRIBUTE_MATCHING:
				result += currentMethod.getWeigth() * attributeMatch;
				break;
			default:
				;
			}
		}

		return result;
	}

}
