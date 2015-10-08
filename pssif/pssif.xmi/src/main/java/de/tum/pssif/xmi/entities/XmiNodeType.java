package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

import de.tum.pssif.xmi.XmiConstants;

public class XmiNodeType implements XmiConstants {
	
	// zusätzliche Attribute die XmiNodeType besitzt
	private String href;
	
	// Konstruktor: Initialisierung der Attribute
	public XmiNodeType(Attributes atts) {
		this.href = atts.getValue(ATTRIBUTE_HREF);
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "\n\t\tXmiType [href=" + href + "]";
	}

	
	
}
