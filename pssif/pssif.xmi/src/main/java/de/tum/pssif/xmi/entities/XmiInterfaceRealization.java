package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

import de.tum.pssif.xmi.XmiConstants;

public class XmiInterfaceRealization extends XmiChild {

	// zusätzliche Attribute die XmiInterfaceRealization besitzt
	private String contract;

	// Konstruktor: erbt von Oberklasse - Initialisierung der eigenen
	// Attribute
	public XmiInterfaceRealization(Attributes atts) {
		super(atts);
		this.contract = atts.getValue(XmiConstants.ATTRIBUTE_CONTRACT);
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	@Override
	public String toString() {
		return "XmiInterfaceRealization [contract=" + contract + ", xmiID="
				+ xmiID + ", xmiType=" + xmiType + "]";
	}

}
