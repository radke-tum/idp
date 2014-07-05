package org.pssif.mergedDataStructures;

import org.pssif.comparedDataStructures.ComparedLabelPair;
import org.pssif.comparedDataStructures.ComparedNormalizedTokensPair;

import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.model.Node;

public class MergedNodePair {

	private Node nodeOriginalModel, nodeNewModel;
	
	/**
	 * the type of the node from the node nodeOriginalModel
	 */
	private NodeType typeOriginModel;

	/**
	 * the type of the node from the node nodeNewModel
	 */
	private NodeType typeNewModel;

	private ComparedLabelPair labelComparison;	
	
	/**
	 * the result of the matching between two elements based on their attribute
	 * values
	 */
	private double attributeMatchResult;
	
	private boolean traceLink,merge;

	/**
	 * @param nodeOriginalModel
	 * @param nodeNewModel
	 * @param typeOriginModel
	 * @param typeNewModel
	 * @param labelComparison
	 * @param traceLink
	 * @param merge
	 */
	public MergedNodePair(Node nodeOriginalModel, Node nodeNewModel,
			NodeType typeOriginModel, NodeType typeNewModel,
			ComparedLabelPair labelComparison, boolean traceLink, boolean merge) {
		super();
		this.nodeOriginalModel = nodeOriginalModel;
		this.nodeNewModel = nodeNewModel;
		this.typeOriginModel = typeOriginModel;
		this.typeNewModel = typeNewModel;
		this.labelComparison = labelComparison;
		this.traceLink = traceLink;
		this.merge = merge;
	}

	/**
	 * @return the nodeOriginalModel
	 */
	public Node getNodeOriginalModel() {
		return nodeOriginalModel;
	}

	/**
	 * @param nodeOriginalModel the nodeOriginalModel to set
	 */
	public void setNodeOriginalModel(Node nodeOriginalModel) {
		this.nodeOriginalModel = nodeOriginalModel;
	}

	/**
	 * @return the nodeNewModel
	 */
	public Node getNodeNewModel() {
		return nodeNewModel;
	}

	/**
	 * @param nodeNewModel the nodeNewModel to set
	 */
	public void setNodeNewModel(Node nodeNewModel) {
		this.nodeNewModel = nodeNewModel;
	}

	/**
	 * @return the typeOriginModel
	 */
	public NodeType getTypeOriginModel() {
		return typeOriginModel;
	}

	/**
	 * @param typeOriginModel the typeOriginModel to set
	 */
	public void setTypeOriginModel(NodeType typeOriginModel) {
		this.typeOriginModel = typeOriginModel;
	}

	/**
	 * @return the typeNewModel
	 */
	public NodeType getTypeNewModel() {
		return typeNewModel;
	}

	/**
	 * @param typeNewModel the typeNewModel to set
	 */
	public void setTypeNewModel(NodeType typeNewModel) {
		this.typeNewModel = typeNewModel;
	}

	/**
	 * @return the attributeMatchResult
	 */
	public double getAttributeMatchResult() {
		return attributeMatchResult;
	}

	/**
	 * @param attributeMatchResult the attributeMatchResult to set
	 */
	public void setAttributeMatchResult(double attributeMatchResult) {
		this.attributeMatchResult = attributeMatchResult;
	}

	/**
	 * @return the traceLink
	 */
	public boolean isTraceLink() {
		return traceLink;
	}

	/**
	 * @param traceLink the traceLink to set
	 */
	public void setTraceLink(boolean traceLink) {
		this.traceLink = traceLink;
	}

	/**
	 * @return the merge
	 */
	public boolean isMerge() {
		return merge;
	}

	/**
	 * @param merge the merge to set
	 */
	public void setMerge(boolean merge) {
		this.merge = merge;
	}
}
