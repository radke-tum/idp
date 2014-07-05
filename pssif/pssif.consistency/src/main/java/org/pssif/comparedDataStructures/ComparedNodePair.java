package org.pssif.comparedDataStructures;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Node;

/**
 * 
 * this class stores two nodes. one from the first imported model and the
 * other one from the recent imported model. In addition to this this class
 * stores the match result of the label compairson of the two nodes, the
 * tokenization of the labels (plus the according metric results) and the result
 * of two node metrics (depth matching and contextual similarity)
 * 
 * @author Andreas
 * 
 */
public class ComparedNodePair extends Compared {

	private Node nodeOriginalModel, nodeNewModel;

	private ComparedLabelPair labelComparison;
	private ComparedNormalizedTokensPair tokensComparison;

	/**
	 * the result of the matching between two elements based on their the
	 * surrounding elements
	 */
	private double contextMatchResult;

	/**
	 * the result of the matching between two elements based on their depth in
	 * the modelgraph
	 */
	private double depthMatchResult;

	/**
	 * the result of the matching between two elements based on their attribute
	 * values
	 */
	private double attributeMatchResult;

	/**
	 * the type of the node from the node nodeOriginalModel
	 */
	private NodeType typeOriginModel;

	/**
	 * the type of the node from the node nodeNewModel
	 */
	private NodeType typeNewModel;

	private double weightedSyntacticResult, weightedSemanticResult,
			weightedContextResult;

	/**
	 * @return the contextMatchResult
	 */
	public double getContextMatchResult() {
		return contextMatchResult;
	}

	/**
	 * @return the depthMatchResult
	 */
	public double getDepthMatchResult() {
		return depthMatchResult;
	}

	/**
	 * @return the labelComparison
	 */
	public ComparedLabelPair getLabelComparison() {
		return labelComparison;
	}

	/**
	 * @return the tokensComparison
	 */
	public ComparedNormalizedTokensPair getTokensComparison() {
		return tokensComparison;
	}

	/**
	 * @param nodeOriginalModel
	 *            the nodeOriginalModel to set
	 */
	public void setNodeOriginalModel(Node nodeOriginalModel) {
		this.nodeOriginalModel = nodeOriginalModel;
	}

	/**
	 * @param nodeNewModel
	 *            the nodeNewModel to set
	 */
	public void setNodeNewModel(Node nodeNewModel) {
		this.nodeNewModel = nodeNewModel;
	}

	/**
	 * @param labelComparison
	 *            the labelComparison to set
	 */
	public void setLabelComparison(ComparedLabelPair labelComparison) {
		this.labelComparison = labelComparison;
	}

	/**
	 * @param tokensComparison
	 *            the tokensComparison to set
	 */
	public void setTokensComparison(
			ComparedNormalizedTokensPair tokensComparison) {
		this.tokensComparison = tokensComparison;
	}

	/**
	 * @param contextMatchResult
	 *            the contextMatchResult to set
	 */
	public void setContextMatchResult(double contextMatchResult) {
		this.contextMatchResult = contextMatchResult;
	}

	/**
	 * @param depthMatchResult
	 *            the depthMatchResult to set
	 */
	public void setDepthMatchResult(double depthMatchResult) {
		this.depthMatchResult = depthMatchResult;
	}

	public void setNodeTypes(NodeType actTypeOriginModel,
			NodeType actTypeNewModel) {
		this.typeOriginModel = actTypeOriginModel;
		this.typeNewModel = actTypeNewModel;

	}

	/**
	 * @return the weightedSyntacticResult
	 */
	public double getWeightedSyntacticResult() {
		return weightedSyntacticResult;
	}

	/**
	 * @param weightedSyntacticResult
	 *            the weightedSyntacticResult to set
	 */
	public void setWeightedSyntacticResult(double weightedSyntacticResult) {
		this.weightedSyntacticResult = weightedSyntacticResult;
	}

	/**
	 * @return the weightedSemanticResult
	 */
	public double getWeightedSemanticResult() {
		return weightedSemanticResult;
	}

	/**
	 * @param weightedSemanticResult
	 *            the weightedSemanticResult to set
	 */
	public void setWeightedSemanticResult(double weightedSemanticResult) {
		this.weightedSemanticResult = weightedSemanticResult;
	}

	/**
	 * @return the weightedContextResult
	 */
	public double getWeightedContextResult() {
		return weightedContextResult;
	}

	/**
	 * @param weightedContextResult
	 *            the weightedContextResult to set
	 */
	public void setWeightedContextResult(double weightedContextResult) {
		this.weightedContextResult = weightedContextResult;
	}

	/**
	 * @return the nodeOriginalModel
	 */
	public Node getNodeOriginalModel() {
		return nodeOriginalModel;
	}

	/**
	 * @return the nodeNewModel
	 */
	public Node getNodeNewModel() {
		return nodeNewModel;
	}

	/**
	 * @return the typeOriginModel
	 */
	public NodeType getTypeOriginModel() {
		return typeOriginModel;
	}

	/**
	 * @return the typeNewModel
	 */
	public NodeType getTypeNewModel() {
		return typeNewModel;
	}

	/**
	 * @param attributeMatchResult
	 *            the attributeMatchResult to set
	 */
	public void setAttributeMatchResult(double attributeMatchResult) {
		this.attributeMatchResult = attributeMatchResult;
	}

	/**
	 * @return the attributeMatchResult
	 */
	public double getAttributeMatchResult() {
		return attributeMatchResult;
	}
}
