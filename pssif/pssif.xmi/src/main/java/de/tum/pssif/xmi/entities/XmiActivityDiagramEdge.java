package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiActivityDiagramEdge extends XmiNode {
	
	private String sourceNode;
	private String targetNode;
	private XmiActivityDiagramWeight weigth;
	
	public XmiActivityDiagramEdge(Attributes atts) {
		super(atts);
		this.sourceNode = atts.getValue(ATTRIBUTE_SOURCE);
		this.targetNode = atts.getValue(ATTRIBUTE_TARGET);
	}
	

	// Unterteilungen der <packagedElement> Tags
	public static XmiActivityDiagramEdge create(Attributes atts) {
		if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(TYPE_OBJECT_FLOW_EDGE)) {
			return new XmiActivityDiagramEdgeObjectFlow(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_CONTROL_FLOW_EDGE)) {
			return new XmiActivityDiagramEdgeControlFlow(atts);
		}
		return new XmiActivityDiagramEdge(atts);
	}

	public String getSourceNode() {
		return sourceNode;
	}

	public String getTargetNode() {
		return targetNode;
	}

	public XmiActivityDiagramWeight getWeigth() {
		return weigth;
	}

	public void setWeigth(XmiActivityDiagramWeight weigth) {
		this.weigth = weigth;
	}

	@Override
	public String toString() {
		return "activityDiagramEdge [sourceNode=" + sourceNode
				+ ", targetNode=" + targetNode + ", weigth=" + weigth
				+ ", xmiID=" + xmiID + ", xmiType=" + xmiType + ", name="
				+ name + "]";
	}
	
}
