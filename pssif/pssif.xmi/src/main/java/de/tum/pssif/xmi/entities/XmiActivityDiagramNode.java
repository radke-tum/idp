package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiActivityDiagramNode extends XmiNode {

	public XmiActivityDiagramNode(Attributes atts) {
		super(atts);
		// TODO Auto-generated constructor stub
	}

	// Unterteilungen der <packagedElement> Tags
	public static XmiActivityDiagramNode create(Attributes atts) {
		if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_CENTRAL_BUFFER_NODE)
				|| atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
						TYPE_INITIAL_NODE)
				|| atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
						TYPE_FINAL_NODE)) {
			return new XmiActivityDiagramNodeBuffer(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_CALL_BEHAVIOUR_ACTION)) {
			return new XmiActivityDiagramNodeAction(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_DECISION_NODE)) {
			return new XmiActivityDiagramNodeDecision(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_MERGE_NODE)) {
			return new XmiActivityDiagramNodeMerge(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_FORK_NODE)) {
			return new XmiActivityDiagramNodeFork(atts);
		} else if (atts.getValue(ATTRIBUTE_XMI_TYPE).equalsIgnoreCase(
				TYPE_JOIN_NODE)) {
			return new XmiActivityDiagramNodeJoin(atts);
		}
		return new XmiActivityDiagramNode(atts);
	}

}
