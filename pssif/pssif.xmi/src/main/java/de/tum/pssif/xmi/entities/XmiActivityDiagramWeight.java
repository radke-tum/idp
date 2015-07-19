package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiActivityDiagramWeight extends XmiNode {
	
	private String value;

	public XmiActivityDiagramWeight(Attributes atts) {
		super(atts);
		this.value = atts.getValue(ATTRIBUTE_VALUE);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "XmiWeight [value=" + value + ", xmiID=" + xmiID + ", xmiType="
				+ xmiType + ", name=" + name + "]";
	}

}
