package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

import de.tum.pssif.xmi.XmiConstants;

// enthält die Referenz auf die ownedAttributes von Klassen die eine Assoziation zueinander haben

public class XmiMemberEnd implements XmiConstants {

	// zusätzliche Attribute die XmiMemberEnd besitzt
	private String idref;

	// Konstruktor: Initialisierung der Attribute
	public XmiMemberEnd(Attributes atts) {
		this.idref = atts.getValue(ATTRIBUTE_XMI_IDREF);
	}

	public String getIdref() {
		return idref;
	}

	public void setIdref(String idref) {
		this.idref = idref;
	}

	@Override
	public String toString() {
		return "\n\tXmiMemberEnd [idref=" + idref + "]";
	}

}
