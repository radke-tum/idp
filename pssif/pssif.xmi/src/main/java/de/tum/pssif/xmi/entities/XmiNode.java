package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

import de.tum.pssif.xmi.XmiConstants;

// Inhalt und Aufbau eines XmiNodes
public class XmiNode implements XmiConstants {

	// Attribute eines XmiNodes
	protected String xmiID;
	protected String xmiType;
	protected String name;

	// Konstruktor
	public XmiNode(Attributes atts) {
		this.xmiID = atts.getValue(ATTRIBUTE_XMI_ID);
		this.xmiType = atts.getValue(ATTRIBUTE_XMI_TYPE);
		this.name = atts.getValue(ATTRIBUTE_NAME);
	}

	public String getXmiID() {
		return xmiID;
	}

	public void setXmiID(String xmiID) {
		this.xmiID = xmiID;
	}

	public String getXmiType() {
		return xmiType;
	}

	public void setXmiType(String xmiType) {
		this.xmiType = xmiType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "xmiID=" + xmiID + ", xmiType=" + xmiType;
	}

}
