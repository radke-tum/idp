package de.tum.pssif.xmi.entities;

import org.xml.sax.Attributes;

public class XmiOwnedEnd extends XmiChild {

	// zusätzliche Attribute die XmiOwnedEnd besitzt
	private String visibility;
	private String association;
	private String type;
	
	private String lowerValue;
	private String upperValue;

	// Konstruktor: erbt von Oberklasse - Initialisierung der eigenen
	// Attribute
	public XmiOwnedEnd(Attributes atts) {
		super(atts);
		this.visibility = atts.getValue(ATTRIBUTE_VISIBILITY);
		this.association = atts.getValue(ATTRIBUTE_ASSOCIATION);
		this.type = atts.getValue(ATTRIBUTE_TYPE);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getAssociation() {
		return association;
	}

	public void setAssociation(String association) {
		this.association = association;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getLowerValue() {
		return lowerValue;
	}

	public void setLowerValue(Attributes atts) {
		this.lowerValue = atts.getValue(ATTRIBUTE_VALUE);
	}

	public String getUpperValue() {
		return upperValue;
	}
	
	public void setUpperValue(Attributes atts) {
		this.upperValue = atts.getValue(ATTRIBUTE_VALUE);
	}

	@Override
	public String toString() {

		return "\n\tOwnedEnd [" + super.toString() + ", visibility="
				+ visibility + ", association=" + association + ", type="
				+ type + "]";
	}

}
