package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

// beschreibt eine Klasse im Klassendiagramm
public class XmiPackagedElementClass extends XmiPackagedElement {
	
	private String isAbstract;

	// Konstruktor: erbt von der Oberklasse
	public XmiPackagedElementClass(Attributes atts) {
		super(atts);
		this.isAbstract = atts.getValue(ATTRIBUTE_IS_ABSTRACT);
	}

	public String getIsAbstract() {
		return isAbstract;
	}

	public void setIsAbstract(String isAbstract) {
		this.isAbstract = isAbstract;
	}

	@Override
	public String toString() {
		return "XmiPackagedElementClass [isAbstract=" + isAbstract + ", xmiID="
				+ xmiID + ", xmiType=" + xmiType + ", name=" + name + "]";
	}


}
