package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

// beschreibt die Assoziation zwischen Elementen
public class XmiPackagedElementAssociation extends XmiPackagedElement {

	// Attribute einer Assoziation
	private XmiMemberEnd memberEndSource;
	private XmiMemberEnd memberEndTarget;
	
	private String aggregation;
	
	//Konstruktor
	public XmiPackagedElementAssociation(Attributes atts) {
		super(atts);
		this.aggregation = atts.getValue(ATTRIBUTE_AGGREGATION);
	}

	public XmiMemberEnd getMemberEndSource() {
		return memberEndSource;
	}
	
	public void setMemberEndSource(XmiMemberEnd memberEndSource) {
		this.memberEndSource = memberEndSource;
	}

	public XmiMemberEnd getMemberEndTarget() {
		return memberEndTarget;
	}

	public void setMemberEndTarget(XmiMemberEnd memberEndTarget) {
		this.memberEndTarget = memberEndTarget;
	}

	public String getAggregation() {
		return aggregation;
	}

	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

}
