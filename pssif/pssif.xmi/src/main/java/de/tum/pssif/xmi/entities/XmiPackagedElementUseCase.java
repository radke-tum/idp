package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiPackagedElementUseCase extends XmiPackagedElement {

	private XmiSubject xmiSubject;
	
	// Konstruktor
	public XmiPackagedElementUseCase(Attributes atts) {
		super(atts);
	}

	public XmiSubject getXmiSubject() {
		return xmiSubject;
	}

	public void setXmiSubject(XmiSubject xmiSubject) {
		this.xmiSubject = xmiSubject;
	}

}
