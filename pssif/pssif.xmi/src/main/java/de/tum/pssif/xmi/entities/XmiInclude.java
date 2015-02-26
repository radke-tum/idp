package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiInclude extends XmiChild {
	
	private String addition;

	public XmiInclude(Attributes atts) {
		super(atts);
		this.addition = atts.getValue(ATTRIBUTE_ADDITION);
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	@Override
	public String toString() {
		return "XmiInclude [addition=" + addition + "]";
	}

}
