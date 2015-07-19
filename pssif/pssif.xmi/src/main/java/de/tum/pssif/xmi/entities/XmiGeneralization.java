package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiGeneralization extends XmiChild {

	// zusätzliche Attribute die XmiGeneralization besitzt
	private String general;

	// Konstruktor: erbt von Oberklasse - Initialisierung der eigenen
	// Attribute
	public XmiGeneralization(Attributes atts) {
		super(atts);
		this.general = atts.getValue(ATTRIBUTE_GENERAL);
	}
	
	public String getGeneral() {
		return general;
	}

	public void setGeneral(String general) {
		this.general = general;
	}

	@Override
	public String toString() {
		return "\n\tXmiGeneralization [" + super.toString() + ", general=" + general
				+ "]";
	}

}
