package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiChildVisibility extends XmiChild {

	private String visibility;
	
	public XmiChildVisibility(Attributes atts) {
		super(atts);
		this.visibility = atts.getValue(ATTRIBUTE_VISIBILITY);
	}
	
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	@Override
	public String toString() {
		return "XmiChildVisibility [visibility=" + visibility + ", parentId="
				+ parentId + ", xmiID=" + xmiID + ", xmiType=" + xmiType
				+ ", name=" + name + "]";
	}

}
