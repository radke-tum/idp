package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

// Kindknoten
public class XmiChild extends XmiNode {
	 
	protected String parentId;
	
	public XmiChild(Attributes atts) {
		super(atts);
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
