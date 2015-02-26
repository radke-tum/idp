package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

import de.tum.pssif.xmi.XmiConstants;

public class XmiSubject implements XmiConstants {

	// zusätzliche Attribute die Subject besitzt
	private String idref;
	
	public XmiSubject(Attributes atts) {
		this.idref = atts.getValue(ATTRIBUTE_XMI_IDREF);
	}

	public String getIdref() {
		return idref;
	}

	public void setIdref(String idref) {
		this.idref = idref;
	}
	
}
